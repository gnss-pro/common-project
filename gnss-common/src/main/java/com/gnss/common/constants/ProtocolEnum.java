package com.gnss.common.constants;

import lombok.Getter;

/**
 * <p>Description: 通信协议</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2017/6/19
 */
public enum ProtocolEnum {
    /**
     * 部标JT808
     */
    JT808("JT808");

    @Getter
    private String desc;

    ProtocolEnum(String desc) {
        this.desc = desc;
    }
}
