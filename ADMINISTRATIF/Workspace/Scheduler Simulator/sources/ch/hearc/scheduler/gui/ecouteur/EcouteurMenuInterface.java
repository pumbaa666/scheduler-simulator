
//Specification:
package ch.hearc.scheduler.gui.ecouteur;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.hearc.scheduler.moo.Manager;


public class EcouteurMenuInterface  implements ActionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur						   *|
	\*-----------------------------------------------------------------*/
	public EcouteurMenuInterface(Manager manager)
	{
		this.manager = manager;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes Publiques					   *|
	\*-----------------------------------------------------------------*/
	public void actionPerformed(ActionEvent e) 
	{
		MenuItem item = (MenuItem)e.getSource();
		Menu parent = (Menu)item.getParent();
		if(parent.getLabel().compareTo("Fichier") == 0)
		{
			if(item.getLabel().compareTo("Ouvrir") == 0)
				this.manager.ouvrirFichier(this.manager.getPanneauInterface().getValueCheckVider());
			else if(item.getLabel().compareTo("Enregistrer") == 0)
				this.manager.enregistrerFichier(this.manager.getPanneauInterface().getValueCheckVider());
			else if(item.getLabel().compareTo("Quitter") == 0)
				this.manager.quit();
		}
		else if(parent.getLabel().compareTo("Aide") == 0)
			if(item.getLabel().compareTo("A propos") == 0)
				this.manager.getFrameAPropos().run();
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
}

