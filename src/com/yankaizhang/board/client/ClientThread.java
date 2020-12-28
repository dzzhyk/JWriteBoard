package com.yankaizhang.board.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JOptionPane;

import com.yankaizhang.board.commons.MyShape;
import com.yankaizhang.board.util.Logger;

/**
 * 客户端线程
 */
public class ClientThread extends Thread {

	private final Logger log = Logger.getInstance();
	Socket socket = null;
	public List<String> onlineList = new CopyOnWriteArrayList<>();
	ObjectOutputStream objOut = null;
	ObjectInputStream objIn = null;

	public ClientThread(InetAddress address, int port) {
		try {
			this.socket = new Socket(address, port);
			// 连接之后首先发送用户名
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			out.println(ClientDraw.userName);
			out.flush();

			objOut = new ObjectOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());

			ClientDraw.isOnNet = true;
			log.debug("连接服务器成功 : userName : " + ClientDraw.userName);

		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "服务器连接错误", "连接错误", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			try {
				this.socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}


	@Override
	public void run() {
		while (true) {
			try {
				MyShape shape = (MyShape) objIn.readObject();
				ClientDraw.drawPanel.getDataAndRepaint(shape);
				log.debug("收到新内容，重新绘制");
			}catch(ClassNotFoundException | IOException e) {
				e.printStackTrace();
				try {
					this.socket.close();
					break;
				} catch (IOException ioException) {
					ioException.printStackTrace();
					break;
				}
			}
		}
	}


	/**
	 * 发送消息
	 */
	public void sendClientMsg(Object msg) {
		try {
			objOut.writeObject(msg);
			log.debug("发送内容");
		}catch(IOException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}

		}
	}

	@Override
	public void interrupt() {
		super.interrupt();
		try {
			objOut.close();
			objIn.close();
			socket.close();
			log.debug("断开连接成功");
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
