package ch.correvon.scheduler.moo.jTable;

import javax.swing.table.TableRowSorter;



public class FullProcessRowSorter<M extends Abstract_ProcessTableModel> extends TableRowSorter<Abstract_ProcessTableModel>
{
	public FullProcessRowSorter(Abstract_ProcessTableModel model)
	{
		super(model);
		IntegerComparator integerComparator = new IntegerComparator();
		int nbColumn = model.getColumnCount();
		for(int i = 1; i < nbColumn; i++)
			super.setComparator(i, integerComparator);
		
		super.toggleSortOrder(1);
	}
}
