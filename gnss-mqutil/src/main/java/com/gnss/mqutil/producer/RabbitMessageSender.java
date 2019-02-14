package com.gnss.mqutil.producer;

import com.gnss.common.constants.AlarmActionEnum;
import com.gnss.common.constants.CommonConstants;
import com.gnss.common.proto.AlarmProtoDTO;
import com.gnss.common.proto.CommandProto;
import com.gnss.common.proto.LocationProto;
import com.gnss.common.proto.LocationProtoDTO;
import com.gnss.common.proto.MediaFileProtoDTO;
import com.gnss.common.proto.RpcProto;
import com.gnss.common.proto.TerminalProto;
import com.gnss.common.service.RedisService;
import com.gnss.mqutil.constants.MqConstants;
import com.gnss.mqutil.converter.ProtobufMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;

/**
 * <p>Description: RabbitMQ消息发送</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/4/13
 */
@Slf4j
public class RabbitMessageSender {

    private RabbitTemplate rabbitTemplate;

    private RedisService redisService;

    public RabbitMessageSender(RabbitTemplate rabbitTemplate, RedisService redisService) {
        rabbitTemplate.setMessageConverter(new ProtobufMessageConverter());
        RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate();
        rabbitMessagingTemplate.setRabbitTemplate(rabbitTemplate);
        this.rabbitTemplate = rabbitTemplate;
        this.redisService = redisService;
    }

    /**
     * 发送状态和位置
     *
     * @param terminalProto 终端信息
     * @param locationProto 位置信息
     * @return 位置传输信息
     * @throws Exception 异常
     */
    public LocationProtoDTO sendStatusAndLocation(TerminalProto terminalProto, LocationProto locationProto) throws Exception {
        LocationProtoDTO locationProtoDTO = sendLocation(terminalProto, locationProto);
        String statusRoutingKey = String.format("%d.status", terminalProto.getTerminalId());
        rabbitTemplate.convertAndSend(MqConstants.STATUS_EXCHANGE, statusRoutingKey, locationProtoDTO);
        return locationProtoDTO;
    }

    /**
     * 发送位置信息
     *
     * @param terminalProto 终端信息
     * @param locationProto 位置信息
     * @return 位置传输信息
     * @throws Exception 异常
     */
    public LocationProtoDTO sendLocation(TerminalProto terminalProto, LocationProto locationProto) throws Exception {
        String nodeName = terminalProto.getNodeName();
        LocationProtoDTO locationProtoDTO = new LocationProtoDTO();
        locationProtoDTO.setNodeName(nodeName);
        locationProtoDTO.setLocationProto(locationProto);
        locationProtoDTO.setTerminalProto(terminalProto);
        String routingKey = String.format("%d.location", terminalProto.getTerminalId());
        rabbitTemplate.convertAndSend(MqConstants.LOCATION_EXCHANGE, routingKey, locationProtoDTO);
        //有效位置处理完报警后放入缓存
        if (handleAlarm(locationProtoDTO)) {
            redisService.putLastLocation(locationProtoDTO);
        }
        return locationProtoDTO;
    }

    /**
     * 处理报警
     *
     * @param locationProtoDTO 位置传输信息
     * @return
     */
    private boolean handleAlarm(LocationProtoDTO locationProtoDTO) {
        Long terminalId = locationProtoDTO.getTerminalProto().getTerminalId();
        LocationProto currLocation = locationProtoDTO.getLocationProto();
        LocationProto prevLocation = redisService.getLastLocation(terminalId);
        //最新位置必须是已定位
        if (currLocation.getGpsValid() == CommonConstants.NO) {
            return false;
        }
        //缓存不存在上一个位置,直接新增报警
        if (prevLocation == null) {
            sendAlarm(AlarmActionEnum.START, locationProtoDTO.getTerminalProto(), currLocation, currLocation.getAlarmBits());
            return true;
        }
        //最新位置的时间不能小于上一个位置的时间
        if (currLocation.getTime() < prevLocation.getTime()) {
            return false;
        }

        TerminalProto terminalProto = locationProtoDTO.getTerminalProto();
        List<Integer> currAlarmBits = currLocation.getAlarmBits();
        List<Integer> prevAlarmBits = prevLocation.getAlarmBits();
        Map<Boolean, List<Integer>> groups = currAlarmBits.stream()
                .collect(partitioningBy(t -> prevAlarmBits.contains(t)));
        //当前位置与上一个位置的交集
        List<Integer> intersectList = groups.get(Boolean.TRUE);
        //当前位置与上一个位置的差集
        List<Integer> currSubstractList = groups.get(Boolean.FALSE);
        //上一个位置与当前位置的差集
        List<Integer> prevSubstractList = prevAlarmBits.stream()
                .filter(t -> !intersectList.contains(t))
                .collect(Collectors.toList());
        sendAlarm(AlarmActionEnum.STOP, terminalProto, prevLocation, prevSubstractList);
        sendAlarm(AlarmActionEnum.START, terminalProto, currLocation, currSubstractList);
        return true;
    }

