//Specification
package ch.hearc.scheduler.gui.tools;

import java.awt.Color;
import javax.swing.JPanel;

/**
 * Déclare les méthodes whenShowCard et whenHideCard
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
	 |*							Méthodes Abstract						*|
	 \*-----------------------------------------------------------------*/
	/**
	 * Methodes appelées lorsque la carte vient au premier plan ou disparait
	 */
	protected abstract void whenShowCard();
	protected abstract void whenHideCard();	
	
	/*------------------------------------------------------------------------------*\
	|*								Attributs privés								*|
	\*------------------------------------------------------------------------------*/
	private CardLayouts cardLayout;
}