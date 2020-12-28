package com.yankaizhang.board.server;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JTextArea;

import com.yankaizhang.board.commons.MyShape;

/**
 * �����߳�
 */
public class ServerThread implements Runnable {

	private Socket socket = null;
	private ObjectOutputStream objOut = null;
	private ObjectInputStream objIn = null;
	JTextArea textArea = null;

	public ServerThread(Socket src, JTextArea textArea) {
		this.socket = src;
		this.textArea = textArea;
		String clientName = "";
		try {
			//��ÿͻ��˵�����
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			clientName = in.readLine();

		} catch(IOException e) {
			e.printStackTrace();
		}

		ServerAcceptor.registerThread(clientName, this);
		ServerAcceptor.showServerMsg("\n [������] ip: " + socket.getInetAddress());
	}

	@Override
	public void run() {

		// ��Ҫ�������Ĵ���˳��
		try {
			objIn = new ObjectInputStream(socket.getInputStream());
			objOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// �����Ƿ������ݴ���
		while(true) {
			try {
				MyShape shape = (MyShape) objIn.readObject();
				shape.onlineList = new CopyOnWriteArrayList<>(ServerAcceptor.getThreadMap().keySet());
				sendClientMsg(shape, this);

			} catch(ClassNotFoundException e) {
				e.printStackTrace();
				break;
			} catch (EOFException e){
				ServerAcceptor.showServerMsg("\n [�Ͽ�����] user: "+ Thread.currentThread().getName() +", ip: " + this.socket.getInetAddress());
				ServerAcceptor.removeThread(this);
				break;
			} catch(Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	/**
	 * ��ͷ��˹㲥��Ϣ(��������)
	 */
	private void sendClientMsg(MyShape shape, ServerThread src) {
		ServerAcceptor.broadcastMsg(shape, src);
	}

	public ObjectOutputStream getObjOut() {
		return objOut;
	}
}
