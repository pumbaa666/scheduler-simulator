
package ch.correvon.scheduler.gui.ui;


/**
 * Chronometre implémenté dans FrameUI.
 */
public class Chronometer implements Runnable
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
    public Chronometer(FrameUI frameUI)
    {
    	this.runner = null;
    	this.on = false;
    	this.isRunning = false;
    	this.frameUI = frameUI;
    }
    
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
    public void run()
	{
    	int time = 0;
    	this.isRunning = true;
		while(this.on)
		{
			sleep(50);
			time++;
			if(time >= 2)
			{
				this.stop();
				this.frameUI.updateLocation();
			}
		}
	} 
    
	public void start()
	{
		this.isRunning = true;
	    
		this.on = true;
	    this.runner = new Thread(this);
    	this.runner.start();
	}	
	
	public void stop()
	{
	    this.isRunning = false;
	    this.on = false;
	}
	
	public boolean isRunning()
	{
		return this.isRunning;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes protegées					   *|
	\*-----------------------------------------------------------------*/
	protected void sleep(int time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch(InterruptedException e)
		{
			System.out.println("Runner CRASH");
		}
	} 
	
	private FrameUI frameUI;
    private boolean isRunning;
    private Thread runner;
    private boolean on;
}
