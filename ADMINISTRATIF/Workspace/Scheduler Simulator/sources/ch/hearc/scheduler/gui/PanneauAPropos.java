
//Specification:
package ch.hearc.scheduler.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import ch.hearc.scheduler.gui.tools.PanelCardsGraphics;


public class PanneauAPropos extends PanelCardsGraphics
{
	 /*-----------------------------------------------------------------*\
	 |*							Constructeurs							*|
	 \*-----------------------------------------------------------------*/
	public PanneauAPropos(int w, int h)
	{
		this.w = w;
		this.h = h;
	}
	
	 /*-----------------------------------------------------------------*\
	 |*							Méthodes Public							*|
	 \*-----------------------------------------------------------------*/
	@Override public void paintComponent(Graphics g)
	{
		super.paintComponent(g);	
		this.g2D = (Graphics2D)g;
		dessin();
	}
	
	 /*-----------------------------------------------------------------*\
	 |*							Méthodes Private						*|
	 \*-----------------------------------------------------------------*/
	private void dessin()
	{
		this.g2D.setColor(new Color(10,155,200));
		this.g2D.setFont(new Font("Times new roman", Font.PLAIN, 20));
		super.drawCenteredString(this.g2D, "Scheduler Simulator", this.w/2, this.h/10);
		super.drawCenteredString(this.g2D, "Version 0.0.1", this.w/2, 2*this.h/10);
		super.drawCenteredString(this.g2D, "Dévoloppé par Loïc Correvon", this.w/2, 4*this.h/10);
		super.drawCenteredString(this.g2D, "Haute Ecole Arc Jurassien", this.w/2, 5*this.h/10);
	}
	
	 /*-----------------------------------------------------------------*\
	 |*							Attributs Private						*|
	 \*-----------------------------------------------------------------*/
	private Graphics2D g2D;
	private int w;
	private int h;
}

