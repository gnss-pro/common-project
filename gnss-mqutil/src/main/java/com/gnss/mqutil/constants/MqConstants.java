package com.gnss.mqutil.constants;

/**
 * <p>Description: MQ常量</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/9/14
 */
public class MqConstants {
    private MqConstants() {
    }

    public static final String STATUS_EXCHANGE = "status.exchange";

    public static final String STATUS_ROUTING_KEY = "#.status";

    public static final String LOCATION_EXCHANGE = "location.exchange";

    public static final String LOCATION_ROUTING_KEY = "#.location";

    public static final String ALARM_EXCHANGE = "alarm.exchange";

    public static final String ALARM_ROUTING_KEY = "#.alarm";

    public static final String MEDIA_FILE_EXCHANGE = "media.file.exchange";

    public static final String MEDIA_FILE_ROUTING_KEY = "#.media.file";

    public static final String UP_COMMAND_EXCHANGE = "up.command.exchange";

    public static final String UP_COMMAND_ROUTING_KEY = "#.up.command";

    public static final String DOWN_COMMAND_EXCHANGE = "down.command.exchange";

    public static final String DOWN_COMMAND_ROUTING_KEY = "#.down.command";

    public static final String RPC_EXCHANGE = "rpc.exchange";

    public static final String RPC_ROUTING_KEY = "gnss.rpc";
}