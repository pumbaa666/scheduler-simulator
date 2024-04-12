
//Specification:
package ch.hearc.scheduler.gui.ecouteur;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import ch.hearc.scheduler.gui.tools.Tools;

/**
 * Ecouteur instanci� dans PanneauInterface.
 * Lorsqu'une textBox �cout�e par cette �couteur recoit le focus (un click dans la textBox, un TAB ou autre chose...)
 * il s�lectionne tout le texte qui y est �crit.
 * Ceci dans le but de simplifier l'entr�e de nombreux processus � la suite.
 */
public class EcouteurTextBox implements FocusListener
{
	public void focusGained(FocusEvent e)
	{
		Tools.selectAllText((JTextField)e.getSource());
	}
	
	public void focusLost(FocusEvent e)
	{
	}
}

