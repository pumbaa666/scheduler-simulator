
//Specification:
package ch.correvon.scheduler.gui.ui;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.correvon.scheduler.moo.Manager;

/**
 * Cet écouteur est instancié dans FrameUI
 */
public class InterfaceMenuListener  implements ActionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur						   *|
	\*-----------------------------------------------------------------*/
	public InterfaceMenuListener(Manager manager)
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
			if(item.getLabel().compareTo("Nouveau") == 0)
				this.manager.getPanelUI().raz();
			else if(item.getLabel().compareTo("Ouvrir") == 0)
				this.manager.openFile(this.manager.getPanelUI().getCheckboxClearValue());
			else if(item.getLabel().compareTo("Enregistrer") == 0)
				this.manager.saveFile(this.manager.getPanelUI().getCheckboxClearValue());
			/*else if(item.getLabel().compareTo("Imprimer") == 0)
				this.manager.getPanelUI().print();*/
			else if(item.getLabel().compareTo("Quitter") == 0)
				this.manager.quit();
			if(item.getLabel().compareTo("Préférences") == 0)
				this.manager.getFrameOptions().showUI();
			if(item.getLabel().compareTo("A propos") == 0)
				this.manager.getFrameAbout().showUI();
		}
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
}

