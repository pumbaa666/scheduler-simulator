package ch.correvon.scheduler.moo.jTable;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;

import ch.correvon.scheduler.gui.statistic.PanelStatisticView;


public class JTableButtonMouseListener implements MouseListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public JTableButtonMouseListener(JTable table, PanelStatisticView panelView) 
	{
		this.table = table;
		this.panelView = panelView;
	}
	
	public JTableButtonMouseListener(JTable table) 
	{
		this(table, null);
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void mouseClicked(MouseEvent e)
	{
		TableColumnModel columnModel = this.table.getColumnModel();
		int column = columnModel.getColumnIndexAtX(e.getX());
		int row = e.getY() / this.table.getRowHeight();

		if(row >= this.table.getRowCount() || row < 0 || column >= this.table.getColumnCount() || column < 0)
			return;

		Object value = this.table.getValueAt(row, column);

		if(!(value instanceof JButton))
			return;

		JButton button = (JButton)value;

		MouseEvent buttonEvent = SwingUtilities.convertMouseEvent(this.table, e, button);
		button.dispatchEvent(buttonEvent);
		this.table.repaint();
		Color colorReturn = JColorChooser.showDialog(button, "Choisissez la couleur du type", button.getBackground());
		if(colorReturn != null)
			((StatisticTableModel)this.table.getModel()).setValueAt(colorReturn, row, 1);
		
		if(this.panelView != null)
			this.panelView.update();
		
		this.table.requestFocus();
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
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private JTable table;
	private PanelStatisticView panelView;
}
