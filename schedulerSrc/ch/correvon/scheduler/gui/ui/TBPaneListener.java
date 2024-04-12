package ch.correvon.scheduler.gui.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

public class TBPaneListener implements MouseListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur						   *|
	\*-----------------------------------------------------------------*/
	public TBPaneListener(JTabbedPane tbPane)
	{
		this.tbpane = tbPane;
		
		this.labelPopupMenu = new JPopupMenu();
		JMenuItem itemDelete = new JMenuItem("Supprimer");
		itemDelete.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						// TODO lib�rer correctement la m�moire. En effet, m�me si on supprime l'onglet les panneau de dessins restent r�f�renc�s
						tbpane.remove(tbpane.getSelectedIndex());
					}
				}
		);
		this.labelPopupMenu.add(itemDelete);
	}

	/*-----------------------------------------------------------------*\
	|*						M�thodes publiques						   *|
	\*-----------------------------------------------------------------*/
	public void mouseClicked(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON3 && this.tbpane.getTitleAt(this.tbpane.getSelectedIndex()).compareTo("Vue") != 0)
			this.labelPopupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	public void mouseEntered(MouseEvent e) 
	{
	}

	public void mouseExited(MouseEvent e) 
	{
	}

	public void mousePressed(MouseEvent e) 
	{
	}

	public void mouseReleased(MouseEvent e) 
	{
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs priv�s					   *|
	\*-----------------------------------------------------------------*/
	private JTabbedPane tbpane;
	private JPopupMenu labelPopupMenu;
}
