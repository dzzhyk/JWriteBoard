package com.yankaizhang.board.client;

import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;
import javax.swing.border.*;

import com.yankaizhang.board.commons.MyShape;
import com.yankaizhang.board.commons.TypeCanvas;
import com.yankaizhang.board.util.Logger;


/**
 * 客户端主界面
 */
@SuppressWarnings("serial")
public class ClientCanvas extends JFrame implements ActionListener{

	private final Logger log = Logger.getInstance();
	private ClientToolBar toolBar = null;
	private ClientDrawArea drawArea = null;
	private ClientChat chatArea = null;
	ClientThread clientThread = null;

	private JMenuBar bar;
	private JMenu menu1, menu2;
	private JMenuItem offlineItem, onlineItem, exit, fun1, fun2, fun3, fun4;

	private Point pOld = null; // 绘图的起点
	private Point pNew = null; // 绘图的终点
	private Point pTemp = null; // 中间辅助点
	private Point pTempOld = null;// 中间辅助点

	private String shapeType = "PEN";
	private boolean online = false;
	private String userName = "未命名用户";

	/**
	 * 构造函数
	 * 创建用户主页面
	 */
	public ClientCanvas() {

		setTitle("网络白板 - 客户端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		setResizable(false);
		
		//添加菜单栏
		bar = new JMenuBar();
		menu1 = new JMenu("开始");
		menu2 = new JMenu("选项");
		bar.add(menu1);
		bar.add(menu2);
		offlineItem = new JMenuItem("单机模式");
		offlineItem.setEnabled(false);
		onlineItem = new JMenuItem("联网模式");
		exit = new JMenuItem("退出");
		menu1.add(offlineItem);
		menu1.add(onlineItem);
		menu1.add(exit);
		offlineItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				getToolBar().manageConnection(false);
			}
		});
		onlineItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// 建立连接
				toolBar.showModeJDialog();
				onlineItem.setEnabled(false);
				offlineItem.setEnabled(false);
				getToolBar().btnNet.setEnabled(false);
				getToolBar().btnLocal.setEnabled(false);
			}
		});
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				System.exit(0);
			}
		});
		fun1 = new JMenuItem("背景色");
		fun2 = new JMenuItem("字体颜色");
		fun3 = new JMenuItem("字体设置");
		fun4 = new JMenuItem("清屏");
		menu2.add(fun1);
		menu2.add(fun2);
		menu2.add(fun3);
		menu2.add(fun4);

		fun1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				drawArea.RepaintType(JColorChooser.showDialog(null,"背景色设置", Color.black), null);
			}
		});
		fun2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				drawArea.RepaintType(null, JColorChooser.showDialog(null, "字体颜色", Color.black));
			}
		});
		fun3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				showTypeCanvas();
			}
		});
		fun4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				clearBoard();
			}
		});
		setJMenuBar(bar);


		// 创建工具栏区域
		toolBar = new ClientToolBar(this);
		toolBar.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "--------工具栏-------", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, new Color(46, 48, 50)));
		toolBar.setBackground(new Color(106,147,176));
		getContentPane().add(toolBar, BorderLayout.NORTH);

		// 创建绘图区域
		drawArea = new ClientDrawArea(this);
		drawArea.setBackground(Color.white);
		getContentPane().add(drawArea, BorderLayout.CENTER);

		// 创建聊天区域
		chatArea = new ClientChat(this);
		getContentPane().add(chatArea, BorderLayout.EAST);

		// 监听主窗口的关闭事件，主要是释放相关资源
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			if (online) {
				toolBar.manageConnection(false);
			}
			}
		});


		// 以下为监听鼠标的事件，包括按下，移动，释放
		pTempOld = new Point(0, 0);// 初始化辅助绘图点
		drawArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				pOld = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				pNew = e.getPoint();
				pTempOld.x = pOld.x;
				pTempOld.y = pOld.y;
				drawArea.drawShape(shapeType, pTempOld, pNew, true);
			}
		});

		drawArea.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				pTemp = e.getPoint();
				pTempOld.x = pOld.x;
				pTempOld.y = pOld.y;
				drawArea.drawShape(shapeType, pTempOld, pTemp, false);
			}
		});
	}


	/**
	 * 清屏
	 */
	private void clearBoard() {
		drawArea.drawShape("CLS", null, null, true);
	}

	/**
	 * 连接管理
	 */
	public void connect(InetAddress address, int port, boolean just) {
		if (just) {
			clientThread = new ClientThread(address, port, this);
			clientThread.start();
		} else {
			if (clientThread != null && clientThread.isAlive()){
				clientThread.onlineList.remove(getUserName());
				while (!clientThread.isInterrupted()){
					clientThread.interrupt();
				}
			}
			this.setOnline(false);
		}
	}

	/**
	 * 发送文本消息
	 */
	public void sendTextMessage(String str) {
		// 包装为对象数据
		MyShape shape = new MyShape();
		shape.message = str;
		shape.type = 6;
		if (clientThread != null && clientThread.onlineList.size() > 0){
			shape.onlineList = new CopyOnWriteArrayList<>(clientThread.onlineList);
		}
		sendObjectMessage(shape);
	}

	/**
	 * 发送对象数据
	 */
	public void sendObjectMessage(Object object){
		if(online){
			clientThread.sendClientMsg(object);
		}
	}

	/**
	 * 根据新数据重新绘制
	 */
	public void getDataAndRepaint(MyShape shape) {
		//获取在线列表
		clientThread.onlineList = new CopyOnWriteArrayList<>(shape.onlineList);
		chatArea.jtonline.setText("Total："+ clientThread.onlineList.size());
		for(String user : clientThread.onlineList){
			chatArea.jtonline.append( "\n" +"User:"+ user);
		}
		if (shape.type == 6) {
			chatArea.showMsg(shape.message + "\n");
			chatArea.repaint();
			return;
		}
		if (shape.type == 7) {
			drawArea.getShapes().add(shape.deepClone());
			drawArea.setBackColor(shape.backColor);
			drawArea.repaint();
			return;
		}
		if (shape.type == 999){
			drawArea.getShapes().clear();
			drawArea.getTempShape().reset();
			drawArea.repaint();
			return;
		}
		drawArea.getShapes().add(shape.deepClone());
		drawArea.repaint();
	}

	/**
	 * 显示样式设置面板
	 */
	public void showTypeCanvas(){
		new TypeCanvas(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}

	public boolean isOnline() {
		return this.online;
	}

	public synchronized void setOnline(boolean online) {
		this.online = online;
		// 更新各种按钮状态
		if (online){
			onlineItem.setEnabled(false);
			offlineItem.setEnabled(true);
			getToolBar().btnNet.setEnabled(false);
			getToolBar().btnLocal.setEnabled(true);
		}else{
			onlineItem.setEnabled(true);
			offlineItem.setEnabled(false);
			getToolBar().btnNet.setEnabled(true);
			getToolBar().btnLocal.setEnabled(false);
			getChatArea().reset();
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ClientToolBar getToolBar() {
		return toolBar;
	}

	public ClientDrawArea getDrawArea() {
		return drawArea;
	}

	public ClientChat getChatArea() {
		return chatArea;
	}

	public void setShapeType(String shapeType) {
		this.shapeType = shapeType;
	}
}
