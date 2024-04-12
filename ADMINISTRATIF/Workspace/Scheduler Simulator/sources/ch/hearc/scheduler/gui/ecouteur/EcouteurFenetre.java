
//Specification:
package ch.hearc.scheduler.gui.ecouteur;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import ch.hearc.scheduler.moo.Manager;

/**
 * Invoque la m�thode quit() du manager lorsque l'application est quitt�e.
 * Cet �couteur est appliqu� dans FrameInterface
 */
public class EcouteurFenetre implements WindowListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public EcouteurFenetre(Manager manager)
	{
		this.manager = manager;
	}
	
	/*-----------------------------------------------------------------*\
	|*						M�thodes publiques						   *|
	\*-----------------------------------------------------------------*/
	public void windowDeiconified(WindowEvent e)
	{
		
	}
	
	public void windowActivated(WindowEvent e)
	{
		
	}
	
	public void windowIconified(WindowEvent e)
	{
		
	}
	
	public void windowDeactivated(WindowEvent e)
	{
		
	}
	
	public void windowClosing(WindowEvent e)
	{
		this.manager.quit();
	}
	
	public void windowClosed(WindowEvent e)
	{

	}
	
	public void windowOpened(WindowEvent e)
	{
		
	}
	
	/*-----------------------------------------------------------------*\
	|*						Attributs priv�s						   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
}

