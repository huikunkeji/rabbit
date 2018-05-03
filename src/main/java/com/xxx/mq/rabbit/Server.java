package com.xxx.mq.rabbit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.xxx.util.LogUtils;

@Component
public class Server implements CommandLineRunner,Ordered {

	@Override
	public void run(String... args) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int port = 7000;
					DiscardServer discardServer = new DiscardServer(port);
					discardServer.run();
					LogUtils.info(getClass(), "Server started");
				} catch (Exception e) {
					e.printStackTrace();
					LogUtils.error(getClass(), "Server start error:", e);
				}
			}
		}).start();
		System.out.println("====================");
	}

	@Override
	public int getOrder() {
		return 1;
	}

}
