
//Specification:
package ch.hearc.scheduler.gui.ecouteur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultListModel;

import ch.hearc.scheduler.gui.PanneauModifierProcessus;
import ch.hearc.scheduler.gui.tools.Tools;
import ch.hearc.scheduler.moo.Manager;
import ch.hearc.scheduler.moo.Processus;

/**
 * Cet écouteur est instancié dans PanneauModifierProcessus
 * Il récupère simplement un clique sur le bouton Ok, ou une pression de la touche Enter dans une des 3 textBox
 */
public class EcouteurModifier implements KeyListener, ActionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public EcouteurModifier(Manager manager, PanneauModifierProcessus panneau)
	{
		this.manager = manager;
		this.panneau = panneau;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void actionPerformed(ActionEvent e)
	{
		action();
	}
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == 10) // Enter
			action();
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
	
	private void action()
	{
		DefaultListModel model = (DefaultListModel)this.manager.getPanneauInterface().getListBoxInput().getModel();
		Processus newProcess = this.panneau.getNewProcess();
		Processus process = this.panneau.getProcess();
		Processus old = new Processus(process);
		int indice = this.panneau.getIndice();
		this.manager.getListeProcessus().remove(indice);
		model.remove(indice);
		
		if(Tools.verifierExistance(newProcess, this.manager.getListeProcessus())) // Test si le processus ne génère pas de conflit (nom ou heure d'arrivée déjà existante)
		{
			process = new Processus(newProcess);
			model.add(indice, process.toStringEspace());
			this.manager.getListeProcessus().add(indice, process);
		}
		else // Si il y a un conflit : remet l'ancien processus à sa place
		{
			this.manager.getListeProcessus().add(indice, old);
			model.add(indice, old.toStringEspace());
		}
		this.panneau.getFrameParent().close();
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private PanneauModifierProcessus panneau;
	private Manager manager;
}

