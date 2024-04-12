
//Specification:
package ch.correvon.scheduler.moo.simulation;

import ch.correvon.scheduler.moo.process.SortedProcess;



public class GanttDiagram
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public GanttDiagram(String name, SortedProcess sortedProcess)
	{
		this.name = name;
		this.sortedProcess = sortedProcess;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Methodes publiques					   *|
	\*-----------------------------------------------------------------*/
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public SortedProcess getSortedProcess()
	{
		return this.sortedProcess;
	}
	
	public String getName()
	{
		return this.name;
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private SortedProcess sortedProcess;
	private String name;
}

