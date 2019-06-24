import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;


import javax.imageio.ImageIO;

public class Explosion extends JLabel
{

	private static final long serialVersionUID = 1L;
	private BufferedImage image;

	public Explosion()
	{
		// Read the explosion picture
		try {                
			image = ImageIO.read(new File("explosion.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();
		double panelWidth = this.getWidth();
		double panelHeight = this.getHeight();

        double sx = panelWidth / imageWidth;
        double sy = panelHeight / imageHeight;

        AffineTransform af = AffineTransform.getScaleInstance(sx, sy);
        g2D.drawImage(image, af, this);           
    }
}
