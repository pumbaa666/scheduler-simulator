package ch.correvon.scheduler.moo.jTable;

import ch.correvon.scheduler.moo.myObjects.MyClonable;

public class SimplifiedCheckBox implements MyClonable
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public SimplifiedCheckBox(String text, boolean isSelected)
	{
		this.text = text;
		this.isSelected = new Boolean(isSelected);
	}
	
	public SimplifiedCheckBox(String text)
	{
		this(text, false);
	}
	
	public SimplifiedCheckBox()
	{
		this("");
	}
	
	public SimplifiedCheckBox(SimplifiedCheckBox source)
	{
		this(source.getText(), source.isSelected());
	}
	
	/*-----------------------------------------------------------------*\
	|*						Méthodes publiques					   	   *|
	\*-----------------------------------------------------------------*/
	public SimplifiedCheckBox myClone()
	{
		return new SimplifiedCheckBox(this);
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public String getText()
	{
		return this.text;
	}

	/*---------------------------------*\
	|*				Is				   *|
	\*---------------------------------*/
	public Boolean isSelected()
	{
		return this.isSelected;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private Boolean isSelected;
	private String text;
}
