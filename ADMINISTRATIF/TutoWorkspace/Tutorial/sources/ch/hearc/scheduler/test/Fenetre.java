package ch.hearc.scheduler.test;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Fenetre extends JFrame
{
	public Fenetre(ImageIcon icon)
	{
		Panneau panneau = new Panneau(icon);
		this.add(panneau);
	}
}