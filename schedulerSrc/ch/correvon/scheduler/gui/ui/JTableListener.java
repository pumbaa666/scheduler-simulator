
//Specification:
package ch.correvon.scheduler.gui.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTable;

import ch.correvon.scheduler.moo.jTable.Abstract_ProcessTableModel;

/**
 * Cet ecouteur est instancié dans PanelUI et PanelOptions
 * Ecoute les entrées claviers dans la listBox de processus
 */
public class JTableListener implements KeyListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public JTableListener(JTable table)
	{
		this.table = table;
		this.model = (Abstract_ProcessTableModel)this.table.getModel();
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void keyPressed(KeyEvent e)
	{
			this.model.setCellEditable(false);
			if(e.getKeyCode() == 27 || e.getKeyCode() == 127) // [del]
			{
				if(this.table.getSelectedRows().length > 0)
				{
					int nb = this.table.getSelectedRows()[0];
					this.model.removeRows(this.table.getSelectedRows());
					if(nb < this.table.getRowCount()-1)
						this.table.setRowSelectionInterval(nb, nb);
					else if(this.table.getRowCount() > 0)
						this.table.setRowSelectionInterval(0, 0);
				}
			}
			else if(e.getKeyCode() == 38) // [haut]
			{
				if(e.isAltDown())
				{
					if(e.isControlDown())
						this.model.upTop();
					else
						this.model.up();
				}
			}
			else if(e.getKeyCode() == 40) // [bas]
			{
				if(e.isAltDown())
				{
					if(e.isControlDown())
						this.model.downBottom();
					else
						this.model.down();
				}
			}
			else if(new String(""+e.getKeyChar()).toLowerCase().compareTo("s") == 0) // [S], [s] = échanger processus
			{
				this.model.switchElements();
			}
	}
	
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyReleased(KeyEvent e)
	{
		if(!e.isAltDown())
			this.model.setCellEditable(true);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private Abstract_ProcessTableModel model;
	private JTable table;
}

