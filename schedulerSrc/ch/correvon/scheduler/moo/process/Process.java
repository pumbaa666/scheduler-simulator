
//Specification:
package ch.correvon.scheduler.moo.process;

import java.awt.Rectangle;

import ch.correvon.scheduler.moo.myObjects.MyClonable;



public class Process implements MyClonable
{
	/*----------------------------------------------------------------*\
	|*							Constructeurs						  *|
	\*----------------------------------------------------------------*/
	public Process(String name, int incomingTime, int duration, int realIncomingTime, int firstExecution, int endingTime, ProcessType type)
	{
		this.name = name;
		this.incomingTime = incomingTime;
		this.realIncomingTime = realIncomingTime;
		this.duration = duration;
		this.remainingTime = duration;
		this.firstExecution = firstExecution;
		this.endingTime = endingTime;
		this.type = type;

		this.cpuInCharge = -1;
		this.isAlgoInChargePreemptif = false;
		this.isLast = false;
		this.usedQuantum = 0;
		this.affinity = -1;
		this.waitSince = 0;
		this.responseTime = -1;
		
		this.bounds = new Rectangle();
	}
	
	public Process(String name, int incomingTime, int duration, ProcessType type)
	{
		this(name, incomingTime, duration, -1, -1, 0, type);
	}
	
	public Process(Process process)
	{
		this(process.getName(), process.getIncomingTime(), process.getDuration(), process.getRealIncomingTime(), process.getFirstExecution(), process.getEndingTime(), new ProcessType(process.getType()));
		this.cpuInCharge = process.getCPUInCharge();
		this.isAlgoInChargePreemptif = process.isAlgoInChargePreemptif();
		this.usedQuantum = process.getUsedQuantum();
		this.affinity = process.getAffinity();
		this.waitSince = process.waitSince();
		
		this.waitingTime = process.getWaitingTime();
		this.rotateTime = process.getRotateTime();
		this.responseTime = process.getResponseTime();
		this.isLast = process.isLast();
	}
	
	/*----------------------------------------------------------------*\
	|*						Methodes publiques						  *|
	\*----------------------------------------------------------------*/
	public Process myClone()
	{
		Process clone = new Process(this);
		clone.setEndingTime(this.endingTime);
		return clone;
	}
	
