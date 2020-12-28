package com.yankaizhang.board.client;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.UUID;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * 客户端工具栏区域
 */
@SuppressWarnings("serial")
class ClientToolBar extends JPanel implements ActionListener, WindowListener {

	private final ClientCanvas mainCanvas;

	private ImageIcon[] icons;
	JPanel jpDrawMode = new JPanel();
	JPanel jpFontAndColor = new JPanel();
	JPanel jpDrawPen = new JPanel();
	JPanel jpShapeSelect = new JPanel();
	JPanel jpEarser = new JPanel();

	JLabel jbName = new JLabel("昵称：", JLabel.CENTER);
	JLabel jbIP = new JLabel("目标IP：", JLabel.CENTER);
	JLabel jbPort = new JLabel("目标端口：", JLabel.CENTER);
	JTextField jtfName = new JTextField(UUID.randomUUID().toString().substring(0, 8));
	JTextField jtfIP = new JTextField("127.0.0.1");
	JTextField jtfPort = new JTextField("7800");
	JDialog jd = null;

	JButton btnConnect = new JButton("连接");	//连接
	JButton btnLine = new JButton("A");		//"直线"
	JButton btnRect = new JButton("B");		//"矩形"
	JButton btnDiamond = new JButton("C");		//"方形"
	JButton btnEcli = new JButton("D");		//"椭圆"
	JButton btnEraser = new JButton("E");		//"橡皮擦"
	JButton btnPen = new JButton("F");			//"钢笔"
	JButton btnText = new JButton("G");		//"文字"
	JButton btnSprayPen = new JButton("H");	//"喷枪"
	JButton btnRoundRect = new JButton("I");	//"圆角矩形"
	JButton btnLocal = new JButton("M");			//"单机模式"
	JButton btnNet = new JButton("N");				//"联机模式"
	JButton btnBackColor = new JButton("J");		//"背景颜色"
	JButton btnFrontColor = new JButton("K");		//"字体颜色"
	JButton btnFont = new JButton("L");			//"字体"

	JSlider jsPen = new JSlider(SwingConstants.HORIZONTAL, 0, 30, 5);
	JSlider jsEarser = new JSlider(SwingConstants.HORIZONTAL, 0, 60, 30);

	ButtonGroup btnGDrawMode = new ButtonGroup();
	ButtonGroup btnGShapeSelect = new ButtonGroup();

	private final String tiptext[] = { "单机模式", "联机模式", "直线", "矩形", "菱形", "椭圆", "橡皮擦", "画笔", "喷枪", "文字", "圆角矩形", "字体颜色", "背景色", "字体设置" };

	public ClientToolBar(ClientCanvas canvas) {
		mainCanvas = canvas;
		initToolBar();
	}

