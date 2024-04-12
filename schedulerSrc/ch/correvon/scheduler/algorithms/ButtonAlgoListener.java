package ch.correvon.scheduler.algorithms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import ch.correvon.scheduler.gui.algorithm.PanelAlgoView;
import ch.correvon.scheduler.gui.algorithm.PanelAlgorithm;
import ch.correvon.scheduler.gui.painting.PanelPainting;
import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.process.Process;


/**
 * Cet écouteur est instancié dans ButtonAlgorithm
 */
public class ButtonAlgoListener implements ActionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public ButtonAlgoListener(Manager manager, ButtonAlgorithm button)
	{
		this.manager = manager;
		this.button = button;
		this.alreadyRunned = false;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Methodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void actionPerformed(ActionEvent e)
	{
		if(this.manager.getPreferances("checkSameAlgo").compareTo("false") != 0 || this.alreadyRunned == false)
		{
			PanelAlgoView panelAlgoView = this.panelAlgorithm.getPanelAlgoView();
			ArrayList<Process> list = panelAlgoView.getInputList();
			if(list != null && list.size() != 0)
			{
				this.alreadyRunned = true;
				this.button.runAlgorithm(list, this.manager.getFrameOptions().getPanelOptions().getQuantum());
				
				this.panelAlgorithm.getTbpane().add(this.button.getName(), new PanelPainting(this.panelAlgorithm, this.button, this.manager, panelAlgoView.getInputList(), panelAlgoView.getSliderValue(), panelAlgoView.getCheckUseAutoScaleValue()));
				panelAlgoView.setDataToDraw(this.button.getName(), this.button.getResult().getListSortedProcess(), this.button.getResult().getStats());
				
				this.panelAlgorithm.repaint();
			}
		}
	}
	
	public void setTbpane(PanelAlgorithm panelAlgorithm)
	{
		this.panelAlgorithm = panelAlgorithm;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private ButtonAlgorithm button;
	private Manager manager;
	private PanelAlgorithm panelAlgorithm;
	private boolean alreadyRunned;
}
