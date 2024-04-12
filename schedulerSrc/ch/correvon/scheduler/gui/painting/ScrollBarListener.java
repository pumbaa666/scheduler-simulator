package ch.correvon.scheduler.gui.painting;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollPane;

import ch.correvon.scheduler.gui.myInterface.PanelScrollable;



/**
 * Cet écouteur est instancié dans PanelPainting
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
	|*						Méthodes Publiques						   *|
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
	|*						Attributs privés						   *|
	\*-----------------------------------------------------------------*/
	private PanelScrollable panelPainting;
}
