//Specification:
package ch.hearc.scheduler.gui.tools;

import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Impl�mente une m�thode qui permet d'�crire une String centr�e sur le point donn�.
 * D'autres m�thodes de dessins pourraient y �tre ajout�es.  
 */
public abstract class PanelCardsGraphics extends JPanel
{
	/*------------------------------------------------------------------------------*\
	|*								M�thodes publiques								*|
	\*------------------------------------------------------------------------------*/
	public void drawCenteredString(Graphics g, String string, int x, int y)
	{
		FontMetrics fontMetrics = g.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth(string);
		int stringHeight = fontMetrics.getHeight();
		g.drawString(string, (x - stringWidth / 2), (y + stringHeight / 3));
	}
}