package ch.correvon.scheduler.moo.jTable;

import java.util.Comparator;

public class IntegerComparator implements Comparator
{

	@Override public int compare(Object arg0, Object arg1)
	{
		Integer i0;
		Integer i1;
		
		if(arg0 instanceof Integer && arg1 instanceof Integer)
		{
			i0 = (Integer)arg0;
			i1 = (Integer)arg1;
		}
		else
		{
			try 
			{
				i0 = new Integer((String)arg0);
				i1 = new Integer((String)arg1);
			}
			catch (NumberFormatException e)
			{
				i0 = new Integer("0");
				i1 = new Integer("0");
				System.err.println("Impossible de comparer les objets "+arg0+"["+arg0.getClass()+"] et "+arg1+"["+arg1.getClass()+"]");
				e.printStackTrace();
			}
		}
		
		return i0.compareTo(i1);
	}
	
	public void test()
	{
		System.out.println("test comparat");
	}
}
