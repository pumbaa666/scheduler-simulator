
//Specification:
package ch.correvon.scheduler.moo.myObjects;

import javax.swing.JTextField;


public class MyJTextField extends JTextField
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public MyJTextField()
	{
		super();
	}
	
	public MyJTextField(int nbChamp)
	{
		super(nbChamp);
	}
	
	public MyJTextField(String txt)
	{
		super(txt);
	}
	
	/*------------------------------------------------------------------*\
	|*							Méthodes publiques						*|
	\*------------------------------------------------------------------*/	
	public static MyJTextField cloneOf(MyJTextField source)
	{
		MyJTextField clone = new MyJTextField();
		clone.setPreferredSize(source.getPreferredSize());
		return clone; 
	}
}

