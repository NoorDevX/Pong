/*import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Main extends JFrame implements ActionListener {

	JButton b;
	boolean isBlue;

	public Main() {

		this.setTitle("FIRST FRAME");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(500, 400);
		this.setVisible(true);
		this.setSize(400, 300);
		this.setSize(500, 400);
		this.getContentPane().setBackground(Color.blue);

		b = new JButton("click me!");
		b.addActionListener(this);

		Container c = getContentPane();
		c.setLayout(null);
		b.setSize(100, 100);
		b.setLocation(0, 0);
	}

	public static void main(String[] args) {
		new Main();

	}

	public void paint(Graphics g) {
		super.paint(g);
		g.drawString("MISHI MAHROOSH!", 200, 200); // location of text is the other 2 arguments
	}

//actionPerformed is automatically called whenever it needs to be by actionListener - never call it yourself
 * 
	public void actionPerformed(ActionEvent evt) {
		if (isBlue) {
			getContentPane().setBackground(Color.red);
			isBlue = false;
		} else {
			getContentPane().setBackground(Color.blue);
			isBlue = true;
		}
		repaint();
	}
}*/
