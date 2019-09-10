package com.example.struct.rabbitmq.reciever;

import com.alibaba.fastjson.JSON;
import com.example.struct.common.Constant;
import com.example.struct.common.MqDto;
import com.example.struct.util.RandomUtil;
import com.example.struct.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消费消息
 */
@Component
@RabbitListener(queues = Constant.LOCAL_COMMON_QUEUE)
public class HelloReciever {
	private static final Logger logger = LoggerFactory.getLogger(HelloReciever.class);
	private static final String MQ_MSG_KEYS = "MQ_MSG_KEY";

	@Resource
	private RedisUtil redisUtil;

	@RabbitHandler
	public void process(String mqDtoStr) {
		logger.info("消费队列：" + Constant.LOCAL_COMMON_QUEUE + "正在消费");
		String msgMd5Key = MQ_MSG_KEYS + RandomUtil.getMd5Str(mqDtoStr);
		if (!redisUtil.setnx(msgMd5Key, "1")) {
			logger.error("mq 重复消费 msg:{}", mqDtoStr);
			return;
		}
		redisUtil.expire(msgMd5Key, 100);
		logger.info("mq 成功消费 msg:{}", mqDtoStr);

		try {
			MqDto mqDto = JSON.parseObject(mqDtoStr, MqDto.class);
			if (mqDto.getMsgType().equals(Constant.MQ_TYPE_ONE)) {
				System.out.println("receive mq type one");
			} else if (mqDto.getMsgType().equals(Constant.MQ_TYPE_TWO)) {
				System.out.println("receive mq type two");
			} else {
				System.out.println("wrong mq type!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}