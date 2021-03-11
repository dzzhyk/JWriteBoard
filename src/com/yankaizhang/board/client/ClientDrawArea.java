package com.yankaizhang.board.client;

import java.awt.*;
import java.awt.geom.*;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;

import com.yankaizhang.board.commons.MyShape;
import com.yankaizhang.board.util.Logger;


/**
 * 客户端绘图区域
 */
public class ClientDrawArea extends JPanel {

	private final ClientCanvas mainCanvas;
	private MyShape tempShape = new MyShape();

	private CopyOnWriteArrayList<MyShape> shapes = new CopyOnWriteArrayList<>();
	private Color backColor = Color.white;


	public ClientDrawArea(ClientCanvas canvas) {
		this.mainCanvas = canvas;
	}


	@Override
	public void paint(Graphics g) {
		// 使用Graphics2D绘图
		Graphics2D g2 = (Graphics2D) g;
		super.paint(g2);
		this.setBackground(this.backColor);
		for(MyShape temp : shapes) {
			drawShape(g2, temp);
		}

		// 显示当前正在绘制的
		if(tempShape.type != -1){
			drawShape(g2, tempShape);
		}
	}

	/**
	 * 绘制图形
	 */
	private void drawShape(Graphics2D g2, MyShape shape) {
		g2.setColor(shape.fontColor);
		g2.setStroke(new BasicStroke(shape.penLength));
		g2.setFont(shape.font);
		int[][] points = null;

		if(shape.pointList.size() > 1) {
			points = new int[2][shape.pointList.size()];
			for(int i1 = 0; i1 < shape.pointList.size(); i1++) {
				points[0][i1] = shape.pointList.get(i1).x;
				points[1][i1] = shape.pointList.get(i1).y;
			}
		}

		// 根据具体类型绘制图形
		switch(shape.type) {
			// 绘制标准图形
			case 0:
				g2.draw(shape.shape);
				break;
			// 画笔
			case 1:
				if (shape.pointList.size() > 1)
					g2.drawPolyline(points[0], points[1], shape.pointList.size());
				break;
			// 喷枪
			case 2:
				if(shape.pointList.size() > 1) {
					for(int i = 0; i < shape.pointList.size(); i++) {
						points[0][i] = (int) (points[0][i] + Math.pow(-1, i) * 3 * Math.sin(Math.PI * (100 * i - 180) / 180));
						points[1][i] = (int) (points[1][i] + Math.pow(-1, i) * 3 * Math.cos(Math.PI * (100 * i - 180) / 180));
					}
					for(int i1 = 0; i1 < shape.pointList.size(); i1++){
						g2.drawLine(points[0][i1], points[1][i1], points[0][i1], points[1][i1]);
					}
				}
				break;
			// 橡皮擦
			case 3:
				if(shape.pointList.size() > 1) {
					Color old = g2.getColor();
					g2.setColor(backColor);
					for(int i = 0; i < shape.pointList.size(); i++){
						g2.fillRect(points[0][i], points[1][i], shape.eraserLength, shape.eraserLength);
					}
					g2.setColor(old);
				}
				break;
			// 菱形
			case 4:
				int x = Math.min(shape.pointOld.x, shape.pointNew.x), y = Math.min(shape.pointOld.y, shape.pointNew.y);
				int weight = Math.abs(shape.pointOld.x - shape.pointNew.x), height = Math.abs(shape.pointOld.y - shape.pointNew.y);
				int[] xx = new int[4];
				int[] yy = new int[4];
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
			// 绘制字符串
			case 5:
				g2.drawString(shape.text, shape.pointOld.x, shape.pointOld.y);
				break;
			// 清屏
			case 999:
				g2.fillRect(0, 0, this.getWidth(), this.getHeight());
				break;
		}
	}


