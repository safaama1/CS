
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameOver extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameOver() {
		super();
	}
	public void paint(Graphics g) {
		g.setFont(new Font("David", Font.PLAIN, 30));
		g.drawString("Game Over",50, 100);
	}

	public static void create() {
		JFrame frame = new JFrame("JavaTutorial.net");
		frame.getContentPane().add(new GameOver());
		frame.setSize(256, 256);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	}
}
