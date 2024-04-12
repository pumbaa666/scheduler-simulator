package ch.correvon.scheduler.moo.process;

import java.awt.Color;

import ch.correvon.scheduler.moo.myObjects.MyClonable;
import ch.correvon.scheduler.plugin.Algo_I;

public class ProcessType implements MyClonable
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public ProcessType(String name, Algo_I algo, Color color, int priority)
	{
		this.name = name;
		this.setAlgo(algo);
		this.color = color;
		this.priority = priority;
	}
	
	public ProcessType(String name, Color color, int priority)
	{
		this(name, null, color, priority);
	}
	
	public ProcessType(ProcessType type)
	{
		this(type.toString(), type.getAlgo(), type.getColor(), type.getPriority());
	}
	
	public ProcessType(String name,int priority)
	{
		this(name, Color.white, priority);
	}
	
	public ProcessType(String name)
	{
		this(name, Color.white, 1);
	}
	
	public ProcessType()
	{
		this("");
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	@Override public MyClonable myClone()
	{
		return new ProcessType(this);
	}
	
	/*---------------------------------*\
	|*				Is				   *|
	\*---------------------------------*/
	public boolean isEquals(ProcessType type)
	{
		return (type.toString().compareTo(this.name) == 0 && type.getColor().equals(this.color) && type.getPriority() == this.priority);
	}
	
	public boolean isEquals(String type)
	{
		return type.compareTo(this.name) == 0;
	}

	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	@Override public String toString()
	{
		return this.name;
	}
	
	public Color getColor()
	{
		return this.color;
	}
	
	public int getPriority()
	{
		return this.priority;
	}
	
	public Algo_I getAlgo()
	{
		return this.algo;
	}

	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setAlgo(Algo_I algo)
	{
		if(algo != null)
			this.algo = algo.cloneOf();
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private String name;
	private Color color;
	private int priority;
	private Algo_I algo;
}