	public String toStringSpaceIn()	
	{
		String str = new String(this.name);
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.incomingTime);
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.duration);
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.type.toString().substring(0, Math.min(3, this.type.toString().length())));
		return str;
	}
	
	public String toStringTabulationIn()	
	{
		return this.name+SPACING_CHAR_FILE+this.incomingTime+SPACING_CHAR_FILE+this.duration+SPACING_CHAR_FILE+this.type;
	}
	
	public String toStringSpaceOut()
	{
		String str = new String(this.name);
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.realIncomingTime);
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.endingTime);
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.waitingTime);
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.responseTime);
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.rotateTime);
		return str;
	}
	
	public String toStringTabulationOut()
	{
		return this.name+SPACING_CHAR_FILE+this.realIncomingTime+SPACING_CHAR_FILE+this.endingTime+SPACING_CHAR_FILE+this.waitingTime+SPACING_CHAR_FILE+this.responseTime+SPACING_CHAR_FILE+this.rotateTime;
	}
	
	public String toStringFull()
	{
		return
			this.name+
			"\nArrivée : "+this.incomingTime+
			"\nDurée : "+this.duration+
			"\nArrivée réelle : "+this.realIncomingTime+
			"\nFin : "+this.endingTime+
			"\nAttente : "+this.waitingTime+"\nRéponse : "+this.responseTime+
			"\nRotation : "+this.rotateTime+
			"\nTemps restant : "+this.remainingTime+
			"\naffecté au CPU"+this.cpuInCharge+
			"\nUsedQuantum : "+this.usedQuantum;
	}
	
	public String toStringDash()	
	{
		String str = new String(this.name);
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.realIncomingTime);
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+(this.endingTime+1));
		for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
			str = str.concat(" ");
		if(this.isLast)
		{
			str = str.concat(""+this.waitingTime);
			for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
				str = str.concat(" ");
			str = str.concat(""+this.responseTime);
			for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
				str = str.concat(" ");
			str = str.concat(""+this.rotateTime);
		}
		else
		{
			for(int j = 0; j < 3; j++)
			{
				for(int i = str.length(); i%TABULATION_LENGTH!=0; i++)
					str = str.concat(" ");
				str = str.concat("-");
			}
		}
		return str;
	}
	
	public void increaseWaitSince()
	{
		this.waitSince++;
	}
	
	public void clearWaitSince()
	{
		this.waitingTime = this.waitSince - 1;
		this.waitSince = 0;
	}
	
	public void clearUsedQuantum()
	{
		this.usedQuantum = 0;
	}
	
	/*--------------------------------*\
	|*				Set				  *|
	\*--------------------------------*/
	public void setResponseTime()
	{
		if(this.responseTime == -1)
		{
			int time = this.firstExecution-this.incomingTime;
			if(time >= 0)
				this.responseTime = time;
		}
	}
	public void setResponseTime(int time)
	{
		this.responseTime = time;
	}
	
	public void setBounds(int x, int y, int w, int h)
	{
		this.bounds = new Rectangle(x, y, w, h);
	}
	
	public void setAffinity(int num)
	{
		this.affinity = num;
	}
	
	public void setCPUInCharge(int num)
	{
		this.cpuInCharge = num;
	}
	
	public void setType(ProcessType type)
	{
		this.type = new ProcessType(type);
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setIncomingTime(int incomingTime)
	{
		this.incomingTime = incomingTime;
	}
	
	public void setRealncomingTime(int incomingTime)
	{
		this.realIncomingTime = incomingTime;
		if(this.firstExecution == -1)
			this.firstExecution = incomingTime;
	}
	
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	
	public void setEndingTime(int endingTime)
	{
		setEndingTime(endingTime, 0);
	}

	public void setEndingTime(int endingTime, int decalage)
	{
		this.endingTime = endingTime + decalage;
		this.waitingTime = this.endingTime - this.incomingTime - this.duration + 1;
		this.rotateTime = this.endingTime-this.firstExecution + 1;
		this.setResponseTime();
	}
	
	/**
	 * Décrémente le temps restant du processus (remainingTime) de un et renvoie false si il est fini (remainingTime == 0)
	 */
	public boolean decrement()
	{
		this.remainingTime--;
		this.usedQuantum++;

		return this.remainingTime > 0;
	}
	
	public void setLast(boolean value)
	{
		this.isLast = value;
	}
	
	public void setWaitingTime(int time)
	{
		this.waitingTime = time;
	}
	
	public void setRotateTime(int time)
	{
		this.rotateTime = time;
	}
	
	public void setAlgoInChargePreemptif(boolean value)
	{
		this.isAlgoInChargePreemptif = value;
	}
	
	/*--------------------------------*\
	|*				Get				  *|
	\*--------------------------------*/
	public Rectangle getBounds()
	{
		return this.bounds;
	}
	
	public int getAffinity()
	{
		return this.affinity;
	}
	
	public int getUsedQuantum()
	{
		return this.usedQuantum;
	}
	
	public int getCPUInCharge()
	{
		return this.cpuInCharge;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getNameTypeTime()
	{
		return this.name +"["+this.type+", "+this.remainingTime+"]";
	}
	
	public int getEndingTime()
	{
		return this.endingTime;
	}
	
	public int getDuration()
	{
		return this.duration;
	}
	
	public int getIncomingTime()
	{
		return this.incomingTime;
	}
	
	public int getRemainingTime()
	{
		return this.remainingTime;
	}
	
	public int getWaitingTime()
	{
		return this.waitingTime;
	}
	
	public int getRealIncomingTime()
	{
		return this.realIncomingTime;
	}
	
	public int getRotateTime()
	{
		return this.rotateTime;
	}
	
	public int getResponseTime()
	{
		return this.responseTime;
	}
	
	public int getFirstExecution()
	{
		return this.firstExecution;
	}
	
	public ProcessType getType()
	{
		return this.type;
	}
	
	public int waitSince()
	{
		return this.waitSince;
	}
	
	/*--------------------------------*\
	|*				Is				  *|
	\*--------------------------------*/
	public boolean isEqual(Process process)
	{
		boolean value = false;
		
		if(this.name.compareTo(process.getName()) == 0)
			value = true;
		
		if(this.incomingTime == process.getEndingTime())
			value = true;
		else
			value = false;
		
		if(this.duration == process.getDuration())
			value = true;
		else
			value = false;
		
		return value;
	}
	
	public boolean isLast()
	{
		return this.isLast;
	}
	
	public boolean isAlgoInChargePreemptif()
	{
		return this.isAlgoInChargePreemptif;
	}
	
	/*----------------------------------------*\
	|*				Statiques				  *|
	\*----------------------------------------*/
	public static Process convert(String process)
	{
		String[] str = process.split(SPACING_CHAR_FILE);
		
		if(str.length != 4)
			return null;
		
		String name = str[0];
		int time = 0;
		int duration = 0;
		ProcessType type;
		
		if(name.length() == 0 || name.length() > 4) {
			return null;
		}
		
		try
		{
			time = Integer.parseInt(str[1]);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			return null;
		}
		
		try
		{
			duration = Integer.parseInt(str[2]);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			return null;
		}
		
		type = new ProcessType(str[3]);

		return new Process(name, time, duration, type);
	}
	
	/*----------------------------------------------------------------*\
	|*						Attributs Privés						  *|
	\*----------------------------------------------------------------*/
	private String name;
	private int incomingTime;
	private int duration;
	private ProcessType type;
	
	private Rectangle bounds;
	
	private int realIncomingTime;
	private int endingTime;
	private int waitingTime; // Temps total passé à attendre
	private int rotateTime; // Fin réelle - Arrivée réelle
	private int responseTime; // temps entre Arrivée et 1ère execution
	private int firstExecution;
	private int remainingTime;
	private int usedQuantum;
	
	private boolean isLast;
	private int cpuInCharge;
	private boolean isAlgoInChargePreemptif;
	private int affinity;
	private int waitSince;
	
	/*-----------------------------------------*\
	|*				Statiques				   *|
	\*-----------------------------------------*/
	private static final String SPACING_CHAR_FILE = ", ";
	
	/*-------------------------------------*\
	|*				Publiques			   *|
	\*-------------------------------------*/
	public static final int TABULATION_LENGTH = 5;
}