
//Specification:
package ch.correvon.scheduler.gui.about;

import ch.correvon.scheduler.moo.myObjects.MyJFrame;


public class FrameAbout extends MyJFrame
{
	 /*-----------------------------------------------------------------*\
	 |*							Constructeurs							*|
	 \*-----------------------------------------------------------------*/
	public FrameAbout()
	{
		super(100, 100, 400, 300);
		this.setTitle("A propos");
		
		PanelAbout panelAbout = new PanelAbout();
		this.addComponentListener(panelAbout);
		this.add(panelAbout);
		this.setVisible(false);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes Publiques					   *|
	\*-----------------------------------------------------------------*/
	@Override public void close()
	{
		this.setVisible(false);
	}
}

