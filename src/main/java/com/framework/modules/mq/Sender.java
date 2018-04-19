package com.framework.modules.mq;


import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender implements RabbitTemplate.ConfirmCallback, ReturnCallback{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RabbitTemplate template;
	
	@PostConstruct
    public void init() {
		template.setConfirmCallback(this);
		template.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) { 
        	logger.info("消息发送成功:{}",correlationData);
            System.out.println("消息发送成功:" + correlationData);  
        } else {  
            logger.warn("消息发送失败:{}",cause);  
        }  
        
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println(message.getMessageProperties().getCorrelationIdString() + " 发送失败");
        
    }

    public void send(String exchangName,String routingKey,String msg){
        
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        
        logger.info("开始发送消息 :{} ",msg);
        //String response = template.convertSendAndReceive(exchangName,routingKey, msg, correlationId).toString();
        template.convertAndSend(exchangName,routingKey, msg, correlationId);
        logger.info("结束发送消息 :{}",msg);
    }

}
