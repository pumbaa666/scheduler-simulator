package ch.correvon.scheduler.gui.painting;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JTable;

import ch.correvon.scheduler.gui.myInterface.PanelSelectionable;

/**
 * Ecouteur implémenté dans PanelPainting. Il écoute les sélections de champs (par souris ou cliaver) de la JTable qui contient les entrées.
 */
public class InputTableListener implements MouseListener, MouseMotionListener, KeyListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur						   *|
	\*-----------------------------------------------------------------*/
	public InputTableListener(PanelSelectionable parent, JTable inputList)
	{
		this.inputList = inputList;
		this.parent = parent;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méhtodes publiques					   *|
	\*-----------------------------------------------------------------*/
	/*---------------------------------------------*\
	|*					Souris					   *|
	\*---------------------------------------------*/
	@Override public void mouseClicked(MouseEvent arg0)
	{
	}

	@Override public void mouseEntered(MouseEvent arg0) 
	{
	}

	@Override public void mouseExited(MouseEvent arg0) 
	{
	}

	@Override public void mousePressed(MouseEvent arg0)
	{
		this.action();
		this.clicked = true;
	}

	@Override public void mouseReleased(MouseEvent arg0)
	{
		this.clicked = false;
	}
	
	/*---------------------------------------------*\
	|*					Mouvement				   *|
	\*---------------------------------------------*/
	@Override public void mouseDragged(MouseEvent arg0)
	{
		if(this.clicked)
			this.action();
	}

	@Override public void mouseMoved(MouseEvent arg0)
	{
	}
	
	/*---------------------------------------------*\
	|*					Clavier					   *|
	\*---------------------------------------------*/

	@Override public void keyPressed(KeyEvent arg0) 
	{
	}

	@Override public void keyReleased(KeyEvent arg0)
	{
		this.action();
	}

	@Override public void keyTyped(KeyEvent arg0) 
	{
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes privées					   *|
	\*-----------------------------------------------------------------*/
	private void action()
	{
		ArrayList<String> listName = new ArrayList<String>();
		
		int[] selectedRow = this.inputList.getSelectedRows();
		for(int j = 0; j < selectedRow.length; j++)
		{
			String name = (String)this.inputList.getModel().getValueAt(this.inputList.convertRowIndexToModel(selectedRow[j]), 0);
			listName.add(name);
		}
		this.parent.setSelectedProcess(listName);
		this.parent.repaint();
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private JTable inputList;
	private PanelSelectionable parent;
	private boolean clicked;
}
