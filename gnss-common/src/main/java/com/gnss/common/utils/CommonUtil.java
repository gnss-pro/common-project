package com.gnss.common.utils;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.time.LocalDate;

/**
 * <p>Description: 工具类</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/9/16
 */
@Slf4j
public class CommonUtil {

    private CommonUtil() {
    }

    /**
     * 格式化消息ID(转成0xXXXX)
     *
     * @param msgId 消息ID
     * @return 格式化字符串
     */
    public static String formatMessageId(int msgId) {
        return toHexFormat(msgId, 4);
    }

    /**
     * 格式化数字
     *
     * @param num  数字
     * @param size 长度
     * @return 格式化字符串
     */
    public static String toHexFormat(int num, int size) {
        return "0x" + hexStr(num, size);
    }

    /**
     * 转4位的十六进制字符串
     *
     * @param num 数字
     * @return 格式化字符串
     */
    public static String hexStr(int num) {
        return hexStr(num, 4);
    }

    /**
     * 转十六进制字符串
     *
     * @param num  数字
     * @param size 长度
     * @return 格式化字符串
     */
    public static String hexStr(int num, int size) {
        return StringUtils.leftPad(Integer.toHexString(num).toUpperCase(), size, '0');
    }

    /**
     * 获取客户端IP
     *
     * @param ctx ChannelHandlerContext
     * @return 返回IP
     */
    public static String getClientIp(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        return insocket.getAddress().getHostAddress();
    }

    /**
     * 获取客户端端口
     *
     * @param ctx ChannelHandlerContext
     * @return 返回端口
     */
    public static int getClientPort(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        return insocket.getPort();
    }

    /**
     * 获取客户端IP信息
     *
     * @param ctx ChannelHandlerContext
     * @return 返回IP
     */
    public static String getClientAddress(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        return String.format("%s:%d", insocket.getAddress().getHostAddress(), insocket.getPort());
    }

    /**
     * 打印版权信息
     */
    public static void printCopyright() {
        log.info("====================================================================================");
        log.info("此程序未经授权不得擅自复制、传播、修改，如有上述行为我司均保留追究法律责任的权利。");
        log.info("@Copyright: " + LocalDate.now().getYear() + " www.gps-pro.cn All rights reserved.");
        log.info("====================================================================================");
    }
}