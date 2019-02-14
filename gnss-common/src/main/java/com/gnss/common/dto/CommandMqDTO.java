package com.gnss.common.dto;

import lombok.Data;

import java.util.Map;

/**
 * <p>Description: 指令队列传输</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/2/3
 */
@Data
public class CommandMqDTO {
    private Map<String, Object> params;
    private TerminalDTO terminalDto;
}
