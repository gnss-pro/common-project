package com.gnss.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>Description: 多媒体文件传输对象protobuf定义</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/4/13
 */
@Getter
@Setter
@ToString(exclude = {"mediaData"})
@ProtobufClass
public class MediaFileProtoDTO implements Serializable {
    /**
     * 多媒体ID
     */
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private Long mediaId;

    /**
     * 多媒体类型
     */
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private Integer mediaType;

    /**
     * 多媒体格式编码
     */
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private Integer mediaFormatCode;

    /**
     * 事件项编码
     */
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private Integer eventItemCode;

    /**
     * 通道ID
     */
    @Protobuf(fieldType = FieldType.INT32, order = 5)
    private Integer channelId;

    /**
     * 位置信息
     */
    @Protobuf(fieldType = FieldType.OBJECT, order = 6)
    private LocationProto location;

    /**
     * 多媒体数据
     */
    @Protobuf(fieldType = FieldType.BYTES, order = 7, required = true)
    private byte[] mediaData;

    /**
     * 终端信息
     */
    @Protobuf(fieldType = FieldType.OBJECT, order = 8, required = true)
    private TerminalProto terminalInfo;
}
