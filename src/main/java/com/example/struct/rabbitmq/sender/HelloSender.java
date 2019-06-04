package com.example.struct.rabbitmq.sender;

import com.example.struct.util.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
	private static final String DEFAULT_EXCHANGE = PropertyUtils.getString("stone-exchange");

    private static final Logger log = LoggerFactory.getLogger(HelloSender.class);
    @Value("${hello_mq_key}")
    private String HELLO_MQ_KEY;
	
    @Autowired
    private AmqpTemplate amqpTemplate;

    /** 发送消息到mq */
    public void send(Object object) {
    	try {
    		log.info("---------creater-------------开始加入 {} 队列", HELLO_MQ_KEY);
    		amqpTemplate.convertAndSend(DEFAULT_EXCHANGE, HELLO_MQ_KEY, object);
    		log.info("---------creater-------------成功加入 {} 队列", HELLO_MQ_KEY);
    	} catch (Throwable e) {
			log.error("---------creater-------------加入失败 {"+ HELLO_MQ_KEY +"} 队列",e);
			e.printStackTrace();
		}
    }


}
