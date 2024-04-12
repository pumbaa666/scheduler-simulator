package ch.correvon.scheduler.gui.statistic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import ch.correvon.scheduler.algorithms.ButtonAlgorithm;
import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.Tools;
import ch.correvon.scheduler.moo.jTable.CheckboxTableModel;
import ch.correvon.scheduler.moo.jTable.JTableButtonRenderer;
import ch.correvon.scheduler.moo.jTable.StatisticTableModel;
import ch.correvon.scheduler.moo.process.Statistic;
import ch.correvon.scheduler.plugin.Algo_I;

public class PanelStatisticView extends JPanel
{
	/*----------------------------------------------------------------*\
	|*							Constructeurs						  *|
	\*----------------------------------------------------------------*/
	public PanelStatisticView(Manager manager, int w, int h)
	{
		this.listPanel = new ArrayList<PanelStatistic>(manager.getAlgoPlugin().size());
		this.manager = manager;
		this.h = h;
		this.hMax = -1;
		this.hDifferential = 80;
		int nb = manager.getStatPlugin().size();
		this.lastEnabledStatistic = new ArrayList<Boolean>(nb);
		this.lastEnabledAlgorithm = new Hashtable<String, Boolean>(nb);
		this.createPanels(w, h);
	}
	
	/*----------------------------------------------------------------*\
	|*						Méthodes publiques						  *|
	\*----------------------------------------------------------------*/
	public void update()
	{
		ArrayList<Statistic> listStat = ((StatisticTableModel)this.statTable.getModel()).getData();
		ArrayList<Statistic> listUpdatedStats = new ArrayList<Statistic>(listStat.size());
		for(PanelStatistic panel:this.listPanel)
		{
			for(Statistic stat:panel.getButtonAlgorithm().getResult().getStats())
				listUpdatedStats.add(Tools.findStatistic(stat.getName(), listStat));
			panel.getButtonAlgorithm().getResult().setStatsColor(listUpdatedStats);
			panel.repaint();
		}
		this.updateUI();
	}
	
	public void addAlgorithm(ButtonAlgorithm buttonAlgo)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		PanelStatistic panelStat = new PanelStatistic(this, buttonAlgo, this.lastEnabledStatistic, this.hMax);
		panelStat.setVisible(this.lastEnabledAlgorithm.get(buttonAlgo.getName()));
		
		this.listPanel.add(panelStat);
		
		this.leftAlignPanel.add(panelStat , BorderLayout.CENTER);
		this.leftAlignPanel.updateUI();
		
		this.mainScroll.updateUI();

