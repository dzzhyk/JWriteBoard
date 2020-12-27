package com.yankaizhang.board.client;

/**
 * 客户端主程序
 */
public class Client {
	public static void main(String[] args) {
		try {
			ClientDraw frame = new ClientDraw();
			frame.setVisible(true);
			frame.setResizable(true);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
