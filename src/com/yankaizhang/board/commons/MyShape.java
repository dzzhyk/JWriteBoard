package com.yankaizhang.board.commons;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ͼ����
 */
public class MyShape implements Serializable, Cloneable {

	public int type = -1; // ͼ�ε�����
	public Shape shape = null; // ��ʾ��Բ���߶Σ����ε�һ��ͼ��
	public Color fontColor = Color.black; // ǰ��ɫ
	public Color backColor = Color.white; // ����ɫ
	public Font font = null; // �����ʽ
	public List<Point> pointList = new CopyOnWriteArrayList<>(); // ��ʾ�ɵ��󹹳ɵ�ͼ�εĵ㣬�� ����
	public Point pointOld = null; // �������κ���ʾ�ַ���ʹ��
	public Point pointNew = null; // �������κ���ʾ�ַ���ʹ��
	public String text = null; // ��Ҫ��ʾ���ַ���
	public int penLength = 15; // ���ʿ��
	public int eraserLength = 20; // ��Ƥ���
	public String message = null;  //��Ϣ
	public List<String> onlineList = new CopyOnWriteArrayList<>();

	public MyShape() {}

	/**
	 * ���õ�ǰͼ��
	 */
	public void reset() {
		type = -1;
		shape = null;
		pointOld = null;
		pointNew = null;
		text = null;
		message = null;
		pointList.clear();
		onlineList.clear();
	}

	@Override
	public MyShape clone() {
		MyShape newShape = new MyShape();
		newShape.shape = this.shape;
		newShape.type = this.type;
		newShape.backColor = this.backColor;
		newShape.fontColor = this.fontColor;
		newShape.font = this.font;
		newShape.pointList = this.pointList;
		newShape.text = this.text;
		newShape.pointOld = this.pointOld;
		newShape.pointNew = this.pointNew;
		newShape.penLength = this.penLength;
		newShape.eraserLength = this.eraserLength;
		newShape.message = this.message;
		return newShape;
	}
}
