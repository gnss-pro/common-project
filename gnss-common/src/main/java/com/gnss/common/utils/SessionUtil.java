package com.gnss.common.utils;

import com.gnss.common.proto.TerminalProto;
import com.gnss.common.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: 会话工具</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/11/1
 */
@Slf4j
public class SessionUtil {

    private static final AttributeKey<Session> SESSION_ATTR = AttributeKey.newInstance("session");

    private static final Map<Long, Channel> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 终端注册,如果重复登录则将上一个连接断开
     *
     * @param session 会话
     * @param channel socket通道
     * @return 返回boolean
     */
    public static boolean bindSession(Session session, Channel channel) {
        final boolean[] isAbsent = {true};
        Long terminalId = session.getTerminalInfo().getTerminalId();
        SESSION_MAP.compute(terminalId, (k, v) -> {
            if (v != null) {
                v.close();
                isAbsent[0] = false;
            }
            return channel;
        });
        channel.attr(SESSION_ATTR).set(session);
        log.info("注册在线终端ID:{},数量:{}", terminalId, SESSION_MAP.size());
        return isAbsent[0];
    }

    /**
     * 终端注销,Channel跟注册的Channel相等才会移除
     *
     * @param channel socket通道
     * @return 返回boolean
     */
    public static boolean unbindSession(Channel channel) {
        Session session = channel.attr(SESSION_ATTR).get();
        if (session == null) {
            return false;
        }
        Long terminalId = session.getTerminalInfo().getTerminalId();
        SESSION_MAP.computeIfPresent(terminalId, (k, v) -> {
            if (Objects.equals(channel, v)) {
                return null;
            }
            return v;
        });
        channel.attr(SESSION_ATTR).set(null);
        log.info("注销在线终端ID:{},数量:{}", terminalId, SESSION_MAP.size());
        return true;
    }

    /**
     * 获取终端信息
     *
     * @param ctx ChannelHandlerContext
     * @return 终端信息
     */
    public static TerminalProto getTerminalInfo(ChannelHandlerContext ctx) {
        return ctx.channel().attr(SESSION_ATTR).get().getTerminalInfo();
    }

    /**
     * 获取终端信息
     *
     * @param channel socket通道
     * @return 终端信息
     */
    public static TerminalProto getTerminalInfo(Channel channel) {
        return channel.attr(SESSION_ATTR).get().getTerminalInfo();
    }

    /**
     * 获取手机号码数组
     * @param channel socket通道
     * @return 手机号码数组
     */
    public static byte[] getPhoneNum(Channel channel) {
        return channel.attr(SESSION_ATTR).get().getPhoneNumArr();
    }

    /**
     * 获取消息流水号
     *
     * @param channel socket通道
     * @return 消息流水号
     */
    public static int getNextMsgFlowId(Channel channel) {
        Session session = channel.attr(SESSION_ATTR).get();
        return session.getNextMsgFlowId();
    }

    /**
     * 是否登录
     *
     * @param ctx ChannelHandlerContext
     * @return 返回boolean
     */
    public static boolean isLogin(ChannelHandlerContext ctx) {
        return ctx.channel().hasAttr(SESSION_ATTR);
    }

    /**
     * 根据终端ID获取Channel
     * @param terminalId 终端ID
     * @return socket通道
     */
    public static Channel getChannel(Long terminalId) {
        return SESSION_MAP.get(terminalId);
    }
}
