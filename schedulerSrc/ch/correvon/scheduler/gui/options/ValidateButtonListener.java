
//Specification:
package ch.correvon.scheduler.gui.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.jTable.SimplifiedCheckBox;
import ch.correvon.scheduler.moo.process.Statistic;

/**
 * Cet écouteur est instancié dans PanelOptions
 */
public class ValidateButtonListener implements KeyListener, ActionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public ValidateButtonListener(Manager manager, PanelOptions panelOptions)
	{
		this.manager = manager;
		this.panelOptions = panelOptions;
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
		if(e.getKeyCode() == 10) // [Enter]
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
		int totalPriorityTime = this.panelOptions.getTotalPriorityTimeValue();
		if(this.panelOptions.getQuantum() <= 0)
			JOptionPane.showMessageDialog(this.panelOptions, "Le quantum est incorrect, entrez un nombre supérieur à zéro", "Erreur", JOptionPane.ERROR_MESSAGE);
		else if(totalPriorityTime <= 0)
			JOptionPane.showMessageDialog(this.panelOptions, "Le temps de rotation total est incorrect, entrez un nombre supérieur à zéro", "Erreur", JOptionPane.ERROR_MESSAGE);
		else if(this.panelOptions.getDefaultProcessName().length() <= 0)
			JOptionPane.showMessageDialog(this.panelOptions, "Le nom des processus par défaut est incorrect. Il doit contenir au moins un caractère.", "Erreur", JOptionPane.ERROR_MESSAGE);
		else
		{
			this.manager.writeFileTypeProcess(this.panelOptions.getListType());
			this.manager.searchProcessType();
			this.manager.updateListType();

			this.manager.writeFileStatProcess(this.panelOptions.getCheckBoxesStat());
			this.manager.searchStatistic();
			
			this.manager.getPreferances().put("txtQuantum", ""+this.panelOptions.getQuantum());
			this.manager.getPreferances().put("checkDeleteHistory", ""+this.panelOptions.getCheckboxDeleteHistoryValue());
			this.manager.getPreferances().put("txtTotalPriorityTime", ""+totalPriorityTime);
			this.manager.getPreferances().put("txtDefaultProcessName", ""+this.panelOptions.getDefaultProcessName());
			this.manager.getPreferances().put("checkSameArrival", ""+this.panelOptions.getCheckboxSameArrivalValue());
			this.manager.getPreferances().put("checkSameAlgo", ""+this.panelOptions.getCheckboxSameAlgoValue());
			this.manager.getPreferances().put("checkAutoRunAlgo", ""+this.panelOptions.getCheckboxAutoRunAlgoValue());
			this.manager.getPreferances().put("checkAutoValidFileRead", ""+this.panelOptions.getCheckboxAutoValidFileReadValue());
			this.manager.getPreferances().put("checkEnableMultiZero", ""+this.panelOptions.getCheckboxEnableMultiZeroValue());
			this.manager.getPreferances().put("checkShowToolTipFrame", ""+this.panelOptions.getCheckboxShowToolTipFrameValue());
			this.manager.getPreferances().put("txtNbCPU", ""+this.panelOptions.getNbCPU());
			
			
			boolean showButtons = this.panelOptions.getCheckboxShowButtonsValue();
			this.manager.getPanelUI().showButtons(showButtons);
			this.manager.getPreferances().put("checkShowButtons", ""+showButtons);
			for(SimplifiedCheckBox check:this.panelOptions.getCheckBoxesAlgo())
				this.manager.getPreferances().put("Algo_"+check.getText(), ""+check.isSelected());
			for(Statistic stat:this.panelOptions.getCheckBoxesStat())
				this.manager.getPreferances().put("Stat_"+stat.getName(), ""+stat.isSelected());

			this.panelOptions.getFrameOptions().close();
		}
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
	private PanelOptions panelOptions;
}

