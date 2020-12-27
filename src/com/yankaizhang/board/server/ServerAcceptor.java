package com.yankaizhang.board.server;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.*;
import javax.swing.JTextArea;

/**
 * ר�Ÿ���������ӵ��߳�
 */
public class ServerAcceptor extends Thread {

	private static ServerSocket serversocket = null;
	private static JTextArea textArea;
	private static int port;

	/** �߳�Map */
	private static final Map<String, ServerThread> threadMap = new ConcurrentHashMap<>(32);

	/** ���32�� */
	private final ExecutorService executors = new ThreadPoolExecutor(32, 32,
			30, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

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
			executors.submit(new ServerThread(newSocket, textArea));
		}
	}

	@Override
	public void interrupt() {
		super.interrupt();
		threadMap.clear();
		this.executors.shutdown();
	}

	/**
	 * ע���µķ����߳�
	 */
	public static void registerThread(String threadName, ServerThread serverThread) throws RuntimeException {
		if (threadMap.containsKey(threadName)){
			throw new RuntimeException("�û�����ͻ => " + threadName);
		}
		threadMap.put(threadName, serverThread);
	}

	/**
	 * �Ƴ������߳�
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
	 * �㲥�ͻ�����Ϣ
	 */
	public static void broadcastMsg(Object msg, ServerThread src){
		try {
			for(ServerThread thread : threadMap.values()) {
				if(src != thread){
					thread.getObjOut().writeObject(msg);
				}
			}
		}catch (IOException e){

		}
	}

	/**
	 * �ڷ������ʾ��Ϣ
	 */
	public static void showServerMsg(String msg){
		textArea.append(msg);
	}

	/**
	 * �������ö˿�
	 */
	public void setPort(int p) {
		port = p;
		try {
			serversocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, ServerThread> getThreadMap() {
		return threadMap;
	}
}
