
//Specification:
package ch.correvon.scheduler.algorithms;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import ch.correvon.scheduler.gui.algorithm.PanelAlgorithm;
import ch.correvon.scheduler.gui.options.PanelOptions;
import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.Tools;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.SortedProcess;
import ch.correvon.scheduler.moo.simulation.Simulator;
import ch.correvon.scheduler.plugin.Algo_I;


public class ButtonAlgorithm
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public ButtonAlgorithm(String name, Algo_I algorithm, Manager manager)
	{
		if(algorithm != null)
			this.algorithm = algorithm.cloneOf();

		this.button = new JButton(name);
		this.manager = manager;
		Dimension dimension = new Dimension(WIDTH_BUTTON, this.button.getPreferredSize().height);
		this.button.setPreferredSize(dimension);
		this.button.setMinimumSize(dimension);
		this.button.setMaximumSize(dimension);
		this.listener = new ButtonAlgoListener(manager, this);
		this.button.addActionListener(this.listener);
	}
	
	public ButtonAlgorithm(ButtonAlgorithm source)
	{
		this(source.getName(), null, source.getManager());
		
		Algo_I algoSource = source.getAlgorithm();
		if(algoSource != null)
			this.algorithm = algoSource.cloneOf();
		try
		{
			this.result = new SortedProcess(source.getResult());
		}
		catch(NullPointerException e)
		{
			this.result = new SortedProcess();
		}
		
		this.listener = new ButtonAlgoListener(this.manager, this);
		JButton sourceButton = source.getButton();
		this.button = new JButton(sourceButton.getText());
		Dimension dimension = sourceButton.getPreferredSize();
		this.button.setPreferredSize(dimension);
		this.button.setMinimumSize(dimension);
		this.button.setMaximumSize(dimension);
		for(ActionListener l:this.button.getActionListeners())
			this.button.removeActionListener(l);
		this.button.addActionListener(this.listener);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void runAlgorithm(ArrayList<Process> listProcess, int quantum)
	{
		PanelOptions option = this.manager.getFrameOptions().getPanelOptions();
		
		Simulator sim;
		if(this.algorithm != null)
			sim = new Simulator(this.algorithm, listProcess, quantum, new Integer(option.getNbCPU()).intValue(), option.getTotalPriorityTimeValue());
		else
			sim = new Simulator(this.panelAlgorithm.getListType(), listProcess, quantum, new Integer(option.getNbCPU()).intValue(), option.getTotalPriorityTimeValue());
		
		this.result = new SortedProcess(sim.run());
		
		this.result.setStats(Tools.averagecalcul(this.result.getListSortedProcess(), this.manager.getStatPlugin(), this.manager.getPreferances(), this.manager.getStatistics()));
		this.manager.getPanelUI().getSelectedAlgorithmPane().getSelectedAlgoViewPane().getFrameStatistic().getPanelStatistic().addAlgorithm(this);
	}
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setPanelAlgorithm(PanelAlgorithm panelAlgorithm)
	{
		this.panelAlgorithm = panelAlgorithm;
		this.listener.setTbpane(this.panelAlgorithm);
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public Manager getManager()
	{
		return this.manager;
	}
	
	public String getName()
	{
		if(this.algorithm != null)
			return this.algorithm.getName();
		return "Personnalisé";
	}
	
	/**
	 * Retourne un clone car une instance d'un bouton ne peut exister qu'a un seul endroit
	 * et ne peut donc pas - par conséquent - apparaitre sur plusieurs PanelAlgoView.
	 */
	public JButton getButton()
	{
		return this.button;
	}
	
	public SortedProcess getResult()
	{
		return new SortedProcess(this.result);
	}
	
	public Algo_I getAlgorithm()
	{
		return this.algorithm;
	}
	
	public PanelAlgorithm getPanelAlgorithm()
	{
		return this.panelAlgorithm;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private Algo_I algorithm;
	private JButton button;
	private PanelAlgorithm panelAlgorithm;
	private Manager manager;
	private SortedProcess result;
	private ButtonAlgoListener listener;
	
	/*---------------------------------------------*\
	|*					Statiques				   *|
	\*---------------------------------------------*/
	private static final int WIDTH_BUTTON = 130;
	
}

