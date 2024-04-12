
//Specification:
package ch.hearc.scheduler.gui.ecouteur;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.hearc.scheduler.gui.FrameAPropos;


public class EcouteurMenuAPropos implements ActionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur						   *|
	\*-----------------------------------------------------------------*/
	public EcouteurMenuAPropos(FrameAPropos frameAPropos)
	{
		this.frameAPropos = frameAPropos;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes Publiques					   *|
	\*-----------------------------------------------------------------*/
	public void actionPerformed(ActionEvent e)
	{
		MenuItem item = (MenuItem)e.getSource();
		Menu parent = (Menu)item.getParent();
		if(parent.getLabel().compareTo("Fichier") == 0)
			if(item.getLabel().compareTo("Fermer") == 0)
				this.frameAPropos.close();
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private FrameAPropos frameAPropos;
}

