package com.gnss.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.gnss.common.constants.CommonConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Description: 位置protobuf定义</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/4/13
 */
@Getter
@Setter
@ToString
@ProtobufClass
public class LocationProto implements Serializable {

    /**
     * 是否在线
     */
    @Protobuf(fieldType = FieldType.INT32, order = 1, required = true)
    private Integer online = CommonConstants.YES;

    /**
     * 是否定位：0未定位，1定位
     */
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private Integer gpsValid;

    /**
     * ACC状态：0关，1开
     */
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private Integer acc;

    /**
     * 纬度
     */
    @Protobuf(fieldType = FieldType.DOUBLE, order = 4)
    private Double lat;

    /**
     * 经度
     */
    @Protobuf(fieldType = FieldType.DOUBLE, order = 5)
    private Double lon;

    /**
     * 高程
     */
    @Protobuf(fieldType = FieldType.INT32, order = 6)
    private Integer altitude;

    /**
     * 速度
     */
    @Protobuf(fieldType = FieldType.DOUBLE, order = 7)
    private Double speed;

    /**
     * 方向
     */
    @Protobuf(fieldType = FieldType.INT32, order = 8)
    private Integer direction;

    /**
     * 时间
     */
    @Protobuf(fieldType = FieldType.INT64, order = 9)
    private Long time;

    /**
     * 里程
     */
    @Protobuf(fieldType = FieldType.DOUBLE, order = 10)
    private Double mileage;

    /**
     * 油量
     */
    @Protobuf(fieldType = FieldType.DOUBLE, order = 11)
    private Double fuel;

    /**
     * 行驶记录仪速度
     */
    @Protobuf(fieldType = FieldType.DOUBLE, order = 12)
    private Double recoderSpeed;

    /**
     * 报警标志
     */
    @Protobuf(fieldType = FieldType.INT64, order = 13)
    private Long alarmFlag;

    /**
     * 状态
     */
    @Protobuf(fieldType = FieldType.INT64, order = 14)
    private Long status;

    /**
     * 附加信息
     */
    @Protobuf(fieldType = FieldType.STRING, order = 15)
    private String extraInfo;

    /**
     * 报警位
     */
    @Protobuf(fieldType = FieldType.INT32, order = 16)
    private List<Integer> alarmBits;

    /**
     * 状态位
     */
    @Protobuf(fieldType = FieldType.INT32, order = 17)
    private List<Integer> statusBits;

    /**
     * 报警位(JSON字符串)
     */
    @Protobuf(fieldType = FieldType.STRING, order = 18)
    private String alarmBitsJson;

    /**
     * 状态位(JSON字符串)
     */
    @Protobuf(fieldType = FieldType.STRING, order = 19)
    private String statusBitsJson;
}