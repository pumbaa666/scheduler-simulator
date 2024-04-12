package ch.correvon.scheduler.moo.jTable;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JTableButtonRenderer implements TableCellRenderer
{
	/**
	 * 
	 * @param disableChange : interdit ou non le changement de couleur d'un boutton après son attribution
	 */
	public JTableButtonRenderer(boolean disableChange)
	{
		this.lastButton = new ArrayList<JButton>();
		this.disableChange = disableChange;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if(row >= this.lastButton.size() || !this.disableChange)
			if(value instanceof JButton)
			{
				JButton button = (JButton)value;
				this.lastButton.add(button);
				return button;
			}
		
		return this.lastButton.get(row);
	}
	
	public void disableChange(boolean value)
	{
		this.disableChange = value;
	}
	
	private boolean disableChange;
	private ArrayList<JButton> lastButton;
}
