package com.yankaizhang.board.client;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.TitledBorder;

import com.yankaizhang.board.commons.Typeface;

/**
 * �ͻ��˽���
 */
@SuppressWarnings("serial")
class ClientCanvas extends JPanel implements ActionListener, WindowListener {
	private ImageIcon[] icons;
	JPanel jpDrawMode = new JPanel();
	JPanel jpFontAndColor = new JPanel();
	JPanel jpDrawPen = new JPanel();
	JPanel jpShapeSelect = new JPanel();
	JPanel jpEarser = new JPanel();

	JLabel jbName = new JLabel("�����ǳƣ�", JLabel.CENTER);
	JLabel jbIP = new JLabel("Ŀ��IP��", JLabel.CENTER);
	JLabel jbPort = new JLabel("Ŀ��˿ڣ�", JLabel.CENTER);
	JTextField jtfName = new JTextField("�������ǳ�");
	JTextField jtfIP = new JTextField("127.0.0.1");
	JTextField jtfPort = new JTextField("7800");
	JDialog jd = null;

	JButton btnLine = new JButton("A");		//"ֱ��"
	JButton btnRect = new JButton("B");		//"����"
	JButton btnRoundRect = new JButton("I");	//"Բ�Ǿ���"
	JButton btnEcli = new JButton("D");		//"��Բ"
	JButton btnPen = new JButton("F");			//"�ֱ�"
	JButton btnDiamond = new JButton("C");		//"����"
	JButton btnEraser = new JButton("E");		//"��Ƥ��"

	JButton btnText = new JButton("G");		//"����"
	JButton btnSprayPen = new JButton("H");	//"��ǹ"

	JButton btnConnect = new JButton("����");	//����
	JButton btnLocal = new JButton("M");			//"������ͼ"
	JButton btnNet = new JButton("N");				//"�����ͼ"

	JButton btnBackColor = new JButton("J");		//"������ɫ"
	JButton btnFrontColor = new JButton("K");		//"ǰ����ɫ"
	JButton btnFont = new JButton("L");			//"����"

	JSlider jsPen = new JSlider(SwingConstants.HORIZONTAL, 0, 30, 15);
	JSlider jsEarser = new JSlider(SwingConstants.HORIZONTAL, 0, 60, 30);

	ButtonGroup btnGDrawMode = new ButtonGroup();
	ButtonGroup btnGShapeSelect = new ButtonGroup();

	// ����������Ƶ���Ӧ�İ�ť�ϸ�����Ӧ����ʾ
	private String tiptext[] = { "����ģʽ", "����ģʽ", "ֱ��", "����", "����", "��Բ",
			"��Ƥ��", "�ֱ�", "��ǹ", "����", "Բ�Ǿ���", "ǰ��ɫ", "����ɫ", "����" };

