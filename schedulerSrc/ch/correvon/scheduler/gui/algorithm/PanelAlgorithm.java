package ch.correvon.scheduler.gui.algorithm;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ch.correvon.scheduler.algorithms.ButtonAlgorithm;
import ch.correvon.scheduler.gui.ui.TBPaneListener;
import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.Tools;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.ProcessType;

public class PanelAlgorithm extends JPanel implements ComponentListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur						   *|
	\*-----------------------------------------------------------------*/
	public PanelAlgorithm(String name, ArrayList<ButtonAlgorithm> listButtonAlgorithm, Manager manager)
	{
		this.quantum = manager.getFrameOptions().getPanelOptions().getQuantum();
		this.listProcess = new ArrayList<Process>(manager.getListProcess());
		
		this.tbpane = new JTabbedPane();
		this.tbpane.addMouseListener(new TBPaneListener(this.tbpane));
		this.listButtonAlgorithm = new ArrayList<ButtonAlgorithm>(listButtonAlgorithm.size());

		for(ButtonAlgorithm buttonAlgo:listButtonAlgorithm)
			if(manager.getPreferances("Algo_"+buttonAlgo.getName()).compareTo("false") != 0)
				this.listButtonAlgorithm.add(new ButtonAlgorithm(buttonAlgo));
		
		
		this.panelAlgoView = new PanelAlgoView(name, manager, this.listButtonAlgorithm, this, this.listProcess);
		this.tbpane.add(this.panelAlgoView, "Vue");
		this.add(this.tbpane);
		this.addComponentListener(this);
		this.listType = Tools.deepClone(manager.getProcessType());
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void runAlgorithms()
	{
		this.listButtonAlgorithm.add(this.panelAlgoView.getPimpedButton());
		for(ButtonAlgorithm buttonAlgo:this.listButtonAlgorithm)
			for(ActionListener listener:buttonAlgo.getButton().getActionListeners())
				listener.actionPerformed(new ActionEvent(this, 0, "AutoRun"));
	}
	
	/*-----------------------------------------*\
	|*				Ecouteur 				   *|
	\*-----------------------------------------*/
	public void componentResized(ComponentEvent e)
	{
		this.tbpane.setPreferredSize(new Dimension((int)(this.getSize().width*95/100.0), (int)(this.getSize().height*95/100.0)));
		this.tbpane.updateUI();
	}

	public void componentShown(ComponentEvent e)
	{
	}

	public void componentHidden(ComponentEvent e)
	{
	}

	public void componentMoved(ComponentEvent e)
	{
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public ArrayList<ButtonAlgorithm> getListButtonAlgorithm()
	{
		return this.listButtonAlgorithm;
	}
	
	public JTabbedPane getTbpane()
	{
		return this.tbpane;
	}
	
	public PanelAlgoView getSelectedAlgoViewPane()
	{
		return (PanelAlgoView)this.tbpane.getSelectedComponent();
	}
	
	public int getQuantum()
	{
		return this.quantum;
	}
	
	public PanelAlgoView getPanelAlgoView()
	{
		return this.panelAlgoView;
	}
	
	public ArrayList<ProcessType> getListType()
	{
		return this.listType;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private ArrayList<Process> listProcess;
	private PanelAlgoView panelAlgoView;
	private JTabbedPane tbpane;
	private int quantum;
	private ArrayList<ButtonAlgorithm> listButtonAlgorithm;
	private ArrayList<ProcessType> listType;
}
