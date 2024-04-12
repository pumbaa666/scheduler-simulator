package ch.hearc.scheduler.use;

import javax.swing.JApplet;

import ch.hearc.scheduler.moo.Manager;

public class Main extends JApplet
{
	@Override public void start()
	{
		Manager manager = new Manager();
		manager.run();
	}
	
	@Override public void stop()
	{
		
	}
	
	@Override public void destroy()
	{

	}
	
	public static void main(String[] args)
	{
		Main main = new Main();
		main.start();
	}
}