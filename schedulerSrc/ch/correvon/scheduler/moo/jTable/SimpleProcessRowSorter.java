package ch.correvon.scheduler.moo.jTable;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


public class SimpleProcessRowSorter<M extends TableModel> extends TableRowSorter<TableModel>
{
	public SimpleProcessRowSorter(TableModel model)
	{
		super(model);
		IntegerComparator integerComparator = new IntegerComparator();
		super.setComparator(1, integerComparator);
		super.setComparator(2, integerComparator);
		super.setComparator(3, new ProcessTypeComparator());
		super.toggleSortOrder(0);
	}
	
	public SimpleProcessRowSorter()
	{
		this(new DefaultTableModel());
	}
}
