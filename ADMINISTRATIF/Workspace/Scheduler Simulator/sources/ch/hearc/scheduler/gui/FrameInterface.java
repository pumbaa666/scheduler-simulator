// Specification:
package ch.hearc.scheduler.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import ch.hearc.scheduler.gui.ecouteur.EcouteurFenetre;
import ch.hearc.scheduler.gui.ecouteur.EcouteurMenuInterface;
import ch.hearc.scheduler.gui.ecouteur.EcouteurResizeFrameInterface;
import ch.hearc.scheduler.gui.tools.Menus;
import ch.hearc.scheduler.moo.Manager;

public class FrameInterface extends JFrame
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public FrameInterface(int x, int y, int w, int h, Manager manager)
	{
		super();
		setSize(w, h);
		setLocation(x, y);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new EcouteurFenetre(manager));
		setResizable(true);
		setTitle("Scheduler Simulator");

		this.addComponentListener(new EcouteurResizeFrameInterface(this));
		
		List<List<String>> structure = new ArrayList<List<String>>();
		List<String> fichier = new ArrayList<String>();
		fichier.add("Fichier");
			fichier.add("Ouvrir");
			fichier.add("Enregistrer");
			fichier.add(" - ");
			fichier.add("Quitter");
			structure.add(fichier);

		List<String> aPropos = new ArrayList<String>();
		aPropos.add("Aide");
			aPropos.add("A propos");
			structure.add(aPropos);
		
		Menus menu = new Menus(structure, new EcouteurMenuInterface(manager));
		setMenuBar(menu);
		this.setVisible(false);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes Publiques					   *|
	\*-----------------------------------------------------------------*/
	public void run()
	{
		this.setVisible(true);
	}
	
	public void verifySize()
	{
		int w = this.getWidth();
		int h = this.getHeight();
		boolean hasChanged = false;
		
		if(w < 400)
		{
			w = 400;
			hasChanged = true;
		}
		
		if(h < 450)
		{
			h = 450;
			hasChanged = true;
		}
		
		if(hasChanged)
			this.setSize(w, h);
	}
}
