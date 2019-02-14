package com.gnss.common.session;

import com.gnss.common.proto.TerminalProto;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.MutablePair;

/**
 * <p>Description: 通信会话</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/11/1
 */
public class Session {

    /**
     * 消息流水号最小值
     */
    public static final int DEFAULT_MIN_MSG_FLOW_ID = 0x0001;

    /**
     * 消息流水号最大值
     */
    public static final int DEFAULT_MAX_MSG_FLOW_ID = 0x7FFF;

    /**
     * 终端信息
     */
    @Getter
    @Setter
    private TerminalProto terminalInfo;

    /**
     * 手机号数组
     */
    @Getter
    @Setter
    private byte[] phoneNumArr;

    /**
     * 消息流水号范围
     */
    private final MutablePair<Integer, Integer> msgFlowIdRange;

    /**
     * 当前消息流水号
     */
    private int currentMsgFlowId;

    public Session(TerminalProto terminalInfo) {
        this.msgFlowIdRange = MutablePair.of(DEFAULT_MIN_MSG_FLOW_ID, DEFAULT_MAX_MSG_FLOW_ID);
        this.terminalInfo = terminalInfo;
    }

    public Session(Integer minMsgFlowId, Integer maxMsgFlowId, TerminalProto terminalInfo) {
        this.msgFlowIdRange = MutablePair.of(minMsgFlowId, maxMsgFlowId);
        this.terminalInfo = terminalInfo;
    }

    /**
     * 获取消息流水号，并设置下个消息流水号
     *
     * @return 返回消息流水号
     */
    public int getNextMsgFlowId() {
        int msgFlowId = currentMsgFlowId;
        int minMsgFlowId = msgFlowIdRange.getLeft();
        int maxMsgFlowId = msgFlowIdRange.getRight();
        currentMsgFlowId = currentMsgFlowId >= maxMsgFlowId ? minMsgFlowId : currentMsgFlowId + 1;
        return msgFlowId;
    }
}
