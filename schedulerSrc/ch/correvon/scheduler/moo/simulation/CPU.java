package ch.correvon.scheduler.moo.simulation;

import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.ProcessType;


public class CPU
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public CPU()
	{
		this.process = null;
		this.id = ID;
		ID++;
	}

	/*-----------------------------------------------------------------*\
	|*							Methodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void affectProcess(Process process)
	{
		this.hasDoneOneDecrement = false;
		this.process = process;
	}
	
	public boolean decrement()
	{
		if(this.process != null)
		{
			boolean result = this.process.decrement();
			this.process.setAffinity(this.id);
			this.process.setCPUInCharge(this.id);
			this.process.setAlgoInChargePreemptif(this.process.getType().getAlgo().isPreemptif());
			this.process.clearWaitSince();
			this.hasDoneOneDecrement = true;
			return result;
		}
		
		return true;
	}
	
	public Process removeProcess()
	{
		Process process = this.process;
		this.process = null;
		return process;
	}

	public void clear()
	{
		this.process = null;
	}
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setRealIncomingTime(int time)
	{
		this.process.setRealncomingTime(time);
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public ProcessType getProcessType()
	{
		if(this.process == null)
			return null;
		return new ProcessType(this.process.getType());
	}
	
	public String getProcessName()
	{
		if(this.process != null)
			return this.process.getName();
		return null;
	}
	
	public int getRealIncomingTime()
	{
		return this.process.getRealIncomingTime();
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public int getRemainingTime()
	{
		if(this.process != null)
			return this.process.getRemainingTime();
		return -1;
	}
	
	public Process getProcess()
	{
		return this.process;
	}
	
	/*---------------------------------*\
	|*				Is				   *|
	\*---------------------------------*/
	public boolean isWorking()
	{
		return this.process != null;
	}
	
	public boolean isWorkingSinceOneTime()
	{
		return (this.isWorking() && this.hasDoneOneDecrement());
	}
	
	public boolean hasDoneOneDecrement()
	{
		return this.hasDoneOneDecrement;
	}
	
	/*-----------------------------------------*\
	|*				Statiques 				   *|
	\*-----------------------------------------*/
	public static void resetID()
	{
		ID = 0;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private Process process;
	private boolean hasDoneOneDecrement;
	private int id;
	
	/*-----------------------------------------*\
	|*				Statiques 				   *|
	\*-----------------------------------------*/
	private static int ID = 0;
}
