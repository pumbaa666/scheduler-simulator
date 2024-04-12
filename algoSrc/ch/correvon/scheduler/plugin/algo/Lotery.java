
//Specification:
package ch.correvon.scheduler.plugin.algo;

import java.util.ArrayList;
import java.util.Random;

import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.SortedProcess;
import ch.correvon.scheduler.plugin.Algo_I;


public class Lotery implements Algo_I
{
	/*----------------------------------------------------------------*\
	|*						Methodes publiques						  *|
	\*----------------------------------------------------------------*/
	public Process runAlgorithm(ArrayList<Process> listProcess, int quantum)
	{
		return listProcess.get(Math.abs(new Random().nextInt())%listProcess.size());
	}
	
	public Algo_I cloneOf()
	{
		Lotery lotery = new Lotery();
		lotery.setSortedProcess(this.sortedProcess);
		return lotery;
	}
	
	/*--------------------------------*\
	|*				Set				  *|
	\*--------------------------------*/
	public void setSortedProcess(SortedProcess sortedProcess)
	{
		if(sortedProcess == null)
			this.sortedProcess = new SortedProcess();
		else
			this.sortedProcess = new SortedProcess(sortedProcess);
	}
	
	/*--------------------------------*\
	|*				Get				  *|
	\*--------------------------------*/
	public SortedProcess getResult()
	{
		return this.sortedProcess;
	}
	
	
	public String getName()
	{
		return this.toString();
	}
	
	@Override public String toString()
	{
		return "Lotery";
	}
	
	/*--------------------------------*\
	|*				Is				  *|
	\*--------------------------------*/
	public boolean isPreemptif()
	{
		return false;
	}
	
	/*----------------------------------------------------------------*\
	|*						Attributs privés						  *|
	\*----------------------------------------------------------------*/
	private SortedProcess sortedProcess;
}

