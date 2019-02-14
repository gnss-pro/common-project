package com.gnss.common.service;

import com.gnss.common.exception.ApplicationException;
import com.gnss.common.model.BaseMessage;
import com.gnss.common.proto.TerminalProto;
import com.gnss.common.utils.SessionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Description: 消息处理器</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2017/9/15
 */
@Getter
@Setter
public abstract class BaseMessageService<T extends BaseMessage> {

    private int messageId;

    private String strMessageId;

    private String desc;

    /**
     * 获取终端信息
     *
     * @param ctx ChannelHandlerContext
     * @return 终端信息
     */
    public TerminalProto getTerminalInfo(ChannelHandlerContext ctx) {
        return SessionUtil.getTerminalInfo(ctx.channel());
    }

    /**
     * 检查消息体长度
     *
     * @param msg 消息
     * @param msgBodyLen 消息体长度
     * @throws ApplicationException 应用异常
     */
    public void checkMessageBodyLen(T msg, int msgBodyLen) throws ApplicationException {
        byte[] msgBody = msg.getMsgBodyArr();
        if (msgBody.length < msgBodyLen) {
            throw new ApplicationException("消息体长度不对,不能小于" + msgBodyLen);
        }
    }

    /**
     * 处理消息
     *
     * @param ctx ChannelHandlerContext
     * @param msg 消息
     * @param msgBodyBuf 消息体
     * @return 返回结果
     * @throws Exception 异常
     */
    public abstract Object process(ChannelHandlerContext ctx, T msg, ByteBuf msgBodyBuf) throws Exception;
}
