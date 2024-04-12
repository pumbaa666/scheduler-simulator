package ch.correvon.scheduler.gui.statistic;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.correvon.scheduler.algorithms.ButtonAlgorithm;
import ch.correvon.scheduler.moo.jTable.MyCellRenderer;
import ch.correvon.scheduler.moo.process.Statistic;

public class PanelStatistic extends JPanel
{
	/*----------------------------------------------------------------*\
	|*							Constructeurs						  *|
	\*----------------------------------------------------------------*/
	public PanelStatistic(PanelStatisticView panelStatisticView, ButtonAlgorithm buttonAlgo, ArrayList<Boolean> listEnabledStatistic, double hMax)
	{
		this.hMax = hMax;
		this.buttonAlgo = buttonAlgo;
		this.setLayout(new BorderLayout());
		this.panelStatisticView = panelStatisticView;
		this.disableSelection = false;
		this.listEnabledStatistic = listEnabledStatistic;
		
		this.creatStatList();
		this.calculRatio();
		this.add(this.scrollList, BorderLayout.SOUTH);
		
		int w = Math.max(this.scrollList.getPreferredSize().width, (this.buttonAlgo.getResult().getStats().size()+1)*(STAT_WIDTH+V_GAP));
		int h = this.panelStatisticView.getH() - this.scrollList.getPreferredSize().height-10;
		Dimension dimension = new Dimension(w, h); 
		this.setMinimumSize(dimension);
		this.setPreferredSize(dimension);
		this.setMaximumSize(dimension);
	}
	
	/*----------------------------------------------------------------*\
	|*						Méthodes publiques						  *|
	\*----------------------------------------------------------------*/
	private void calculRatio()
	{
		double hMax = 0;
		double statValue;
		
		for(Statistic stat:this.buttonAlgo.getResult().getStats())
		{
			statValue = stat.getValue();
			if(statValue > hMax)
				hMax = statValue;
		}

		if(this.hMax < hMax)
		{
			this.hMax = hMax;
			this.panelStatisticView.setHMax(this.hMax);
		}
	}
	
	private void creatStatList()
	{
		DefaultListModel model = new DefaultListModel();
		this.listStat = new JList(model);
		MyCellRenderer cellRenderer = new MyCellRenderer();
		cellRenderer.setRepeat(true);
		this.listStat.setCellRenderer(cellRenderer);
		this.listStat.addListSelectionListener(new ListSelectionListener()
			{
				@Override public void valueChanged(ListSelectionEvent e)
				{
					if(!disableSelection)
						panelStatisticView.setSelectedStatistic(((JList)e.getSource()).getSelectedIndices());
				}
			}
		);
		
		ArrayList<Statistic >listStats = this.buttonAlgo.getResult().getStats();
		Color[] colors = new Color[listStats.size()];
		int i = 0;
		for(Statistic stat:listStats)
		{
			model.addElement(stat.toString(2));
			colors[i] = stat.getColor();
			i++;
		}
		cellRenderer.setColorIndices(colors);
		
		this.scrollList = new JScrollPane(this.listStat);
		this.scrollList.setPreferredSize(new Dimension(this.scrollList.getPreferredSize().width, 60));
	}
	
	@Override public void paintComponent(Graphics g)
	{
		this.g2D = (Graphics2D)g;
		this.paintPanel();
	}
	
	/*--------------------------------*\
	|*				Get				  *|
	\*--------------------------------*/
	public ButtonAlgorithm getButtonAlgorithm()
	{
		return this.buttonAlgo;
	}
	
	/*--------------------------------*\
	|*				Set				  *|
	\*--------------------------------*/
	public void setSelectedStatistic(int[] indices)
	{
		this.selectedIndices = indices;
		this.disableSelection = true;
		this.listStat.setSelectedIndices(this.selectedIndices);
		this.disableSelection = false;
		repaint();
	}

	/*----------------------------------------------------------------*\
	|*						Méthodes privées						  *|
	\*----------------------------------------------------------------*/
	private void paintPanel()
	{
		if(Math.abs(this.hMax - this.panelStatisticView.getHMax()) > 0.2)
			this.hMax = this.panelStatisticView.getHMax();
		this.g2D.clearRect(0, 0, this.getWidth(), this.getHeight());
		int x = X_INIT;
		
		this.g2D.setFont(TITLE_FONT);
		this.g2D.drawString(this.buttonAlgo.getName(), x, Y_TITLE);
		
		this.g2D.setFont(FONT);

		int hTotal = this.getHeight() - Y_TITLE - this.scrollList.getPreferredSize().height - 2*H_GAP;
		double ratio = hTotal / this.hMax; 

		int i = 0;
		int h;
		Stroke oldStroke = this.g2D.getStroke();
		for(Statistic stat:this.buttonAlgo.getResult().getStats())
		{
			if(this.listEnabledStatistic.get(i))
			{
				h = (int)(stat.getValue() * ratio);
				this.g2D.setColor(((MyCellRenderer)this.listStat.getCellRenderer()).getColor(i));
				this.g2D.fillRect(x, this.scrollList.getLocation().y-h-H_GAP, STAT_WIDTH, h);
				if(this.selectedIndices != null)
					for(int index = 0; index < this.selectedIndices.length; index++)
						if(i == this.selectedIndices[index])
						{
							this.g2D.setColor(Color.black);
							this.g2D.setStroke(new BasicStroke(2));
							this.g2D.drawRect(x, this.scrollList.getLocation().y-h-H_GAP, STAT_WIDTH, h);
							this.g2D.setStroke(oldStroke);
						}
				
				x += STAT_WIDTH+V_GAP;
			}
			i++;
		}
	}
	
	/*----------------------------------------------------------------*\
	|*						Attributs privés						  *|
	\*----------------------------------------------------------------*/
	private Graphics2D g2D;
	private ButtonAlgorithm buttonAlgo;
	private JScrollPane scrollList;
	private JList listStat;
	private PanelStatisticView panelStatisticView;
	private int[] selectedIndices;
	private boolean disableSelection;
	private ArrayList<Boolean> listEnabledStatistic;
	private double hMax;
	
	/*-----------------------------------------*\
	|*			Statiques finaux			   *|
	\*-----------------------------------------*/
	private static final Font TITLE_FONT = new Font("Times new roman", Font.BOLD, 18);
	private static final Font FONT = new Font("Times new roman", Font.PLAIN, 12);
	private static final int X_INIT = 10; // Espacement à gauche
	private static final int Y_TITLE = 15; // Hauteur du titre
	private static final int H_GAP = 5; 
	private static final int V_GAP = 5; // Espacement vertical entre le titre, le graphe et les stats
	private static final int STAT_WIDTH = 10;
}
