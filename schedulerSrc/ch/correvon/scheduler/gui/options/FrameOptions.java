
//Specification:
package ch.correvon.scheduler.gui.options;

import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.myObjects.MyJFrame;


public class FrameOptions extends MyJFrame
{
	 /*-----------------------------------------------------------------*\
	 |*							Constructeurs							*|
	 \*-----------------------------------------------------------------*/
	public FrameOptions(int x, int y, int w, int h, Manager manager)
	{
		super(x, y, w, h);
		this.setTitle("Options");
		
		this.panelOptions = new PanelOptions(manager, this);
		this.add(this.panelOptions);
		this.setVisible(false);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes Publiques					   *|
	\*-----------------------------------------------------------------*/
	public void run()
	{
		this.panelOptions.run();
	}
	
	@Override public void close()
	{
		this.setVisible(false);
	}
	
	public PanelOptions getPanelOptions()
	{
		return this.panelOptions;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private PanelOptions panelOptions;
}

