package ch.correvon.scheduler.gui.myInterface;

import javax.swing.JScrollPane;


public interface PanelScrollable
{
	public void repaint();
	public void setXDifferential(int x);
	public void setYDifferential(int y);
	public JScrollPane getScroll();
}
