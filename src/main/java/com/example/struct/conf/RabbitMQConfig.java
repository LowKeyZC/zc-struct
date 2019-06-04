package com.example.struct.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ribbit mq 配置
 */
@Configuration
public class RabbitMQConfig {
	@Value("${stone-exchange}")
    private String DEFAULT_EXCHANGE ;

    //学前分享统计
    @Value("${hello_mq_key}")
    private String HELLO_MQ_KEY;
    
    /**
     * stone-exchange
     */
    @Bean
    public DirectExchange stoneExchange() {
        return new DirectExchange(DEFAULT_EXCHANGE);
    }
    
    /*************************hello-mq**********************************************/
    @Bean
    public Queue helloQueue() {
        return new Queue(HELLO_MQ_KEY);
    }

    @Bean
    public Binding bindingHelloQueue(Queue helloQueue, DirectExchange stoneExchange) {
        return BindingBuilder.bind(helloQueue).to(stoneExchange).with(HELLO_MQ_KEY);
    }
    
}
