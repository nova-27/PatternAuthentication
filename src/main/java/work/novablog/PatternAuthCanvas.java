package work.novablog;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * 3x3パターンからパスワードを生成する
 * @author nova27
 */
public class PatternAuthCanvas extends JPanel implements MouseMotionListener{
	// x=マウスx座標 y=マウスy座標 c_width=横幅の中心 c_height=縦幅の中心 min_width=横幅と縦幅の短い方
	int x,y,c_width,c_height,min_width;
	/*
	 * ドットの真ん中座標リスト
	 *
	 * ドットID
	 * [0][n] [1][n] [2][n]   ・・・
	 * [3][n] [4][n] [5][n] = ・・・
	 * [6][n] [7][n] [8][n]   ・・・
	 *
	 * [n][0] = x座標
	 * [n][1] = y座標
	 */
	int[][] circ_center = new int[9][2];
	/*
	 * ドット移動の順番
	 * [0]=最初のドットID [9]=最後のドットID
	 */
	int[] Order = new int[9];
	// ドラッグ中か？ true=ドラッグ中
	boolean dragged;
	// ドラッグ最初か？ true=ドラッグ最初である
	boolean f_dragged;

	// 初期化
	public PatternAuthCanvas() {
		x = 0;
		y = 0;
		dragged = false;
		f_dragged = true;

		addMouseMotionListener(this);
	}

	// 描画時に呼ばれる
	public void paint(Graphics g) {
		// 初期化
		c_width = getWidth()/2;
		c_height = getHeight()/2;
		min_width = Math.min(getWidth(), getHeight());

		// r=ドットの半径
		int r = (int)((double)7/388*min_width);

		// 背景色で塗りつぶす
		g.setColor(Color.blue);
		g.fillRect(0, 0, getWidth(), getHeight());

		/* ドットの描画
		 *
		 * ドットID = 4
		 * ✕✕✕
		 * ✕○✕
		 * ✕✕✕
		 */
		g.setColor(Color.white);
		circ_center[4][0] = c_width;
		circ_center[4][1] = c_height;
		g.fillOval(circ_center[4][0]-r, circ_center[4][1]-r, r*2, r*2);

		//ドットID = 1
		// ✕○✕
		// ✕✕✕
		// ✕✕✕
		g.setColor(Color.white);
		circ_center[1][0] = c_width;
		circ_center[1][1] = c_height - (int)((double)102/388*min_width);
		g.fillOval(circ_center[1][0]-r, circ_center[1][1]-r, r*2, r*2);

		if(dragged) {
			BasicStroke stroke = new BasicStroke(3.0f);
		    ((Graphics2D)g).setStroke(stroke);
			g.drawLine(c_width, c_height, x, y);
		}
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		Point point = arg0.getPoint();
		x = point.x;
		y = point.y;

		if(f_dragged) {
			if (Math.pow(circ_center[4][0]-x, 2) + Math.pow(circ_center[4][1]-y, 2)< Math.pow(22.5/388*min_width, 2)) {
				dragged = true;
				f_dragged = false;

				Order[0] = 4;
				repaint();
			}
		}else {
			repaint();
		}

	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		dragged = false;
		f_dragged = true;

		repaint();
	}
}
