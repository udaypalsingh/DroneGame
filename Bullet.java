import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import javax.swing.Icon;

public class Bullet extends JLabel
{
	private static final long serialVersionUID = 1L;

	public Bullet(int size)
	{
		this.setIcon(new BulletIcon(size));
		this.setSize(size, size);
	}
	
	// Create a bullet icon
	class BulletIcon implements Icon
	{
		private int diameter;
		
		public BulletIcon(int diameter)
		{
			this.diameter = diameter;
		}
		public int getIconWidth()
		{
			return diameter;
		}
		public int getIconHeight()
		{
	      return diameter;
		}
		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			Graphics2D g2 = (Graphics2D) g;
			Ellipse2D.Double frontTire = new Ellipse2D.Double(x, y, diameter, diameter);

			g2.setColor(Color.black);
			g2.fill(frontTire);
		}
	}
}
