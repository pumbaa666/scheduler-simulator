
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
 * Génère le nombre de procecuss demandé par l'utilisateur
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
	|*							Méthodes publiques					   *|
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
	|*							Méthodes privées					   *|
	\*-----------------------------------------------------------------*/
	private void action()
	{
		Random rand = new Random();
		int nbIteration = 0;
		int arriveeMax = 0;
		int dureeMax = 0;
		boolean ok = true;
		
		/* Récupération des données entrées par l'utilisateur */
		JTextField txtGenArrivee = this.manager.getPanneauInterface().getTxtFieldGenerer()[0];
		JTextField txtGenDuree = this.manager.getPanneauInterface().getTxtFieldGenerer()[1];
		JTextField txtGenNombre = this.manager.getPanneauInterface().getTxtFieldGenerer()[2];
		
		/* Test si toutes les données sont conforme */
		/* Entrer des lettres ou des valeurs en dehors de la plage autorisée provoque une erreur et ne génère rien */
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
			JOptionPane.showMessageDialog(null, "Nombres incorrects!\nEntrez des valeurs suppérieures à 0", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && nbIteration > arriveeMax)
		{
			JOptionPane.showMessageDialog(null, "Nombres incorrects!\nLe nombre de processus demandé doit être plus petit que l'heure d'arrivée maximale", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && nbIteration > Options.NB_PROC_MAX)
		{
			JOptionPane.showMessageDialog(null, "Nombres incorrects!\nLe nombre de processus demandé ne doit pas être suppérieur à "+Options.NB_PROC_MAX, "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		/* Génération */
		if(ok)
		{
			List<Integer> listeHeureArrivee = new ArrayList<Integer>();
			List<String> listeNom = new ArrayList<String>();
			int num = 1;

			/* Cette boucle assure que 2 processus n'arriveront jamais en même temps
			   et que l'heure d'arrivée ne dépassera pas l'heure demandée par l'utilisateur */
			for(int i = 0; i < nbIteration; i++)
			{
				listeHeureArrivee.add(new Integer(num));
				num += Math.abs(rand.nextInt())%(arriveeMax/nbIteration)+1;
				listeNom.add("P"+i);
			}
			
			for(int i = 0; i < 3*nbIteration; i++)
				listeNom.add(new String("P"+i));
			
			/* Si il faut vider la liste avant d'en générer des nouveau, c'est simple */
			if(this.manager.getPanneauInterface().getValueCheckVider())
				this.manager.removeAllProcessus();
			
			/* Sinon, il faut :  */
			else
			{
				/* Vérifier qu'il reste assez de place pour contenir tout les nouveaux processus */
				List<List<?>> retour = verifierPlaceDispo(nbIteration, arriveeMax);
				if(retour != null)
				{
					List<?> listeHeureIndispo = retour.get(0);
					List<?> listeNomIndispo = retour.get(1);
					/* Créer une nouvelle liste d'heure d'arrivée */
					listeHeureArrivee.clear();
					for(int i = 0; i < dureeMax; i++)
					{
						listeHeureArrivee.add(i);
					}
					
					/* Et supprimer les heures d'arrivée déjà prises par les processus qui étaient là auparavant */
					listeHeureArrivee.removeAll(listeHeureIndispo);
					/* Ainsi que les noms */
					listeNom.removeAll(listeNomIndispo);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Impossible de générer autant de processus avec cet heure d'arrivée max et ces processus déjà en liste", "Erreur", JOptionPane.OK_OPTION);
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
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
}

