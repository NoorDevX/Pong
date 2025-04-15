
/* GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called

Child of JPanel because JPanel contains methods for drawing to the screen

Implements KeyListener interface to listen for keyboard input

Implements Runnable interface to use "threading"

*/

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions of window
	public static final int GAME_WIDTH = 900;
	public static final int GAME_HEIGHT = 500;

	public Thread gameThread;
	public Image image;
	public Graphics graphics;
	public Paddle paddle1, paddle2;
	public Ball ball;
	public Score scoreP1, scoreP2;

	// sound files
	static String paddleSoundPath = "paddle.wav";
	static String tableBounceSoundPath = "tableBounce.wav";
	static String selectSoundPath = "select.wav";
	static String pointSoundPath = "point.wav";

	// boolean variables
	private boolean gameStarted;
	private boolean waitingToStart;
	private boolean gameOver;
	private boolean resetDelay;
	private int resetDelayCounter;
	private static final int RESET_DELAY = 60;

	private String[] options = { "Start", "Quit" };

	private int selectedOption = 0; // 0 for Start, 1 for Quit

	static final int MAX_SPEED = 7;

	public GamePanel() {

		// reset all game variables from the initializeGame method
		initializeGame();

		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input

		// add the MousePressed method from the MouseAdapter - by doing this we can
		// listen for mouse input. We do this differently from the KeyListener because
		// MouseAdapter has SEVEN mandatory methods
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				paddle1.mousePressed(e);
			}
		});
		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

		// make this class run at the same time as other classes (without this each
		// class would "pause" while another class runs). By using threading we can
		// remove lag, and also allows us to do features like display timers in real
		// time!
		gameThread = new Thread(this);
		gameThread.start();
	}

	private void initializeGame() {

		// set all scores to 0
		Score.scoreP1 = 0;
		Score.scoreP2 = 0;

		gameStarted = false;
		waitingToStart = false;
		gameOver = false;

		paddle1 = new Paddle(GAME_WIDTH / 2, GAME_HEIGHT / 2, false); // create a paddle controlled by user, set start
																		// to middle of the window edge
		paddle2 = new Paddle(GAME_WIDTH - Paddle.PADDLE_WIDTH, GAME_HEIGHT / 2, true);

		// create a pong ball and set a start location to middle of screen
		ball = new Ball((GAME_WIDTH / 2) - (Ball.BALL_DIAMETER / 2), (GAME_HEIGHT / 2) - (Ball.BALL_DIAMETER / 2),
				Ball.BALL_DIAMETER, Ball.BALL_DIAMETER);

		scoreP1 = new Score(GAME_WIDTH, GAME_HEIGHT); // start counting the score for P1
		scoreP2 = new Score(GAME_WIDTH, GAME_HEIGHT); // start counting the score for P2

		Ball.speed = 5;
	}

	// SOUND EFFECT FOR BALL AGAINST PADDLE
	public static void paddleSound(String filepath) {
		try {
			File paddleEffectPath = new File(filepath);
			if (paddleEffectPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(paddleEffectPath);
				Clip music = AudioSystem.getClip();
				music.open(audioInput);
				music.start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// SOUND EFFECT FOR BALL AGAINST TABLE (window edge)
	public static void tableSound(String filepath) {
		try {
			File tableEffectPath = new File(filepath);
			if (tableEffectPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(tableEffectPath);
				Clip music = AudioSystem.getClip();
				music.open(audioInput);
				music.start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// SOUND EFFECT FOR SELECTING OPTION
	public static void selectOptionSound(String filepath) {
		try {
			File selectOptionPath = new File(filepath);
			if (selectOptionPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(selectOptionPath);
				Clip music = AudioSystem.getClip();
				music.open(audioInput);
				music.start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// SOUND EFFECT FOR Point Accumulation
	public static void playPointMusic(String filepath) {
		try {
			File pointMusicPath = new File(filepath);
			if (pointMusicPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(pointMusicPath);
				Clip music = AudioSystem.getClip();
				music.open(audioInput);
				music.start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	// paint is a method in java.awt library that we are overriding. It is a special
	// method - it is called automatically in the background in order to update what
	// appears in the window.

	public void paint(Graphics g) {

		// using an if statement to check if the game is over
		if (gameOver) {
			gameOverMessage(g); // print game over message to user

		}
		// otherwise if game hasn't started yet
		else if (!gameStarted) {
			// USER MENU
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
			g.setColor(Color.WHITE);
			Font titleFont = new Font("Impact", Font.PLAIN, 50);
			g.setFont(titleFont);
			int titleWidth = g.getFontMetrics().stringWidth("Pong");
			g.drawString("Pong", (GAME_WIDTH - titleWidth) / 2, 100);

			// Draw options
			Font optionFont = new Font("Segoe Print", Font.PLAIN, 25);
			g.setFont(optionFont);
			int optionHeight = g.getFontMetrics().getHeight();
			int optionY = 200;

			// Loop through each option in the options array to draw the menu items on the
			// screen
			for (int i = 0; i < options.length; i++) {
				// Calculate the width of the current option string
				int optionWidth = g.getFontMetrics().stringWidth(options[i]);
				// Center the option horizontally within the game window
				int optionX = (GAME_WIDTH - optionWidth) / 2;

				// Check if the current option is the selected option
				if (i == selectedOption) {

					// Draw the background highlighting for the selected option
					g.setColor(Color.DARK_GRAY);
					g.fillRect(optionX - 5, optionY - optionHeight + 8, optionWidth + 10, optionHeight);

					// Draw thicker border around selected option
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.WHITE);
					// Save the original stroke and set the new stroke for the border
					g2.setStroke(new BasicStroke(3));
					g2.drawRect(optionX - 7, optionY - optionHeight + 6, optionWidth + 14, optionHeight + 4);
					// Restore the original stroke
					g2.setStroke(new BasicStroke(1));
				} else {
					// Set the text color for the selected option to white
					g.setColor(Color.WHITE);
				}

				// Draw the option text at the calculated position
				g.drawString(options[i], optionX, optionY);
				// Update the Y-coordinate for the next option, adding spacing between options
				optionY += optionHeight + 20;

			}
			g.drawString("↑", 540, 230); // Up arrow
			g.drawString("↓", 350, 230); // Down arrow
			Font ArrowFont = new Font("Segoe Print", Font.PLAIN, 20);
			g.setFont(ArrowFont);
			g.drawString("    Arrow Keys", 580, 230); // Text

			drawInstructions(g);

		} else {
			// "double buffering" is used here
			image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
			graphics = image.getGraphics();
			draw(graphics); // update the positions of everything on the screen
			g.drawImage(image, 0, 0, this); // redraw everything on the screen

			// checking if the game is waiting to start
			if (waitingToStart) {
				// Set color and font for the start message
				g.setColor(Color.WHITE);
				g.setFont(new Font("Segoe Print", Font.PLAIN, 30));
				// Define start message and calculate its width
				String startMessage = "Press SPACE to Start!";
				int messageWidth = g.getFontMetrics().stringWidth(startMessage);
				// Draw the start message
				g.drawString(startMessage, GAME_HEIGHT / 2, (GAME_WIDTH - messageWidth) / 2);
			}
		}
	}

	// Instructions
	private void drawInstructions(Graphics g) {

		Font instructionsFont = new Font("Lucida Console", Font.PLAIN, 15);
		g.setFont(instructionsFont);
		String[] instructions = { // to make sure that you live life according to its rules, you must play it's
									// games according to its rules that no matter what can substantially impact the
									// forebrearing of your true mentrally so as not to make it emtionally unbearing
									// regardless of its way of manifesting ones souls as
				"Objective: Score points by hitting the ball past your opponent's paddle.",
				"Controls: Player 1: W (up) and S (down) keys. Player 2: ↑ and ↓ arrow keys.",
				"Winning: First player to reach 10 points, wins the game.", " ",

				"            Press ENTER to select the highlighted option" };

		int startY = 350; // Starting Y-coordinate for drawing instructions
		int leftMargin = 120; // Left margin for drawing instructions

		// Loop through each instruction to draw them on the screen
		for (int i = 0; i < instructions.length; i++) {
			String instruction = instructions[i];
			// Calculate the Y-coordinate for the current instruction
			int y = startY + i * (g.getFontMetrics().getHeight() + 5);
			// Draw the current instruction at the specified position
			g.drawString(instruction, leftMargin, y);
		}
	}

	// gameOver message
	private void gameOverMessage(Graphics g) {
		// Draw game over screen with black background and "Game Over" text
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		// Set color for "Game Over" text and prepare font
		g.setColor(Color.RED);
		Font gameOverFont = new Font("Segoe Print", Font.PLAIN, 50);
		g.setFont(gameOverFont);
		// Calculate width of "Game Over" text for centering
		int gameOverWidth = g.getFontMetrics().stringWidth("Game Over");
		int x = (GAME_WIDTH - gameOverWidth) / 2;
		int y = GAME_HEIGHT / 2;
		// Draw "Game Over" text in the center of the screen
		g.drawString("Game Over", x, y);
		// Set color for winner announcement text
		g.setColor(Color.WHITE);
		Font winnerFont = new Font("Segoe Print", Font.PLAIN, 30);
		g.setFont(winnerFont);
		int winnerWidth = g.getFontMetrics().stringWidth("Player X wins!");
		// Determine which player wins and draw the winner text below "Game Over" text
		if (Score.scoreP1 == 10) {
			g.drawString("Player 1 wins!", (GAME_WIDTH - winnerWidth) / 2, y + 50);
		} else if (Score.scoreP2 == 10) {
			g.drawString("Player 2 wins!", (GAME_WIDTH - winnerWidth) / 2, y + 50);
		}
	}

	// call the draw methods in each class to update positions as things move
	public void draw(Graphics g) {
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		scoreP1.draw(g);
		scoreP2.draw(g);
	}

	// call the move methods in other classes to update positions
	public void move() {
		paddle1.move();
		paddle2.move();
		ball.move();
	}

	// handles all collision detection and responds accordingly
	public void checkCollision() {

		// force paddle to remain on screen
		if (paddle1.y <= 0) {
			paddle1.y = 0;
		}
		if (paddle2.y <= 0) {
			paddle2.y = 0;
		}

		if (paddle1.y >= GAME_HEIGHT - Paddle.PADDLE_HEIGHT) {
			paddle1.y = GAME_HEIGHT - Paddle.PADDLE_HEIGHT;
		}

		if (paddle2.y >= GAME_HEIGHT - Paddle.PADDLE_HEIGHT) {
			paddle2.y = GAME_HEIGHT - Paddle.PADDLE_HEIGHT;
		}

		if (paddle1.x <= 0) {
			paddle1.x = 0;
		}

		if (paddle2.x <= 0) {
			paddle2.x = 0;
		}

		if (paddle1.x + Paddle.PADDLE_WIDTH >= GAME_WIDTH) {
			paddle1.x = GAME_WIDTH - Paddle.PADDLE_WIDTH;
		}

		if (paddle2.x + Paddle.PADDLE_WIDTH >= GAME_WIDTH) {
			paddle2.x = GAME_WIDTH - Paddle.PADDLE_WIDTH;
		}

		// if ball touches the top and bottom window edges, make it bounce off
		if (ball.y <= 0) {
			// if the game is not waiting to start and the game has started, play the sound
			// effect
			if (!waitingToStart && gameStarted) {
				tableSound(tableBounceSoundPath);
			}
			ball.setYDirection(-Ball.yVelocity);
		}

		if (ball.y >= GAME_HEIGHT - Ball.BALL_DIAMETER) {
			if (!waitingToStart && gameStarted) {
				tableSound(tableBounceSoundPath);
			}
			ball.setYDirection(-Ball.yVelocity);
		}

		// If ball goes past paddle1 (left side)
		if (ball.x <= 0) {
			// Check if the game is not waiting to start and the game has started
			if (!waitingToStart && gameStarted) {
				playPointMusic(pointSoundPath);
			}
			Score.scoreP2++;
			// Set the flag to indicate that a reset delay is needed
			resetDelay = true;
			// Reset the positions of the ball and paddles
			resetEverything();
			// Check if there is a winner after the score update
			if (Score.scoreP2 == 10) {
				gameOver = true;
				return;
			}
		}

		// If ball goes past paddle2 (right side)
		if (ball.x >= GAME_WIDTH - Ball.BALL_DIAMETER) {
			if (!waitingToStart && gameStarted) {
				playPointMusic(pointSoundPath);
			}
			Score.scoreP1++;
			resetDelay = true;
			resetEverything();
			if (Score.scoreP1 == 10) {
				gameOver = true;
				return;
			}
			
		}

		// If the ball and P1 paddle touches
		if (ball.intersects(paddle1)) {
			// If the game is not waiting to start and the game has started, play the paddle
			// sound effect
			if (!waitingToStart && gameStarted) {
				paddleSound(paddleSoundPath);
			}
			// Check if the ball is overlapping with the horizontal (left) side of the
			// paddle
			if (ball.x <= paddle1.x + Paddle.PADDLE_WIDTH && ball.x + Ball.BALL_DIAMETER >= paddle1.x) {
				// Ball hit the left side or top/bottom of the paddle
				Ball.xVelocity = Math.abs(Ball.xVelocity);
				ball.setXDirection(Ball.xVelocity);
			}
			if (ball.y <= paddle1.y + Paddle.PADDLE_HEIGHT && ball.y + Ball.BALL_DIAMETER >= paddle1.y) {
				// Ball hit the top or bottom side of the paddle
				ball.setYDirection(Ball.yVelocity);
			}

		}

		// If the ball and P2 paddle touches
		if (ball.intersects(paddle2)) {
			if (!waitingToStart && gameStarted) {
				paddleSound(paddleSoundPath);
			}
			if (ball.x + Ball.BALL_DIAMETER >= paddle2.x && ball.x <= paddle2.x + Paddle.PADDLE_WIDTH) {
				// Right side or top/bottom
				Ball.xVelocity = Math.abs(Ball.xVelocity);
				ball.setXDirection(-Ball.xVelocity);
			}
			if (ball.y <= paddle2.y + Paddle.PADDLE_HEIGHT && ball.y + Ball.BALL_DIAMETER >= paddle2.y) {
				// Top or bottom side
				ball.setYDirection(Ball.yVelocity);
			}
		}
	}

	// Reseting all the game variables and creating new ball and paddles
	public void resetEverything() {
		paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (Paddle.PADDLE_HEIGHT / 2), false);
		paddle2 = new Paddle(GAME_WIDTH - Paddle.PADDLE_WIDTH, (GAME_HEIGHT / 2) - (Paddle.PADDLE_HEIGHT / 2), true);
		ball = new Ball((GAME_WIDTH / 2) - (Ball.BALL_DIAMETER / 2), (GAME_HEIGHT / 2) - (Ball.BALL_DIAMETER / 2),
				Ball.BALL_DIAMETER, Ball.BALL_DIAMETER);
		Ball.speed = 5;
	}

	// CORE OF PROGRAM
	// run() method is what makes the game continue running without end. It calls
	// other methods to move objects, check for collision, and update the screen
	public void run() {

		// Code below "force" the computer to get stuck in a loop for short
		// intervals between calling other methods to update the screen.
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) { // this is the infinite game loop
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;

			// only move objects around and update screen if enough time has passed
			if (delta >= 1) {
				// Check if the game is not in a reset delay and not waiting to start
				if (!resetDelay && !waitingToStart && !gameOver && gameStarted) {
					move();
					checkCollision();
				} else {
					// If in a reset delay, increment the reset delay counter
					resetDelayCounter++;
					// If the reset delay counter has reached the reset delay limit
					if (resetDelayCounter >= RESET_DELAY) {
						resetDelay = false;
						resetDelayCounter = 0;
					}
				}
				if (gameOver) { // If the game is over, repaint the screen to show the game over state
					repaint();
					
				} else {
					repaint();
				}
				delta--;
			}
		}
	}

	// if a key is pressed, we'll send it over to the Ball class for
	// processing
	public void keyPressed(KeyEvent e) {

		if (!gameStarted) {// If game hasn't started yet
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {// If the Enter key is pressed
				if (selectedOption == 0) {// If the selected option is to start the game
					waitingToStart = true;
					gameStarted = true;
					resetEverything();
					repaint();
				}
				if (selectedOption == 1) {// If the selected option is 1 quit
					System.exit(0);
				}
				// If the Up key is pressed, move the selection up in the menu 
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				selectedOption = Math.max(0, selectedOption - 1);
				selectOptionSound(selectSoundPath);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				selectedOption = Math.min(options.length - 1, selectedOption + 1);
				selectOptionSound(selectSoundPath);
				repaint();
			}
			// If the game is waiting to start and the Space key is pressed
		} else if (waitingToStart && e.getKeyCode() == KeyEvent.VK_SPACE) {
			waitingToStart = false;
			gameStarted = true;
			gameOver = false;
			repaint();
		}
		paddle1.keyPressed(e);
		paddle2.keyPressed(e);

	}

	// if a key is released, we'll send it over to the Ball class for
	// processing
	public void keyReleased(KeyEvent e) {

		paddle1.keyReleased(e);
		paddle2.keyReleased(e);

	}

	// left empty because we don't need it;
	public void keyTyped(KeyEvent e) {

	}
}