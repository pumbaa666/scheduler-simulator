
//Specification:
package ch.correvon.scheduler.gui.about;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import ch.correvon.scheduler.moo.Tools;


public class PanelAbout extends JPanel implements ComponentListener
{
	/*-----------------------------------------------------------------*\
	|*							Construteur							   *|
	\*-----------------------------------------------------------------*/
	public PanelAbout()
	{
		this.logoHearc = getToolkit().getImage("ressources\\Images\\logo_hearc.png");
		this.logoJava = getToolkit().getImage("ressources\\Images\\logo_java.png");
	}
	
	/*-----------------------------------------------------------------*\
	|*						Méthodes publiques						   *|
	\*-----------------------------------------------------------------*/
	@Override public void componentHidden(ComponentEvent e)
	{
	}

	@Override public void componentMoved(ComponentEvent e)
	{
	}

	@Override public void componentResized(ComponentEvent e)
	{
	}

	@Override public void componentShown(ComponentEvent e)
	{
		this.logoJavaWidth = this.logoJava.getWidth(this);
		this.logoJavaHeight = this.logoJava.getHeight(this);
	}
	
	@Override public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int panelHeight = this.getHeight();

		this.g2D = (Graphics2D)g;
		this.g2D.setColor(Color.black);
		this.g2D.setFont(new Font("Times new roman", Font.PLAIN, 20));

		Tools.drawString(this.g2D, "Scheduler Simulator", x, 20);
		Tools.drawString(this.g2D, "Version 0.0.2", x, 50);
		Tools.drawString(this.g2D, "Dévoloppé par Pumbaa", x, 80);
		Tools.drawString(this.g2D, "Travail de diplôme 2007-2008 n°07INF-TD205", x, 110);
		Tools.drawString(this.g2D, "Haute École ARC", x, 140);
		
		double ratio = this.logoHearc.getWidth(this) / (double)this.logoJavaHeight;
		int w = 300;
		int h = (int)(w/ratio);
		this.logoHearcHeight = panelHeight-h;
		this.g2D.drawImage(this.logoHearc, 0, this.logoHearcHeight, w, h, this);
		this.g2D.drawImage(this.logoJava, this.getWidth() - this.logoJavaWidth, panelHeight - this.logoJavaHeight, this);
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private Graphics2D g2D;
	
	private Image logoHearc;
	private int logoHearcHeight;
	
	private Image logoJava;
	private int logoJavaWidth;
	private int logoJavaHeight;
	
	/*-----------------------------------------------------*\
	|*					Statiques finaux				   *|
	\*-----------------------------------------------------*/
	private static final int x = 5;
}

