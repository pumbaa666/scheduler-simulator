package ch.correvon.scheduler.moo.process;

import java.awt.Color;

import ch.correvon.scheduler.moo.myObjects.MyClonable;

public class Statistic implements MyClonable
{
	/*----------------------------------------------------------------*\
	|*							Constructeurs						  *|
	\*----------------------------------------------------------------*/
	public Statistic(String name, double value, Color color)
	{
		this.name = name;
		this.value = value;
		this.color = color;
		this.selected = true;
	}
	
	public Statistic(String name, double value)
	{
		this(name, value, Color.white);
	}
	
	public Statistic(String name)
	{
		this(name, 0);
	}
	
	public Statistic(Statistic source)
	{
		this(source.getName(), source.getValue(), source.getColor());
		this.selected = source.isSelected();
	}
	
	/*----------------------------------------------------------------*\
	|*						M�thodes publiques						  *|
	\*----------------------------------------------------------------*/
	public Statistic myClone()
	{
		return new Statistic(this);
	} 
	
	@Override public String toString()
	{
		return this.toString(10);
	}
	
	/**
	 * Retourne le nom et la valeure de la statistique avec autant de chiffre apr�s la virgule que pr�cis� dans le parametre int precision
	 */
	public String toString(int precision)
	{
		String val = ""+this.value;
		int indexPoint = val.indexOf(".");
		if(indexPoint != -1)
		{
			int begin = 0;
			int positif = Math.max(0, precision); // minimum 0 si entr�e n�gative
			int lenght = Math.min(positif, val.length()-indexPoint-1); // ne pas d�passer la longueur de la chaine
			int end = indexPoint + 1 + lenght;
			
			val = val.substring(begin, end);
			
			int differential = precision - (val.length()-indexPoint-1);
			if(differential < precision)
				val = this.fillWithZero(val, differential);
		}
		
		return this.name + " : " + val;
	}
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setValue(double value)
	{
		this.value = value;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void setSelected(boolean value)
	{
		this.selected = value;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public String getName()
	{
		return this.name;
	}
	
	public double getValue()
	{
		return this.value;
	}
	
	public Color getColor()
	{
		return this.color;
	}
	
	/*---------------------------------*\
	|*				Is				   *|
	\*---------------------------------*/
	public boolean isSelected()
	{
		return this.selected;
	}
	
	/*----------------------------------------------------------------*\
	|*						M�thodes priv�es						  *|
	\*----------------------------------------------------------------*/
	private String fillWithZero(String string, int number)
	{
		String result = string;
		
		for(int i = 0; i < number; i++)
			result = result.concat("0");
		
		return result;
	}
	
	/*----------------------------------------------------------------*\
	|*						Attributs priv�s						  *|
	\*----------------------------------------------------------------*/
	private String name;
	private double value;
	private Color color;
	private boolean selected;
}
