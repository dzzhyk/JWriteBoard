package com.yankaizhang.board.server;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


/**
 * ��������������
 */

@SuppressWarnings("serial")
public class Server extends JFrame {

	private JPanel contentPane = new JPanel();
	ServerCanvas panel = null;

	public static void main(String[] args) {
		try {
			Server frame = new Server();
			frame.setVisible(true);
			frame.setResizable(false);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


	public Server() {
		setTitle("����װ� - �����");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// ����JTextArea
		JTextArea txtrn = new JTextArea();
		txtrn.setEditable(false);
		txtrn.setText("Server ��־:");
		txtrn.setToolTipText("");
		txtrn.setForeground(Color.pink);
		txtrn.setBackground(Color.WHITE);
		txtrn.setFont(new Font("΢���ź�", Font.PLAIN, 16));

		JScrollPane scroll = new JScrollPane(txtrn);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		txtrn.setLineWrap(true);

		// ����������
		panel = new ServerCanvas(txtrn);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		contentPane.add(scroll, BorderLayout.CENTER);
		contentPane.add(panel, BorderLayout.NORTH);
	}
}