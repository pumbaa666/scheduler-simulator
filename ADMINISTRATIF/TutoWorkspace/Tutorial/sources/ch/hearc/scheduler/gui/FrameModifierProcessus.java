
//Specification:
package ch.hearc.scheduler.gui;

import javax.swing.JFrame;

import ch.hearc.scheduler.moo.Manager;


public class FrameModifierProcessus extends JFrame
{
	 /*-----------------------------------------------------------------*\
	 |*							Constructeurs							*|
	 \*-----------------------------------------------------------------*/
	public FrameModifierProcessus(int x, int y, int w, int h, Manager manager)
	{
		super();
		this.x = x;
		this.y = y;
		setSize(w,h);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Modifier processus");
		this.panneau = new PanneauModifierProcessus(w, h, this, manager);
		this.add(this.panneau);
		this.setVisible(false);
		existance = true;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes Publiques					   *|
	\*-----------------------------------------------------------------*/
	public void run()
	{
		setLocation(this.x, this.y);
		this.setVisible(true);
		this.panneau.run();
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
	private PanneauModifierProcessus panneau;
	
	/*-----------------------------------------------------------------*\
	|*				Attributs statiques et publiques				   *|
	\*-----------------------------------------------------------------*/
	/* Cet attribut notifie de l'existance ou non d'une instance de la classe */
	public static boolean existance = false;
}

