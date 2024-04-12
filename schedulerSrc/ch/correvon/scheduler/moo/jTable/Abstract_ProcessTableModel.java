package ch.correvon.scheduler.moo.jTable;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import ch.correvon.scheduler.moo.process.Process;

public abstract class Abstract_ProcessTableModel extends AbstractTableModel 
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public Abstract_ProcessTableModel(JTable table)
	{
		this.table = table;
		this.isEditable = true;
		this.listProcess = new ArrayList<Process>();
		this.selectionModel = this.table.getSelectionModel();
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void addRow(Process process)
	{
		this.listProcess.add(new Process(process));
		int index = this.listProcess.size()-1;
		super.fireTableRowsInserted(index, index);
	}
	
	public void insertRow(int rowIndex, Process process)
	{
		this.listProcess.add(rowIndex, new Process(process));
		super.fireTableRowsInserted(rowIndex, rowIndex);
	}
	
	public void removeRow(int rowIndex)
	{
		this.listProcess.remove(rowIndex);
		super.fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	public void removeRows(int[] rowIndex)
	{
	    for(int i = 0; i < rowIndex.length; i++)
			this.removeRow(rowIndex[i]-i);
	}
	
	public void removeAll()
	{
		int nb = this.getRowCount();
		if(nb > 0)
		{
			this.listProcess.clear();
			super.fireTableRowsDeleted(0, nb-1);
		}
	}
	
	public void update()
	{
		int nb = this.getRowCount(); 
		if(nb > 0)
			this.fireTableRowsUpdated(0, nb-1);
	}
	
	/*-------------------------------------------------*\
	|*					Déplacement					   *|
	\*-------------------------------------------------*/
	public void up()
	{
		int indices[] = this.table.getSelectedRows();
		int index1;
		int index2;
		for(int i = 0; i < indices.length ; i++)
		{
			index1 = indices[i];
			index2 = index1 - 1;
			if(index1 > 0)
				this.switchProcess(index1, index2);
		}
		this.increaseTabIndices(indices);
		this.setSelectedIndices(indices);
	}
	
	public void down()
	{
		int tabIndices[] = this.table.getSelectedRows();
		int index1;
		int index2;
		for(int i = tabIndices.length-1; i >= 0 ; i--)
		{
			index1 = tabIndices[i];
			index2 = index1 + 1;
			if(index1 != -1 && index1 < this.getRowCount()-1)
				this.switchProcess(index1, index2);
		}
		this.decreaseTabIndice(tabIndices);
		this.setSelectedIndices(tabIndices);
	}
	
	public void upTop()
	{
		int tabIndices[] = this.table.getSelectedRows();
		for(int j = 0; j < tabIndices.length ; j++)
			for(int i = tabIndices[j]; i > 0; i--)
			{
				this.setSelectedIndex(i);
				this.up();
			}
		
		// Permute les process
		for(int i = 0; i < tabIndices.length/2; i++)
			switchProcess(i, tabIndices.length-i-1);
		
		// Nouvelle sélection
		for(int i = 0; i < tabIndices.length; i++)
			tabIndices[i] = tabIndices.length-i-1;
		this.setSelectedIndices(tabIndices);
	}
	
	public void downBottom()
	{
		int tabIndices[] = this.table.getSelectedRows();
		int listBoxSize = this.getRowCount();
		for(int j = tabIndices.length-1; j >= 0 ; j--)
			for(int i = tabIndices[j]; i < listBoxSize-1; i++)
			{
				this.setSelectedIndex(i);
				this.down();
			}
		
		// Permute les process
		for(int i = 0; i < tabIndices.length/2; i++)
			this.switchProcess(listBoxSize-1-i, listBoxSize-tabIndices.length+i);
		
		// Nouvelle sélection
		for(int i = 0; i < tabIndices.length; i++)
			tabIndices[i] = listBoxSize-i-1;		
		this.setSelectedIndices(tabIndices);
	}
	
	public void switchElements()
	{
		int tabIndices[] = this.table.getSelectedRows();
		
		if(tabIndices.length > 1)
			for(int i = tabIndices.length-1; i > 0 ; i--)
				this.switchProcess(tabIndices[i], tabIndices[i-1]);
	}

	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public int getRowCount()
	{
		return this.listProcess.size();
	}

	public abstract int getColumnCount();
	
	@Override public String getColumnName(int columnIndex)
	{
		if(columnIndex >= 0 && columnIndex < this.getColumnCount())
			return this.columnName[columnIndex];
		return "Colonne inexistante";
	}
	
	public ArrayList<Process> getListProcess()
	{
		return this.listProcess;
	}
	
	public JTable getTable()
	{
		return this.table;
	}

	@Override public abstract Class getColumnClass(int columnIndex);
	
	public abstract Object getValueAt(int rowIndex, int columnIndex);
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setCellEditable(boolean value)
	{
		this.isEditable = value;
	}
	
	public void setColumnName(String[] columnName)
	{
		if(columnName.length == this.getColumnCount())
		{
			this.columnName = new String[columnName.length];
			for(int i = 0; i < columnName.length; i++)
				this.columnName[i] = columnName[i];
		}
	}
	
	@Override public abstract void setValueAt(Object aValue, int rowIndex, int columnIndex);


	/*---------------------------------*\
	|*				Is				   *|
	\*---------------------------------*/
	@Override public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return this.isEditable;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes privées					   *|
	\*-----------------------------------------------------------------*/
	/**
	 * Echange de place 2 processus dans la liste des processus
	 */
	private void switchProcess(int index1, int index2)
	{
		if(index1 < this.listProcess.size() && index2 < this.listProcess.size())
		{
			Process temp = this.listProcess.get(index1);
			this.listProcess.set(index1, this.listProcess.get(index2));
			this.listProcess.set(index2, temp);
	
			super.fireTableRowsUpdated(Math.min(index1, index2), Math.max(index1, index2));
		}
	}
	
	private void decreaseTabIndice(int[] tab)
	{
		for(int i = 0; i < tab.length; i++)
			tab[i]++;
	}
	
	private void increaseTabIndices(int[] tab)
	{
		for(int i = 0; i < tab.length; i++)
			tab[i]--;
	}
	
	private void setSelectedIndices(int[] indices)
	{
		this.selectionModel.clearSelection();
		for(int i = 0; i < indices.length; i++)
			this.selectionModel.addSelectionInterval(indices[i], indices[i]);
	}
	
	private void setSelectedIndex(int index)
	{
		this.selectionModel.setSelectionInterval(index, index);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private ListSelectionModel selectionModel;
	private JTable table;
	private String[] columnName;
	private ArrayList<Process> listProcess;
	private boolean isEditable;
}