    /**
     * 发送报警
     *
     * @param alarmAction
     * @param terminalProto
     * @param prevLocation
     * @param alarmBits
     */
    private void sendAlarm(AlarmActionEnum alarmAction, TerminalProto terminalProto, LocationProto prevLocation, List<Integer> alarmBits) {
        if (alarmBits.isEmpty()) {
            return;
        }
        AlarmProtoDTO alarmProtoDTO = new AlarmProtoDTO();
        alarmProtoDTO.setAlarmAction(alarmAction);
        alarmProtoDTO.setNodeName(terminalProto.getNodeName());
        alarmProtoDTO.setLocationProto(prevLocation);
        alarmProtoDTO.setTerminalProto(terminalProto);
        alarmProtoDTO.setAlarmBits(alarmBits);
        String routingKey = String.format("%d.%s.alarm", terminalProto.getTerminalId(), alarmAction == AlarmActionEnum.START ? "start" : "stop");
        rabbitTemplate.convertAndSend(MqConstants.ALARM_EXCHANGE, routingKey, alarmProtoDTO);
        log.info("发送报警({}),终端信息:{},报警位:{}", alarmAction, terminalProto, alarmBits);
    }

    /**
     * 发送多媒体文件
     *
     * @param mediaFileDTO 多媒体文件
     * @throws Exception 异常
     */
    public void sendMediaFile(MediaFileProtoDTO mediaFileDTO) throws Exception {
        String routingKey = String.format("%d.media.file", mediaFileDTO.getTerminalInfo().getTerminalId());
        rabbitTemplate.convertAndSend(MqConstants.MEDIA_FILE_EXCHANGE, routingKey, mediaFileDTO);
    }

    /**
     * 发送下行指令
     *
     * @param commandProto 指令信息
     * @throws Exception 异常
     */
    public void sendDownCommand(CommandProto commandProto) throws Exception {
        String nodeName = commandProto.getToNode();
        String routingKey = String.format("%d.%s.down.command", commandProto.getTerminalId(), nodeName);
        rabbitTemplate.convertAndSend(MqConstants.DOWN_COMMAND_EXCHANGE, routingKey, commandProto);
    }

    /**
     * 发送上行指令
     *
     * @param commandProto 指令信息
     * @throws Exception 异常
     */
    public void sendUpCommand(CommandProto commandProto) throws Exception {
        String nodeName = commandProto.getFromNode();
        String routingKey = String.format("%d.%s.up.command", commandProto.getTerminalId(), nodeName);
        rabbitTemplate.convertAndSend(MqConstants.UP_COMMAND_EXCHANGE, routingKey, commandProto);
    }

    /**
     * 发送RPC请求
     *
     * @param rpcProto RPC信息
     * @return RPC结果
     * @throws Exception 异常
     */
    public RpcProto sendAndReceive(RpcProto rpcProto) throws Exception {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Object response = rabbitTemplate.convertSendAndReceive(MqConstants.RPC_EXCHANGE, MqConstants.RPC_ROUTING_KEY, rpcProto, correlationId);
        return (RpcProto) response;
    }

    /**
     * 通知上线
     *
     * @param terminalProto 终端信息
     */
    public void noticeOnline(TerminalProto terminalProto) {
        sendTerminalStatus(terminalProto.getNodeName(), terminalProto, CommonConstants.YES);
    }

    /**
     * 通知所有终端离线
     *
     * @param nodeName 节点名称
     */
    public void noticeAllOffline(String nodeName) {
        sendTerminalStatus(nodeName, null, CommonConstants.NO);
    }

    /**
     * 通知离线
     *
     * @param terminalProto 终端信息
     */
    public void noticeOffline(TerminalProto terminalProto) {
        sendTerminalStatus(terminalProto.getNodeName(), terminalProto, CommonConstants.NO);
    }

    /**
     * 发送终端状态
     *
     * @param nodeName      节点名称
     * @param terminalProto 终端信息
     * @param onlineStatus  在线状态
     */
    private void sendTerminalStatus(String nodeName, TerminalProto terminalProto, int onlineStatus) {
        LocationProto locationProto = new LocationProto();
        locationProto.setGpsValid(CommonConstants.NO);
        locationProto.setOnline(onlineStatus);
        LocationProtoDTO locationProtoDTO = new LocationProtoDTO();
        locationProtoDTO.setNodeName(nodeName);
        locationProtoDTO.setTerminalProto(terminalProto);
        locationProtoDTO.setLocationProto(locationProto);
        String routingKey = terminalProto == null ? "all.status" : String.format("%d.status", terminalProto.getTerminalId());
        rabbitTemplate.convertAndSend(MqConstants.STATUS_EXCHANGE, routingKey, locationProtoDTO);
    }
}