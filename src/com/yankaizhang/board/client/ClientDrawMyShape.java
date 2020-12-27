package com.yankaizhang.board.client;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;

import com.yankaizhang.board.commons.MyShape;


public class ClientDrawMyShape extends JPanel {

	CopyOnWriteArrayList<MyShape> shapes;	// ��ǰ����ͼ��
	public MyShape tempShape;				// ��ʱͼ��
	ClientThread clientData = null;				// ���ݴ�����
	public static Color backColor = Color.white;


	public ClientDrawMyShape() {
		shapes = new CopyOnWriteArrayList<>();
		tempShape = new MyShape();
	}


	@Override
	public void paint(Graphics g) {
		// ʹ��Graphics2D��ͼ
		Graphics2D g2 = (Graphics2D) g;
		super.paint(g2);
		this.setBackground(backColor);
		for(MyShape temp : shapes) {
			drawShape(g2, (MyShape) temp.clone());
		}

		// ��ʾ��ǰ���ڻ��Ƶ�
		if(tempShape.type != -1){
			drawShape(g2, tempShape);
		}
	}

	/**
	 * ����ͼ��
	 */
	public void drawShape(Graphics2D g2, MyShape shape) {
		g2.setColor(shape.fontColor);// ����ǰ��ɫ
		g2.setStroke(new BasicStroke(shape.penLength)); // ���û��ʿ��
		g2.setFont(shape.font);// ��������
		int[][] points = null;
		// ��vector��Point��תΪ��ά����int[][]
		if(shape.pointList.size() > 1) {
			points = new int[2][shape.pointList.size()];
			for(int i1 = 0; i1 < shape.pointList.size(); i1++) {
				points[0][i1] = shape.pointList.get(i1).x;
				points[1][i1] = shape.pointList.get(i1).y;
			}
		}
		// ���ݾ������ƣ�����ͼ��
		switch(shape.type) {
			case 0: // ���Ʊ�׼��ͼ�Σ����߶Σ����Σ�Բ�Ǿ��Σ���Բ
				g2.draw(shape.shape);
				break;
			case 1:// ʵ�ֻ��ʣ���һϵ�е���
				if(shape.pointList.size() > 1)
					g2.drawPolyline(points[0], points[1], shape.pointList.size());
				break;
			case 2:// ʵ����ǹ����һϵ�е���
				if(shape.pointList.size() > 1) {
					for(int i = 0; i < shape.pointList.size(); i++)// ���㻻Ϊ���Ա�ֱ��Ϊstroke�ĵ�
					{
						points[0][i] = (int) (points[0][i] + Math.pow(-1, i)
								* 3
								* Math.sin(Math.PI * (100 * i - 180) / 180));
						points[1][i] = (int) (points[1][i] + Math.pow(-1, i)
								* 3
								* Math.cos(Math.PI * (100 * i - 180) / 180));
					}
					for(int i1 = 0; i1 < shape.pointList.size(); i1++)
						// ���㻭����
						g2.drawLine(points[0][i1], points[1][i1],
								points[0][i1], points[1][i1]);
				}
				break;

			case 3:// ʵ����Ƥ��Ҳ�ǵ��󣬵���ɫΪ����ɫ
				if(shape.pointList.size() > 1) {
					Color old = g2.getColor();
					g2.setColor(backColor);

					for(int i = 0; i < shape.pointList.size(); i++)
						g2.fillRect(points[0][i], points[1][i],
								shape.eraserLength, shape.eraserLength);
					g2.setColor(old);
				}
				break;

			case 4: // ��������
				int x = Math.min(shape.pointOld.x, shape.pointNew.x),
				y = Math.min(shape.pointOld.y, shape.pointNew.y);
				int weight = Math.abs(shape.pointOld.x - shape.pointNew.x),
						height = Math.abs(shape.pointOld.y - shape.pointNew.y);
				int xx[] = new int[4];
				int yy[] = new int[4];
				xx[0] = x + weight / 2;
				yy[0] = y;
				xx[2] = x + weight / 2;
				yy[2] = y + height;
				xx[1] = x;
				yy[1] = y + height / 2;
				xx[3] = x + weight;
				yy[3] = y + height / 2;
				g2.drawPolygon(xx, yy, 4);
				break;
			case 5:// �����ַ���
				g2.drawString(shape.text, shape.pointOld.x, shape.pointOld.y);
				break;
		}
	}


