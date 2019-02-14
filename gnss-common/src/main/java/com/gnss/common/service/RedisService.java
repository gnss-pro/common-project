package com.gnss.common.service;

import com.gnss.common.proto.LocationProto;
import com.gnss.common.proto.LocationProtoDTO;
import com.gnss.common.proto.TerminalProto;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * <p>Description: Redis服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/11/5
 */
public class RedisService {

    private StringRedisTemplate redisTemplate;

    private HashOperations<String, String, TerminalProto> terminalHashOperations;

    private HashOperations<String, String, LocationProto> locationHashOperations;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.terminalHashOperations = redisTemplate.opsForHash();
        this.locationHashOperations = redisTemplate.opsForHash();
    }

    /**
     * Redis在线终端缓存Key
     */
    private static final String TERMINAL_ONLINE_REDIS_KEY = "terminal-online";

    /**
     * Redis终端信息缓存Key
     */
    private static final String TERMINAL_INFO_REDIS_KEY = "terminal-info";

    /**
     * Redis手机号缓存Key
     */
    private static final String SIMCARD_REDIS_KEY = "simcard";

    /**
     * Redis终端号码缓存Key
     */
    private static final String TERMINAL_NUM_REDIS_KEY = "terminal-num";

    /**
     * Redis车牌号码缓存Key
     */
    private static final String VEHICLE_NUM_REDIS_KEY = "vehicle-num";

    /**
     * Redis终端位置缓存Key
     */
    private static final String LOCATION_REDIS_KEY = "location";

    /**
     * 缓存在线终端
     *
     * @param terminalInfo 终端信息
     */
    public void putOnlineTerminal(TerminalProto terminalInfo) {
        terminalHashOperations.put(TERMINAL_ONLINE_REDIS_KEY, String.valueOf(terminalInfo.getTerminalId()), terminalInfo);
    }

    /**
     * 获取在线终端
     *
     * @param terminalId 终端ID
     * @return 终端信息
     */
    public TerminalProto getOnlineTerminal(Long terminalId) {
        return terminalHashOperations.get(TERMINAL_ONLINE_REDIS_KEY, String.valueOf(terminalId));
    }

    /**
     * 删除在线终端
     *
     * @param terminalInfo 终端信息
     */
    public void deleteOnlineTerminal(TerminalProto terminalInfo) {
        terminalHashOperations.delete(TERMINAL_ONLINE_REDIS_KEY, String.valueOf(terminalInfo.getTerminalId()));
    }

    /**
     * 删除某个节点的所有在线终端
     *
     * @param nodeName 节点名称
     */
    public void deleteAllOnlineTerminals(String nodeName) {
        Map<String, TerminalProto> map = terminalHashOperations.entries(TERMINAL_ONLINE_REDIS_KEY);
        map.forEach((k, v) -> {
            if (Objects.equals(nodeName, v.getNodeName())) {
                terminalHashOperations.delete(TERMINAL_ONLINE_REDIS_KEY, k);
            }
        });
    }

    /**
     * 缓存终端信息
     *
     * @param terminalInfo 终端信息
     */
    public void putTerminalInfo(TerminalProto terminalInfo) {
        terminalHashOperations.put(TERMINAL_INFO_REDIS_KEY, String.valueOf(terminalInfo.getTerminalId()), terminalInfo);
        terminalHashOperations.put(SIMCARD_REDIS_KEY, String.valueOf(terminalInfo.getTerminalSimCode()), terminalInfo);
        terminalHashOperations.put(TERMINAL_NUM_REDIS_KEY, String.valueOf(terminalInfo.getTerminalNum()), terminalInfo);
        terminalHashOperations.put(VEHICLE_NUM_REDIS_KEY, String.valueOf(terminalInfo.getVehicleNum()), terminalInfo);
    }

    /**
     * 删除所有终端信息
     */
    public void deleteAllTerminalInfo() {
        terminalHashOperations.entries(TERMINAL_INFO_REDIS_KEY)
                .forEach((k, v) -> terminalHashOperations.delete(TERMINAL_INFO_REDIS_KEY, k));
        terminalHashOperations.entries(SIMCARD_REDIS_KEY)
                .forEach((k, v) -> terminalHashOperations.delete(SIMCARD_REDIS_KEY, k));
        terminalHashOperations.entries(TERMINAL_NUM_REDIS_KEY)
                .forEach((k, v) -> terminalHashOperations.delete(TERMINAL_NUM_REDIS_KEY, k));
        terminalHashOperations.entries(VEHICLE_NUM_REDIS_KEY)
                .forEach((k, v) -> terminalHashOperations.delete(VEHICLE_NUM_REDIS_KEY, k));
    }

    /**
     * 获取终端信息
     *
     * @param terminalId 终端ID
     * @return 终端信息
     */
    public TerminalProto getTerminalInfo(Long terminalId) {
        return terminalHashOperations.get(TERMINAL_INFO_REDIS_KEY, String.valueOf(terminalId));
    }

    /**
     * 获取终端信息
     *
     * @param simCode 终端手机号
     * @return 终端信息
     */
    public TerminalProto getTerminalInfoBySimCode(String simCode) {
        return terminalHashOperations.get(SIMCARD_REDIS_KEY, simCode);
    }

    /**
     * 获取终端信息
     *
     * @param terminalNum 终端号码
     * @return 终端信息
     */
    public TerminalProto getTerminalInfoByTerminalNum(String terminalNum) {
        return terminalHashOperations.get(TERMINAL_NUM_REDIS_KEY, terminalNum);
    }

    /**
     * 获取终端信息
     *
     * @param vehicleNum 车牌号码
     * @return 终端信息
     */
    public TerminalProto getTerminalInfoByVehicleNum(String vehicleNum) {
        return terminalHashOperations.get(VEHICLE_NUM_REDIS_KEY, vehicleNum);
    }

    /**
     * 缓存终端位置
     *
     * @param locationProtoDTO 位置信息
     */
    public void putLastLocation(LocationProtoDTO locationProtoDTO) {
        locationHashOperations.put(LOCATION_REDIS_KEY, String.valueOf(locationProtoDTO.getTerminalProto().getTerminalId()), locationProtoDTO.getLocationProto());
    }


    /**
     * 获取终端最新位置
     *
     * @param terminalId 终端ID
     * @return
     */
    public LocationProto getLastLocation(Long terminalId) {
        return locationHashOperations.get(LOCATION_REDIS_KEY, String.valueOf(terminalId));
    }

}