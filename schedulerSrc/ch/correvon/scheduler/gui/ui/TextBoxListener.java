
//Specification:
package ch.correvon.scheduler.gui.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import ch.correvon.scheduler.moo.Tools;

/**
 * Ecouteur instancié dans PanelUI et PanelOptions
 * Lorsqu'une textBox écoutée par cette écouteur recoit le focus (un click dans la textBox, un TAB ou autre chose...)
 * il sélectionne tout le texte qui y est écrit.
 * Ceci dans le but de simplifier l'entrée de nombreux processus à la suite.
 */
public class TextBoxListener implements FocusListener
{
	public void focusGained(FocusEvent e)
	{
		Tools.selectAllText((JTextField)e.getSource());
	}
	
	public void focusLost(FocusEvent e)
	{
	}
}

