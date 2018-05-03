package com.xxx.mq.rabbit;

import com.xxx.util.SpringUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {

	private Sender sender;

	public DiscardServerHandler() {
		sender = SpringUtil.getBean(Sender.class);
	}

	/**
	 * 每当从客户端收到新的数据时， 这个方法会在收到消息时被调用
	 * @param ctx 通道处理的上下文信息
	 * @param msg 接收的消息
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			ByteBuf buf = (ByteBuf) msg;
			byte[] data = new byte[buf.readableBytes()];
			buf.readBytes(data);
			String request = new String(data, "utf-8");
			sender.send(request);
			System.out.println("Server: " + request);
			// 写给客户端
			String response = "我是反馈的信息";
			ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
		} catch (Exception e) {
			// 抛弃接收到的数据
			ReferenceCountUtil.release(msg);
			e.printStackTrace();
		}
	}

	/**
	 * 发生异常时触发
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		try {
			/**
			 * exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，即当 Netty 由于 IO
			 * 错误或者处理器在处理事件时抛出的异常时。在大部分情况下，捕获的异常应该被记录下来 并且把关联的 channel
			 * 给关闭掉。然而这个方法的处理方式会在遇到不同异常的情况下有不 同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
			 */
			cause.printStackTrace();
			ctx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}