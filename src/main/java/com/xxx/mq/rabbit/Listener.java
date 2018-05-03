package com.xxx.mq.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.xxx.util.LogUtils;

@Component
@RabbitListener(queues = "foo")
public class Listener {

	@Bean
	public Queue foo1Queue() {
		return new Queue("foo");
	}

	@RabbitHandler
	public void process(@Payload String foo) {
		LogUtils.info(Listener.class,"Listener: " + foo);
	}
}
