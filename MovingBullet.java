import java.util.*;
import java.awt.geom.*;

public class MovingBullet extends TimerTask
{	
	private static int FIRING_RANGE = 750;
	private static int BULLET_REACH_MAX = 1500;
	private static int PLANE_START_POSITION = 1700;
	private static int EXPLOSION_DIAMETER = 200;
	private static int EXPLOSION_DISPLAY_TIME = 200;
	private static int EXPLOSION_POSITION_ADJUSTMENT_Y = 40;
	
	private Drone drone;
	private Plane[] planes;
	private Bullet bullet;
	private PlaneField field;
	private Explosion explosion;
	
	public MovingBullet(Drone drone, Plane[] planes, Bullet bullet, PlaneField field)
	{
		this.drone = drone;
		this.planes = planes;
		this.bullet = bullet;
		this.field = field;
		
		explosion = new Explosion();
		this.field.add(explosion);
		explosion.setVisible(false);
	}
	public void run()
	{	
		// Move the bullet 1px to right
		bullet.setBounds(bullet.getX() + 1, bullet.getY(), bullet.getWidth(), bullet.getHeight());
		
		// Remove the bullet if it gets out of its firing range
		if (bullet.getX() > drone.getX() + FIRING_RANGE || bullet.getX() > BULLET_REACH_MAX) {
			field.remove(bullet);
			field.setBulletOnField();
			return;
		}
		
		for (int i = 0; i < planes.length; i++) {
			
			//Detect a collision.
			Area planeArea = new Area(planes[i].getBounds());
			Area bulletArea = new Area(bullet.getBounds());
			if (planeArea.intersects(bulletArea.getBounds2D())) {
				
				// Move the shot-down plane back to the start position
				int planeXAxis = planes[i].getX();
				planes[i].setBounds(PLANE_START_POSITION, planes[i].getY(), planes[i].getWidth(), planes[i].getHeight());
				
				// Remove the bullet from the field
				field.remove(bullet);

				// Display an explosion for 200ms at the position where the shot-down plane used to be
				explosion.setBounds(planeXAxis, planes[i].getY() - EXPLOSION_POSITION_ADJUSTMENT_Y,
						EXPLOSION_DIAMETER, EXPLOSION_DIAMETER);
				explosion.setVisible(true);
				try {
					Thread.sleep(EXPLOSION_DISPLAY_TIME);
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
				explosion.setVisible(false);
				
				// Notify the bullet was removed from the field 
				field.setBulletOnField();
				return;
			}
		}
	}
}
