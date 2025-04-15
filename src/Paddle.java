
/* Paddle class defines behaviours for the paddle for P1 and P2 

child of Rectangle because that makes it easy to draw and check for collision
*/


import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle {

	public int yVelocity;
	public final int SPEED = 4; // movement speed of paddle
	public static final int PADDLE_WIDTH = 13; // size of paddle
	public static final int PADDLE_HEIGHT = 88;
	private boolean isP2Paddle; // to identify which paddle it is

	// constructor creates paddle for P1 and P2 at given location with given
	// dimensions
	public Paddle(int x, int y, boolean isP2Paddle) {

		super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);

		// Check if this is the paddle for P2
		if (isP2Paddle) {
			this.x = GamePanel.GAME_WIDTH - PADDLE_WIDTH;
		} else {
			this.x = 0;
		}
		this.y = (GamePanel.GAME_HEIGHT - PADDLE_HEIGHT) / 2;

		this.isP2Paddle = isP2Paddle;

	}

	// updates the direction of the paddle

	public void keyPressed(KeyEvent e) {

		// Checks if this is the paddle for P2
		if (isP2Paddle) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				setYDirection(SPEED * -1);
				move();
			}

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				setYDirection(SPEED);
				move();
			}
		}

		else if (!isP2Paddle) {
			if (e.getKeyChar() == 'w') {
				setYDirection(SPEED * -1);
				move();
			}

			if (e.getKeyChar() == 's') {
				setYDirection(SPEED);
				move();
			}
		}

	}

	// called from GamePanel when any key is released (no longer being pressed down)
	public void keyReleased(KeyEvent e) {
		// checks if this is the paddle for P2
		if (isP2Paddle) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				setYDirection(0);
				move();
			}

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				setYDirection(0);
				move();
			}
		}

		else if (!isP2Paddle) {

			if (e.getKeyChar() == 'w') {
				setYDirection(0);
				move();
			}

			if (e.getKeyChar() == 's') {
				setYDirection(0);
				move();
			}
		}

	}

	// changes the current location of the paddle to be wherever the mouse is
	// located
	// on the screen
	public void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	// called whenever the movement of the paddle changes in the y-direction
	// (up/down)
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}

	// updates the current location of the paddle
	public void move() {
		y += yVelocity;

	}

	// draws the current location of the paddle to the screen
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
	}

}