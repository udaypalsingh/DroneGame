import java.util.Timer;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class PlaneField extends JPanel
{
	private static final long serialVersionUID = 1L;
	private static int DRONE_LAUNCH_POINT_X = 30;
	private static int DRONE_LAUNCH_POINT_Y = 350;
	private static int DRONE_WIDTH = 120;
	private static int DRONE_HEIGHT = 60;
	private static int DRONE_MOVE_DISTANCE = 10;
	private static int DRONE_DOWN_MAX = 650;
	private static int DRONE_RIGHT_MAX = 1500;
	private static int DRONE_BLINK_INTERVAL = 300;
	private static int DRONE_BLINK_TIMES = 10;
	
	private static int PLANE_WIDTH = 250;
	private static int PLANE_HEIGHT = 100;
	private static int PLANE_LAUNCH_POINT_X = 1700;
	private static int PLANE_LAUNCH_DELAY_MAX = 5000;
	private static int NUMBER_OF_PLANES = 5;
	private static int FIRST_PLANE_Y_AXIS = 30;
	private static int DISTANCE_BETWEEN_PLANES = 150;
	private static int PLANE_TIMER_INTERVAL = 3;
	
	private static int BULLET_DIAMETER = 20;
	private static int BULLET_POSITION_ADJUSTMENT = 20;
	
	
	private DroneGame game;
	private Image image;
	private Drone drone;
	private Plane[] planes;
	private Bullet bullet;
	private HashMap<Plane, Timer> planeTimers;
	private Timer bulletTimer;
	private Timer blinkTimers[];
	private Timer moveTimer;
	private int collisionCount;
	private int maxCollisionAllowed;
	private boolean bulletOnField;
	private boolean droneFreeze;
	private boolean firstShoot;
	
	public PlaneField(int lives, DroneGame game)
	{	
		this.game = game;
		planes = new Plane[NUMBER_OF_PLANES];
		planeTimers = new HashMap<>();
		maxCollisionAllowed = lives - 1;
		blinkTimers = new Timer[maxCollisionAllowed];
		collisionCount = 0;
		bulletOnField = false;
		droneFreeze = false;
		firstShoot = true;
		
		image = Toolkit.getDefaultToolkit().createImage("background.gif");
		
		init();
	}

	private void init()
	{
		setLayout(null);
		
		// Create a drone
		drone = new Drone();
		drone.setBounds(DRONE_LAUNCH_POINT_X, DRONE_LAUNCH_POINT_Y, DRONE_WIDTH, DRONE_HEIGHT);
		add(drone);
		
		int planeYAxis = FIRST_PLANE_Y_AXIS;
		Random random = new Random(System.currentTimeMillis());
		
		for (int i = 0; i < NUMBER_OF_PLANES; i++) {
			
			// Create a plane.
			planes[i] = new Plane();
			planes[i].setBounds(PLANE_LAUNCH_POINT_X, planeYAxis, PLANE_WIDTH, PLANE_HEIGHT);
			add(planes[i]);
			
			// Get a random number for a delay for launching plane
			int delay = random.nextInt(PLANE_LAUNCH_DELAY_MAX) + 1;
			
			// Create a timer for a plane(launch a plane)
			Timer planeTimer = new Timer();
			planeTimer.schedule(new MovingPlane(drone, planes[i], this), delay, PLANE_TIMER_INTERVAL);
			planeTimers.put(planes[i], planeTimer);
			
			// Set Y axis of the next plane.
			planeYAxis += DISTANCE_BETWEEN_PLANES;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;

		double imageWidth = image.getWidth(this);
		double imageHeight = image.getHeight(this);
		double panelWidth = this.getWidth();
		double panelHeight = this.getHeight();
		
		double sx = panelWidth / imageWidth;
		double sy = panelHeight / imageHeight;

		AffineTransform af = AffineTransform.getScaleInstance(sx, sy);
		g2D.drawImage(image, af, this);
	}

	public void upDrone()
	{
		//If the drone and a plane collides, the drone cannot move for 5 seconds.
		if (!droneFreeze) {
			//Move the drone up.
			drone.setBounds(drone.getX(), drone.getY() - DRONE_MOVE_DISTANCE, drone.getWidth(), drone.getHeight());
			if (drone.getY() < 0) {
				drone.setBounds(drone.getX(), 0, drone.getWidth(), drone.getHeight());
			}
		}
	}
	public void downDrone()
	{
		//If the drone and a plane collides, the drone cannot move for 5 seconds.
		if (!droneFreeze) {
			// Move the drone down.
			drone.setBounds(drone.getX(), drone.getY() + DRONE_MOVE_DISTANCE, drone.getWidth(), drone.getHeight());	
			if (drone.getY() > DRONE_DOWN_MAX) {
				drone.setBounds(drone.getX(), DRONE_DOWN_MAX, drone.getWidth(), drone.getHeight());
			}
		}
	}
	public void rightDrone()
	{
		//If the drone and a plane collides, the drone cannot move for 5 seconds.
		if (!droneFreeze) {
			// Move the drone right.
			drone.setBounds(drone.getX() + DRONE_MOVE_DISTANCE, drone.getY(), drone.getWidth(), drone.getHeight());	
			if (drone.getX() > DRONE_RIGHT_MAX) {
				drone.setBounds(DRONE_RIGHT_MAX, drone.getY(), drone.getWidth(), drone.getHeight());
			}
		}
	}
	
	public void leftDrone()
	{
		//If the drone and a plane collides, the drone cannot move for 5 seconds.
		if (!droneFreeze) {
			//Move the drone left.
			drone.setBounds(drone.getX() - DRONE_MOVE_DISTANCE, drone.getY(), drone.getWidth(), drone.getHeight());	
			if (drone.getX() < 0) {
				drone.setBounds(0, drone.getY(), drone.getWidth(), drone.getHeight());
			}
		}
	}
	public void notifyCollision()
	{
		//Decrease the lives by one. 
		game.decreaseLife();
		collisionCount++;
		
		if (collisionCount <= maxCollisionAllowed) {
			blinkTimers[collisionCount - 1] = new Timer();
			blinkTimers[collisionCount - 1].schedule(new TimerTask() {
				private int blinkCount = 0;
			
				//Freeze and blink the drone for 3 seconds.
				public void run() {
					if (blinkCount == 0) {
						droneFreeze = true;
					}
					if (blinkCount % 2 == 0) {
						drone.setVisible(false);
						blinkCount++;
					} else {
						drone.setVisible(true);
						blinkCount++;
					}
					if (blinkCount >= DRONE_BLINK_TIMES) {
						this.cancel();
						droneFreeze = false;
						blinkCount = 0;
					}
				}
			}, 0, DRONE_BLINK_INTERVAL);
		}
	}
	
	public void setBulletOnField()
	{
		bulletOnField = false;	
	}
	
	public void shoot()
	{
		//If the bullet is still going on the field or the drone and a plane collide,
		//the drone will be kept away from shooting.
		if (!bulletOnField && !droneFreeze) {
			
			// If it is not the first shoot, cancel the old timer.
			if (!firstShoot) {
				bulletTimer.cancel();
				bulletTimer.purge();
			}
			firstShoot = false;
			
			//Create a bullet
			bullet = new Bullet(BULLET_DIAMETER);
			bullet.setBounds(drone.getX() + BULLET_POSITION_ADJUSTMENT, drone.getY() + BULLET_POSITION_ADJUSTMENT,
					bullet.getWidth(), bullet.getHeight());
			add(bullet);
			
			bulletOnField = true;
			
			//Create the timer for  a bullet (shoot the bullet)
			bulletTimer = new Timer();
			bulletTimer.schedule(new MovingBullet(drone, planes, bullet, this), 0L, 1L);
		}
	}
	
	public void move()
	{
		moveTimer = new Timer();
		moveTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				rightDrone();
				
			}
		}, 10, 500);
	}
	public void cancelAllTimers()
	{
		// Cancel all the timers
		for(Timer planeTimer : planeTimers.values()) {
			planeTimer.cancel();
			planeTimer.purge();
		}
		if (bulletTimer != null) {
			bulletTimer.cancel();
			bulletTimer.purge();
		}
		for (Timer blinkTimer : blinkTimers) {
			if (blinkTimer != null) {
				blinkTimer.cancel();
				blinkTimer.purge();
			}
		}
		moveTimer.cancel();
		moveTimer.purge();
	}
}
