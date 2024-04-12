
//Specification:
package ch.correvon.scheduler.gui.options;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ch.correvon.scheduler.moo.myObjects.MyJList;

/**
 * Cet ecouteur est instancié dans PanelUI et PanelOptions
 * Ecoute les entrées claviers dans la listBox de processus
 */
public class ListBoxInputListener implements KeyListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public ListBoxInputListener(MyJList list)
	{
		this.list = list;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == 38) // [haut]
		{
			if(e.isAltDown())
			{
				if(e.isControlDown())
					this.list.upTop();
				else
					this.list.up();
			}
		}
		else if(e.getKeyCode() == 40) // [bas]
		{
			if(e.isAltDown())
			{
				if(e.isControlDown())
					this.list.downBottom();
				else
					this.list.down();
			}
		}
		else if(new String(""+e.getKeyChar()).toLowerCase().compareTo("s") == 0) // [S], [s] = échanger processus
		{
			this.list.switchElements();
		}
	}
	
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private MyJList list;
}