	// �����������д��ݵ�ͼ�������Լ��㣬������Ӧ��ͼ��
	public void drawShape(String shapeType, Point pOld, Point pNew, boolean isFinish) {
		switch(shapeType) {
			case "LINE":// ͼ��Ϊֱ��
				tempShape.shape = new Line2D.Double(pOld, pNew);
				tempShape.type = 0;
				break;
			case "RECT":// ͼ��Ϊ����
				tempShape.shape = new Rectangle2D.Double(Math.min(pOld.x,
						pNew.x), Math.min(pOld.y, pNew.y), Math.abs(pOld.x
								- pNew.x), Math.abs(pOld.y - pNew.y));
				tempShape.type = 0;
				break;
			case "ECLI":// ͼ��Ϊ��Բ
				tempShape.shape = new Ellipse2D.Double(
						Math.min(pOld.x, pNew.x), Math.min(pOld.y, pNew.y),
						Math.abs(pOld.x - pNew.x), Math.abs(pOld.y - pNew.y));
				tempShape.type = 0;
				break;
			case "RRECT":// ͼ��ΪԲ�Ǿ���
				tempShape.shape = new RoundRectangle2D.Double(Math.min(pOld.x,
						pNew.x), Math.min(pOld.y, pNew.y), Math.abs(pOld.x
								- pNew.x), Math.abs(pOld.y - pNew.y), 45, 45);
				tempShape.type = 0;
				break;
			case "PEN":// ͼ��Ϊ���ʣ�Ҫʵ�ֵ���
				tempShape.pointList.add(pNew);
				tempShape.type = 1;
				break;
			case "SPEN":// ͼ��Ϊ��ǹ��Ҫʵ�ֵ���
				tempShape.pointList.add(pNew);
				tempShape.type = 2;
				break;
			case "EARSER":// ͼ��Ϊ��Ƥ����Ҫʵ�ֵ���
				tempShape.pointList.add(pNew);
				tempShape.type = 3;
				break;
			case "DIAMOND":// ͼ��Ϊ���Σ�Ҫʵ�������յ�
				tempShape.pointOld = (Point) pOld.clone();
				tempShape.pointNew = (Point) pNew.clone();
				tempShape.type = 4;
				break;
			case "TEXT":// ͼ��Ϊ�ַ�����Ҫʵ�����
				// ���û�����������֣���Կ�������д���
				tempShape.text = JOptionPane.showInputDialog("���������֣�", "�������");
				if(tempShape.text == null){
					return ;
				}
				if(tempShape.text.equals("")){
					JOptionPane.showMessageDialog(null,
							"����������","��ʾ��Ϣ",JOptionPane.ERROR_MESSAGE);
					return;
				}
				tempShape.pointOld = (Point) pOld.clone();
				tempShape.type = 5;
				break;
		}

		// ������ͷţ�����Ϊ��ͼ��ɣ�����ͼ�Σ��������������
		if (isFinish && tempShape.type <= 4 && tempShape.type >= 0) {
			shapes.add((MyShape) tempShape.clone());
			if(ClientDraw.isOnNet){
				clientData.sendData((MyShape) tempShape.clone());
			}
			tempShape.reset();
		}

		// ��Ϊ�ַ���������ͼ�Σ��������������
		if (tempShape.type == 5) {
			shapes.add((MyShape) tempShape.clone());
			if(ClientDraw.isOnNet){
				clientData.sendClientMsg(tempShape.clone());
			}
			tempShape.reset();
		}
		repaint();
	}

	// ��÷��������ݵ�����
	public void getDataAndRepaint(MyShape shape) {
		//��ȡ�����б�
		clientData.vecOnlineList = shape.onlineList;
		ClientDraw.jtonline.setText("");
		ClientDraw.jtonline.append("Total��"+String.valueOf(clientData.vecOnlineList.size()));
		for(String str : clientData.vecOnlineList)
			ClientDraw.jtonline.append( "\n" +"User:"+ str);
		if (shape.type == 6) {
			getMessage(shape.message);
			return;
		}
		if (shape.type == 7) {
			backColor = shape.backColor;
			repaint();
			return;
		}
		shapes.add((MyShape) shape.clone());
		repaint();
	}

	// ��������
	public void connect(InetAddress address, int port, boolean just)
			throws IOException {
		if(just) {
			clientData = new ClientThread(address, port);
			clientData.start();
		}
		else {
			ClientDraw.drawPanel.sendMessage(" ���ĺ���:"+ ClientDraw.userName + "������!");
			clientData.close();
			ClientDraw.isOnNet = false;
		}
	}
	// ������ɫ����ǰ��ɫ�ı�ʱ���ã�
	public void Repaint(Color cB, Color cF) {
		shapes.add((MyShape) tempShape.clone());
		if(ClientDraw.isOnNet){
			clientData.sendData((MyShape) tempShape.clone());
		}
		tempShape.reset();
		if(cB != null) {
			backColor = cB;
			tempShape.backColor = cB;
			tempShape.type = 7;
			shapes.add((MyShape) tempShape.clone());
			if(ClientDraw.isOnNet)
				clientData.sendData((MyShape) tempShape.clone());
			repaint();
		}
		tempShape.reset();
		if(cF != null)
			tempShape.fontColor = cF;
	}

	public void sendMessage(String str) {
		MyShape shape = new MyShape();
		shape.message = str;
		shape.type = 6;
		if(ClientDraw.isOnNet){
			clientData.sendClientMsg(shape.clone());
		}
	}

	public void getMessage(String str) {
		String[] sttr = str.split("@");
		if(sttr.length < 2){
			ClientDraw.jta.append(str + "\n");
		}
		else {
			if(sttr[1].equals(ClientDraw.userName)){
				ClientDraw.jta.append("(���Ļ�)" + sttr[0] + sttr[2] + "\n");
			}
		}
	}
}
