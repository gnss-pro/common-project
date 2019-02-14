package com.gnss.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Description: 终端信息DTO</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2017/11/3
 */
@Getter
@Setter
@ToString
public class TerminalDTO {
    private Long terminalId;
    private String terminalSimCode;
    private String terminalNum;
    private String vehicleNo;
}
