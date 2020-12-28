package com.yankaizhang.board.server;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;


/**
 * 服务器界面
 */
class ServerCanvas extends JPanel {

	private final JTextField textField = new JTextField();
	private JTextArea textArea;
	private final ServerAcceptor acceptor;

	public ServerCanvas(JTextArea textArea) {

		this.textArea = textArea;
		this.acceptor = new ServerAcceptor(textArea);

		JLabel lblNewLabel = new JLabel("Port:");
		lblNewLabel.setFont(new Font("Times new Roman", Font.BOLD, 26));
		lblNewLabel.setForeground(Color.black);
		this.add(lblNewLabel);

		JButton startServer = new JButton("启动");
		JButton endServer = new JButton("停止");
		startServer.setEnabled(true);
		endServer.setEnabled(false);
		textField.setFont(new Font("宋体", Font.PLAIN, 18));
		textField.setText("7800");
		textField.setEditable(true);
		textField.setColumns(8);

		startServer.setBackground(new Color(34,139,34));
		startServer.addActionListener(e -> {
			int port = Integer.parseInt(textField.getText().trim());
			acceptor.setPort(port);
			acceptor.start();
			startServer.setEnabled(false);
			endServer.setEnabled(true);
		});

		startServer.setFont(new Font("宋体", Font.BOLD, 18));
		endServer.setBackground(new Color(178,34,34));
		endServer.addActionListener(e -> {
			synchronized (this.acceptor){
				while (!acceptor.isInterrupted()){
					acceptor.interrupt();
				}
				startServer.setEnabled(true);
				endServer.setEnabled(false);
			}
		});
		endServer.setFont(new Font("宋体", Font.BOLD, 18));

		this.add(textField);
		this.add(startServer);
		this.add(endServer);
	}
}
