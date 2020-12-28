package com.yankaizhang.board.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.yankaizhang.board.commons.Typeface;


@SuppressWarnings("serial")
public class ClientDraw extends JFrame implements ActionListener{


	private JMenuBar bar ;//定义菜单条
	private JMenu menu1, menu2,help;//定义菜单
	private JMenuItem start,pause,exit;//菜单中的菜单项
	private JMenuItem fun1,fun2,fun3;//菜单项

	private Point pOld = null; // 绘图的起点
	private Point pNew = null; // 绘图的终点
	private Point pTemp = null; // 中间辅助点
	private Point pTempOld = null;// 中间辅助点

	public static String shapeType = "PEN";// 默认绘图类型为直线

	public static boolean isOnNet = false;// 检测是否联网绘制

	private ClientCanvas optionPanel = null; // 集中各种控件的Jpanel
	public static ClientDrawMyShape drawPanel = null;// 绘制图形JPanel
	JPanel jpChat = null; // 聊天Jpanel及其相关组件
	JPanel jponline = null; // 显示在线人员
	JPanel jpMessage = null; // 发送消息组件
	JButton jbSend = null; // 发送按钮
	JButton jbClear = null; // 清除按钮
	JTextField jtf = null; // 消息编辑框
	public static JTextArea jta = null; // 聊天记录框
	public static JTextArea jtonline = null;
	public static String userName = "未命名用户";// 记录使用客户端的用户名
	JScrollPane jscr = null;// 聊天记录框滚动条
	JScrollPane jscro = null;// 在线人员显示框滚动条


	public ClientDraw() {

		setResizable(false);
		setTitle("网络白板 - 客户端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口正确关闭
		setBounds(100, 100, 980, 710);// 设置窗口大小
		
		//添加菜单栏
		bar = new JMenuBar();
		menu1 = new JMenu("开始");
		menu2 = new JMenu("选项");
		bar.add(menu1).add(menu2);
		start = new JMenuItem("单机模式");
		start.setEnabled(false);
		pause = new JMenuItem("联网模式");
		exit = new JMenuItem("退出");
		menu1.add(start);
		menu1.add(pause);
		menu1.add(exit);

		// TODO: 单机模式断开联网

		pause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				optionPanel.showModeJDialog();
			}
		});
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				System.exit(0);
			}
		});


		start.addActionListener(this);
		pause.addActionListener(this);
		exit.addActionListener(this);

		fun1 = new JMenuItem("背景色");
		fun2 = new JMenuItem("前景色");
		fun3 = new JMenuItem("字体");
		menu2.add(fun1);
		fun1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				drawPanel.Repaint(JColorChooser.showDialog(null,"背景色设置",Color.black), null);//////////////////////////
			}
		});
		menu2.add(fun2);
		fun2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				drawPanel.Repaint(null,
						JColorChooser.showDialog(null, "前景色设置", Color.black));
			}
		});
		menu2.add(fun3);
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
		setJMenuBar(bar);


		// 创建工具栏区域
		optionPanel = new ClientCanvas();
		optionPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "--------工具栏-------", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, new Color(46, 48, 50)));
		optionPanel.setBackground(new Color(106,147,176));
		getContentPane().add(optionPanel, BorderLayout.NORTH);

		// 创建绘图区域
		drawPanel = new ClientDrawMyShape();
		drawPanel.setBackground(Color.white);
		getContentPane().add(drawPanel, BorderLayout.CENTER);

		// 创建聊天区域
		jpChat = new JPanel();
		jponline = new JPanel();
		jpMessage = new JPanel();
		jbSend = new JButton("发送");
		jbSend.setFont(new Font("宋体", Font.BOLD, 16));
		jbSend.addActionListener(e -> {
			String str = jtf.getText().trim();
			if(str.isEmpty()){    // 如果是空字串或全为空格，则提示不可发送
				JOptionPane.showMessageDialog(null,"不能发送空消息","提示",JOptionPane.ERROR_MESSAGE);
				jtf.setText("");    // 如果输入了一串空格，提示后应该清空空格
			}
			else{     // 否则可以发送
				jta.append("我：" + str + "\n");
				jtf.setText("");
			}
			if (isOnNet){
				drawPanel.sendMessage(userName + ":" + str);
			}
		});


		// 清空聊天框
		jbClear = new JButton("清空");
		jbClear.setFont(new Font("宋体", Font.BOLD, 16));
		jbClear.addActionListener(e -> {
			jtf.setText("");
			jta.setText("");
		});


		jtf = new JTextField();
		jtf.setForeground(Color.BLUE);
		jtf.setFont(new Font("宋体", Font.PLAIN, 16));
		jtf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String str = jtf.getText().trim();
					if ("".equals(str)) {
						JOptionPane.showMessageDialog(null,"不能发送空消息","提示",JOptionPane.ERROR_MESSAGE);
					} else {
						jta.append("我：" + str + "\n");
					}
					jtf.setText("");
					if (isOnNet){
						drawPanel.sendMessage(userName + ":" + str);
					}
				}
			}
		});


		jta = new JTextArea();
		jta.setEditable(false);    // 设置聊天记录框不可编辑
		jta.setForeground(Color.blue);
		jta.setFont(new Font("Monospaced", Font.PLAIN, 16));
		jscr = new JScrollPane(jta);
		// 分别设置水平和垂直滚动条自动出现
		jscr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jta.setLineWrap(true);
		jpMessage.setLayout(new GridLayout(2, 1, 0, 0));
		
		// 设置在线人员显示框
		jtonline = new JTextArea();
		jtonline.setEditable(false);   // 设置在线人数框不可编辑
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
		// 添加在线人数JPanel
		jponline = new JPanel();
		jponline.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "聊天窗口：",
				TitledBorder.CENTER, TitledBorder.BELOW_BOTTOM, null,
				Color.black));
		jponline.add(jscro,BorderLayout.CENTER);

		jpChat = new JPanel();
		jpChat.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "在线人员：",
				TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, Color.black));
		jpChat.setLayout(new BorderLayout());
		jpChat.setPreferredSize(new Dimension(200, 70));
		jpChat.add(jscr);
		jpChat.add(jponline, BorderLayout.NORTH);
		jpChat.add(jpMessage, BorderLayout.SOUTH);
		// 加载聊天到主页面右边
		getContentPane().add(jpChat, BorderLayout.EAST);

		// 监听主窗口的关闭事件，主要是释放相关资源
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (isOnNet) {
					optionPanel.canConnect(false);
				}
			}
		});


		// 以下为监听鼠标的事件，包括按下，移动，释放
		pTempOld = new Point(0, 0);// 初始化辅助绘图点
		drawPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				pOld = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				pNew = e.getPoint();
				pTempOld.x = pOld.x;
				pTempOld.y = pOld.y;
				drawPanel.drawShape(shapeType, pTempOld, pNew, true);
			}
		});

		drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				pTemp = e.getPoint();
				pTempOld.x = pOld.x;
				pTempOld.y = pOld.y;
				drawPanel.drawShape(shapeType, (Point) pTempOld.clone(), pTemp, false);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}
}
