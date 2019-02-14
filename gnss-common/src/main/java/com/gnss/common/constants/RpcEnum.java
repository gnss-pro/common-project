package com.gnss.common.constants;

import lombok.Getter;

/**
 * <p>Description: RPC消息类型</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/2/3
 */
@Getter
public enum RpcEnum {

    /**
     * 上级平台配置
     */
    SUPERIOR_CONFIG("上级平台配置");

    private String name;

    RpcEnum(String name) {
        this.name = name;
    }
}
