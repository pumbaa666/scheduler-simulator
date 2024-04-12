package ch.correvon.scheduler.gui.painting;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollPane;

import ch.correvon.scheduler.gui.myInterface.PanelScrollable;



/**
 * Cet �couteur est instanci� dans PanelPainting
 */
public class ScrollBarListener implements AdjustmentListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public ScrollBarListener(PanelScrollable panelPainting)
	{
		this.panelPainting = panelPainting;
	}
	
	/*-----------------------------------------------------------------*\
	|*						M�thodes Publiques						   *|
	\*-----------------------------------------------------------------*/
	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		JScrollPane scroll = this.panelPainting.getScroll(); 
		int x = scroll.getHorizontalScrollBar().getValue();
		int y = scroll.getVerticalScrollBar().getValue();
		
		this.panelPainting.setXDifferential(x);
		this.panelPainting.setYDifferential(y);
		this.panelPainting.repaint();
	}
	
	/*-----------------------------------------------------------------*\
	|*						Attributs priv�s						   *|
	\*-----------------------------------------------------------------*/
	private PanelScrollable panelPainting;
}
