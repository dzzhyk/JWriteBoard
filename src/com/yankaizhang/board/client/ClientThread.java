package com.yankaizhang.board.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
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

			mainCanvas.setOnline(true);
			log.debug("连接服务器成功 : userName : " + canvas.getUserName());

		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "服务器连接错误", "连接错误", JOptionPane.ERROR_MESSAGE);
			mainCanvas.getToolBar().manageConnection(false);
		}
	}


	@Override
	public void run() {
		if (this.socket == null){
			return;
		}

		try {
			objOut = new ObjectOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());
//			mainCanvas.sendTextMessage(mainCanvas.getUserName() + " 已登录");
		} catch (IOException e) {
			this.interrupt();
		}

		while (true) {
			try {
				if (this.objIn == null){
					log.debug("对象输入流为Null");
					throw new IOException("对象输入流为Null");
				}
				if (this.socket == null || this.socket.isClosed()){
					throw new IOException("Broken Pipe");
				}
				MyShape shape = (MyShape) objIn.readObject();
				mainCanvas.getDataAndRepaint(shape);
			}catch(ClassNotFoundException | IOException e) {
				mainCanvas.getToolBar().manageConnection(false);
				break;
			}
		}
	}


	/**
	 * 发送消息
	 */
	public void sendClientMsg(Object msg) {
		try {
			objOut.writeObject(msg);
		}catch(IOException e) {
			mainCanvas.getToolBar().manageConnection(false);
		}
	}

	@Override
	public void interrupt() {
		super.interrupt();
		try {
			if (objIn != null && !socket.isClosed()) objIn.close();
			if (objOut != null && !socket.isClosed()) objOut.close();
			if (this.socket != null && !socket.isClosed()) this.socket.close();
			mainCanvas.setOnline(false);
			log.debug("断开远程连接");
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
