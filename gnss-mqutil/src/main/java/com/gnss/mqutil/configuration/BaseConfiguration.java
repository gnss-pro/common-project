package com.gnss.mqutil.configuration;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.gnss.common.service.RedisService;
import com.gnss.common.utils.CommonUtil;
import com.gnss.mqutil.converter.ProtobufMessageConverter;
import com.gnss.mqutil.producer.RabbitMessageSender;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>Description: 基础bean配置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018-12-30
 */
public class BaseConfiguration {

    @Value("${gnss.middleware-ip}")
    private String host;

    /**
     * Redis模板
     *
     * @param connectionFactory 连接工厂
     * @return
     */
    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        stringRedisTemplate.setValueSerializer(genericFastJsonRedisSerializer);
        stringRedisTemplate.setHashValueSerializer(genericFastJsonRedisSerializer);
        return stringRedisTemplate;
    }

    /**
     * Redis服务
     *
     * @param redisTemplate Redis模板
     * @return
     */
    @Bean
    public RedisService redisService(StringRedisTemplate redisTemplate) {
        return new RedisService(redisTemplate);
    }

    /**
     * 配置接收消息的MessageConverter
     *
     * @param connectionFactory 连接工厂
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(50);
        factory.setConcurrentConsumers(5);
        factory.setMessageConverter(new ProtobufMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

    /**
     * 消息发送
     *
     * @param rabbitTemplate Rabbit模板
     * @param redisService   Redis服务
     * @return
     */
    @Bean
    public RabbitMessageSender rabbitMessageSender(RabbitTemplate rabbitTemplate, RedisService redisService) {
        CommonUtil.printCopyright();
        return new RabbitMessageSender(rabbitTemplate, redisService);
    }

}
