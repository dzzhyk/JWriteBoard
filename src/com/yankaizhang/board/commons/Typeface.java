package com.yankaizhang.board.commons;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import com.yankaizhang.board.client.ClientDraw;


/**
 * ѡ����ʽ����
 */
public class Typeface implements ActionListener {
	final JDialog fontDialog = new JDialog(); // ����ѡ��Ի���
	final JTextField tfFont = new JTextField(11); // ������
	final JTextField tfSize = new JTextField(6); // �����С
	final JTextField tfStyle = new JTextField(10); // ������ʽ
	// ������ʽ����
	final int fontStyleConst[] = {Font.PLAIN, Font.BOLD, Font.ITALIC,
			Font.BOLD + Font.ITALIC};
	@SuppressWarnings("rawtypes")
	final JList listStyle; // ѡ��������ʽ���б�
	@SuppressWarnings("rawtypes")
	final JList listFont; // ѡ����������б�
	@SuppressWarnings("rawtypes")
	final JList listSize; // ѡ�������ͺŵ��б�
	private JLabel sample; // ʾ��
	private JButton fontOkButton = new JButton("ȷ��"); // �����������"ȷ��"��ť

	// ���������񣬴�С������ӵ������.
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Typeface() {
		// ��ʼ�����
		Container container = fontDialog.getContentPane();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));

		Font currentFont = container.getFont();// ��ȡ��ǰ�������ʽ

		JLabel lblFont = new JLabel("����(F):");
		lblFont.setPreferredSize(new Dimension(100, 20));
		JLabel lblStyle = new JLabel("����(Y):");
		lblStyle.setPreferredSize(new Dimension(100, 20));
		JLabel lblSize = new JLabel("��С(S):");
		lblSize.setPreferredSize(new Dimension(100, 20));

		// ��������
		tfFont.setText(currentFont.getFontName());
		tfFont.selectAll();
		tfFont.setPreferredSize(new Dimension(200, 20));

		// ����������
		if(currentFont.getStyle() == Font.PLAIN)
			tfStyle.setText("����");
		else if(currentFont.getStyle() == Font.BOLD)
			tfStyle.setText("����");
		else if(currentFont.getStyle() == Font.ITALIC)
			tfStyle.setText("б��");
		else if(currentFont.getStyle() == (Font.BOLD + Font.ITALIC))
			tfStyle.setText("��б��");

		tfStyle.setPreferredSize(new Dimension(200, 20));

		// �����С
		tfSize.setText(currentFont.getSize() + "");
		tfSize.setPreferredSize(new Dimension(200, 20));

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		final String fontName[] = ge.getAvailableFontFamilyNames();
		int defaultFontIndex = 0;
		for(int i = 0; i < fontName.length; i++) {
			if(fontName[i].equals(currentFont.getFontName())) {
				defaultFontIndex = i;
				break;
			}
		}
		listFont = new JList(fontName);
		listFont.setSelectedIndex(defaultFontIndex);
		listFont.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFont.setVisibleRowCount(7);
		listFont.setFixedCellWidth(82);
		listFont.setFixedCellHeight(20);
		listFont.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				tfFont.setText(fontName[listFont.getSelectedIndex()]);
				tfFont.requestFocus();
				tfFont.selectAll();
				updateSample();
			}
		});

		final String fontStyle[] = {"����", "����", "б��", "��б��"};
		listStyle = new JList(fontStyle);
		listStyle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if(currentFont.getStyle() == Font.PLAIN)
			listStyle.setSelectedIndex(0);
		else if(currentFont.getStyle() == Font.BOLD)
			listStyle.setSelectedIndex(1);
		else if(currentFont.getStyle() == Font.ITALIC)
			listStyle.setSelectedIndex(2);
		else if(currentFont.getStyle() == (Font.BOLD + Font.ITALIC))
			listStyle.setSelectedIndex(3);

		listStyle.setVisibleRowCount(7);
		listStyle.setFixedCellWidth(99);
		listStyle.setFixedCellHeight(20);
		listStyle.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				tfStyle.setText(fontStyle[listStyle.getSelectedIndex()]);
				tfStyle.requestFocus();
				tfStyle.selectAll();
				updateSample();
			}
		});

		final String fontSize[] = {"8", "9", "10", "11", "12", "14", "16",
				"18", "20", "22", "24", "26", "28", "36", "48", "72"};
		listSize = new JList(fontSize);

		int defaultFontSizeIndex = 0;

		for(int i = 0; i < fontSize.length; i++) {
			if(fontSize[i].equals(currentFont.getSize() + "")) {
				defaultFontSizeIndex = i;
				break;
			}
		}

		listSize.setSelectedIndex(defaultFontSizeIndex);
		listSize.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSize.setVisibleRowCount(7);
		listSize.setFixedCellWidth(39);
		listSize.setFixedCellHeight(20);
		listSize.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				tfSize.setText(fontSize[listSize.getSelectedIndex()]);
				tfSize.requestFocus();
				tfSize.selectAll();
				updateSample();
			}
		});

		fontOkButton.addActionListener(this);
		fontOkButton.setFont(new Font("����", Font.BOLD, 11));
		JButton cancelButton = new JButton("ȡ��");
		cancelButton.setFont(new Font("����", Font.BOLD, 11));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontDialog.dispose();
			}
		});

		sample = new JLabel("hello world, ���磬��ã�");
		sample.setHorizontalAlignment(SwingConstants.CENTER);
		sample.setPreferredSize(new Dimension(300, 50));

		JPanel samplePanel = new JPanel();
		samplePanel.setBorder(BorderFactory.createTitledBorder("ʾ��"));
		samplePanel.add(sample);

		// ����齨�����
		container.add(lblFont);
		container.add(lblStyle);
		container.add(lblSize);
		container.add(tfFont);
		container.add(tfStyle);
		container.add(tfSize);
		container.add(fontOkButton);
		container.add(new JScrollPane(listFont));
		container.add(new JScrollPane(listStyle));
		container.add(new JScrollPane(listSize));
		container.add(cancelButton);
		container.add(samplePanel);

		// ����ʾ������
		updateSample();
		fontDialog.setSize(350, 340);
		fontDialog.setLocation(200, 200);
		fontDialog.setResizable(false);
		fontDialog.setVisible(true);

	}// ���캯������

	// ����ʾ����ʾ������ͷ���С.
	public void updateSample() {
		Font sampleFont = new Font(tfFont.getText(),
				fontStyleConst[listStyle.getSelectedIndex()],
				Integer.parseInt(tfSize.getText()));
		sample.setFont(sampleFont);
	}

	// �������ֵ�����.
	public void actionPerformed(ActionEvent e) {
		// ��ȷ��ʱ��������
		if(e.getSource() == fontOkButton) {
			ClientDraw.drawPanel.tempShape.font = new Font(tfFont.getText(),
					fontStyleConst[listStyle.getSelectedIndex()],
					Integer.parseInt(tfSize.getText()));
			fontDialog.dispose();
		}
	}
}
