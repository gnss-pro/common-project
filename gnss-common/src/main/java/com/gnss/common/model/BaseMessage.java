package com.gnss.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>Description: 基础消息模型</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/9/15
 */
@Getter
@Setter
public class BaseMessage implements Serializable {

    /**
     * 消息体
     */
    private byte[] msgBodyArr;
}
