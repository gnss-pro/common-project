package com.gnss.common.api;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 通用应答</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/2/3
 */
@Data
public class CommonReplyParam {

    /**
     * 应答消息ID
     */
    private String replyMessageId;

    /**
     * 结果
     */
    private String result;

    /**
     * 附加信息
     */
    private Map<String, Object> extraInfo = new HashMap<>();

    public Map<String, Object> setExtraInfo(String key, Object value) {
        extraInfo.put(key, value);
        return extraInfo;
    }
}
