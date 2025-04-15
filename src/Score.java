/* Score class defines behaviours for the Score for P1 and P2
*/


import java.awt.*;
import java.awt.event.*;

public class Score extends Rectangle {

	public static int GAME_WIDTH;// width of the window
	public static int GAME_HEIGHT;// height of the window
	public static int scoreP1, scoreP2;

	// constructor creates the score and sets initial score for P1 and P2
	public Score(int w, int h) {
		scoreP1 = 0;
		scoreP2 = 0;
		Score.GAME_WIDTH = w;
		Score.GAME_HEIGHT = h;
	}

	// draw the score to the screen (called from GamePanel frequently)
	public void draw(Graphics g) {

		g.setColor(Color.white);
		g.setFont(new Font("Consolas", Font.PLAIN, 40));
		g.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);

		g.drawString(String.valueOf(scoreP1), (int) (GAME_WIDTH * 0.25), (int) (GAME_HEIGHT * 0.1)); // setting location
																										// of score to
																										// be about the
																										// middle
		g.drawString(String.valueOf(scoreP2), (int) (GAME_WIDTH * 0.75), (int) (GAME_HEIGHT * 0.1));
	}

}
