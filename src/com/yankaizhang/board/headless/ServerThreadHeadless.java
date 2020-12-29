package com.yankaizhang.board.headless;

import com.yankaizhang.board.commons.MyShape;
import com.yankaizhang.board.util.Logger;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 服务线程
 */
public class ServerThreadHeadless implements Runnable {

	private final Logger log = Logger.getInstance();
	private Socket socket = null;
	private ObjectOutputStream objOut = null;
	private ObjectInputStream objIn = null;
	private final String clientName;

	public ServerThreadHeadless(Socket src) {
		String clientName1;
		this.socket = src;
		clientName1 = "";
		try {
			// 获得客户端的名字
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			clientName1 = in.readLine();

		} catch(IOException e) {
			e.printStackTrace();
			try {
				disConnect();
			} catch (IOException ioException) {

			}
		}

		clientName = clientName1;
		try {
			ServerAcceptorHeadless.registerThread(clientName, this);
			log.debug("[新连接] 客户端ip: " + socket.getInetAddress().getHostAddress() + " 客户端user: " + clientName);
		}catch (RuntimeException re){
			try {
				disConnect();
			}catch (IOException e){

			}
		}
	}

	@Override
	public void run() {
		try {
			objIn = new ObjectInputStream(socket.getInputStream());
			objOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			try {
				disConnect();
				return;
			}catch (IOException e1){
				return;
			}
		}

		log.debug("开始监听 : " + this.clientName);
		// 监听是否有数据传入
		while(true) {
			try {
				MyShape shape = (MyShape) objIn.readObject();
				shape.onlineList = new CopyOnWriteArrayList<>(ServerAcceptorHeadless.getThreadMap().keySet());
				sendClientMsg(shape);
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			} catch(IOException e) {
				try {
					disConnect();
					break;
				} catch (IOException ioException) {
					ioException.printStackTrace();
					break;
				}
			}
		}
		log.debug("终止监听 : " + this.clientName);
	}

	/**
	 * 向客服端广播消息(画板内容)
	 */
	private void sendClientMsg(MyShape shape) {
		ServerAcceptorHeadless.broadcastMsg(shape);
	}

	public ObjectOutputStream getObjOut() {
		return objOut;
	}

	private void disConnect() throws IOException {
		if (objIn != null && !socket.isClosed()) objIn.close();
		if (objOut != null && !socket.isClosed()) objOut.close();
		if (this.socket != null && !socket.isClosed()) this.socket.close();
		ServerAcceptorHeadless.removeThread(this);
		log.debug("[断开连接] 客户端user: "+ Thread.currentThread().getName() +", ip: " + this.socket.getInetAddress());
	}
}
