import javax.swing.JLabel;

public class Life extends JLabel
{
	private static final long serialVersionUID = 1L;
	
	private int life;
	
	public Life(int life)
	{
		this.life = life;
		setText(String.format("     Lives: %d", life));
		repaint();
	}
	
	public int getLife()
	{
		return life;
	}
	
	public void decreseLives()
	{
		life--;
		setText(String.format("     Lives: %d", life));
		repaint();
	}
}