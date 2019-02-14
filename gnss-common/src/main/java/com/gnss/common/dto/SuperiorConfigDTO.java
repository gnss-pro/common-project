package com.gnss.common.dto;

import lombok.Data;

/**
 * <p>Description: JT809上级平台配置DTO</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/2/3
 */
@Data
public class SuperiorConfigDTO {

    /**
     * 上级平台名称
     */
    private String name;

    /**
     * 是否启用
     */
    private String isEnable;

    /**
     * 上级平台IP
     */
    private String superiorIp;

    /**
     * 上级平台端口
     */
    private Integer superiorPort;

    /**
     * 本地IP
     */
    private String localIp;

    /**
     * 本地端口
     */
    private Integer localPort;

    /**
     * 接入码
     */
    private Integer centerId;

    /**
     * 用户名
     */
    private Integer userId;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否加密
     */
    private String isCrypto;

    /**
     * 加密密钥
     */
    private Integer cryptoKey;

    /**
     * 加密元素M1
     */
    private Integer m1;

    /**
     * 加密元素IA1
     */
    private Integer ia1;

    /**
     * 加密元素IC1
     */
    private Integer ic1;

    /**
     * 平台编码
     */
    private String platformId;

    /**
     * 版本标识
     */
    private byte[] versionArr;
}
