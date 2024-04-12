
//Specification:
package ch.correvon.scheduler.plugin.algo;

import java.util.ArrayList;

import ch.correvon.scheduler.algorithms.AlgoTools;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.SortedProcess;
import ch.correvon.scheduler.plugin.Algo_I;

public class FCFS implements Algo_I
{
	/*----------------------------------------------------------------*\
	|*						Methodes publiques						  *|
	\*----------------------------------------------------------------*/
	public Process runAlgorithm(ArrayList<Process> listProcess, int quantum)
	{
		AlgoTools.sortListByIncomingTime(listProcess);
		return listProcess.get(0);
	}
	
	public Algo_I cloneOf()
	{
		FCFS fcfs = new FCFS();
		fcfs.setSortedProcess(this.sortedProcess);
		return fcfs;
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
		return "FCFS";
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

