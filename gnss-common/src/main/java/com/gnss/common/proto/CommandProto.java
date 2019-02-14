package com.gnss.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.gnss.common.constants.CommandRequestTypeEnum;
import com.gnss.common.constants.CommandSendResultEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>Description: 指令MQ传输信息, 用于应用程序间上行下行指令传输</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/2/3
 */
@Data
@ProtobufClass
public class CommandProto implements Serializable {

    /**
     * 指令操作日志ID
     */
    @Protobuf(fieldType = FieldType.INT64, order = 1, required = true)
    private long commandOperationLogId;

    /**
     * 终端ID
     */
    @Protobuf(fieldType = FieldType.INT64, order = 2, required = true)
    private long terminalId;

    /**
     * 指令请求方式
     */
    @Protobuf(fieldType = FieldType.ENUM, order = 3, required = true)
    private CommandRequestTypeEnum requestType;

    /**
     * 下行指令ID
     */
    @Protobuf(fieldType = FieldType.STRING, order = 4, required = true)
    private String downCommandId;

    /**
     * 参数
     */
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    private String params;

    /**
     * 指令发送结果
     */
    @Protobuf(fieldType = FieldType.ENUM, order = 6)
    private CommandSendResultEnum sendResult;

    /**
     * 响应指令ID
     */
    @Protobuf(fieldType = FieldType.STRING, order = 7)
    private String respCommandId;

    /**
     * 发送方节点
     */
    @Protobuf(fieldType = FieldType.STRING, order = 8, required = true)
    private String fromNode;

    /**
     * 接收方节点
     */
    @Protobuf(fieldType = FieldType.STRING, order = 9, required = true)
    private String toNode;

    /**
     * 开始时间
     */
    @Protobuf(fieldType = FieldType.INT64, order = 10, required = true)
    private long startTime;

    /**
     * 超时时间
     */
    @Protobuf(fieldType = FieldType.INT32, order = 11)
    private Integer timeout;

    /**
     * 下行指令描述
     */
    @Protobuf(fieldType = FieldType.STRING, order = 12)
    private String downCommandDesc;

    /**
     * 透传消息体
     */
    @Protobuf(fieldType = FieldType.BYTES, order = 13)
    private byte[] messageBody;
}
