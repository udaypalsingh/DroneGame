import java.util.*;
import java.util.Timer;

import javax.swing.*;

public class CountDown extends JLabel
{	
	private static final long serialVersionUID = 1L;

	private int sec;
	private int savedSec;
	private DroneGame game;
	private Timer countDownTimer;

	public CountDown(int sec, DroneGame game)
	{
		this.sec = sec;
		savedSec = sec;
		this.game = game;
		
		// Set a timer that counts down in every 1 second
		countDownTimer = new Timer();
		countDownTimer.schedule(new TimerTask() {
			@Override
			public void run()
			{
				countDown();
			}
		}, 0L, 1000L);
		
		setText(String.format("%02d:%02d", sec / 60, sec % 60));
		repaint();
	}
	private void countDown()
	{
		// Count down and display the remaining time
		sec--;
		setText(String.format("%02d:%02d", sec / 60, sec % 60));
		repaint();
		
		if (sec == 0) {
			game.increaseScore();
			sec = savedSec;
		}
	}
	public void cancelCountDownTimer()
	{
		countDownTimer.cancel();
	}
}
