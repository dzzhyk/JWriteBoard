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
	private final ClientCanvas mainCanvas;
	public List<String> onlineList = new CopyOnWriteArrayList<>();
	ObjectOutputStream objOut = null;
	ObjectInputStream objIn = null;

	public ClientThread(InetAddress address, int port, ClientCanvas canvas) {
		mainCanvas = canvas;
		assert canvas != null;
		try {
			this.socket = new Socket(address, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			out.println(canvas.getUserName());
			out.flush();

			objOut = new ObjectOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());

			mainCanvas.setOnline(true);
			log.debug("连接服务器成功 : userName : " + canvas.getUserName());

		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "服务器连接错误", "连接错误", JOptionPane.ERROR_MESSAGE);
			try {
				if (this.socket != null){
					this.socket.close();
				}
				mainCanvas.setOnline(false);
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}


	@Override
	public void run() {
		while (true) {
			try {
				if (this.objIn == null){
					log.debug("对象输入流为Null");
					break;
				}
				if (this.socket.isClosed()){
					break;
				}
				MyShape shape = (MyShape) objIn.readObject();
				mainCanvas.getDataAndRepaint(shape);
				log.debug("收到新内容，重新绘制");
			}catch(ClassNotFoundException | IOException e) {
				try {
					if (this.socket != null){
						this.socket.close();
					}
					mainCanvas.setOnline(false);
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
			mainCanvas.setOnline(false);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
