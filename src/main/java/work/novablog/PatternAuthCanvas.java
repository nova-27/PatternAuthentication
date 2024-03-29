package work.novablog;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.swing.JPanel;

/**
 * 3x3パターンからパスワードを生成する
 */
public class PatternAuthCanvas extends JPanel implements MouseMotionListener {
	// x=マウスx座標 y=マウスy座標 c_width=横幅の中心 c_height=縦幅の中心 min_width=横幅と縦幅の短い方
	int x, y, c_width, c_height, min_width;
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
	// パターン作成し終わったか？ true=し終わった
	boolean stop;
	// イベント処理
	PatternEvent p_event;
	// 一致確認用パスワード
	String passwd;
	// 残り試行可能回数
	int remaining;

	public PatternAuthCanvas(PatternEvent p_event) {
		this.p_event = p_event;
		passwd = "";
		remaining = -1;

		Init();
		addMouseMotionListener(this);
	}

	public PatternAuthCanvas(PatternEvent p_event, String passwd, int count) {
		this.p_event = p_event;
		this.passwd = passwd;
		this.remaining = count;

		Init();
		addMouseMotionListener(this);
	}

	// 描画時に呼ばれる
	public void paint(Graphics g) {
		// 初期化
		c_width = getWidth() / 2;
		c_height = getHeight() / 2;
		min_width = Math.min(getWidth(), getHeight());

		// r=ドットの半径
		int r = (int) ((double) 7 / 388 * min_width);

		// 背景色で塗りつぶす
		g.setColor(new Color(49, 124, 183));
		g.fillRect(0, 0, getWidth(), getHeight());

		// ドットの描画

		/* ドットID = 0
		 * ○✕✕
		 * ✕✕✕
		 * ✕✕✕
		 */
		g.setColor(Color.white);
		circ_center[0][0] = c_width - (int) ((double) 102 / 388 * min_width);
		circ_center[0][1] = c_height - (int) ((double) 102 / 388 * min_width);
		g.fillOval(circ_center[0][0] - r, circ_center[0][1] - r, r * 2, r * 2);

		/* ドットID = 1
		 * ✕○✕
		 * ✕✕✕
		 * ✕✕✕
		 */
		circ_center[1][0] = c_width;
		circ_center[1][1] = c_height - (int) ((double) 102 / 388 * min_width);
		g.fillOval(circ_center[1][0] - r, circ_center[1][1] - r, r * 2, r * 2);

		/* ドットID = 2
		 * ✕✕○
		 * ✕✕✕
		 * ✕✕✕
		 */
		circ_center[2][0] = c_width + (int) ((double) 102 / 388 * min_width);
		circ_center[2][1] = c_height - (int) ((double) 102 / 388 * min_width);
		g.fillOval(circ_center[2][0] - r, circ_center[2][1] - r, r * 2, r * 2);

		/* ドットID = 3
		 * ✕✕✕
		 * ○✕✕
		 * ✕✕✕
		 */
		circ_center[3][0] = c_width - (int) ((double) 102 / 388 * min_width);
		circ_center[3][1] = c_height;
		g.fillOval(circ_center[3][0] - r, circ_center[3][1] - r, r * 2, r * 2);

		/* ドットID = 4
		 * ✕✕✕
		 * ✕○✕
		 * ✕✕✕
		 */
		circ_center[4][0] = c_width;
		circ_center[4][1] = c_height;
		g.fillOval(circ_center[4][0] - r, circ_center[4][1] - r, r * 2, r * 2);

		/* ドットID = 5
		 * ✕✕✕
		 * ✕✕○
		 * ✕✕✕
		 */
		circ_center[5][0] = c_width + (int) ((double) 102 / 388 * min_width);
		circ_center[5][1] = c_height;
		g.fillOval(circ_center[5][0] - r, circ_center[5][1] - r, r * 2, r * 2);

		/* ドットID = 6
		 * ✕✕✕
		 * ✕✕✕
		 * ○✕✕
		 */
		circ_center[6][0] = c_width - (int) ((double) 102 / 388 * min_width);
		circ_center[6][1] = c_height + (int) ((double) 102 / 388 * min_width);
		g.fillOval(circ_center[6][0] - r, circ_center[6][1] - r, r * 2, r * 2);

		/* ドットID = 7
		 * ✕✕✕
		 * ✕✕✕
		 * ✕○✕
		 */
		circ_center[7][0] = c_width;
		circ_center[7][1] = c_height + (int) ((double) 102 / 388 * min_width);
		g.fillOval(circ_center[7][0] - r, circ_center[7][1] - r, r * 2, r * 2);

		/* ドットID = 8
		 * ✕✕✕
		 * ✕✕✕
		 * ✕✕○
		 */
		circ_center[8][0] = c_width + (int) ((double) 102 / 388 * min_width);
		circ_center[8][1] = c_height + (int) ((double) 102 / 388 * min_width);
		g.fillOval(circ_center[8][0] - r, circ_center[8][1] - r, r * 2, r * 2);

		// ドラッグ中なら
		if (dragged) {
			int j;
			for (j = 0; Order[j] != -1; j++) {
				// ドット間の線を引く
				BasicStroke stroke = new BasicStroke(8.0f);
				((Graphics2D) g).setStroke(stroke);
				if (j != 0) {
					int from_x = circ_center[Order[j - 1]][0];
					int from_y = circ_center[Order[j - 1]][1];

					int to_x = circ_center[Order[j]][0];
					int to_y = circ_center[Order[j]][1];

					g.drawLine(from_x, from_y, to_x, to_y);
				}

				// 円を描く
				g.setColor(new Color(7, 153, 6));
				stroke = new BasicStroke(4.0f);
				((Graphics2D) g).setStroke(stroke);
				g.drawOval(circ_center[Order[j]][0] - (int) ((double) 28.5 / 388 * min_width),
						circ_center[Order[j]][1] - (int) ((double) 28.5 / 388 * min_width),
						(int) ((double) 57 / 388 * min_width), (int) ((double) 57 / 388 * min_width));
				if (j == 8) {
					j++;
					break;
				}
			}
			// 円の中心座標
			int x = circ_center[Order[j - 1]][0];
			int y = circ_center[Order[j - 1]][1];

			if (j != 9) {
				// ドットからマウスまでの線を引く
				BasicStroke stroke = new BasicStroke(8.0f);
				((Graphics2D) g).setStroke(stroke);
				g.drawLine(x, y, this.x, this.y);
			}

		}
	}