	public ClientCanvas() {
		// ��Ӱ�ťͼ��
		icons = new ImageIcon[14];
		for (int i = 0; i < 14; i++) {
			icons[i] = new ImageIcon("./src/com/yankaizhang/board/icon/" + (i + 1) + ".JPG");
		}
		jbName.setForeground(Color.red);
		jbName.setFont(new Font("���Ŀ���", Font.BOLD, 20));
		jbIP.setForeground(Color.red);
		jbIP.setFont(new Font("���Ŀ���", Font.BOLD, 20));
		jbPort.setForeground(Color.red);
		jbPort.setFont(new Font("���Ŀ���", Font.BOLD, 20));
		jtfName.setFont(new Font("����", Font.PLAIN, 16));
		jtfIP.setFont(new Font("����", Font.PLAIN, 16));
		jtfPort.setFont(new Font("����", Font.PLAIN, 16));
		// Ϊ��ť��Ӽ�����ͼƬ����ʾ��
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
		btnConnect.addActionListener(this);
		btnLocal.addActionListener(this);
		btnLocal.setIcon(icons[13]);
		btnLocal.setToolTipText(tiptext[0]);
		btnLocal.setBackground(new Color(240,240,180));
		btnNet.addActionListener(this);
		btnNet.setIcon(icons[12]);
		btnNet.setToolTipText(tiptext[1]);
		btnNet.setBackground(new Color(240,240,180));
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

		// ѡ���ͼģʽ
		jpDrawMode.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "ģʽѡ��", TitledBorder.CENTER,
				TitledBorder.ABOVE_TOP, null, Color.gray));
		jpDrawMode.setLayout(new GridLayout(1, 2));

		jpDrawMode.add(btnLocal);
		jpDrawMode.add(btnNet);
		btnGDrawMode.add(btnLocal);
		btnGDrawMode.add(btnNet);
		jpDrawMode.setPreferredSize(new Dimension(84, 50));

		// ͼ��ѡ��
		jpShapeSelect.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "ͼ�ι���", TitledBorder.CENTER,
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
		// �����Լ���ɫ����
		jpFontAndColor.setLayout(new GridLayout(1, 3));
		jpFontAndColor.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "��ɫ����",
				TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, Color.gray));
		jpFontAndColor.add(btnFrontColor);
		jpFontAndColor.add(btnFont);
		jpFontAndColor.add(btnBackColor);
		jpFontAndColor.setPreferredSize(new Dimension(150, 50));

		// ѡ�񻭱��Լ���Ƥ����
		jpDrawPen.setLayout(new GridLayout(1, 1, 0, 0));
		jpDrawPen.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "���ʴ�ϸ", TitledBorder.CENTER,
				TitledBorder.ABOVE_TOP, null, Color.gray));

		jsPen.setMajorTickSpacing(3);
		jsPen.setMinorTickSpacing(1);
		jsPen.setPaintTicks(true);// ��ʾ�̶�
		jsPen.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent evt) {
				ClientDraw.drawPanel.tempShape.penLength = jsPen.getValue();
			}
		});
		jpEarser.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "��Ƥ��С", TitledBorder.CENTER,
				TitledBorder.ABOVE_TOP, null, Color.gray));
		jpEarser.setLayout(new GridLayout(1, 1, 0, 0));

		jsEarser.setMajorTickSpacing(6);
		jsEarser.setMinorTickSpacing(2);
		jsEarser.setPaintTicks(true);// ��ʾ�̶�
		jsEarser.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent evt) {
				ClientDraw.drawPanel.tempShape.eraserLength = jsEarser
						.getValue();
			}
		});
		jpEarser.add(jsEarser);
		jpDrawPen.add(jsPen);
		jpDrawPen.setPreferredSize(new Dimension(120, 50));
		jpEarser.setPreferredSize(new Dimension(120, 50));
		// ��Ӹ���JPanel
		this.setLayout(new FlowLayout());
		this.add(jpDrawMode);
		this.add(jpFontAndColor);
		this.add(jpShapeSelect);		
		this.add(jpDrawPen);
		this.add(jpEarser);
	}

	// ���������ť�ĵ���¼�
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
			case "����"://����
				canConnect(true);
				jd.dispose();
				break;
			case "A"://"ֱ��"
				ClientDraw.shapeType = "LINE";
				break;
			case "G"://����
				ClientDraw.shapeType = "TEXT";
				break;
			case "D"://��Բ
				ClientDraw.shapeType = "ECLI";
				break;
			case "B"://����
				ClientDraw.shapeType = "RECT";
				break;
			case "I"://Բ�Ǿ���
				ClientDraw.shapeType = "RRECT";
				break;
			case "E"://��Ƥ��
				ClientDraw.shapeType = "EARSER";
				break;
			case "C"://������
				ClientDraw.shapeType = "DIAMOND";
				break;
			case "F"://�ֱ�
				ClientDraw.shapeType = "PEN";
				break;
			case "H"://��ǹ
				ClientDraw.shapeType = "SPEN";
				break;
			case "N"://�����ͼ
				showModeJDialog();
				break;
			case "J"://������ɫ
				ClientDraw.drawPanel
						.Repaint(JColorChooser.showDialog(this, "����ɫ����",Color.black), null);
				break;
			case "K"://ǰ����ɫ
				ClientDraw.drawPanel.Repaint(null,
						JColorChooser.showDialog(this, "ǰ��ɫ����", Color.black));
				break;
			case "L"://����
				new Typeface();
				break;
			default: break;
		}
	}

	// ��ʾ����ģʽ�ĶԻ�������˿ںţ�IP��
	public void showModeJDialog() {
		jd = new JDialog();
		jd.setTitle("���ӷ�����");
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
		final int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		final int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		jd.setLocation(width / 2 - 125, height / 2 - 125);
		jd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				jd.dispose();
			}
		});

	}

	// ȷ������
	public void canConnect(final boolean just) {
		if (!just) {
			ClientDraw.drawPanel.connect(null, -1, just);
			return;
		}
		ClientDraw.userName = jtfName.getText().trim();
		final String portstr = jtfPort.getText().trim();
		final String IPstr = jtfIP.getText().trim(); // �õ�IP���˿ں�
		final int port = Integer.parseInt(portstr);
		InetAddress address = null;
		try {
			address = InetAddress.getByName(IPstr);
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(null, "�������IP��������������!", "IP����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		assert address != null;
		ClientDraw.drawPanel.connect(address, port, just);
		ClientDraw.isOnNet = true;
		ClientDraw.drawPanel.sendMessage(ClientDraw.userName + "������!");
	}

	@Override
	public void windowOpened(final WindowEvent e) {

	}

	@Override
	public void windowClosing(final WindowEvent e) {

	}

	@Override
	public void windowClosed(final WindowEvent e) {

	}

	@Override
	public void windowIconified(final WindowEvent e) {

	}

	@Override
	public void windowDeiconified(final WindowEvent e) {

	}

	@Override
	public void windowActivated(final WindowEvent e) {

	}

	@Override
	public void windowDeactivated(final WindowEvent e) {

	}

}
