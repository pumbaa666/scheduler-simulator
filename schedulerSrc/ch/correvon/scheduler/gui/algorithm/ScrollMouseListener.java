package ch.correvon.scheduler.gui.algorithm;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Ecouteur d'une ScrollBar implémenté dans PanelAlgoView 
 */
public class ScrollMouseListener implements MouseMotionListener, MouseListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public ScrollMouseListener(PanelAlgoView panel)
	{
		this.panel = panel;
		this.drag = false;
	}
	
	/*-----------------------------------------------------------------*\
	|*						Méthodes publiques						   *|
	\*-----------------------------------------------------------------*/
	public void mouseDragged(MouseEvent e)
	{
		if(this.drag)
		{
			int w = 2*this.panel.getWidthCursor();
			int x = e.getX()+w;
			this.panel.changeCursorPosition(x);
		}
	}

	public void mouseMoved(MouseEvent e)
	{
		this.panel.isCursorInProcess(e.getPoint(), e.getLocationOnScreen());
	}

	public void mouseClicked(MouseEvent e)
	{

	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{
		this.drag = true;
		this.mouseDragged(e);
	}

	public void mouseReleased(MouseEvent e)
	{
		this.drag = false;
	}
	
	/*-----------------------------------------------------------------*\
	|*						Attributs privés						   *|
	\*-----------------------------------------------------------------*/
	private PanelAlgoView panel;
	private boolean drag;
}
