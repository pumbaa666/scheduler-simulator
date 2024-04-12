
//Specification:
package ch.correvon.scheduler.moo;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Permet de créer un menu de manière plus structurée qu'en utilisant simplement les outils fournis par Sun 
 */
public class Menus extends MenuBar
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public Menus(ArrayList<ArrayList<String>> structure, ActionListener listener)
	{
		this.structure = structure;
		this.ecouteur = listener;
		createMenu();
	}

	/*------------------------------------------------------------------*\
	|*							Méthodes privées						*|
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
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private ArrayList<ArrayList<String>> structure;
	private ActionListener ecouteur;
}

