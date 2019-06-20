package com.example.struct.rabbitmq.reciever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消费消息
 */
@Component
@RabbitListener(queues = "${hello_mq_key}")
public class HelloReciever {

    private static final Logger logger = LoggerFactory.getLogger(HelloReciever.class);
    
    @Value("${hello_mq_key}")
    private String helloMqKey;
	
    @RabbitHandler
    public void process(String json) {
    	logger.info("消费队列：" + helloMqKey + "正在消费");

    	try {
    		logger.info(json);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return;
    	}
	}

}
