package ch.correvon.scheduler.moo.myObjects;

import javax.swing.JFrame;

public abstract class MyJFrame extends JFrame
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur						   *|
	\*-----------------------------------------------------------------*/
	public MyJFrame()
	{
		this(0, 0, 800, 600);
	}
	
	public MyJFrame(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		super.setSize(w, h);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes Publiques					   *|
	\*-----------------------------------------------------------------*/
	public void showUI()
	{
		this.setLocation(this.x, this.y);
		this.setVisible(true);
	}

	/*-------------------------------------------------*\
	|*					Abstraites					   *|
	\*-------------------------------------------------*/
	public abstract void close();

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private int x;
	private int y;
}
