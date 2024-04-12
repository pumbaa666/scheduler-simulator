
//Specification:
package ch.hearc.scheduler.gui.tools;

import java.awt.Component;

/**
 * Déclare la structure d'un menu créé avec la classe Menu
 */
public class StructureMenu
{
	/*------------------------------------------------------------------------------*\
	|*								Constructeur									*|
	\*------------------------------------------------------------------------------*/
	public StructureMenu(String titre, String items, Component compo)
	{
		this.titre = titre;
		this.item = items;
		this.compo = compo;
	}
	
	/*------------------------------------------------------------------------------*\
	|*								Méthodes publiques								*|
	\*------------------------------------------------------------------------------*/
	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/
	public String getTitre()
	{
		return this.titre;
	}
	
	public String getItem()
	{
		return this.item;
	}
	
	public Component getCompo()
	{
		return this.compo;
	}
	
	/*------------------------------------------------------------------------------*\
	|*								Attributs privés								*|
	\*------------------------------------------------------------------------------*/
	private String titre;
	private String item;
	private Component compo;
}

