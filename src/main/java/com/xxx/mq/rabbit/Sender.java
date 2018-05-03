package com.xxx.mq.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xxx.util.StringUtils;

@Component
public class Sender {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public void send(String msg){
		if(StringUtils.isNotBlank(msg)) {
			rabbitTemplate.convertAndSend("foo",msg);
		}
	}
}
