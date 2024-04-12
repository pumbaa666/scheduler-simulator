package ch.correvon.scheduler.gui.algorithm;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Ecouteur d'une ScrollBar implémenté dans PanelAlgoView 
 */
public class ScrollBarMousListener implements MouseListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public ScrollBarMousListener(PanelAlgoView panelAlgoView)
	{
		this.panelAlgoView = panelAlgoView;
	}

	/*-----------------------------------------------------------------*\
	|*						Méthodes Publiques						   *|
	\*-----------------------------------------------------------------*/
	@Override public void mouseClicked(MouseEvent e)
	{
	}

	@Override public void mouseEntered(MouseEvent e)
	{
	}

	@Override public void mouseExited(MouseEvent e)
	{
	}

	@Override public void mousePressed(MouseEvent e)
	{
		this.panelAlgoView.setDragging(true);
	}

	@Override public void mouseReleased(MouseEvent e)
	{
		this.panelAlgoView.setDragging(false);
	}

	/*-----------------------------------------------------------------*\
	|*						Attributs privés						   *|
	\*-----------------------------------------------------------------*/
	private PanelAlgoView panelAlgoView;
}