		this.update();
	}
	
	
	public void enabledStatisticUpdated()
	{
		for(PanelStatistic panel:this.listPanel)
			panel.repaint();
	}
	
	public void enabledAlgorithmUpdated()
	{
		int i = 0;
		for(PanelStatistic panel:this.listPanel)
		{
			panel.setVisible(this.lastEnabledAlgorithm.get(panel.getButtonAlgorithm().getName()));
			i++;
		}
		this.updateUI();
	}

	
	/*--------------------------------*\
	|*				Get				  *|
	\*--------------------------------*/
	public StatisticTableModel getStatModel()
	{
		return (StatisticTableModel)this.statTable.getModel();
	}
	
	public CheckboxTableModel getAlgoModel()
	{
		return (CheckboxTableModel)this.algoTable.getModel();
	}
	
	public int getH()
	{
		return this.h;
	}
	
	public double getHMax()
	{
		return this.hMax;
	}
	
	/*--------------------------------*\
	|*				Set				  *|
	\*--------------------------------*/
	public void setSelectedStatistic(int[] indices)
	{
		for(PanelStatistic panel:this.listPanel)
			panel.setSelectedStatistic(indices);
	}
	
	public void setHMax(double hMax)
	{
		this.hMax = hMax;
		this.update();
	}

	/*----------------------------------------------------------------*\
	|*						Méthodes privées						  *|
	\*----------------------------------------------------------------*/
	private void createPanels(int w, int h)
	{
		this.setLayout(new BorderLayout());
		this.panelScroll = new JPanel();
		this.panelScroll.setLayout(new BorderLayout());
		this.panelScroll.setMinimumSize(new Dimension(w, h));
		
		this.leftAlignPanel = new JPanel();
		this.leftAlignPanel.setLayout(new FlowLayout());
		this.leftAlignPanel.add(createPanelOption());
		this.panelScroll.add(this.leftAlignPanel, BorderLayout.WEST);
		
		this.mainScroll = new JScrollPane(this.panelScroll);
		Dimension dimension = new Dimension(w-50, h-50);
		this.mainScroll.setPreferredSize(dimension);
		this.mainScroll.setMaximumSize(dimension);
		this.mainScroll.setMinimumSize(dimension);
		this.add(this.mainScroll, BorderLayout.CENTER);
	}
	
	private JPanel createPanelOption()
	{
		this.algoTable = new JTable();
		String[] algoTitle = {"Algo", "[v]"};
		CheckboxTableModel algoTableModel = new CheckboxTableModel(this, this.lastEnabledAlgorithm, this.manager.getAlgoPlugin().size(), algoTitle);
		this.algoTable.setModel(algoTableModel);
		this.algoTable.setRowSorter(new TableRowSorter<TableModel>(algoTableModel));
		
		for(Algo_I algo:this.manager.getAlgoPlugin())
			if(algo != null)
				algoTableModel.addRow(algo.getName());
		Tools.setColumnCheck(this.algoTable, 1);
		this.scrollAlgoTable = new JScrollPane(this.algoTable);
		
		StatisticTableModel statTableModel = new StatisticTableModel(this, this.lastEnabledStatistic, this.manager.getStatPlugin().size());
		this.statTable = new JTable(statTableModel);
		this.statTable.setDefaultRenderer(JButton.class, new JTableButtonRenderer(true));
		
		for(Statistic stat:this.manager.getStatistics())
			statTableModel.addRow(stat);
		Tools.setColumnCheck(this.statTable, 2);
		this.statTable.getColumnModel().getColumn(1).setMaxWidth(50);
		this.scrollStatTable = new JScrollPane(this.statTable);

		
		Box boxV = Box.createVerticalBox();

		boxV.add(this.scrollAlgoTable);
		boxV.add(this.scrollStatTable);
		
		JPanel panel = new JPanel();
		
		panel.add(boxV);

		panel.addComponentListener(new ComponentListener()
		{

			@Override public void componentHidden(ComponentEvent arg0) 
			{
			}

			@Override public void componentMoved(ComponentEvent arg0)
			{
			}

			@Override public void componentResized(ComponentEvent arg0)
			{
				Dimension dim = new Dimension(200, h/2-hDifferential/2);
				
				scrollAlgoTable.setPreferredSize(dim);
				scrollAlgoTable.updateUI();
				
				scrollStatTable.setPreferredSize(dim);
				scrollStatTable.updateUI();
				
				panelScroll.updateUI();
				mainScroll.updateUI();
			}

			@Override public void componentShown(ComponentEvent arg0) 
			{
			}
			
		});
		
		return panel;
	}
	
	/*----------------------------------------------------------------*\
	|*						Attributs privés						  *|
	\*----------------------------------------------------------------*/
	private Manager manager;
	
	private int h;
	private int hDifferential;
	
	private ArrayList<PanelStatistic> listPanel;
	private JScrollPane mainScroll;
	private JPanel panelScroll;
	private JPanel leftAlignPanel;
	
	private JScrollPane scrollAlgoTable;
	private JScrollPane scrollStatTable;
	
	private JTable statTable;
	private JTable algoTable;
	
	private ArrayList<Boolean> lastEnabledStatistic;
	private Hashtable<String, Boolean> lastEnabledAlgorithm;
	
	private double hMax;
}
