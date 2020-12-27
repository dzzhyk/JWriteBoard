package com.yankaizhang.board.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

import com.yankaizhang.board.commons.Typeface;


@SuppressWarnings("serial")
public class ClientDraw extends JFrame implements ActionListener{


	private JMenuBar bar ;//����˵���
	private JMenu files,options,help;//����˵�
	private JMenuItem start,pause,exit;//�˵��еĲ˵���
	private JMenuItem fun1,fun2,fun3;//�˵���
	private JMenuItem aboutp,abouta;//�˵���

	private Point pOld = null; // ��ͼ�����
	private Point pNew = null; // ��ͼ���յ�
	private Point pTemp = null; // �м丨����
	private Point pTempOld = null;// �м丨����

	public static String shapeType = "PEN";// Ĭ�ϻ�ͼ����Ϊֱ��

	public static boolean isOnNet = false;// ����Ƿ���������
	private ClientCanvas optionPanel = null; // ���и��ֿؼ���Jpanel
	public static ClientDrawMyShape drawPanel = null;// ����ͼ��JPanel
	JPanel jpChat = null; // ����Jpanel����������
	JPanel jponline = null; // ��ʾ������Ա
	JPanel jpMessage = null; // ������Ϣ���
	JButton jbSend = null; // ���Ͱ�ť
	JButton jbClear = null; // �����ť
	JTextField jtf = null; // ��Ϣ�༭��
	public static JTextArea jta = null; // �����¼��
	public static JTextArea jtonline = null;
	public static String userName = "δ�����û�";// ��¼ʹ�ÿͻ��˵��û���
	JScrollPane jscr = null;// �����¼�������
	JScrollPane jscro = null;// ������Ա��ʾ�������


	public ClientDraw() {

		setResizable(false);
		setTitle("����װ� - �ͻ���");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô�����ȷ�ر�
		setBounds(100, 100, 980, 710);// ���ô��ڴ�С
		
		//��Ӳ˵���
		bar=new JMenuBar();//��ʼ���ؼ�
		files=new JMenu("��ʼ(B)");
		options=new JMenu("ѡ��(O)");

		bar.add(files);
		bar.add(options);

		setJMenuBar(bar);
		files.setMnemonic('B');//��ALT+��B������Ӳ˵���ݼ�
		options.setMnemonic('O');

		start=new JMenuItem("����ģʽ");//�ļ��˵���Ŀ�趨
		pause=new JMenuItem("����ģ��");
		exit=new JMenuItem("�˳�");
		files.add(start);
		files.add(pause);
		pause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				optionPanel.showModeJDialog();
			}
		});

		files.add(exit);
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				System.exit(0);
			}
		});

		start.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));//��ʼ�˵��и����ݼ�
		pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.CTRL_MASK));
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_MASK));
		start.addActionListener(this);
		pause.addActionListener(this);
		exit.addActionListener(this);

		fun1=new JMenuItem("����ɫ");//ѡ��˵��趨
		fun2=new JMenuItem("ǰ��ɫ");
		fun3=new JMenuItem("����");
		options.add(fun1);
		fun1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				drawPanel.Repaint(JColorChooser.showDialog(null,"����ɫ����",Color.black), null);//////////////////////////
			}
		});
		options.add(fun2);
		fun2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				drawPanel.Repaint(null,
						JColorChooser.showDialog(null, "ǰ��ɫ����", Color.black));
			}
		});
		options.add(fun3);
		fun3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				new Typeface();
			}
		});

		fun1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,InputEvent.CTRL_MASK));
		fun2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,InputEvent.CTRL_MASK));
		fun3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.CTRL_MASK));
		fun1.addActionListener(this);
		fun2.addActionListener(this);
		fun3.addActionListener(this);

		optionPanel = new ClientCanvas(); // ��������Japenl��
		// ���ù���Japenl�����������
		optionPanel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "--------������-------",
				TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, new Color(
						46, 48, 50)));
		optionPanel.setBackground(new Color(106,147,176));   // ���ù���Japenl��ı���ɫ
		getContentPane().add(optionPanel, BorderLayout.NORTH);// ���ع���Japenl�ൽ�����ڶ���

		drawPanel = new ClientDrawMyShape(); // ������ͼJPanel��ʵ��
		drawPanel.setBackground(Color.white);// ��ͼJPanel�౳��ɫĬ������Ϊ��ɫ

		getContentPane().add(drawPanel, BorderLayout.CENTER);// ���ػ�ͼJPanel�ൽ�������м�

		// ����Ϊ����Jpanel�Ĵ���������
		jpChat = new JPanel();
		jponline = new JPanel();
		jpMessage = new JPanel();
		// ������Ϣ
		jbSend = new JButton("����");
		jbSend.setFont(new Font("����", Font.BOLD, 16));
