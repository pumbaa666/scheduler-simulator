package ch.hearc.scheduler.test;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Panneau extends JPanel
{
	public Panneau(ImageIcon icon)
	{
		this.icon = icon;
	}
	@Override public void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D)g;
		g2D.drawImage(icon.getImage(), 0, 0, 600, 800, this);
	}
	ImageIcon icon;
}