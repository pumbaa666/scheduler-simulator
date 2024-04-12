package ch.correvon.scheduler.gui.options;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Cet écouteur est instancié dans PanelOptions
 */
public class ListTypeListener implements ListSelectionListener, MouseListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public ListTypeListener(PanelOptions panelOptions)
	{
		this.panelOptions = panelOptions;
	}
	
	/*-----------------------------------------------------*\
	|*						Souris						   *|
	\*-----------------------------------------------------*/
	public void mouseReleased(MouseEvent e)
	{
		this.panelOptions.setTxtBoxType();
	}
	
	public void mouseExited(MouseEvent vi_Event)
	{
	}
	
	public void mousePressed(MouseEvent vi_Event)
	{
	}
	
	public void mouseEntered(MouseEvent vi_Event)
	{
	}
	
	public void mouseClicked(MouseEvent vi_Event)
	{
	}
	
	/*-----------------------------------------------------*\
	|*						Selection					   *|
	\*-----------------------------------------------------*/
	public void valueChanged(ListSelectionEvent e)
	{
		JList list = (JList)e.getSource();
		this.panelOptions.enableButtons(list.getSelectedIndices().length == 1); // Active le bouton modifier et le bouton couleur si il y a 1 et 1 seul processus de sélectionné
		this.panelOptions.setTxtBoxType();
	}

	/*-----------------------------------------------------------------*\
	|*						Attributs privés						   *|
	\*-----------------------------------------------------------------*/
	private PanelOptions panelOptions;
}
