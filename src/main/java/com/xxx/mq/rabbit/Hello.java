package com.xxx.mq.rabbit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/")
public class Hello {

	@Autowired
	private Sender sender;
	
	@RequestMapping(value="/t")
	public String t(String msg) {
		sender.send(msg);
		return "web/index";
	}
}
