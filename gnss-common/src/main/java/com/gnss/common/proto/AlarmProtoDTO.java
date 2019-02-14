package com.gnss.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.gnss.common.constants.AlarmActionEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Description: 报警传输对象protobuf定义</p>
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
public class AlarmProtoDTO implements Serializable {

    /**
     * 报警动作
     */
    @Protobuf(fieldType = FieldType.ENUM, order = 1, required = true)
    private AlarmActionEnum alarmAction;

    /**
     * 节点名称
     */
    @Protobuf(fieldType = FieldType.STRING, order = 2, required = true)
    private String nodeName;

    /**
     * 位置信息
     */
    @Protobuf(fieldType = FieldType.OBJECT, order = 3, required = true)
    private LocationProto locationProto;

    /**
     * 终端信息
     */
    @Protobuf(fieldType = FieldType.OBJECT, order = 4, required = true)
    private TerminalProto terminalProto;

    /**
     * 报警位
     */
    @Protobuf(fieldType = FieldType.INT32, order = 5, required = true)
    private List<Integer> alarmBits;
}
