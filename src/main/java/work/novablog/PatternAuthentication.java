package work.novablog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * メイン
 */
public class PatternAuthentication extends JFrame {

	private JPanel contentPane;
	private static JButton reset_btn;
	private static JButton end_btn;
	private static JLabel remaining_label;
	private PatternAuthCanvas canvas_1;

	private static String[] args;

	//ボタン無効用のスレッド
	Runnable runnable = new Runnable() {
		public void run() {
			reset_btn.setEnabled(false);
			end_btn.setEnabled(false);
		}
	};

	/**
	 * エントリポイント
	 */
	public static void main(String[] args) {
		try {
			PatternAuthentication.args = args;
			PatternAuthentication frame = new PatternAuthentication();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ウィンドウの作成
	 */
	public PatternAuthentication() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 430, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		//引数の確認
		String passwd = null;
		int count = -1;
		if (args.length >= 2) {
			//引数が2つ以上なら
			passwd = args[0];
			try {
				count = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println("第2引数は数字でないといけません！");
				System.exit(1);
			}
			if(count <= 0) {
				System.out.println("第2引数には1以上の整数を指定してください！");
				System.exit(1);
			}
		}

		//残り試行可能回数表示用ラベル
		remaining_label = new JLabel("");
		remaining_label.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		remaining_label.setBounds(10, 10, 388, 21);
		contentPane.add(remaining_label);

		//パターン画面の表示
		PatternEvent p_event = new PatternEvent() {
			public void create_pattern() {
				reset_btn.setEnabled(true);
				end_btn.setEnabled(true);
			}

			public void wrong_passwd(int remaining) {
				remaining_label.setText(String.format("残り試行可能回数は%d", remaining));
			}

			public void create_end(String passwd) {
				System.out.println(passwd);
				System.exit(0);
			}

			public void auth_end(boolean ok) {
				if (ok) {
					System.out.println("ok");
				} else {
					System.out.println("ng");
				}

				System.exit(0);

			}

		};

		if (passwd == null || count == -1) {
			//パターンからパスワード生成の場合
			canvas_1 = new PatternAuthCanvas(p_event);
		} else {
			//認証の場合
			canvas_1 = new PatternAuthCanvas(p_event, passwd, count);
		}
		canvas_1.setBounds(10, 41, 388, 388);
		contentPane.add(canvas_1);

		//リセットボタンの作成
		reset_btn = new JButton("リセット");
		reset_btn.setEnabled(false);
		reset_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				canvas_1.reset();
				//デッドロック（？）現象を回避するために別スレッドでボタン無効処理をする
				new Thread(runnable).start();
			}
		});
		reset_btn.setBounds(10, 439, 87, 21);
		contentPane.add(reset_btn);

		//完了ボタンの作成
		end_btn = new JButton("完了");
		end_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				canvas_1.end_process();
			}
		});
		end_btn.setEnabled(false);
		end_btn.setBounds(311, 439, 87, 21);
		contentPane.add(end_btn);

	}
}
