package com.yankaizhang.board.server;

import com.yankaizhang.board.util.Logger;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.*;
import javax.swing.JTextArea;

/**
 * 服务端接收线程
 */
public class ServerAcceptor extends Thread {

	private final Logger log = Logger.getInstance();
	private static ServerSocket serversocket = null;
	private static JTextArea textArea;
	private static int port;

	/** 线程Map */
	private static final Map<String, ServerThread> threadMap = new ConcurrentHashMap<>(256);

	public ServerAcceptor(JTextArea area) {
		textArea = area;
	}

	@Override
	public void run()
	{
		while(true) {
			Socket newSocket = null;
			try {
				newSocket = serversocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					serversocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			assert newSocket != null;
			new Thread(new ServerThread(newSocket, textArea)).start();
		}
	}

	@Override
	public void interrupt() {
		super.interrupt();
		threadMap.clear();
		log.debug("服务端停止成功");
	}

	/**
	 * 注册新的服务线程
	 */
	public static void registerThread(String threadName, ServerThread serverThread) throws RuntimeException {
		if (threadMap.containsKey(threadName)){
			throw new RuntimeException("用户名冲突 => " + threadName);
		}
		threadMap.put(threadName, serverThread);
	}

	/**
	 * 移除服务线程
	 */
	public static void removeThread(ServerThread thread){
		for (Map.Entry<String, ServerThread> entry : threadMap.entrySet()) {
			if (entry.getValue().equals(thread)){
				threadMap.remove(entry.getKey());
				break;
			}
		}
	}

	/**
	 * 广播客户端消息
	 */
	public static void broadcastMsg(Object msg, ServerThread src){
		try {
			for(ServerThread thread : threadMap.values()) {
				thread.getObjOut().writeObject(msg);
			}
		}catch (IOException e){

		}
	}

	/**
	 * 在服务端显示消息
	 */
	public static void showServerMsg(String msg){
		textArea.append(msg);
	}

	/**
	 * 必须设置端口
	 */
	public void setPort(int p) {
		port = p;
		try {
			serversocket = new ServerSocket(port);
			log.debug("服务端启动成功 : 端口" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, ServerThread> getThreadMap() {
		return threadMap;
	}
}
