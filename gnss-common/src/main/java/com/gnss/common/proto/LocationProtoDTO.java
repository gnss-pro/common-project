package com.gnss.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>Description: 位置传输对象protobuf定义</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/4/15
 */
@Getter
@Setter
@ToString
@ProtobufClass
public class LocationProtoDTO implements Serializable {

    /**
     * 节点名称
     */
    @Protobuf(fieldType = FieldType.STRING, order = 1, required = true)
    private String nodeName;

    /**
     * 位置信息
     */
    @Protobuf(fieldType = FieldType.OBJECT, order = 2, required = true)
    private LocationProto locationProto;

    /**
     * 终端信息
     */
    @Protobuf(fieldType = FieldType.OBJECT, order = 3)
    private TerminalProto terminalProto;
}
