package ch.correvon.scheduler.gui.statistic;

import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.myObjects.MyJFrame;

public class FrameStatistic extends MyJFrame
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur						   *|
	\*-----------------------------------------------------------------*/
	public FrameStatistic(Manager manager, int x, int y, int w, int h)
	{
		super(x, y, w, h);
		this.panelStatistic = new PanelStatisticView(manager, w, h);
		this.add(this.panelStatistic);
		this.setResizable(true);
	}
	
	/*-----------------------------------------------------------------*\
	|*							M�thodes Publiques					   *|
	\*-----------------------------------------------------------------*/
	@Override public void close()
	{
		this.setVisible(false);
	}
	
	@Override public void showUI()
	{
		super.showUI();
	}
	
	public PanelStatisticView getPanelStatistic()
	{
		return this.panelStatistic;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs priv�s					   *|
	\*-----------------------------------------------------------------*/
	private PanelStatisticView panelStatistic;
}
