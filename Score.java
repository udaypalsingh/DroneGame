import javax.swing.*;

public class Score extends JLabel
{
	private static final long serialVersionUID = 1L;
	
	private int score;
	private int winningScore;
	
	public Score(int winningScore)
	{
		score = 0;
		this.winningScore = winningScore;
		setText(String.format("Score: %d out of %d", score, winningScore));
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void increaseScore()
	{
		score++;
		setText(String.format("Score: %d out of %d", score, winningScore));
		repaint();
	}
}