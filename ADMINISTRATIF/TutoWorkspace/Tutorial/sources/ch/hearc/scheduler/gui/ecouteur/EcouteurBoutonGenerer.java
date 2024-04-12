
//Specification:
package ch.hearc.scheduler.gui.ecouteur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ch.hearc.scheduler.moo.Manager;
import ch.hearc.scheduler.moo.Options;
import ch.hearc.scheduler.moo.Processus;

/**
 * G�n�re le nombre de procecuss demand� par l'utilisateur
 */
public class EcouteurBoutonGenerer implements KeyListener, ActionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/

	public EcouteurBoutonGenerer(Manager manager)
	{
		this.manager = manager;
	}
	
	/*-----------------------------------------------------------------*\
	|*							M�thodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void actionPerformed(ActionEvent e)
	{
		action();
	}
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == 10) // Enter
			action();
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
	
	/*-----------------------------------------------------------------*\
	|*							M�thodes priv�es					   *|
	\*-----------------------------------------------------------------*/
	private void action()
	{
		Random rand = new Random();
		int nbIteration = 0;
		int arriveeMax = 0;
		int dureeMax = 0;
		boolean ok = true;
		
		/* R�cup�ration des donn�es entr�es par l'utilisateur */
		JTextField txtGenArrivee = this.manager.getPanneauInterface().getTxtFieldGenerer()[0];
		JTextField txtGenDuree = this.manager.getPanneauInterface().getTxtFieldGenerer()[1];
		JTextField txtGenNombre = this.manager.getPanneauInterface().getTxtFieldGenerer()[2];
		
		/* Test si toutes les donn�es sont conforme */
		/* Entrer des lettres ou des valeurs en dehors de la plage autoris�e provoque une erreur et ne g�n�re rien */
		try
		{
			nbIteration = Integer.parseInt(txtGenNombre.getText());
			arriveeMax = Integer.parseInt(txtGenArrivee.getText());
			dureeMax = Integer.parseInt(txtGenDuree.getText());
		}
		catch (NumberFormatException e1)
		{
			JOptionPane.showMessageDialog(null, "Veuillez entrer des chiffres", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && (nbIteration <= 0 || arriveeMax <= 0 || dureeMax <= 0))
		{
			JOptionPane.showMessageDialog(null, "Nombres incorrects!\nEntrez des valeurs supp�rieures � 0", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && nbIteration > arriveeMax)
		{
			JOptionPane.showMessageDialog(null, "Nombres incorrects!\nLe nombre de processus demand� doit �tre plus petit que l'heure d'arriv�e maximale", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && nbIteration > Options.NB_PROC_MAX)
		{
			JOptionPane.showMessageDialog(null, "Nombres incorrects!\nLe nombre de processus demand� ne doit pas �tre supp�rieur � "+Options.NB_PROC_MAX, "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		/* G�n�ration */
		if(ok)
		{
			List<Integer> listeHeureArrivee = new ArrayList<Integer>();
			List<String> listeNom = new ArrayList<String>();
			int num = 1;

			/* Cette boucle assure que 2 processus n'arriveront jamais en m�me temps
			   et que l'heure d'arriv�e ne d�passera pas l'heure demand�e par l'utilisateur */
			for(int i = 0; i < nbIteration; i++)
			{
				listeHeureArrivee.add(new Integer(num));
				num += Math.abs(rand.nextInt())%(arriveeMax/nbIteration)+1;
				listeNom.add("P"+i);
			}
			
			for(int i = 0; i < 3*nbIteration; i++)
				listeNom.add(new String("P"+i));
			
			/* Si il faut vider la liste avant d'en g�n�rer des nouveau, c'est simple */
			if(this.manager.getPanneauInterface().getValueCheckVider())
				this.manager.removeAllProcessus();
			
			/* Sinon, il faut :  */
			else
			{
				/* V�rifier qu'il reste assez de place pour contenir tout les nouveaux processus */
				List<List<?>> retour = verifierPlaceDispo(nbIteration, arriveeMax);
				if(retour != null)
				{
					List<?> listeHeureIndispo = retour.get(0);
					List<?> listeNomIndispo = retour.get(1);
					/* Cr�er une nouvelle liste d'heure d'arriv�e */
					listeHeureArrivee.clear();
					for(int i = 0; i < dureeMax; i++)
					{
						listeHeureArrivee.add(i);
					}
					
					/* Et supprimer les heures d'arriv�e d�j� prises par les processus qui �taient l� auparavant */
					listeHeureArrivee.removeAll(listeHeureIndispo);
					/* Ainsi que les noms */
					listeNom.removeAll(listeNomIndispo);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Impossible de g�n�rer autant de processus avec cet heure d'arriv�e max et ces processus d�j� en liste", "Erreur", JOptionPane.OK_OPTION);
					ok = false;
				}
			}
			
			if(ok)
			{
				int nom = 0;
				if(this.manager.getPanneauInterface().getValueCheckShuffle())
					Collections.shuffle(listeHeureArrivee);
				for(int i = 0; i < nbIteration; i++)
				{
			    	this.manager.addProcessus(listeNom.get(i), Integer.parseInt(listeHeureArrivee.get(i).toString()), Math.abs(rand.nextInt())%dureeMax+1);
			    	nom++;
				}
			}
		}

	}
	
	private List<List<?>> verifierPlaceDispo(int nbIteration, int arriveeMax)
	{
		List<List<?>> retour = new ArrayList<List<?>>();
		List<Integer> listeHeureIndispo = new ArrayList<Integer>();
		List<String> listeNomIndispo = new ArrayList<String>();
		int i = 0;
		for(Processus p:this.manager.getListeProcessus())
		{
			if(p.getHeureArrivee() < arriveeMax)
			{
				i++;
				listeHeureIndispo.add(new Integer(p.getHeureArrivee()));
			}
			listeNomIndispo.add(p.getNom());
		}
		int placeRestante = arriveeMax-i;
		if(placeRestante < nbIteration)
			retour = null;
		else
		{
			retour.add(listeHeureIndispo);
			retour.add(listeNomIndispo);
		}
		return retour;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs Priv�s					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
}

