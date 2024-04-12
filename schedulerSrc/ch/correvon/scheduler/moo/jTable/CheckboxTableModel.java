package ch.correvon.scheduler.moo.jTable;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import ch.correvon.scheduler.gui.statistic.PanelStatisticView;
import ch.correvon.scheduler.moo.Tools;

public class CheckboxTableModel extends AbstractTableModel 
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public CheckboxTableModel(PanelStatisticView panelStatisticView, Hashtable<String, Boolean> lastEnabledValues, int initSize, String[] columnName)
	{
		this.listCheckbox = new ArrayList<SimplifiedCheckBox>(initSize);
		this.panelStatisticView = panelStatisticView;
		this.lastEnabledValues = lastEnabledValues;
		this.lastEnabledValues.put("Personnalisé", true);
		if(columnName != null)
			this.setColumnNames(columnName);
	}
	
	public CheckboxTableModel(PanelStatisticView panelStatisticView, Hashtable<String, Boolean> lastEnabledValues, int initSize)
	{
		this(panelStatisticView, lastEnabledValues, initSize, null);
	}
	
	public CheckboxTableModel(int initSize)
	{
		this(null, new Hashtable<String, Boolean>(0), initSize);
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void addRow(String name)
	{
		this.insertRow(this.listCheckbox.size(), name);
	}
	
	public void insertRow(int rowIndex, String name)
	{
		this.listCheckbox.add(rowIndex, new SimplifiedCheckBox(name, true));
		this.lastEnabledValues.put(name, true);
		super.fireTableRowsInserted(rowIndex, rowIndex);
	}
	
	public void removeRow(int rowIndex)
	{
		this.listCheckbox.remove(rowIndex);
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
		int nb = this.listCheckbox.size();
		if(nb > 0)
		{
			this.listCheckbox.clear();
			this.lastEnabledValues.clear();
			super.fireTableRowsDeleted(0, nb-1);
		}
	}
	
	public void update()
	{
		this.fireTableRowsUpdated(0, this.getRowCount());
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public int getRowCount()
	{
		return listCheckbox.size();
	}

	public int getColumnCount()
	{
		return 2;
	}
	
	@Override public Class getColumnClass(int columnIndex)
	{
		switch(columnIndex)
		{
			case 0 : return String.class;
			case 1 : return Boolean.class;
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
			case 0 : return this.listCheckbox.get(rowIndex).getText();
			
			case 1 : 	boolean value = this.listCheckbox.get(rowIndex).isSelected();
						String name = this.listCheckbox.get(rowIndex).getText();
						if(this.panelStatisticView != null && this.lastEnabledValues.get(name) != null && value != this.lastEnabledValues.get(name))
						{
							this.lastEnabledValues.put(name, value);
							this.panelStatisticView.enabledAlgorithmUpdated();
						}
						return value;
			
			default : return null;
		}
	}
	
	public ArrayList<SimplifiedCheckBox> getData()
	{
		return Tools.deepClone(this.listCheckbox);
	}

	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	@Override public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
		boolean val = false;
		boolean test = false;
		try
		{
			test = true;
			val = (Boolean)value;
		}
		catch(ClassCastException e)
		{
			try
			{
				test = true;
				String s = (String)value;
				val = (s.compareTo("true") == 0);
			}
			catch(ClassCastException e1)
			{
				JOptionPane.showMessageDialog(null, "Impossible d'affecter la valeur ["+value+"] à la case ["+rowIndex+", "+columnIndex+"]", "Erreur", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
		if(test)
			this.listCheckbox.set(rowIndex, new SimplifiedCheckBox(this.listCheckbox.get(rowIndex).getText(), val));
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
		return columnIndex == 1;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private PanelStatisticView panelStatisticView;
	private Hashtable<String, Boolean> lastEnabledValues;
	private ArrayList<SimplifiedCheckBox> listCheckbox;
	private String[] columnName = {"Option", "Valeur"};
}
