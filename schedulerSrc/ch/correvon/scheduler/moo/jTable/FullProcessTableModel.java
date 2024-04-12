package ch.correvon.scheduler.moo.jTable;

import javax.swing.JOptionPane;
import javax.swing.JTable;


public class FullProcessTableModel extends Abstract_ProcessTableModel
{
	/*-----------------------------------------------------------------*\
	|*							Construteur							   *|
	\*-----------------------------------------------------------------*/
	public FullProcessTableModel(JTable table)
	{
		super(table);
		String[] columnName = {"Nom", "Arrivée", "Fin", "Attente", "Réponse", "Rotation"};
		super.setColumnName(columnName);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	@Override public Class getColumnClass(int columnIndex)
	{
		if(columnIndex == 0)
			return String.class;
		return Integer.class;
    }


	@Override public int getColumnCount()
	{
		return 6; // TODO mettre le tableau du constructeur en attribut et utiliser .size
	}

	@Override public Object getValueAt(int rowIndex, int columnIndex)
	{
		if(columnIndex < this.getColumnCount())
		{
			Object result = null;
			switch(columnIndex)
			{
				case 0: result = super.getListProcess().get(rowIndex).getName(); break;
				case 1: result = ""+super.getListProcess().get(rowIndex).getRealIncomingTime(); break;
				case 2: result = ""+super.getListProcess().get(rowIndex).getEndingTime(); break;
				case 3: result = ""+super.getListProcess().get(rowIndex).getWaitingTime(); break;
				case 4: result = ""+super.getListProcess().get(rowIndex).getResponseTime(); break;
				case 5: result = ""+super.getListProcess().get(rowIndex).getRotateTime(); break;
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
		super.getTable().setValueAt(aValue, rowIndex, columnIndex);
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
}
