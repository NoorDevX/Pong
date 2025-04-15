/*import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Main extends JFrame implements ActionListener {

	JTextField inFat = new JTextField(7);  //the argument (7) represents the number of "columns" in the text box
	//columns - basically means how wide the text field is, but changes based on the current font
	//learn more about text fields here: https://docs.oracle.com/javase/tutorial/uiswing/components/textfield.html
	JTextField inCal = new JTextField(7);
	JTextField outPer = new JTextField(7);
	JButton calc = new JButton("Calculate...");
	
	int calories, fatGrams;
	double percent;

	public Main() {

		this.setTitle("Calories from Fat");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(280, 200);
		this.setVisible(true);
		this.setSize(400, 300);
		this.setSize(280, 200);
		this.setVisible(true); //this always has to be at the bottom of the constructor
		
		
		JLabel title = new JLabel("Percent of Calories from Fat");
		JLabel fatLabel = new JLabel("Enter grams of fat: ");
		JLabel calLabel = new JLabel("Enter total calories: ");
		JLabel perLabel = new JLabel("Percent calories from fat: ");
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
		c.add(title);
		c.add(fatLabel);
		c.add(inFat);
		c.add(calLabel);
		c.add(inCal);
		c.add(perLabel);
		c.add(outPer);
		outPer.setEditable(false);
		c.add(calc);
		
		calc.addActionListener(this);
		
	}
	
	private void calcPercent() {
		percent = ((fatGrams*9.0) / calories) * 100.0;
	}

	public static void main(String[] args) {
		new Main();

	}
	
	public void actionPerformed(ActionEvent evt) {
		//getText() gets text from the textfield, as a string. Must convert to integer to do math calculations
		
		fatGrams = Integer.parseInt(inFat.getText());
		calories = Integer.parseInt(inCal.getText());
		
		calcPercent();
		
		outPer.setText(percent + " "); //display result. Must convert to String, which is why there is a space in there
		repaint();
		
	}

	
	
}*/
