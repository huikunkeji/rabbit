package com.xxx.mq.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value= {"com.xxx.mq.rabbit","com.xxx.util"})
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
