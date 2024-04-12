
//Specification:
package ch.correvon.scheduler.moo.process;

import java.util.ArrayList;

import ch.correvon.scheduler.moo.Tools;

/**
 * Contient une liste de processus (trié selon un algorithme) et l'historique correspondant.
 */
public class SortedProcess
{
	/*----------------------------------------------------------------*\
	|*							Constructeurs						  *|
	\*----------------------------------------------------------------*/
	public SortedProcess(ArrayList<Process> listSortedProcess, ArrayList<String> history, ArrayList<Statistic> stats)
	{
		this.listSortedProcess = new ArrayList<Process>(listSortedProcess);
		this.history = new ArrayList<String>(history);
		this.stats = new ArrayList<Statistic>(stats);
	}
	
	public SortedProcess(ArrayList<Process> listSortedProcess, ArrayList<String> history)
	{
		this(new ArrayList<Process>(listSortedProcess), new ArrayList<String>(history), new ArrayList<Statistic>());		
	}
	
	public SortedProcess(SortedProcess sortedProcess)
	{
		this(new ArrayList<Process>(sortedProcess.getListSortedProcess()), new ArrayList<String>(sortedProcess.getHistory()), new ArrayList<Statistic>(sortedProcess.getStats()));
	}	
	
	public SortedProcess()
	{
		this(new ArrayList<Process>(), new ArrayList<String>(), new ArrayList<Statistic>());
	}
	
	/*----------------------------------------------------------------*\
	|*						Methodes publiques						  *|
	\*----------------------------------------------------------------*/
	/*--------------------------------*\
	|*				Get				  *|
	\*--------------------------------*/
	public ArrayList<Process> getListSortedProcess()
	{
		return this.listSortedProcess;
	}
	
	public ArrayList<String> getHistory()
	{
		return this.history;
	}
	
	public ArrayList<Statistic> getStats()
	{
		return new ArrayList<Statistic>(Tools.deepClone(this.stats));
	}
	
	/*--------------------------------*\
	|*				Set				  *|
	\*--------------------------------*/
	public void setStats(ArrayList<Statistic> stats)
	{
		this.stats = stats;
	}
	
	public void setStatsColor(ArrayList<Statistic> coloredStat)
	{
		int i = 0;
		for(Statistic stat:this.stats)
		{
			stat.setColor(coloredStat.get(i).getColor());
			i++;
		}
	}
	
	/*----------------------------------------------------------------*\
	|*							Attributs Privés					  *|
	\*----------------------------------------------------------------*/
	private ArrayList<Process> listSortedProcess;
	private ArrayList<String> history;
	private ArrayList<Statistic> stats;
}

