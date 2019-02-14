package com.gnss.common.annotations;

import com.gnss.common.constants.CommonConstants;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MessageService {

    /**
     * 消息ID
     *
     * @return 返回消息ID
     */
    int messageId() default CommonConstants.DEFAULT_MESSAGE_ID;

    /**
     * 字符串类型的消息ID
     *
     * @return 返回字符串类型的消息ID
     */
    String strMessageId() default "";

    /**
     * 消息类型描述
     *
     * @return 返回消息类型描述
     */
    String desc() default "不支持消息";

}
