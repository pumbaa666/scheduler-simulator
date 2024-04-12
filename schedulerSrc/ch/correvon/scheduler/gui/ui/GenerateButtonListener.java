
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
 * Cet �couteur est instanci� dans PanelUI
 * G�n�re le nombre de processus demand� par l'utilisateur
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
		if(e.getKeyCode() == 10) // [Enter]
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
		int incomingTimeMax = 0;
		int durationMax = 0;
		boolean ok = true;
		PanelUI panelUI = this.manager.getPanelUI();
		JTextField[] jTextFields = panelUI.getGenerateTxtField();
		
		// R�cup�ration des donn�es entr�es par l'utilisateur
		JTextField txtGenerateIncoming = jTextFields[0];
		JTextField txtGenerateDuration = jTextFields[1];
		JTextField txtGenerateNumber = jTextFields[2];
		
		// Test si toutes les donn�es sont conforme
		// Entrer des lettres ou des valeurs en dehors de la plage autoris�e provoque une erreur et ne g�n�re rien
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
			JOptionPane.showMessageDialog(null, "Heure d'arriv�e incorrecte!\nEntrez une valeur supp�rieure � 0", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && durationMax < 1)
		{
			JOptionPane.showMessageDialog(null, "Dur�e incorrecte!\nEntrez une valeur supp�rieure � 1", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && nbIteration < 1)
		{
			JOptionPane.showMessageDialog(null, "Nombre d'it�ration incorrect!\nEntrez une valeur supp�rieure � 1", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && nbIteration > incomingTimeMax)
		{
			JOptionPane.showMessageDialog(null, "Nombres incorrects!\nLe nombre de processus demand� doit �tre plus petit que l'heure d'arriv�e maximale", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		if(ok && this.processType.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Il n'existe aucun type.\nAllez en cr�er dans les pr�f�rances", "Erreur", JOptionPane.OK_OPTION);
			ok = false;
		}
		
		// G�n�ration
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

			// Cette boucle assure que 2 processus n'arriveront jamais en m�me temps et que l'heure d'arriv�e ne d�passera pas l'heure demand�e par l'utilisateur
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
			
			// Si il faut vider la liste avant d'en g�n�rer des nouveau, c'est simple
			if(panelUI.getCheckboxClearValue())
				panelUI.removeAllProcess();
			
			// Sinon, il faut : 
			else
			{
				// V�rifier qu'il reste assez de place pour contenir tout les nouveaux processus
				ArrayList<ArrayList<?>> verify = verifySpace(nbIteration, incomingTimeMax);
				if(verify != null)
				{
					ArrayList<?> usedTime = verify.get(0);
					ArrayList<?> usedName = verify.get(1);
					
					// Cr�er une nouvelle liste d'heure d'arriv�e
					listeEndingTime.clear();

					for(int i = 0; i < incomingTimeMax; i++)
						listeEndingTime.add(i);
					Collections.shuffle(listeEndingTime);
					
					// Et supprimer les heures d'arriv�e d�j� prises par les processus qui �taient l� auparavant 
					listeEndingTime.removeAll(usedTime);
					if(listeEndingTime.size() < nbIteration)
					{
						JOptionPane.showMessageDialog(null, "Impossible de g�n�rer autant de processus avec cet heure d'arriv�e max et ces processus d�j� en liste", "Erreur", JOptionPane.OK_OPTION);
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
					JOptionPane.showMessageDialog(null, "Impossible de g�n�rer autant de processus avec cet heure d'arriv�e max et ces processus d�j� en liste", "Erreur", JOptionPane.OK_OPTION);
					ok = false;
				}
			}

			if(ok)
			{
				int name = 0;
				if(panelUI.getValueCheckboxShuffle())
					Collections.shuffle(listeEndingTime);
				ProcessType type = panelUI.getComboGenerateType();
				
				if(type.toString() == "Al�atoire")
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
	|*							M�thodes priv�es					   *|
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
	|*							Attributs priv�s					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
	private ArrayList<ProcessType> processType;
}