//		jbSend.setBackground(new Color(135,206,255));
		jbSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = jtf.getText().trim();
				if(str.isEmpty()){    // ����ǿ��ִ���ȫΪ�ո�����ʾ���ɷ���
					JOptionPane.showMessageDialog(null,"���ܷ��Ϳ���Ϣ","��ʾ",JOptionPane.ERROR_MESSAGE);
					jtf.setText("");    // ���������һ���ո���ʾ��Ӧ����տո�
				}
				else{     // ������Է���
					jta.append("�ң�" + str + "\n");
					jtf.setText("");
				}
				if (isOnNet == true)
					drawPanel.sendMessage(userName + ":" + str);
			}
		});
		// ��������
		jbClear = new JButton("���");
		jbClear.setFont(new Font("����", Font.BOLD, 16));
		jbClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jtf.setText("");
				jta.setText("");
			}
		});
		jtf = new JTextField();
		jtf.setForeground(Color.BLUE);
		jtf.setFont(new Font("����", Font.PLAIN, 16));
		jtf.addKeyListener(new KeyAdapter() {// ����JTextFieldde�س��¼�
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String str = jtf.getText().trim();
					if(str.isEmpty()){    // ����ǿ��ִ���ȫΪ�ո�����ʾ���ɷ���
						JOptionPane.showMessageDialog(null,"���ܷ��Ϳ���Ϣ","��ʾ",JOptionPane.ERROR_MESSAGE);
						jtf.setText("");    // ���������һ���ո���ʾ��Ӧ����տո�
					}
					else{
						jta.append("�ң�" + str + "\n");
						jtf.setText("");
					}
					if (isOnNet == true)
						drawPanel.sendMessage(userName + ":" + str);
				}
			}
		});
		jta = new JTextArea();
		jta.setEditable(false);    // ���������¼�򲻿ɱ༭
		jta.setForeground(Color.blue);
		jta.setFont(new Font("Monospaced", Font.PLAIN, 16));
		jscr = new JScrollPane(jta);
		// �ֱ�����ˮƽ�ʹ�ֱ�������Զ�����
		jscr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jta.setLineWrap(true);
		jpMessage.setLayout(new GridLayout(2, 1, 0, 0));
		
		// ����������Ա��ʾ��
		jtonline = new JTextArea();
		jtonline.setEditable(false);   // �������������򲻿ɱ༭
		jtonline.setForeground(Color.magenta);
		jtonline.setFont(new Font("Monospaced", Font.PLAIN, 16));
		jscro = new JScrollPane(jtonline);
		jscro.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jscro.setPreferredSize(new Dimension(180,80));/////////////////////////////
		jtonline.setLineWrap(true);
		jponline.setLayout(new BorderLayout());
		jtonline.setText("");
		
		JPanel panel = new JPanel();
		jpMessage.add(jtf);
		jpMessage.add(panel);
		panel.add(jbClear);
		panel.add(jbSend);
		// �����������JPanel
		jponline = new JPanel();
		jponline.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "���촰�ڣ�",
				TitledBorder.CENTER, TitledBorder.BELOW_BOTTOM, null,
				Color.black));
		//jponline.setPreferredSize(new Dimension(200, 180));// ����Ա��ʾ�������JPanel
		jponline.add(jscro,BorderLayout.CENTER);

		jpChat = new JPanel();
		jpChat.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "������Ա��",
				TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, Color.black));
		jpChat.setLayout(new BorderLayout());
		jpChat.setPreferredSize(new Dimension(200, 70));
		jpChat.add(jscr);
		jpChat.add(jponline, BorderLayout.NORTH);
		jpChat.add(jpMessage, BorderLayout.SOUTH);
		// �������쵽��ҳ���ұ�
		getContentPane().add(jpChat, BorderLayout.EAST);

		// ���������ڵĹر��¼�����Ҫ���ͷ������Դ
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (isOnNet) {
					optionPanel.canConnect(false);
				}
			}
		});

		// ����Ϊ���������¼����������£��ƶ����ͷ�
		pTempOld = new Point(0, 0);// ��ʼ��������ͼ��
		drawPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				pOld = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				pNew = e.getPoint();
				try {
					pTempOld.x = pOld.x;
					pTempOld.y = pOld.y;
					drawPanel.drawShape(shapeType, pTempOld, pNew, true);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			// ����ƶ��¼�
			public void mouseDragged(MouseEvent e) {
				pTemp = e.getPoint();
				try {
					pTempOld.x = pOld.x;
					pTempOld.y = pOld.y;
					drawPanel.drawShape(shapeType,
							(Point) pTempOld.clone(), pTemp, false);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}
}
