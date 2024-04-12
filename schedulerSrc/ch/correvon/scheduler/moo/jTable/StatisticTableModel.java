package ch.correvon.scheduler.moo.jTable;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import ch.correvon.scheduler.gui.statistic.PanelStatisticView;
import ch.correvon.scheduler.moo.process.Statistic;

public class StatisticTableModel extends AbstractTableModel 
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public StatisticTableModel(PanelStatisticView panelStatisticView, ArrayList<Boolean> lastEnabledValues, int initSize)
	{
		this.panelStatisticView = panelStatisticView;
		this.listStat = new ArrayList<Statistic>(initSize);
		this.lastEnabledValues = lastEnabledValues;
	}

	public StatisticTableModel(int initSize)
	{
		this(null, new ArrayList<Boolean>(0), initSize);
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void addRow(Statistic stat)
	{
		this.insertRow(this.listStat.size(), stat);
	}
	
	public void insertRow(int rowIndex, Statistic stat)
	{
		this.listStat.add(rowIndex, new Statistic(stat));
		this.lastEnabledValues.add(new Boolean(true));
		super.fireTableRowsInserted(rowIndex, rowIndex);
	}
	
	public void removeRow(int rowIndex)
	{
		this.listStat.remove(rowIndex);
		this.lastEnabledValues.remove(rowIndex);
		super.fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	public void removeRows(int[] rowIndex)
	{
	    for(int i = 0; i < rowIndex.length; i++)
			this.removeRow(rowIndex[i]-i);
	}
	
	public void removeAll()
	{
		int nb = this.listStat.size();
		if(nb > 0)
		{
			this.listStat.clear();
			this.lastEnabledValues.clear();
			super.fireTableRowsDeleted(0, nb-1);
		}
	}
	
	public void update()
	{
		this.fireTableRowsUpdated(0, this.getRowCount()-1);
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public Statistic get(int index)
	{
		return new Statistic(this.listStat.get(index));
	}
	
	public int getRowCount()
	{
		return listStat.size();
	}

	public int getColumnCount()
	{
		return 3;
	}
	
	@Override public Class getColumnClass(int columnIndex)
	{
		switch(columnIndex)
		{
			case 0 : return String.class;
			case 1 : return JButton.class;
			case 2 : return Boolean.class;
			default : return null;
		}
    }
	
	@Override public String getColumnName(int columnIndex)
	{
		if(columnIndex >= 0 && columnIndex < this.getColumnCount())
			return this.columnName[columnIndex];
		return "Colonne inexistante";
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		switch(columnIndex)
		{
			case 0 : return this.listStat.get(rowIndex).getName();
			case 1 : JButton b = new JButton();
					 b.setBackground(this.listStat.get(rowIndex).getColor());
					 return b;
					 
			case 2 : 	boolean value = this.listStat.get(rowIndex).isSelected();
						if(this.panelStatisticView != null && value != this.lastEnabledValues.get(rowIndex))
						{
							this.lastEnabledValues.set(rowIndex, value);
							this.panelStatisticView.enabledStatisticUpdated();
						}
						return value;
						
			default : return null;
		}
	}
	
	public ArrayList<Statistic> getData()
	{
		return new ArrayList<Statistic>(this.listStat);
	}

	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	@Override public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
		Statistic stat = this.listStat.get(rowIndex);
		switch(columnIndex)
		{
			case 0 : stat.setName((String)value); break; 
			case 1 : stat.setColor((Color)value); break;
			case 2 : stat.setSelected((Boolean)value); break; 
		}
		this.listStat.set(rowIndex, stat);
	}
	
	public void setColumnNames(String[] columnName)
	{
		if(columnName.length == this.columnName.length)
			for(int i = 0; i < columnName.length; i++)
				this.columnName[i] = columnName[i];
	}
	
	public void setColumnName(String columnName, int columnIndex)
	{
		if(columnIndex < this.columnName.length && columnIndex > 0)
			this.columnName[columnIndex] = columnName;
	}
	
	/*---------------------------------*\
	|*				Is				   *|
	\*---------------------------------*/
	@Override public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return columnIndex == 2; // Seul la colone 2 (celle de la checkbox) est éditable.
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private PanelStatisticView panelStatisticView;
	private ArrayList<Statistic> listStat;
	private String[] columnName = {"Statistique", "Couleur", "[v]"};
	private ArrayList<Boolean> lastEnabledValues;
}
