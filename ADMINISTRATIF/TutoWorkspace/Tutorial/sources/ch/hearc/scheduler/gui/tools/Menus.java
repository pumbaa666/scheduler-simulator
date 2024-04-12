
//Specification:
package ch.hearc.scheduler.gui.tools;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Permet de cr�er un menu de mani�re plus structur�e qu'en utilisant simplement les outils fourni par Sun 
 */
public class Menus extends MenuBar
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*-----------------------------------------------------------------*/
	public Menus(List<List<String>> structure, ActionListener ecouteur)
	{
		this.structure = structure;
		this.ecouteur = ecouteur;
		createMenu();
	}

	/*------------------------------------------------------------------*\
	|*							M�thodes Priv�es						*|
	\*------------------------------------------------------------------*/
	private void createMenu()
	{
		for(int i = 0; i < this.structure.size(); i++)
		{
			Menu menu = new Menu(this.structure.get(i).get(0));
			for(int j = 1; j < this.structure.get(i).size(); j++)
			{
				if(this.structure.get(i).get(j).compareTo(" - ") == 0)
					menu.addSeparator();
				else
				{
					MenuItem item = new MenuItem(this.structure.get(i).get(j));
					item.addActionListener(this.ecouteur);
					menu.add(item);
				}

				this.add(menu);
			}
		}
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Private					   *|
	\*-----------------------------------------------------------------*/
	private List<List<String>> structure;
	private ActionListener ecouteur;
}

