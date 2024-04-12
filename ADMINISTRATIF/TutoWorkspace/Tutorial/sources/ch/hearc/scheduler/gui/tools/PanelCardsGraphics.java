//Specification:
package ch.hearc.scheduler.gui.tools;

import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Implémente une méthode qui permet d'écrire une String centrée sur le point donné.
 * D'autres méthodes de dessins pourraient y être ajoutées.  
 */
public abstract class PanelCardsGraphics extends JPanel
{
	/*------------------------------------------------------------------------------*\
	|*								Méthodes publiques								*|
	\*------------------------------------------------------------------------------*/
	public void drawCenteredString(Graphics g, String string, int x, int y)
	{
		FontMetrics fontMetrics = g.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth(string);
		int stringHeight = fontMetrics.getHeight();
		g.drawString(string, (x - stringWidth / 2), (y + stringHeight / 3));
	}
}