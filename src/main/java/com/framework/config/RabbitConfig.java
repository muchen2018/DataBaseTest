package com.framework.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
	
	public static final String TOPIC_EXCHANGE="topicExchange";
	
	public static final String ROUTING_KEY="routingKey1";

    @Bean
    public Queue queue1() {
        return new Queue("hello.queue1", true); // true表示持久化该队列
    }
    
    /**  
     * 针对消费者配置  
     * 1. 设置交换机类型  
     * 2. 将队列绑定到交换机  
        FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念  
        HeadersExchange ：通过添加属性key-value匹配  
        DirectExchange:按照routingkey分发到指定队列  
        TopicExchange:多关键字匹配  
     */  
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    //绑定
    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(queue1()).to(topicExchange()).with(ROUTING_KEY);
    }
	
}
