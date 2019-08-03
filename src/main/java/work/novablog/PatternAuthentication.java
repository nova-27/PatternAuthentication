package work.novablog;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PatternAuthentication extends JFrame {

	private JPanel contentPane;

	/**
	 * START
	 */
	public static void main(String[] args) {
		try {
			PatternAuthentication frame = new PatternAuthentication();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public PatternAuthentication() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 563, 515);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		PatternAuthCanvas canvas_1 = new PatternAuthCanvas();
		canvas_1.setBounds(10, 10, 388, 388);
		contentPane.add(canvas_1);
	}
}
