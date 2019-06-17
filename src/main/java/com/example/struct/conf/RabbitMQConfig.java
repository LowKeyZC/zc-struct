package com.example.struct.conf;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ribbit mq 配置
 */
@Configuration
public class RabbitMQConfig {

    @Value("${hello_mq_key}")
    private String HELLO_MQ_KEY;

    @Bean
    public Queue helloQueue() {
        return new Queue(HELLO_MQ_KEY);
    }

    @Bean
    public Binding bindingHelloQueue(Queue helloQueue) {
        return BindingBuilder.bind(helloQueue).to(DirectExchange.DEFAULT).with(HELLO_MQ_KEY);
    }
    
}
