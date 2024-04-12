package ch.correvon.scheduler.moo.process;

public class UsedType
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public UsedType(ProcessType type)
	{
		this.type = type;
		this.usedTime = 0;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void increaseUsedTime()
	{
		this.usedTime++;
	}
	
	public void clearUsedTime()
	{
		this.usedTime = 0;
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public int getUsedTime()
	{
		return this.usedTime;
	}
	
	public ProcessType getType()
	{
		return this.type;
	}
	
	/*---------------------------------*\
	|*				Is				   *|
	\*---------------------------------*/
	public boolean isEquals(String name)
	{
		return this.type.toString().compareTo(name) == 0;
	}
	
	public boolean isEquals(ProcessType type)
	{
		return this.isEquals(type.toString());
	}
	
	public boolean isEquals(UsedType type)
	{
		return this.isEquals(type.getType());
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private ProcessType type;
	private int usedTime;
}
