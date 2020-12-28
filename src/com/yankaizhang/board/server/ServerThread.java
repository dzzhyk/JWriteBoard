package com.yankaizhang.board.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JTextArea;

import com.yankaizhang.board.commons.MyShape;
import com.yankaizhang.board.util.Logger;

/**
 * 服务线程
 */
public class ServerThread implements Runnable {

	private final Logger log = Logger.getInstance();
	private Socket socket = null;
	private ObjectOutputStream objOut = null;
	private ObjectInputStream objIn = null;
	JTextArea textArea = null;

	public ServerThread(Socket src, JTextArea textArea) {
		this.socket = src;
		this.textArea = textArea;
		String clientName = "";
		try {
			//获得客户端的名字
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			clientName = in.readLine();

		} catch(IOException e) {
			e.printStackTrace();
		}

		ServerAcceptor.registerThread(clientName, this);
		ServerAcceptor.showServerMsg("\n [新连接] ip: " + socket.getInetAddress() + " user: " + clientName);
		log.debug("\n [新连接] ip: " + socket.getInetAddress() + " user: " + clientName);
	}

	@Override
	public void run() {

		// 主要对象流的创建顺序
		try {
			objIn = new ObjectInputStream(socket.getInputStream());
			objOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// 监听是否有数据传入
		while(true) {
			try {
				MyShape shape = (MyShape) objIn.readObject();
				shape.onlineList = new CopyOnWriteArrayList<>(ServerAcceptor.getThreadMap().keySet());
				sendClientMsg(shape, this);
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
				break;
			} catch(IOException e) {
				ServerAcceptor.removeThread(this);
				ServerAcceptor.showServerMsg("\n [断开连接] user: "+ Thread.currentThread().getName() +", ip: " + this.socket.getInetAddress());
				log.debug("\n [断开连接] user: "+ Thread.currentThread().getName() +", ip: " + this.socket.getInetAddress());
				break;
			}
		}
	}

	/**
	 * 向客服端广播消息(画板内容)
	 */
	private void sendClientMsg(MyShape shape, ServerThread src) {
		ServerAcceptor.broadcastMsg(shape, src);
	}

	public ObjectOutputStream getObjOut() {
		return objOut;
	}
}
