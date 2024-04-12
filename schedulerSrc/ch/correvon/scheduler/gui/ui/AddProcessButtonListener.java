
//Specification:
package ch.correvon.scheduler.gui.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.Tools;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.ProcessType;

/**
 * Cet écouteur est instancié dans PanelUI.
 * Les évenements lui étant associés ont pour effet d'essayer d'ajouter un processus
 * entré au clavier (dans les textBox) dans la listBox d'entrée
 */
public class AddProcessButtonListener implements KeyListener, ActionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public AddProcessButtonListener(Manager manager)
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
		int num;
		PanelUI panelUI = this.manager.getPanelUI();
		ArrayList<Process> listProcess = this.manager.getListProcess();
		if(this.manager.getListProcess().size() == 0)
			num = 0;
		else
			num = 1 + Tools.searchNumber(listProcess.get(listProcess.size()-1).getName());
		String strProcess = panelUI.getProcess(num);
		if(strProcess != null)
		{
			Process process = Process.convert(strProcess);
			for(ProcessType type:this.manager.getProcessType())
				if(type.toString().compareTo(process.getType().toString()) == 0)
				{
					process.setType(type);
					break;
				}
			
			boolean ok = Tools.notYetExist(process, listProcess, true, this.manager.getPreferances("checkSameArrival").compareTo("false") == 0);

			if(ok)
			{
				panelUI.addProcess(process);
				panelUI.incrementNumberProcess();
				panelUI.getTextBoxTime().requestFocus();
				Tools.selectAllText(panelUI.getTextBoxTime());
			}
		}
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
}

