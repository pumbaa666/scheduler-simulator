
//Specification:
package ch.hearc.scheduler.gui.ecouteur;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.hearc.scheduler.moo.Manager;

/**
 * Ecoute les entrées claviers dans la listBox de processus
 */
public class EcouteurListBoxInput implements KeyListener, ListSelectionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/

	public EcouteurListBoxInput(Manager manager)
	{
		this.manager = manager;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/

	/* Selection à la souris */
	public void valueChanged(ListSelectionEvent e)
	{
		JList liste = (JList)e.getSource();
		this.manager.getPanneauInterface().enableBoutonModifier(liste.getSelectedIndices().length == 1); // Active le bouton modifier si il y a 1 et 1 seul processus de sélectionné
	}
	
	/* Clavier */
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == 127) // Delete
		{
		    int[] indices = this.manager.getPanneauInterface().getListBoxInput().getSelectedIndices();
		    if(indices.length > 0)
			    this.manager.removeProcessus(indices);
		}
		
		else if(e.getKeyCode() == 38) // haut
		{
			if(e.isAltDown())
			{
				if(e.isControlDown())
					this.manager.upProcessTop();
				else
					this.manager.upProcess();
			}
		}
		 
		else if(e.getKeyCode() == 40) // bas
		{
			if(e.isAltDown())
			{
				if(e.isControlDown())
					this.manager.downProcessBottom();
				else
					this.manager.downProcess();
			}
		}

		else if(new String(""+e.getKeyChar()).toLowerCase().compareTo("s") == 0) // S, s = switch processus
		{
			this.manager.switchProcess();
		}
	}
	
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	public void keyReleased(KeyEvent e)
	{
		
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
}

