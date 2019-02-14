package com.gnss.common.security;

import com.gnss.common.constants.RoleTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>Description: JWT详情</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2017/6/19
 */
@Getter
@Setter
@ToString
public class AuthTokenDetails {
    /**
     * APP ID
     */
    private String appId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 组织ID
     */
    private Long organizationId;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 角色类型
     */
    private RoleTypeEnum roleType;
    /**
     * 过期时间
     */
    private Date expirationDate;
    /**
     * 语言环境
     */
    private String language;
}
