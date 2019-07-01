package com.example.struct.rabbitmq.reciever;

import com.alibaba.fastjson.JSON;
import com.example.struct.common.Constant;
import com.example.struct.common.MqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费消息
 */
@Component
@RabbitListener(queues = Constant.LOCAL_COMMON_QUEUE)
public class HelloReciever {

    private static final Logger logger = LoggerFactory.getLogger(HelloReciever.class);
    
    private static final String LOCAL_COMMON_QUEUE = Constant.LOCAL_COMMON_QUEUE;
	
    @RabbitHandler
    public void process(String mqDtoStr) {
    	logger.info("消费队列：" + LOCAL_COMMON_QUEUE + "正在消费");

    	try {
			MqDto mqDto = JSON.parseObject(mqDtoStr, MqDto.class);
			if (mqDto.getMsgType().equals(Constant.MQ_TYPE_ONE)) {
				System.out.println("receive mq type one");
			} else {
				System.out.println("receive mq type two");
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	}

}
