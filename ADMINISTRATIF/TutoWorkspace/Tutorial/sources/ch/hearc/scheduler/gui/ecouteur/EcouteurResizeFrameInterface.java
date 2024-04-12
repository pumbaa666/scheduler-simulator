
//Specification:
package ch.hearc.scheduler.gui.ecouteur;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import ch.hearc.scheduler.gui.FrameInterface;

/**
 * Cet �couteur (impl�ment� dans FrameInterface) est appel� lorsque la fen�tre d'interface
 * utilisateur est redimensionn�e. Ceci dans le but d'interdir des dimension trop petites. 
 */
public class EcouteurResizeFrameInterface implements ComponentListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur						   *|
	\*-----------------------------------------------------------------*/
	public EcouteurResizeFrameInterface(FrameInterface frameInterface)
	{
		this.frameInterface = frameInterface;
	}
	
	/*-----------------------------------------------------------------*\
	|*						Methodes publiques						   *|
	\*-----------------------------------------------------------------*/
	public void componentHidden(ComponentEvent e)
	{
	}

	public void componentResized(ComponentEvent e)
	{
		this.frameInterface.verifySize();
	}

	public void componentMoved(ComponentEvent e)
	{
	}

	public void componentShown(ComponentEvent e)
	{
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs priv�s					   *|
	\*-----------------------------------------------------------------*/
	private FrameInterface frameInterface;
}

