package com.xxx.mq.rabbit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {

	private int port;

	public DiscardServer(int port) {
		super();
		this.port = port;
	}

	public void run() throws Exception {
		ServerBootstrap bootstrap = new ServerBootstrap();
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		try {

			bootstrap = bootstrap.group(bossGroup, workerGroup);

			// 指定NIO的模式接受新的链接
			bootstrap = bootstrap.channel(NioServerSocketChannel.class);

			// 配置具体的数据处理方式
			bootstrap = bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new DiscardServerHandler());
				}
			});

			// socket超出已完成三次握手的请求的队列的最大长度
			bootstrap = bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			// 设置发送缓冲大小
			//bootstrap = bootstrap.option(ChannelOption.SO_SNDBUF, 32 * 1024);
			// 这是接收缓冲大小
			bootstrap = bootstrap.option(ChannelOption.SO_RCVBUF, 32 * 1024);

			// 心跳保活,保持连接
			bootstrap = bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

			// 绑定端口并启动去接收进来的连接
			ChannelFuture sync = bootstrap.bind(port).sync();

			// 这里会一直等待，直到socket被关闭
			sync.channel().closeFuture().sync();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 优雅的释放线程池资源
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
