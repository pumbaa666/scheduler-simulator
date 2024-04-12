
//Specification:
package ch.hearc.scheduler.gui;

import javax.swing.JFrame;

import ch.hearc.scheduler.moo.Manager;
import ch.hearc.scheduler.moo.ProcessusTrie;


public class FrameDessin extends JFrame
{
	public FrameDessin(String nom, int x, int y, int w, int h, ProcessusTrie procesTrie, Manager manager)
	{
		super();
		this.x = x;
		this.y = y;
		setSize(w,h);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(nom);
		
		this.add(new PanneauDessin(nom, procesTrie, manager));
		
		this.setVisible(false);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes Publiques					   *|
	\*-----------------------------------------------------------------*/
	public void run()
	{
		setLocation(this.x, this.y);
		this.setVisible(true);
	}
	
	public void close()
	{
		this.setVisible(false);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private int x;
	private int y;
}