	// マウスがドラッグされているときに呼ばれる
	public void mouseDragged(MouseEvent arg0) {
		Point point = arg0.getPoint();
		x = point.x;
		y = point.y;

		if (stop) {
			return;
		}

		if (f_dragged) {
			// ドラッグが初めてのとき
			for (int i = 0; i <= 8; i++) {
				// オーダーを初期化
				// 値が代入されていないときは-1に
				Order[i] = -1;
			}

			for (int i = 0; i <= 8; i++) {
				// ドットの範囲内に入っているかどうか
				if (Math.pow(circ_center[i][0] - x, 2) + Math.pow(circ_center[i][1] - y, 2) < Math
						.pow(22.5 / 388 * min_width, 2)) {
					dragged = true;
					f_dragged = false;

					Order[0] = i;
				}
			}

		} else {
			// ドラッグが2個目以降なら
			for (int i = 0; i <= 8; i++) {
				// ドットの範囲内に入っているかどうか
				if (Math.pow(circ_center[i][0] - x, 2) + Math.pow(circ_center[i][1] - y, 2) < Math
						.pow(22.5 / 388 * min_width, 2)) {

					int j;
					for (j = 0; j <= 8; j++) {
						// すでにオーダーに入っているドットだった場合
						if (Order[j] == i) {
							repaint();
							return;
						}
					}
					// オーダーに追加
					for (j = 0; Order[j] != -1; j++)
						;
					Order[j] = i;
				}
			}
		}

		repaint();

	}

	public void mouseMoved(MouseEvent arg0) {
		// 初期化
		if (dragged) {
			//パターン終了処理
			stop = true;
			if (remaining == -1 || Objects.equals(passwd, "")) {
				//パスワード作成の場合
				p_event.create_pattern();
			} else {
				//パスワード認証の場合
				if (Objects.deepEquals(Password(), passwd)) {
					//パスワードが一致した場合
					p_event.auth_end(true);
					return;
				} else {
					//パスワードが不一致の場合
					remaining--;
					if (remaining == 0) {
						//残り0回の場合
						p_event.auth_end(false);
						return;
					}

					p_event.wrong_passwd(remaining);
					Init();
					repaint();
				}
			}
		}
		dragged = false;
		f_dragged = true;
	}

	// 初期化
	private void Init() {
		x = 0;
		y = 0;
		dragged = false;
		f_dragged = true;
		stop = false;
		for (int i = 0; i <= 8; i++) {
			// オーダーを初期化
			// 値が代入されていないときは-1に
			Order[i] = -1;
		}
	}

	// パターンからパスワードを生成する
	private String Password() {
		if (stop == false) {
			return "error";
		}

		// int配列をstringとして結合
		String order = "";
		for (int i = 0; Order[i] != -1; i++) {
			order += Order[i];
			if (i == 8) {
				break;
			}
		}

		// オーダー(string)からMD5(パスワード)を生成
		String passwd = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] result = md5.digest(order.getBytes());
			BigInteger big_int_result = new BigInteger(1, result);
			passwd = new String(String.format("%032x", big_int_result));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return passwd;
	}

	// パターンを作成し直す(リセット)
	public void reset() {
		Init();
		repaint();
	}

	//終了処理
	public void end_process() {
		p_event.create_end(Password());
	}
}
