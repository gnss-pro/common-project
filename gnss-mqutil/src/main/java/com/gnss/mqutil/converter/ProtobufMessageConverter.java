package com.gnss.mqutil.converter;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: Protobuf转换器</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018/4/13
 */
@Slf4j
public class ProtobufMessageConverter implements MessageConverter {
    private static Map<String, Codec> codecMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections("com.gnss.common.proto");
        reflections.getTypesAnnotatedWith(ProtobufClass.class).stream()
                .forEach(clazz -> {
                    try {
                        Codec codec = (Codec) Class.forName(clazz.getName() + "$$JProtoBufClass").newInstance();
                        codecMap.put(clazz.getSimpleName(), codec);
                        log.info("加载Protobuf转换器{}", clazz.getName());
                    } catch (Exception e) {
                        log.error("加载Protobuf转换器{}失败", clazz.getName(), e);
                    }
                });
    }

    @Override
    public Message toMessage(Object obj, MessageProperties messageProperties) throws MessageConversionException {
        String messageType = obj.getClass().getSimpleName();
        Codec<Object> codec = codecMap.get(messageType);
        if (codec == null) {
            throw new MessageConversionException("不支持转换的消息类型:" + messageType);
        }
        messageProperties.setHeader("messageType", messageType);
        byte[] body;
        try {
            body = codec.encode(obj);
        } catch (Exception e) {
            throw new MessageConversionException("编码失败:" + messageType, e);
        }
        return new Message(body, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String messageType = message.getMessageProperties().getHeaders().get("messageType").toString();
        Codec<Object> codec = codecMap.get(messageType);
        if (codec == null) {
            throw new MessageConversionException("不支持转换的消息类型:" + messageType);
        }
        Object obj;
        try {
            obj = codec.decode(message.getBody());
        } catch (Exception e) {
            throw new MessageConversionException("解码失败:" + messageType, e);
        }
        return obj;
    }
}
