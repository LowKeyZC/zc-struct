package com.example.struct.rabbitmq.sender;

import com.example.struct.common.Constant;
import com.example.struct.common.MqDto;
import com.example.struct.util.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 通用队列消息发送器
 * 学前分享统计生产者
 * <pre>
 *     基本的发送mq消息都可以在这个类添加方法
 * </pre>
 *
 */
@Component
public class HelloSender {

    private static final Logger log = LoggerFactory.getLogger(HelloSender.class);

    private static final String LOCAL_COMMON_QUEUE = Constant.LOCAL_COMMON_QUEUE;
	
    @Resource
    private AmqpTemplate amqpTemplate;

    /** 发送消息到mq */
    public void send(MqDto mqDto) {
    	try {
    		amqpTemplate.convertAndSend(LOCAL_COMMON_QUEUE, mqDto.toString());
    	} catch (Throwable e) {
			log.error("---------creater-------------加入失败 "+ LOCAL_COMMON_QUEUE +" 队列",e);
			e.printStackTrace();
		}
    }


}
