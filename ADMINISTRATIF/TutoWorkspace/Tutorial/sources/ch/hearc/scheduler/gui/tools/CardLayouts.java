//Specification
package ch.hearc.scheduler.gui.tools;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;

/**
 * Cette classe permet une navigation plus précise entre les différentes cards. 
 * L'invocation des méthodes whenShowCard et whenHideCard signal à la card elle-même
 * qu'il s'est produit un évenement
 */
public class CardLayouts extends CardLayout
{
	/*------------------------------------------------------------------------------*\
	|*								Constructeurs									*|
	\*------------------------------------------------------------------------------*/
	public CardLayouts(Container container)
	{
		super();
		this.indexEffectifListe = 1;
		this.container = container;
	}
		
	/*------------------------------------------------------------------------------*\
	|*								Méthodes publique								*|
	\*------------------------------------------------------------------------------*/
	/**
	 * switch les cards et invoque les méthodes whenHideCard et whenShowCard
	 */
	@Override public void next(Container container)
	{
		super.next(container);
		getCard().whenHideCard();		
		incrementerIndice();
		getCard().whenShowCard();
	}
		
	@Override public void previous(Container container)
	{
		super.previous(container);
		getCard().whenHideCard();		
		decrementerIndice();
		getCard().whenShowCard();  
	}
	
	public void next()
	{
		next(this.container);
	}
	
	public void previous()
	{
		previous(this.container);
	}

	private void incrementerIndice()
	{
		if(this.indexEffectifListe >= getTabComponent().length)
			this.indexEffectifListe = 1;
		else
			this.indexEffectifListe++;	
	}
	
	private void decrementerIndice()
	{
		if(this.indexEffectifListe <= 1)
			this.indexEffectifListe = getTabComponent().length;
		else
			this.indexEffectifListe--;	
	}
	
	/*--------------------------------------------------*\
	|*						Get							*|
	\*--------------------------------------------------*/	
	/**
	 * Ne switch pas les cartes, uniquement methode d'accès
	 * 
	 */
	public PanelCards_Abstract getCard()
	{
		return getTabComponent()[indexEffectifListe-1];
	}
	
	public PanelCards_Abstract[] getTabComponent()
	{
		Component[] tabComponent = this.container.getComponents();
		PanelCards_Abstract[] tabDestination = new PanelCards_Abstract[tabComponent.length];
		int i = 0;
		for(Component panel:tabComponent)
		{
			tabDestination[i] = (PanelCards_Abstract)panel;
			i++;
		}
		return tabDestination;
	}
	
	/*------------------------------------------------------------------------------*\
	|*								Attributs privés								*|
	\*------------------------------------------------------------------------------*/
	private int indexEffectifListe;
	private Container container;
}