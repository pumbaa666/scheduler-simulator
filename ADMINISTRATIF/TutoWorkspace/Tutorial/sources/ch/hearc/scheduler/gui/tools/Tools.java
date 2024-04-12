
//Specification:
package ch.hearc.scheduler.gui.tools;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ch.hearc.scheduler.moo.Processus;

/**
 * Différents outils utilisés réguliérement dans ce projet
 */
public class Tools
{
	/**
	 * Sélectionne une partie du texte d'une textBox
	 */
	public static JTextField selectText(JTextField txt, int start, int end)
	{
		txt.setSelectionStart(start);
		txt.setSelectionEnd(end);
		return txt;
	}
	
	/**
	 * Sélectionne tout le texte d'une textBox
	 */
	public static JTextField selectAllText(JTextField txt)
	{
		return selectText(txt, 0, txt.getText().length());
	}
	
	/**
	 * Renvoie true si il est possible d'ajouter le processus *process* à la liste *listeExistant*
	 * Renvoie false si il y a un conflit (nom ou heure d'arrivée déjà existants)
	 */
	public static boolean verifierExistance(Processus process, List<Processus> listeExistant)
	{
		int arrivee = process.getHeureArrivee();
		String nom = process.getNom();
		boolean ok = true;
		for(Processus p:listeExistant)
		{
			if(p.getNom().compareTo(nom) == 0)
			{
				JOptionPane.showMessageDialog(null, "Impossible d'ajouter le processus.\nIl y a un conflit dans le nom", "Erreur", JOptionPane.OK_OPTION);
				ok = false;
				break;
			}
			else if(p.getHeureArrivee() == arrivee)
			{
				JOptionPane.showMessageDialog(null, "Impossible d'ajouter le processus.\nIl y a un conflit dans l'heure d'arrivée", "Erreur", JOptionPane.OK_OPTION);
				ok = false;
				break;
			}
		}
		return ok;
	}
}

