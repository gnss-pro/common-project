package com.gnss.mqutil.consumer;

import com.alibaba.fastjson.JSON;
import com.gnss.common.api.CommonReplyParam;
import com.gnss.common.constants.CommandSendResultEnum;
import com.gnss.common.proto.CommandProto;
import com.gnss.common.proto.TerminalProto;
import com.gnss.common.utils.SessionUtil;
import com.gnss.mqutil.producer.RabbitMessageSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>Description: RabbitMQ下行指令订阅</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/9/14
 */
@Slf4j
public abstract class AbstractDownCommandReceiver {

    @Autowired
    private RabbitMessageSender messageSender;

    private Map<String, Set<String>> respCommandMap = new HashMap<>();

    private Map<String, CopyOnWriteArrayList<CommandProto>> downCommandMap = new ConcurrentHashMap<>();

    /**
     * 订阅处理下行指令
     *
     * @param commandProto  指令信息
     * @param rabbitChannel rabbit通道
     * @param message       消息
     * @throws Exception 异常
     */
    @RabbitHandler
    public void receiveDownCommand(CommandProto commandProto, com.rabbitmq.client.Channel rabbitChannel, Message message) throws Exception {
        try {
            //终端不在线
            Long terminalId = commandProto.getTerminalId();
            Channel channel = SessionUtil.getChannel(terminalId);
            if (channel == null) {
                log.info("处理下发指令,终端不在线{}", commandProto);
                commandProto.setSendResult(CommandSendResultEnum.TERMINAL_OFFLINE);
                messageSender.sendUpCommand(commandProto);
                return;
            }

            registerDownCommand(commandProto);
            printLog(channel, commandProto);
            handleDownCommand(channel, commandProto);
        } catch (Exception e) {
            log.error("处理下发指令异常{}", commandProto, e);
            rabbitChannel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    private void registerDownCommand(CommandProto commandProto) {
        String respCommandId = commandProto.getRespCommandId();
        if (respCommandId == null) {
            return;
        }
        String downCommandId = commandProto.getDownCommandId();
        respCommandMap.computeIfAbsent(respCommandId, k -> new HashSet<>()).add(downCommandId);
        Long terminalId = commandProto.getTerminalId();
        String key = String.format("%d-%s-%s", terminalId, downCommandId, respCommandId);
        downCommandMap.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(commandProto);
    }

    private List<CommandProto> unregisterDownCommand(Long terminalId, String downCommandId, String respCommandId) {
        String key = String.format("%d-%s-%s", terminalId, downCommandId, respCommandId);
        return downCommandMap.remove(key);
    }

    private void printLog(Channel channel, CommandProto commandProto) {
        TerminalProto terminalInfo = SessionUtil.getTerminalInfo(channel);
        log.info("下发指令:{}({}),终端ID:{},终端手机号:{},参数:{}", commandProto.getDownCommandId(), commandProto.getDownCommandDesc(), terminalInfo.getTerminalId(), terminalInfo.getTerminalSimCode(), commandProto.getParams());
    }

    protected abstract void handleDownCommand(Channel channel, CommandProto commandProto) throws Exception;

    /**
     * 发送上行指令
     *
     * @param respCommandId 应答指令
     * @param result        结果
     * @param ctx           ChannelHandlerContext
     * @throws Exception 异常
     */
    public void sendUpCommand(ChannelHandlerContext ctx, String respCommandId, Object result) throws Exception {
        sendUpCommand(ctx.channel(), respCommandId, result);
    }

    /**
     * 发送上行指令
     *
     * @param respCommandId 应答指令
     * @param result        结果
     * @param channel       Socket通道
     * @throws Exception 异常
     */
    public void sendUpCommand(Channel channel, String respCommandId, Object result) throws Exception {
        Set<String> downCommandIdSet = respCommandMap.get(respCommandId);
        if (downCommandIdSet == null) {
            return;
        }
        String downCommandId = null;
        if (result instanceof CommonReplyParam) {
            //通用应答需要判断应答的下行指令
            CommonReplyParam replyParam = (CommonReplyParam) result;
            downCommandId = replyParam.getReplyMessageId();
            if (!downCommandIdSet.contains(downCommandId)) {
                log.error("未注册的下行指令:{},应答结果:{}", downCommandId, result);
                return;
            }
        } else {
            downCommandId = downCommandIdSet.stream().findFirst().get();
        }

        TerminalProto terminalInfo = SessionUtil.getTerminalInfo(channel);
        Long terminalId = terminalInfo.getTerminalId();
        List<CommandProto> commandList = unregisterDownCommand(terminalId, downCommandId, respCommandId);
        if (commandList != null) {
            String resultJson = JSON.toJSONString(result);
            for (CommandProto commandProto : commandList) {
                commandProto.setParams(resultJson);
                commandProto.setSendResult(CommandSendResultEnum.SUCCESS);
                messageSender.sendUpCommand(commandProto);
            }
            log.info("发送上行指令,终端ID:{},终端手机号:{},下行指令:{},响应指令:{},响应结果:{}", terminalId, terminalInfo.getTerminalSimCode(), downCommandId, respCommandId, result);
        }
    }

}