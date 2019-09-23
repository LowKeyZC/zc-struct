package com.example.struct.conf;

import com.example.struct.common.Constant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ribbit mq 配置
 */
//@Configuration
public class RabbitMQConfig {
    /* 定义队列 */
    @Bean
    public Queue helloQueue() {
        return new Queue(Constant.LOCAL_COMMON_QUEUE);
    }
}
