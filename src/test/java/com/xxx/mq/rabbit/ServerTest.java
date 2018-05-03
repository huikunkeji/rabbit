package com.xxx.mq.rabbit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

	private ServerSocket ss;
	private Socket socket;
	PrintWriter out;
	private int i = 0;

	public ServerTest() {
		try {
			ss = new ServerSocket(7000);
			while (true) {
				System.out.println(0);
				socket = ss.accept();
				ss.setSoTimeout(50000);
				String bw = "$GPGSV,2,1,08,06,33,240,45,10,36,074,47,16,21,078,44,17,36,313,42*78";
				byte[] b = bw.getBytes();


				InputStream socketReader = socket.getInputStream();
				OutputStream socketWriter = socket.getOutputStream();
				socketWriter.write(b);
				System.out.println("OK");
				socketWriter.flush();
				i = i + 1;
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ServerTest();
//		String bw = "$GPGSV,2,1,08,06,33,240,45,10,36,074,47,16,21,078,44,17,36,313,42*78";
//		byte[] bytes = bw.getBytes();
//		System.out.println(checksum(bytes));
	}

	public static boolean checksum(byte[] b) {
		byte chk = 0;// 校验和
		byte cb = b[1];// 当前字节
		int i = 0;
		if (b[0] != '$')
			return false;
		for (i = 2; i < b.length; i++)// 计算校验和
		{
			if (b[i] == '*')
				break;
			cb = (byte) (cb ^ b[i]);
		}
		if (i != b.length - 3)// 校验位不正常
			return false;
		i++;
		byte[] bb = new byte[2];// 用于存放语句后两位
		bb[0] = b[i++];
		bb[1] = b[i];
		try {
			chk = (byte) Integer.parseInt(new String(bb), 16);// 后两位转换为一个字节
		} catch (Exception e)// 后两位无法转换为一个字节，格式错误
		{
			return false;
		}
		System.out.println("校验信息");
		System.out.println(" 原文：" + chk);
		System.out.println(" 计算：" + cb);
		return chk == cb;// 计算出的校验和与语句的数据是否一致

	}
}