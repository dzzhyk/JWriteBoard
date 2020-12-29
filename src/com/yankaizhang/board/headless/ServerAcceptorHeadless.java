package com.yankaizhang.board.headless;

import com.yankaizhang.board.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听线程
 */
public class ServerAcceptorHeadless extends Thread {

	private final Logger log = Logger.getInstance();
	private ServerSocket serversocket = null;
	private int port;

	/** 线程Map */
	private static final Map<String, ServerThreadHeadless> threadMap = new ConcurrentHashMap<>(256);

	public ServerAcceptorHeadless(){}

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
			new Thread(new ServerThreadHeadless(newSocket)).start();
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
	public static void registerThread(String threadName, ServerThreadHeadless serverThread) throws RuntimeException {
		if (threadMap.containsKey(threadName)){
			throw new RuntimeException("用户名冲突 => " + threadName);
		}
		threadMap.put(threadName, serverThread);
	}

	/**
	 * 移除服务线程
	 */
	public static void removeThread(ServerThreadHeadless thread){
		for (Map.Entry<String, ServerThreadHeadless> entry : threadMap.entrySet()) {
			if (entry.getValue().equals(thread)){
				threadMap.remove(entry.getKey());
				break;
			}
		}
	}

	/**
	 * 广播客户端消息
	 */
	public static void broadcastMsg(Object msg){
		try {
			for(ServerThreadHeadless thread : threadMap.values()) {
				thread.getObjOut().writeObject(msg);
			}
		}catch (IOException e){

		}
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

	public static Map<String, ServerThreadHeadless> getThreadMap() {
		return threadMap;
	}
}
