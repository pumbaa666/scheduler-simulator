package ch.correvon.scheduler.moo.jTable;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.Tools;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.ProcessType;


public class SimpleProcessTableModel extends Abstract_ProcessTableModel
{
	/*-----------------------------------------------------------------*\
	|*							Construteur							   *|
	\*-----------------------------------------------------------------*/
	public SimpleProcessTableModel(JTable table, Manager manager)
	{
		super(table);
		String[] columnName = {"Nom", "Arrivée", "Durée", "Type"};
		super.setColumnName(columnName);
		this.manager = manager;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	@Override public Class getColumnClass(int columnIndex)
	{
		switch(columnIndex)
		{
			case 0 : return String.class;
			case 1 : return Integer.class;
			case 2 : return Integer.class;
			case 3 : return Process.class;
			default : return null;
		}
    }


	@Override public int getColumnCount()
	{
		return 4;
	}

	@Override public Object getValueAt(int rowIndex, int columnIndex)
	{
		if(columnIndex < this.getColumnCount())
		{
			Object result = null;
			switch(columnIndex)
			{
				case 0: result = super.getListProcess().get(rowIndex).getName(); break;
				case 1: result = ""+super.getListProcess().get(rowIndex).getIncomingTime(); break;
				case 2: result = ""+super.getListProcess().get(rowIndex).getDuration(); break;
				case 3: result = super.getListProcess().get(rowIndex).getType(); break;
			}
			return result;
		}
		
		JOptionPane.showMessageDialog(null, "La colonne ["+columnIndex+"] n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
		return null;
	}
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	@Override public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		ArrayList<Process> listProcess = super.getListProcess();
		if(columnIndex != -1)
		{
			Process old = new Process(listProcess.get(rowIndex));
			Process newProcess =listProcess.get(rowIndex);
			
			int val;
			switch(columnIndex)
			{
				case 0: newProcess.setName((String)aValue); break;
				
				case 1: val = new Integer((Integer)aValue).intValue();
						if(val >= 0)
							newProcess.setIncomingTime(val);
						break;
				
				case 2: val = new Integer((Integer)aValue).intValue();
						if(val >= 1)
							newProcess.setDuration(new Integer((Integer)aValue).intValue());
						break;
				
				case 3: newProcess.setType((ProcessType)aValue); break;
			}
			
			listProcess.remove(rowIndex); // On l'enleve pour le rajouter après, pour ne pas le tester par rapport à lui-même
			if(Tools.notYetExist(newProcess, listProcess, true, this.manager.getPreferances("checkSameArrival").compareTo("false") == 0)) // Test si le processus ne génère pas de conflit (nom ou heure d'arrivée déjà existante)
				listProcess.add(rowIndex, newProcess);
			else // Si il y a un conflit : remet l'ancien processus à sa place
				listProcess.add(rowIndex, old);
			super.fireTableCellUpdated(rowIndex, columnIndex);
		}
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
}
