
//Specification:
package ch.correvon.scheduler.plugin.algo;

import java.util.ArrayList;

import ch.correvon.scheduler.algorithms.AlgoTools;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.SortedProcess;
import ch.correvon.scheduler.plugin.Algo_I;


public class RoundRobin implements Algo_I
{
	/*----------------------------------------------------------------*\
	|*						Methodes publiques						  *|
	\*----------------------------------------------------------------*/
	public Process runAlgorithm(ArrayList<Process> listProcess, int quantum)
	{
		AlgoTools.sortListByIncomingTime(listProcess);
		//System.out.println("< Round Robin - runAlgo(quantum = "+quantum+") >");
		//Tools.printProcess(listProcess);
		
		// Cherche tout les processus qui tournent déjà
		ArrayList<Process> inProgress = new ArrayList<Process>();
		Process longestWaiting = null;
		int count = 0;
		int firstEndingTime = -1;
		for(Process p:listProcess)
		{
			//System.out.println(p.getName()+" last end : "+p.getEndingTime());
			if(p.getUsedQuantum() != 0)
				inProgress.add(p);
			else if(firstEndingTime == -1 || p.waitSince() > firstEndingTime)
			{
				//System.out.println("choose : "+p.getName());
				longestWaiting = p;
				firstEndingTime = p.waitSince();
			}
			count++;
		}
		
		// Cherche un processus qui tourne déjà et qui n'a pas fini son quantum
		Process running = null;
		if(inProgress.isEmpty())
			running = longestWaiting;
		else
		{
			int i = 0;
			for(Process p:inProgress)
			{
				if(p.getUsedQuantum() < quantum)
				{
					running = p;
					break;
				}
				i++;
			}
			
			// Si ils ont tous fini leur quantum
			if(running == null)
			{
				for(Process p:inProgress)
					p.clearUsedQuantum();
				if(longestWaiting != null)
					running = longestWaiting;
				else
					running = inProgress.get(inProgress.size()-1);
			}
		}
		
		//System.out.println("Result : "+running.getName()+"\n");
		return running;
	}
	
	public Algo_I cloneOf()
	{
		RoundRobin rr = new RoundRobin();
		rr.setSortedProcess(this.sortedProcess);
		return rr;
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
		return "Round Robin";
	}
	
	/*--------------------------------*\
	|*				Is				  *|
	\*--------------------------------*/
	public boolean isPreemptif()
	{
		return true;
	}
	
	/*----------------------------------------------------------------*\
	|*						Attributs privés						  *|
	\*----------------------------------------------------------------*/
	private SortedProcess sortedProcess;
}

