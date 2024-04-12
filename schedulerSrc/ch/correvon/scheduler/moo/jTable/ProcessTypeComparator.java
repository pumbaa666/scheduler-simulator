package ch.correvon.scheduler.moo.jTable;

import java.util.Comparator;

import ch.correvon.scheduler.moo.process.ProcessType;

public class ProcessTypeComparator implements Comparator
{

	@Override public int compare(Object arg0, Object arg1)
	{
		ProcessType i0;
		ProcessType i1;
		
		if(arg0 instanceof ProcessType && arg1 instanceof ProcessType)
		{
			i0 = (ProcessType)arg0;
			i1 = (ProcessType)arg1;
		}
		else
		{
			try 
			{
				i0 = new ProcessType((String)arg0);
				i1 = new ProcessType((String)arg1);
			}
			catch (ClassCastException e)
			{
				i0 = new ProcessType("0");
				i1 = new ProcessType("0");
				System.err.println("Impossible de comparer les objets "+arg0+"["+arg0.getClass()+"] et "+arg1+"["+arg1.getClass()+"]");
				e.printStackTrace();
			}
		}
		
		return i0.getPriority() - i1.getPriority();
	}
	
	public void test()
	{
		System.out.println("test comparat");
	}
}
