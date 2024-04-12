package ch.correvon.scheduler.gui.printPreview;

import java.util.ArrayList;

import ch.correvon.scheduler.moo.myObjects.MyJFrame;

public class FramePreview extends MyJFrame
{
	public FramePreview()
	{
		super(0, 0, 1024, 768);
		this.setTitle("Preview"); // TODO ajouter nom genre Algorithme0, 1, 2, ...
		
		this.setVisible(false);
	}
	
	public void showUI()
	{
		super.showUI();
		// TODO delete si rien d'autre
	}
	
	public void setData(ArrayList<String> input, ArrayList<String> output)
	{
		
	}

	@Override public void close()
	{
		this.setVisible(false);
	}

}
