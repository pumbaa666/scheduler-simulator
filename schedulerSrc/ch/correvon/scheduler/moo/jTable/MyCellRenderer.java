package ch.correvon.scheduler.moo.jTable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MyCellRenderer extends JLabel implements ListCellRenderer
{
	/*----------------------------------------------------------------*\
	|*							Constructeurs						  *|
	\*----------------------------------------------------------------*/
	public MyCellRenderer()
	{
		this.repeat = false;
	}
	
	/*----------------------------------------------------------------*\
	|*						Méthodes publiques						  *|
	\*----------------------------------------------------------------*/
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		this.setText(value.toString());
		
		if(isSelected)
			this.setBackground(list.getSelectionBackground());
		else
			this.setBackground(list.getBackground());
		
		if(this.colors == null)
			System.err.println("MyCellRenderer - Null Pointer Exception");
		else if(index < this.colors.length)
			this.setForeground(this.colors[index]);
		else
		{
			if(this.repeat)
				this.setForeground(this.colors[index%this.colors.length]);
			else
				System.err.println("MyCellRenderer - Index out of bounds : "+index+" >= "+this.colors.length);
		}

		this.setEnabled(list.isEnabled());
		this.setFont(list.getFont());
		this.setOpaque(true);
		
		return this;
	}
	
	public Color getColor(int index)
	{
		if(index < this.colors.length)
			return this.colors[index];
		return null;
	}
	
	/*--------------------------------*\
	|*				Set				  *|
	\*--------------------------------*/
	public void setColorIndices(Color[] colors)
	{
		this.colors = colors;
	}
	
	/**
	 * Le couleurs reprennent au début du tableau si l'indice de la liste est plus grand que le nombre de couleur
	 */
	public void setRepeat(boolean value)
	{
		this.repeat = true;
	}
	
	/*----------------------------------------------------------------*\
	|*						Attributs privés						  *|
	\*----------------------------------------------------------------*/
	private Color[] colors;
	private boolean repeat;
}