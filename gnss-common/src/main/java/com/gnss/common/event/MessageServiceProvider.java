package com.gnss.common.event;

import com.gnss.common.annotations.MessageService;
import com.gnss.common.constants.CommonConstants;
import com.gnss.common.exception.ApplicationException;
import com.gnss.common.service.BaseMessageService;
import com.gnss.common.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Description: 消息处理器提供</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/9/15
 */
@Slf4j
public class MessageServiceProvider implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<Integer, BaseMessageService<?>> serviceMap = new HashMap<>();

    private Map<String, BaseMessageService<?>> strServiceMap = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(MessageService.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
            registerMessageService(entry.getValue());
        }
        if (!serviceMap.containsKey(CommonConstants.UNSUPPORT_MESSAGE_ID) && !strServiceMap.containsKey(CommonConstants.UNSUPPORTED_STR_MESSAGE_ID)) {
            throw new ApplicationException("请创建UnsupportedMessageService");
        }
    }

    /**
     * 注册消息处理器
     *
     * @param registerObj
     */
    private void registerMessageService(Object registerObj) {
        Class<?> clazz = registerObj.getClass();
        if (BaseMessageService.class.isAssignableFrom(clazz)) {
            MessageService annotation = clazz.getAnnotation(MessageService.class);
            int messageId = annotation.messageId();
            String strMessageId = annotation.strMessageId();
            String desc = annotation.desc();
            BaseMessageService<?> messageService = (BaseMessageService<?>) registerObj;
            messageService.setDesc(desc);
            if (messageId != CommonConstants.DEFAULT_MESSAGE_ID) {
                messageService.setMessageId(messageId);
                serviceMap.put(messageId, messageService);
                String formatMsgId = messageId == CommonConstants.UNSUPPORT_MESSAGE_ID ? String.valueOf(CommonConstants.UNSUPPORT_MESSAGE_ID) : CommonUtil.formatMessageId(messageId);
                log.info("注册消息处理器,消息类型:{},消息描述:{},处理器:{}", formatMsgId, desc, clazz.getName());
            } else if (!Objects.equals("", strMessageId)) {
                messageService.setStrMessageId(strMessageId);
                strServiceMap.put(strMessageId, messageService);
                log.info("注册消息处理器,消息类型:{},消息描述:{},处理器:{}", strMessageId, desc, clazz.getName());
            }
        }
    }

    /**
     * 获取消息处理器
     *
     * @param messageId 消息ID
     * @return 返回BaseMessageService
     */
    public BaseMessageService getMessageService(int messageId) {
        BaseMessageService<?> messageService = serviceMap.get(messageId);
        if (messageService == null) {
            return serviceMap.get(CommonConstants.UNSUPPORT_MESSAGE_ID);
        }
        return messageService;
    }

    /**
     * 获取消息处理器
     *
     * @param strMessageId 字符串类型消息ID
     * @return 返回BaseMessageService
     */
    public BaseMessageService getMessageService(String strMessageId) {
        BaseMessageService<?> messageService = strServiceMap.get(strMessageId);
        if (messageService == null) {
            return strServiceMap.get(CommonConstants.UNSUPPORTED_STR_MESSAGE_ID);
        }
        return messageService;
    }
}
