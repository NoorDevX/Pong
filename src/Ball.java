/* Ball class defines behaviours for the Pong Ball

Child of Rectangle because that makes it easy to draw and check for collision
*/

import java.awt.*;

public class Ball extends Rectangle {

	public static final int BALL_DIAMETER = 25; // size of ball
	public static int yVelocity, xVelocity;
	public static int speed; // speed of ball
	public static int randXDir, randYDir;

	// constructor creates the ball and sets initial velocity and direction
	public Ball(int x, int y, int width, int height) {
		super(x, y, width, height);
		speed = 6;

		// Randomize initial x and y directions
		randXDir = (int) (Math.random() * 2);
		if (randXDir == 0) {
			randXDir--;
		}
		setXDirection(randXDir * speed);

		randYDir = (int) (Math.random() * 2);
		if (randYDir == 0) {
			randYDir--;
		}
		setYDirection(randYDir * speed);
	}

	// Set the x-direction of the ball
	public void setXDirection(int randXDir) {
		xVelocity = randXDir;
	}

	// Set the y-direction of the ball
	public void setYDirection(int randYDir) {
		yVelocity = randYDir;
	}

	// update the position of the ball (called from GamePanel frequently)
	public void move() {
		x += xVelocity;
		y += yVelocity;
	}

	// draw the ball to the screen (called from GamePanel frequently)
	public void draw(Graphics g) {
		g.setColor(Color.red);// set the color
		g.fillOval(x, y, BALL_DIAMETER, BALL_DIAMETER); // draw with given position and dimensions
	}

}
