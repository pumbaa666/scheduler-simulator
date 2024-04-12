//Specification
package ch.hearc.scheduler.gui.tools;

import java.awt.Color;
import javax.swing.JPanel;

/**
 * D�clare les m�thodes whenShowCard et whenHideCard
 */
public abstract class PanelCards_Abstract extends JPanel
{
	
	/*------------------------------------------------------------------------------*\
	|*								Constructeurs									*|
	\*------------------------------------------------------------------------------*/
	public PanelCards_Abstract(CardLayouts cardLayout)
	{
		super();
		super.setBackground(Color.black);
		this.cardLayout = cardLayout;
	}
	
	 /*-------------------------------------*\
	 |*					Get					*|
	 \*-------------------------------------*/
	public CardLayouts getCardLayout()
	{
		return this.cardLayout;
	}
	
	 /*-----------------------------------------------------------------*\
	 |*							M�thodes Abstract						*|
	 \*-----------------------------------------------------------------*/
	/**
	 * Methodes appel�es lorsque la carte vient au premier plan ou disparait
	 */
	protected abstract void whenShowCard();
	protected abstract void whenHideCard();	
	
	/*------------------------------------------------------------------------------*\
	|*								Attributs priv�s								*|
	\*------------------------------------------------------------------------------*/
	private CardLayouts cardLayout;
}