
//Specification:
package ch.hearc.scheduler.gui.ecouteur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import ch.hearc.scheduler.gui.tools.Tools;
import ch.hearc.scheduler.moo.Manager;
import ch.hearc.scheduler.moo.Options;
import ch.hearc.scheduler.moo.Processus;

/**
 * Cet écouteur est instancié dans PanneauInterface.
 * Les évenements lui étant associés ont pour effet d'essayer d'ajouter un processus
 * entré au clavier (dans les textBox) dans la listBox d'entrée
 */
public class EcouteurAjouter implements KeyListener, ActionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public EcouteurAjouter(Manager manager)
	{
		this.manager = manager;
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
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes privées					   *|
	\*-----------------------------------------------------------------*/
	private void action()
	{
		String strProcess = manager.getPanneauInterface().getProcessus();
		if(strProcess != null)
		{
			Processus process = Processus.convert(strProcess);
			boolean ok = Tools.verifierExistance(process, this.manager.getListeProcessus());
			if(this.manager.getPanneauInterface().getListBoxInput().getModel().getSize() >= Options.NB_PROC_MAX)
			{
				JOptionPane.showMessageDialog(null, "Ajout impossible!\nLe nombre de processus ne doit pas être suppérieur à "+Options.NB_PROC_MAX, "Erreur", JOptionPane.OK_OPTION);
				ok = false;
			}
			
			if(ok)
			{
				this.manager.addProcessus(process);
				this.manager.getPanneauInterface().incrementerNumProcess();
				this.manager.getPanneauInterface().getTextBoxHeure().requestFocus();
				Tools.selectAllText(this.manager.getPanneauInterface().getTextBoxHeure());
			}
		}
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
}

