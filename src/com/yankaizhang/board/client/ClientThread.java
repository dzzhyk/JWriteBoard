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
 * �ͻ����߳�
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
			// ����֮�����ȷ����û���
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			out.println(ClientDraw.userName);
			out.flush();

			objOut = new ObjectOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());

			ClientDraw.isOnNet = true;
			log.debug("���ӷ������ɹ� : userName : " + ClientDraw.userName);

		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "���������Ӵ���", "���Ӵ���", JOptionPane.ERROR_MESSAGE);
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
				log.debug("�յ������ݣ����»���");
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
	 * ������Ϣ
	 */
	public void sendClientMsg(Object msg) {
		try {
			objOut.writeObject(msg);
			log.debug("��������");
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
			log.debug("�Ͽ����ӳɹ�");
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
