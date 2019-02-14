package com.gnss.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: JT808工具</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.7
 * @date 2018-12-13
 */
public class Jt808Util {

    private static Map<Integer, Class<?>> paramTypeMap = new HashMap<>();

    static {
        for (int i = 0x0001; i <= 0x0007; i++) {
            paramTypeMap.put(i, Integer.class);
        }
        for (int i = 0x0010; i <= 0x0017; i++) {
            paramTypeMap.put(i, String.class);
        }
        paramTypeMap.put(0x0018, Integer.class);
        paramTypeMap.put(0x0019, Integer.class);
        paramTypeMap.put(0x001A, String.class);
        paramTypeMap.put(0x001B, Integer.class);
        paramTypeMap.put(0x001C, Integer.class);
        paramTypeMap.put(0x001D, String.class);
        paramTypeMap.put(0x0020, Integer.class);
        paramTypeMap.put(0x0021, Integer.class);
        paramTypeMap.put(0x0022, Integer.class);
        paramTypeMap.put(0x0027, Integer.class);
        paramTypeMap.put(0x0028, Integer.class);
        paramTypeMap.put(0x0029, Integer.class);
        for (int i = 0x002C; i <= 0x0030; i++) {
            paramTypeMap.put(i, Integer.class);
        }
        paramTypeMap.put(0x0031, Short.class);
        for (int i = 0x0040; i <= 0x0044; i++) {
            paramTypeMap.put(i, String.class);
        }
        paramTypeMap.put(0x0045, Integer.class);
        paramTypeMap.put(0x0046, Integer.class);
        paramTypeMap.put(0x0047, Integer.class);
        paramTypeMap.put(0x0048, String.class);
        paramTypeMap.put(0x0049, String.class);
        for (int i = 0x0050; i <= 0x005A; i++) {
            paramTypeMap.put(i, Integer.class);
        }
        for (int i = 0x005B; i <= 0x005E; i++) {
            paramTypeMap.put(i, Short.class);
        }
        paramTypeMap.put(0x0064, Integer.class);
        paramTypeMap.put(0x0065, Integer.class);
        for (int i = 0x0070; i <= 0x0074; i++) {
            paramTypeMap.put(i, Integer.class);
        }
        paramTypeMap.put(0x0080, Integer.class);
        paramTypeMap.put(0x0081, Short.class);
        paramTypeMap.put(0x0082, Short.class);
        paramTypeMap.put(0x0083, String.class);
        paramTypeMap.put(0x0084, Byte.class);
        paramTypeMap.put(0x0090, Byte.class);
        paramTypeMap.put(0x0091, Byte.class);
        paramTypeMap.put(0x0092, Byte.class);
        paramTypeMap.put(0x0093, Integer.class);
        paramTypeMap.put(0x0094, Byte.class);
        paramTypeMap.put(0x0095, Integer.class);
        paramTypeMap.put(0x0100, Integer.class);
        paramTypeMap.put(0x0101, Short.class);
        paramTypeMap.put(0x0102, Integer.class);
        paramTypeMap.put(0x0103, Short.class);
    }

    private Jt808Util() {
    }

    /**
     * 获取JT808终端参数的类型
     * @param paramId 参数ID
     * @return 参数类型
     */
    public static Class<?> getParamType(int paramId) {
        return paramTypeMap.get(paramId);
    }

}