	/**
	 * 根据主窗口中传递的图形类型以及点，构造相应的图形
	 */
	public void drawShape(String shapeType, Point pOld, Point pNew, boolean isFinish) {
		switch(shapeType) {
			case "LINE":// 图形为直线
				tempShape.shape = new Line2D.Double(pOld, pNew);
				tempShape.type = 0;
				break;
			case "RECT":// 图形为矩形
				tempShape.shape = new Rectangle2D.Double(Math.min(pOld.x, pNew.x), Math.min(pOld.y, pNew.y), Math.abs(pOld.x - pNew.x), Math.abs(pOld.y - pNew.y));
				tempShape.type = 0;
				break;
			case "ECLI":// 图形为椭圆
				tempShape.shape = new Ellipse2D.Double(Math.min(pOld.x, pNew.x), Math.min(pOld.y, pNew.y), Math.abs(pOld.x - pNew.x), Math.abs(pOld.y - pNew.y));
				tempShape.type = 0;
				break;
			case "RRECT":// 图形为圆角矩形
				tempShape.shape = new RoundRectangle2D.Double(Math.min(pOld.x, pNew.x), Math.min(pOld.y, pNew.y), Math.abs(pOld.x - pNew.x), Math.abs(pOld.y - pNew.y), 45, 45);
				tempShape.type = 0;
				break;
			case "PEN":// 图形为画笔，要实现点阵
				tempShape.pointList.add(pNew);
				tempShape.type = 1;
				break;
			case "SPEN":// 图形为喷枪，要实现点阵
				tempShape.pointList.add(pNew);
				tempShape.type = 2;
				break;
			case "EARSER":// 图形为橡皮擦，要实现点阵
				tempShape.pointList.add(pNew);
				tempShape.type = 3;
				break;
			case "DIAMOND":// 图形为菱形，要实现起点和终点
				tempShape.pointOld = (Point) pOld.clone();
				tempShape.pointNew = (Point) pNew.clone();
				tempShape.type = 4;
				break;
			case "TEXT":// 图形为字符串，要实现起点
				// 若用户误点输入文字，需对空输入进行处理
				tempShape.text = JOptionPane.showInputDialog("请输入文字：", "添加文字");
				if(tempShape.text == null || "".equals(tempShape.text.trim())){
					JOptionPane.showMessageDialog(null, "请输入文字","提示信息", JOptionPane.ERROR_MESSAGE);
					return;
				}
				tempShape.pointOld = (Point) pOld.clone();
				tempShape.type = 5;
				break;
			case "CLS":	// 清屏
				tempShape.type = 999;
				break;
		}

		// 如果绘图完成，储存图形并向服务器发送
		MyShape cloneShape = tempShape.deepClone();
		if ((isFinish && tempShape.type <= 4 && tempShape.type >= 0) || tempShape.type == 5) {
			shapes.add(cloneShape);
			if(mainCanvas.isOnline()){
				mainCanvas.sendObjectMessage(cloneShape);
			}
			tempShape.reset();
		}

		if (tempShape.type == 999){
			shapes.clear();
			if (mainCanvas.isOnline()){
				mainCanvas.sendObjectMessage(cloneShape);
			}
			tempShape.reset();
		}

		repaint();
	}

	/**
	 * 当背景色或者前景色改变时调用
	 */
	public void RepaintType(Color background, Color front) {
		if(background != null) {
			backColor = background;
			tempShape.backColor = background;
			tempShape.type = 7;
			shapes.add(tempShape.deepClone());
			if(mainCanvas.isOnline()){
				mainCanvas.sendObjectMessage(tempShape.deepClone());
			}
			repaint();
		}
		if(front != null){
			tempShape.fontColor = front;
		}
	}

	public MyShape getTempShape() {
		return tempShape;
	}

	public CopyOnWriteArrayList<MyShape> getShapes() {
		return shapes;
	}

	public Color getBackColor() {
		return backColor;
	}

	public void setShapes(CopyOnWriteArrayList<MyShape> shapes) {
		this.shapes = shapes;
	}

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}
}
