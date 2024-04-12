
//Specification:
package ch.hearc.scheduler.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import ch.hearc.scheduler.gui.ecouteur.EcouteurMenuAPropos;
import ch.hearc.scheduler.gui.tools.Menus;


public class FrameAPropos extends JFrame
{
	 /*-----------------------------------------------------------------*\
	 |*							Constructeurs							*|
	 \*-----------------------------------------------------------------*/
	public FrameAPropos(int x, int y, int w, int h)
	{
		super();
		this.x = x;
		this.y = y;
		setSize(w,h);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("A propos");
		
		List<List<String>> structure = new ArrayList<List<String>>();
		List<String> fichier = new ArrayList<String>();
		fichier.add("Fichier");
			fichier.add("Fermer");
			structure.add(fichier);
		
		EcouteurMenuAPropos ecouteur = new EcouteurMenuAPropos(this);
		Menus menu = new Menus(structure, ecouteur);
		setMenuBar(menu);
		
		this.add(new PanneauAPropos(w, h));
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

