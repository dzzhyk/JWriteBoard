package com.yankaizhang.board.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JOptionPane;

import com.yankaizhang.board.commons.MyShape;

/**
 * 客户端线程
 */
public class ClientThread implements Runnable {

	Socket socket = null;
	private CopyOnWriteArrayList<String> onlineList = new CopyOnWriteArrayList<>();
	ObjectOutputStream objOut = null;
	ObjectInputStream objIn = null;


	public ClientThread(InetAddress address, int port) {
		try {
			this.socket = new Socket(address, port);
			ClientDraw.isOnNet = true;

			// 连接之后首先发送用户名
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			out.println(ClientDraw.userName);
			out.flush();

			objOut = new ObjectOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());

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
		while(true) {
			try {
				MyShape graphics = (MyShape) objIn.readObject();
				assert graphics != null;
				ClientDraw.drawPanel.getDataAndRepaint(graphics);
			}catch(ClassNotFoundException | IOException e) {
				e.printStackTrace();
				try {
					socket.close();
					break;
				} catch (IOException ioException) {
					ioException.printStackTrace();
					break;
				}
			}
		}
	}

	// 向客服端发送消息
	public void sendClientMsg(Object msg) {
		try {
			objOut.writeObject(msg);
		}catch(IOException e) {
			e.printStackTrace();

			try {
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}

		}
	}


	public void close() throws IOException {
		objOut.close();
		objIn.close();
		socket.close();
	}


}
