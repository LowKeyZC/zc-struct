package com.example.struct.rabbitmq.sender;

import com.example.struct.common.Constant;
import com.example.struct.common.MqDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

/**
 * 通用队列消息发送器
 * 学前分享统计生产者
 * <pre>
 *     基本的发送mq消息都可以在这个类添加方法
 * </pre>
 */
@Component
public class HelloSender {
    private static final Logger log = LoggerFactory.getLogger(HelloSender.class);

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送本地消息到mq服务器
     */
    public void sendToLocal(MqDto mqDto) {
        try {
            log.info("发送消息,QUEUE:" + Constant.LOCAL_COMMON_QUEUE + " 时间" + new Date());
            rabbitTemplate.convertAndSend(Constant.LOCAL_COMMON_QUEUE, mqDto.toString());
        } catch (Throwable e) {
            log.error("加入失败 " + Constant.LOCAL_COMMON_QUEUE + " 队列", e);
        }
    }

    public void sendToThird(String thirdModule, MqDto mqDto) {
        try {
            log.info("now:" + new Date());
            rabbitTemplate.convertAndSend(thirdModule, mqDto);
        } catch (Throwable e) {
            log.error("加入失败 " + thirdModule + " 队列", e);
        }
    }
}