	/**
	 * 初始化工具栏页面
	 */
	private void initToolBar(){

		// 添加按钮图标
		icons = new ImageIcon[14];
		for (int i = 0; i < 14; i++) {
			icons[i] = new ImageIcon("./src/com/yankaizhang/board/icon/" + (i + 1) + ".JPG");
		}
		jbName.setForeground(Color.red);
		jbName.setFont(new Font("华文楷体", Font.PLAIN, 20));
		jbIP.setForeground(Color.red);
		jbIP.setFont(new Font("华文楷体", Font.PLAIN, 20));
		jbPort.setForeground(Color.red);
		jbPort.setFont(new Font("华文楷体", Font.PLAIN, 20));
		jtfName.setFont(new Font("宋体", Font.PLAIN, 16));
		jtfIP.setFont(new Font("宋体", Font.PLAIN, 16));
		jtfPort.setFont(new Font("宋体", Font.PLAIN, 16));

		// 为按钮添加监听、图片及提示语
		btnLine.addActionListener(this);
		btnLine.setIcon(icons[0]);
		btnLine.setToolTipText(tiptext[2]);
		btnLine.setBackground(new Color(240,240,180));

		btnRect.addActionListener(this);
		btnRect.setIcon(icons[2]);
		btnRect.setToolTipText(tiptext[3]);
		btnRect.setBackground(new Color(240,240,180));
		btnRoundRect.addActionListener(this);
		btnRoundRect.setIcon(icons[4]);
		btnRoundRect.setToolTipText(tiptext[10]);
		btnRoundRect.setBackground(new Color(240,240,180));
		btnEcli.addActionListener(this);
		btnEcli.setIcon(icons[1]);
		btnEcli.setToolTipText(tiptext[5]);
		btnEcli.setBackground(new Color(240,240,180));
		btnPen.addActionListener(this);
		btnPen.setIcon(icons[9]);
		btnPen.setToolTipText(tiptext[7]);
		btnPen.setBackground(new Color(240,240,180));
		btnDiamond.addActionListener(this);
		btnDiamond.setIcon(icons[3]);
		btnDiamond.setToolTipText(tiptext[4]);
		btnDiamond.setBackground(new Color(240,240,180));
		btnEraser.addActionListener(this);
		btnEraser.setIcon(icons[5]);
		btnEraser.setToolTipText(tiptext[6]);
		btnEraser.setBackground(new Color(240,240,180));
		btnText.addActionListener(this);
		btnText.setIcon(icons[6]);
		btnText.setToolTipText(tiptext[9]);
		btnText.setBackground(new Color(240,240,180));
		btnSprayPen.addActionListener(this);
		btnSprayPen.setIcon(icons[10]);
		btnSprayPen.setToolTipText(tiptext[8]);
		btnSprayPen.setBackground(new Color(240,240,180));
		btnLocal.addActionListener(this);
		btnLocal.setIcon(icons[13]);
		btnLocal.setToolTipText(tiptext[0]);
		btnLocal.setBackground(new Color(240,240,180));
		btnLocal.setEnabled(false);
		btnNet.addActionListener(this);
		btnNet.setIcon(icons[12]);
		btnNet.setToolTipText(tiptext[1]);
		btnNet.setBackground(new Color(240,240,180));
		btnNet.setEnabled(true);
		btnBackColor.addActionListener(this);
		btnBackColor.setIcon(icons[7]);
		btnBackColor.setToolTipText(tiptext[12]);
		btnBackColor.setBackground(new Color(240,240,180));
		btnFrontColor.addActionListener(this);
		btnFrontColor.setIcon(icons[8]);
		btnFrontColor.setToolTipText(tiptext[11]);
		btnFrontColor.setBackground(new Color(240,240,180));
		btnFont.addActionListener(this);
		btnFont.setIcon(icons[11]);
		btnFont.setToolTipText(tiptext[13]);
		btnFont.setBackground(new Color(240,240,180));

		btnConnect.addActionListener(e -> {
			manageConnection(true);
			assert jd != null;
			jd.dispose();
		});

		// 选择绘图模式
		jpDrawMode.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "模式选择", TitledBorder.CENTER,
				TitledBorder.ABOVE_TOP, null, Color.gray));
		jpDrawMode.setLayout(new GridLayout(1, 2));
		jpDrawMode.add(btnLocal);
		jpDrawMode.add(btnNet);
		btnGDrawMode.add(btnLocal);
		btnGDrawMode.add(btnNet);
		jpDrawMode.setPreferredSize(new Dimension(84, 50));

		// 图形选择
		jpShapeSelect.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "图形工具", TitledBorder.CENTER,
				TitledBorder.ABOVE_TOP, null, Color.gray));
		jpShapeSelect.setLayout(new GridLayout(1, 9));
		jpShapeSelect.setPreferredSize(new Dimension(380, 50));
		jpShapeSelect.add(btnLine);
		jpShapeSelect.add(btnRect);
		jpShapeSelect.add(btnDiamond);
		jpShapeSelect.add(btnEcli);
		jpShapeSelect.add(btnRoundRect);
		jpShapeSelect.add(btnEraser);
		jpShapeSelect.add(btnPen);
		jpShapeSelect.add(btnText);
		jpShapeSelect.add(btnSprayPen);

		btnGShapeSelect.add(btnLine);
		btnGShapeSelect.add(btnRect);
		btnGShapeSelect.add(btnDiamond);
		btnGShapeSelect.add(btnEcli);
		btnGShapeSelect.add(btnEraser);
		btnGShapeSelect.add(btnPen);
		btnGShapeSelect.add(btnText);
		btnGShapeSelect.add(btnSprayPen);
		btnGShapeSelect.add(btnRoundRect);

		// 字体以及颜色设置
		jpFontAndColor.setLayout(new GridLayout(1, 3));
		jpFontAndColor.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "字体颜色", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, Color.gray));
		jpFontAndColor.add(btnFrontColor);
		jpFontAndColor.add(btnFont);
		jpFontAndColor.add(btnBackColor);
		jpFontAndColor.setPreferredSize(new Dimension(150, 50));

		// 选择画笔以及橡皮设置
		jpDrawPen.setLayout(new GridLayout(1, 1, 0, 0));
		jpEarser.setLayout(new GridLayout(1, 1, 0, 0));
		jpDrawPen.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "画笔粗细", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, Color.gray));
		jpEarser.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "橡皮大小", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, Color.gray));

		jsPen.setMajorTickSpacing(3);
		jsPen.setMinorTickSpacing(1);
		jsPen.setPaintTicks(true);
		jsPen.addChangeListener(evt -> mainCanvas.getDrawArea().getTempShape().penLength = jsPen.getValue());

		jsEarser.setMajorTickSpacing(6);
		jsEarser.setMinorTickSpacing(2);
		jsEarser.setPaintTicks(true);
		jsEarser.addChangeListener(evt -> mainCanvas.getDrawArea().getTempShape().eraserLength = jsEarser.getValue());
		jpEarser.add(jsEarser);

		jpDrawPen.add(jsPen);
		jpDrawPen.setPreferredSize(new Dimension(120, 50));
		jpEarser.setPreferredSize(new Dimension(120, 50));

		// 添加各个JPanel
		this.setLayout(new FlowLayout());
		this.add(jpDrawMode);
		this.add(jpFontAndColor);
		this.add(jpShapeSelect);
		this.add(jpDrawPen);
		this.add(jpEarser);
	}


	@Override
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
			case "A"://"直线"
				mainCanvas.setShapeType("LINE");
				break;
			case "G"://文字
				mainCanvas.setShapeType("TEXT");
				break;
			case "D"://椭圆
				mainCanvas.setShapeType("ECLI");
				break;
			case "B"://矩形
				mainCanvas.setShapeType("RECT");
				break;
			case "I"://圆角矩形
				mainCanvas.setShapeType("RRECT");
				break;
			case "E"://橡皮擦
				mainCanvas.setShapeType("EARSER");
				break;
			case "C"://正方形
				mainCanvas.setShapeType("DIAMOND");
				break;
			case "F"://钢笔
				mainCanvas.setShapeType("PEN");
				break;
			case "H"://喷枪
				mainCanvas.setShapeType("SPEN");
				break;
			case "M"://单机模式
				manageConnection(false);
				break;
			case "N"://联网模式
				showModeJDialog();
				btnLocal.setEnabled(false);
				btnNet.setEnabled(false);
				break;
			case "J"://背景颜色
				mainCanvas.getDrawArea().RepaintType(JColorChooser.showDialog(this, "背景色设置",Color.black), null);
				break;
			case "K"://前景颜色
				mainCanvas.getDrawArea().RepaintType(null, JColorChooser.showDialog(this, "前景色设置", Color.black));
				break;
			case "L"://字体
				mainCanvas.showTypeCanvas();
				break;
			default: break;
		}
	}


	/**
	 * 联网模式对话框
 	 */
	public void showModeJDialog() {
		jd = new JDialog();
		jd.setTitle("连接服务器");
		jd.setSize(250, 200);
		jd.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		jd.setResizable(false);
		jd.setLayout(new GridLayout(4, 2, 5, 5));
		jd.add(jbName);
		jd.add(jtfName);
		jd.add(jbPort);
		jd.add(jtfPort);
		jd.add(jbIP);
		jd.add(jtfIP);
		jd.add(btnConnect);
		jd.setVisible(true);
		jd.setBackground(Color.cyan);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		jd.setLocation(width / 2 - 125, height / 2 - 125);
		jd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				jd.dispose();
				mainCanvas.setOnline(false);
			}
		});
	}


	/**
	 * 连接管理
	 */
	public void manageConnection(boolean just) {
		// 断开连接
		if (!just) {
			mainCanvas.connect(null, -1, false);
			return;
		}
		mainCanvas.setUserName(jtfName.getText().trim());
		String portStr = jtfPort.getText().trim();
		String IpStr = jtfIP.getText().trim();
		int port = Integer.parseInt(portStr);
		InetAddress address = null;
		try {
			address = InetAddress.getByName(IpStr);
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(null, "连接有误，请重新输入!", "连接错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		assert address != null;
		mainCanvas.connect(address, port, true);
		mainCanvas.sendTextMessage(mainCanvas.getUserName() + " 已登录");
	}


	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}
}
