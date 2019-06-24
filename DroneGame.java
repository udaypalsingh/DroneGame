import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DroneGame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static int WINDOW_WIDTH = 1500;
	private static int WINDOW_HEIGHT = 900;
	
	private int winningScore;
	private int time;
	private int lives;
	
	private Score score;
	private CountDown countDown;
	private Life life;
	private boolean gameOver;
	private boolean win;
	
	public DroneGame(int winningScore, int time, int lives)
	{
		this.winningScore = winningScore;
		this.time = time;
		this.lives = lives;
		gameOver = false;
	}
	
	public boolean isGameOver()
	{
		return gameOver;
	}
	
	public synchronized void decreaseLife()
	{
		// Decrease lives, and if it reaches zero, notify the main thread
		life.decreseLives();
		if (life.getLife() == 0) {
			countDown.cancelCountDownTimer();
			gameOver = true;
			notify();
		}
	}

	public synchronized void increaseScore()
	{
		// Increase score, and if it reaches the winning score, notify the main thread
		score.increaseScore();
		if (score.getScore() == winningScore) {
			countDown.cancelCountDownTimer();
			win = true;
			notify();
		}
	}
	
	public synchronized void run()
	{
		// Set the window size
		setSize(WINDOW_WIDTH , WINDOW_HEIGHT);
		
		setLayout(new BorderLayout());
		
		// Create  the panels for count down timer, score and lives
		JPanel countDownPanel = new JPanel();
		JPanel scoreLifePanel = new JPanel();
		
		countDown = new CountDown(time, this);
		score = new Score(winningScore);
		life = new Life(lives);

		countDown.setFont(new Font("Arial", Font.PLAIN, 18));
		score.setFont(new Font("Arial", Font.PLAIN, 18));
		life.setFont(new Font("Arial", Font.PLAIN, 18));
		
		countDownPanel.add(countDown);
		scoreLifePanel.add(score);
		scoreLifePanel.add(life);
		
		add(countDownPanel, BorderLayout.NORTH);
		add(scoreLifePanel, BorderLayout.SOUTH);
		
		// Create the plane field
		PlaneField field = new PlaneField(lives, this);
		
		// Moves the plain forward in a flow
		field.move();
		
		// Detect keys
		addKeyListener(new KeyListener() {
			private boolean up;
			private boolean down;
			private boolean right;
			private boolean left;
			public void keyPressed(KeyEvent e)
			{
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: //Up arrow key pressed.
					up = true;
					if (right) {
						//Move the drone diagonally to upper right.
						field.upDrone();
						field.rightDrone();
					} else if (left) {
						//Move the drone diagonally to upper left.
						field.upDrone();
						field.leftDrone();
					} else {
						//Move the drone up vertically.
						field.upDrone();
					}
					break;
					
				case KeyEvent.VK_DOWN: //Down arrow key pressed.
					down = true;
					if (right) {
						//Move the drone diagonally to lower right.
						field.downDrone();
						field.rightDrone();
						//Move the drone diagonally to lower left.
					} else if (left) {
						field.downDrone();
						field.leftDrone();
					} else {
						//Move the drone down vertically.
						field.downDrone();
					}
					break;
					
				case KeyEvent.VK_RIGHT: //Right arrow key pressed.
					right = true;
					if (up) {
						//Move the drone diagonally to upper right.
						field.rightDrone();
						field.upDrone();
					} else if (down) {
						//Move the drone diagonally to lower right.
						field.rightDrone();
						field.downDrone();
					} else {
						//Move the drone right horizontally.
						field.rightDrone();
					}
					break;
					
				case KeyEvent.VK_LEFT: //Left arrow key pressed.
					left = true;
					if (up) {
						//Move the drone diagonally to upper left.
						field.leftDrone();
						field.upDrone();
					} else if (down) {
						//Move the drone diagonally to lower left.
						field.leftDrone();
						field.downDrone();
					} else {
						//Move the drone left horizontally.
						field.leftDrone();
					}
					break;
				}
			}
			public void keyReleased(KeyEvent e)
			{
				
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: //Up arrow key released.
					up = false;
					break;
					
				case KeyEvent.VK_DOWN: //Down arrow key released.
					down = false;
					break;

				case KeyEvent.VK_RIGHT: //Right arrow key released.
					right = false;
					break;
					
				case KeyEvent.VK_LEFT: //Left arrow key released.
					left = false;
					break;
				}
			}
			public void keyTyped(KeyEvent e)
			{
				//S key typed
				if (e.getKeyChar() == 's') {
					field.shoot();
				}
			}
		});
		add(field);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		try {
			// Wait until the score reaches the winning score or lives reaches zero
			wait();

			// Cancel all the timers
			field.cancelAllTimers();
			
			// Remove all the component from the frame
			getContentPane().removeAll();
			
			// Prompt user to select whether they will continue or end the game.
			if (win) {
				int option = JOptionPane.showConfirmDialog(this, "You Win! New Game?",
						"Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (option == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
			} else if (gameOver) {
				int option = JOptionPane.showConfirmDialog(this, "You Lose! New Game?",
						"Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (option == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
			}	
			setVisible(false);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{		
		DroneGame game;
		final int winningScore = 5;
		final int countDown = 90;
		final int lives = 3;
		game = new DroneGame(winningScore, countDown, lives);
		while (true) {		
			game.run();
		}
	}

}
