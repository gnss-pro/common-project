package com.gnss.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>Description: 终端protobuf定义</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2017/9/15
 */
@Getter
@Setter
@ToString
@ProtobufClass
public class TerminalProto implements Serializable {

    /**
     * 节点名称
     */
    @Protobuf(fieldType = FieldType.STRING, order = 1, required = true)
    private String nodeName;

    /**
     * 终端ID
     */
    @Protobuf(fieldType = FieldType.INT64, order = 2, required = true)
    private long terminalId;

    /**
     * 终端手机号
     */
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String terminalSimCode;

    /**
     * 终端号码
     */
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String terminalNum;

    /**
     * 车牌号码
     */
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    private String vehicleNum;
}
