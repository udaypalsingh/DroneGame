import java.awt.geom.*;
import java.util.*;

public class MovingPlane extends TimerTask
{	
	private static int PLANE_START_POSITION = 1500;
	private static int EXPLOSION_DIAMETER = 200;
	private static int EXPLOSION_POSITION_ADJUSTMENT_X = 40;
	private static int EXPLOSION_POSITION_ADJUSTMENT_Y = 70;
	private static int EXPLOSION_DISPLAY_TIME = 200;
	
	private Drone drone;
	private Plane plane;
	private PlaneField field;
	private Explosion explosion;
	
	public MovingPlane(Drone drone, Plane plane, PlaneField field)
	{
		this.drone = drone;
		this.plane = plane;
		this.field = field;
		
		explosion = new Explosion();
		this.field.add(explosion);
		explosion.setVisible(false);
	}
	public void run()
	{	
		// Move the plane 1px to left
		plane.setBounds(plane.getX() - 1, plane.getY(), plane.getWidth(), plane.getHeight());
		
		// Detect a collision.
		Area planeArea = new Area(plane.getBounds());
		Area droneArea = new Area(drone.getBounds());
		if (planeArea.intersects(droneArea.getBounds2D())) {
			
			// Display an explosion for 200ms at the position where the drone is
			explosion.setBounds(drone.getX() - EXPLOSION_POSITION_ADJUSTMENT_X, drone.getY() - EXPLOSION_POSITION_ADJUSTMENT_Y, 
					EXPLOSION_DIAMETER, EXPLOSION_DIAMETER);
			explosion.setVisible(true);
			try {
				Thread.sleep(EXPLOSION_DISPLAY_TIME);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			explosion.setVisible(false);
			
			// Move the plane back its start position
			plane.setBounds(PLANE_START_POSITION, plane.getY(), plane.getWidth(), plane.getHeight());
			
			// Notify a collision occur
			field.notifyCollision();
		}
		
		// Move the plane back its start position if it reaches the left end
		if (plane.getX() <= 0) {
			plane.setBounds(PLANE_START_POSITION, plane.getY(), plane.getWidth(), plane.getHeight());
		}
	}
}
