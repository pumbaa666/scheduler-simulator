
//Specification:
package ch.correvon.scheduler.gui.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.Tools;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.ProcessType;

/**
 * Cet écouteur est instancié dans PanelUI
 * Génère le nombre de processus demandé par l'utilisateur
 */
public class GenerateButtonListener implements KeyListener, ActionListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeur					   	   *|
	\*-----------------------------------------------------------------*/
	public GenerateButtonListener(Manager manager)
	{
		this.manager = manager;
		this.processType = this.manager.getProcessType();
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
		if(e.getKeyCode() == 10) // [Enter]
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
		int incomingTimeMax = 0;
		int durationMax = 0;
		boolean ok = true;
		PanelUI panelUI = this.manager.getPanelUI();
		JTextField[] jTextFields = panelUI.getGenerateTxtField();
		
		// Récupération des données entrées par l'utilisateur
		JTextField txtGenerateIncoming = jTextFields[0];
		JTextField txtGenerateDuration = jTextFields[1];
		JTextField txtGenerateNumber = jTextFields[2];
		
		// Test si toutes les données sont conforme
		// Entrer des lettres ou des valeurs en dehors de la plage autorisée provoque une erreur et ne génère rien
		try
		{
			nbIteration = Integer.parseInt(txtGenerateNumber.getText());
			incomingTimeMax = Integer.parseInt(txtGenerateIncoming.getText());
			durationMax = Integer.parseInt(txtGenerateDuration.getText());
		}
		catch (NumberFormatException e1)
		{
			JOptionPane.showMessageDialog(null, "Veuillez entrer des chiffres", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && incomingTimeMax < 0)
		{
			JOptionPane.showMessageDialog(null, "Heure d'arrivée incorrecte!\nEntrez une valeur suppérieure à 0", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && durationMax < 1)
		{
			JOptionPane.showMessageDialog(null, "Durée incorrecte!\nEntrez une valeur suppérieure à 1", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && nbIteration < 1)
		{
			JOptionPane.showMessageDialog(null, "Nombre d'itération incorrect!\nEntrez une valeur suppérieure à 1", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && nbIteration > incomingTimeMax)
		{
			JOptionPane.showMessageDialog(null, "Nombres incorrects!\nLe nombre de processus demandé doit être plus petit que l'heure d'arrivée maximale", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && this.processType.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Il n'existe aucun type.\nAllez en créer dans les préférances", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		// Génération
		if(ok)
		{
			ArrayList<Integer> listeEndingTime = new ArrayList<Integer>(nbIteration);
			ArrayList<String> listName = new ArrayList<String>(nbIteration);
			int nb = 1;
			String defalutProcessName = this.manager.getPreferances("txtDefaultProcessName");
			if(defalutProcessName.compareTo("NaN") == 0)
				defalutProcessName = "P";
			String zero = "";
			int lastLog = 0;
			if(this.manager.getPreferances("checkEnableMultiZero").compareTo("true") == 0);
			{
				int nbZero = (int)Math.log10(nbIteration-1);
				for(int z = 0; z < nbZero; z++)
					zero = zero+"0";
			}

			// Cette boucle assure que 2 processus n'arriveront jamais en même temps et que l'heure d'arrivée ne dépassera pas l'heure demandée par l'utilisateur
			for(int i = 0; i < nbIteration; i++)
			{
				if((int)Math.log10(i) != lastLog && zero.length() > 0 && i != 0)
				{
					lastLog = (int)Math.log10(i);
					zero = zero.substring(1);
				}
				listeEndingTime.add(new Integer(nb));
				nb += Math.abs(rand.nextInt())%(incomingTimeMax/nbIteration)+1;
				listName.add(defalutProcessName+zero+i);
			}
			
			// Si il faut vider la liste avant d'en générer des nouveau, c'est simple
			if(panelUI.getCheckboxClearValue())
				panelUI.removeAllProcess();
			
			// Sinon, il faut : 
			else
			{
				// Vérifier qu'il reste assez de place pour contenir tout les nouveaux processus
				ArrayList<ArrayList<?>> verify = verifySpace(nbIteration, incomingTimeMax);
				if(verify != null)
				{
					ArrayList<?> usedTime = verify.get(0);
					ArrayList<?> usedName = verify.get(1);
					
					// Créer une nouvelle liste d'heure d'arrivée
					listeEndingTime.clear();

					for(int i = 0; i < incomingTimeMax; i++)
						listeEndingTime.add(i);
					Collections.shuffle(listeEndingTime);
					
					// Et supprimer les heures d'arrivée déjà prises par les processus qui étaient là auparavant 
					listeEndingTime.removeAll(usedTime);
					if(listeEndingTime.size() < nbIteration)
					{
						JOptionPane.showMessageDialog(null, "Impossible de générer autant de processus avec cet heure d'arrivée max et ces processus déjà en liste", "Erreur", JOptionPane.OK_OPTION);
						ok = false;
					}
					
					// Ainsi que les noms
					listName.removeAll(usedName);
					while(listName.size() < nbIteration)
					{
						int start = Tools.searchNumber((String)usedName.get(usedName.size()-1)) + 1;
						for(int i = start; i < start+2*nbIteration; i++)
							listName.add(new String(defalutProcessName+i));
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Impossible de générer autant de processus avec cet heure d'arrivée max et ces processus déjà en liste", "Erreur", JOptionPane.OK_OPTION);
					ok = false;
				}
			}

			if(ok)
			{
				int name = 0;
				if(panelUI.getValueCheckboxShuffle())
					Collections.shuffle(listeEndingTime);
				ProcessType type = panelUI.getComboGenerateType();
				
				if(type.toString() == "Aléatoire")
					for(int i = 0; i < nbIteration; i++)
					{
						panelUI.addProcess(listName.get(i), Integer.parseInt(listeEndingTime.get(i).toString()), Math.abs(rand.nextInt())%durationMax+1, getRandomTypeProcess());
				    	name++;
					}
				else
					for(int i = 0; i < nbIteration; i++)
					{
						panelUI.addProcess(listName.get(i), Integer.parseInt(listeEndingTime.get(i).toString()), Math.abs(rand.nextInt())%durationMax+1, type);
				    	name++;
					}
				
				if(this.manager.getPreferances("checkAutoValidFileRead").compareTo("true") == 0)
					this.manager.getPanelUI().valideInput();
			}
		}
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes privées					   *|
	\*-----------------------------------------------------------------*/
	private ProcessType getRandomTypeProcess()
	{
		return this.processType.get(Math.abs(new Random().nextInt())%this.processType.size());
	}
	
	private ArrayList<ArrayList<?>> verifySpace(int nbIteration, int endingTimeMax)
	{
		ArrayList<ArrayList<?>> result = new ArrayList<ArrayList<?>>(2);
		ArrayList<Process> listProcess = this.manager.getListProcess();
		int size = listProcess.size();
		ArrayList<Integer> usedTime = new ArrayList<Integer>(size);
		ArrayList<String> usedName = new ArrayList<String>(size);
		int i = 0;
		for(Process process:listProcess)
		{
			if(process.getIncomingTime() < endingTimeMax)
			{
				i++;
				usedTime.add(new Integer(process.getIncomingTime()));
			}
			usedName.add(process.getName());
		}
		int space = endingTimeMax-i;
		if(space < nbIteration)
			result = null;
		else
		{
			result.add(usedTime);
			result.add(usedName);
		}
		return result;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
	private ArrayList<ProcessType> processType;
}