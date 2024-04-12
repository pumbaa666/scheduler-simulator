// Specification:
package ch.hearc.scheduler.moo;

import java.util.ArrayList;
import java.util.List;

/**
 * Tout les algorithmes de scheduling sont implémentés ici.
 */
public class Algorithmes
{
	/*-----------------------------------------------------------------*\
	|*						Méthodes publiques						   *|
	\*-----------------------------------------------------------------*/
	public static ProcessusTrie applyFCFS(List<Processus> listeProcessus)
	{
		List<Processus> liste = clonerListe(listeProcessus);
		List<Processus> listeFin = new ArrayList<Processus>();
		
		int numProcessAAjouterDansFile = 0;
		int temps = 0;
		boolean test = false;
		List<String> historique = new ArrayList<String>();
		Processus processFini;

		while(!test)
		{
			if(numProcessAAjouterDansFile < liste.size())
			{
				if(temps == liste.get(numProcessAAjouterDansFile).getHeureArrivee())
				{
					fileAttente.add(new Processus(liste.get(numProcessAAjouterDansFile)));
					historique.add(liste.get(numProcessAAjouterDansFile).getNom()+" arrive en file d'attente à "+temps);
					historique.add(etatFile());
					numProcessAAjouterDansFile++;
				}
			}
			else
				test = true;
			if(!fileAttente.isEmpty())
			{
				test = false;
				Processus procTest = fileAttente.get(0); 
				if(procTest.getHeureArriveeReelle() == -1)
				{
					procTest.setHeureArriveeReelle(temps);
					historique.add(procTest.getNom()+" est traité la 1ère fois à  "+temps);
				}
				if(procTest.decrementer() == false)
				{
					processFini = new Processus(fileAttente.remove(0));
					processFini.setHeureFin(temps);
					processFini.setLast(true);
					historique.add(procTest.getNom()+" est achevé à  "+temps);
					historique.add(etatFile());
					listeFin.add(processFini);
				}
			}
			temps++;
		}
		return new ProcessusTrie(listeFin, historique);
	}
	
	public static ProcessusTrie applySJF(List<Processus> listeProcessus)
	{
		List<Processus> liste = clonerListe(listeProcessus);
		List<Processus> listeFin = new ArrayList<Processus>();
		
		int numProcessAAjouterDansFile = 0;
		int temps = 0;
		boolean test = false;
		boolean newDelete = false;
		List<String> historique = new ArrayList<String>();
		Processus testProcess;// = liste.get(0);//new Processus();
		Processus processFini;

		while(!test)
		{
			if(numProcessAAjouterDansFile < liste.size())
			{
				if(temps == liste.get(numProcessAAjouterDansFile).getHeureArrivee())
				{
					fileAttente.add(new Processus(liste.get(numProcessAAjouterDansFile)));
					historique.add(liste.get(numProcessAAjouterDansFile).getNom()+" arrive en file d'attente à "+temps);
					historique.add(etatFile());
					numProcessAAjouterDansFile++;
				}
			}
			else
				test = true;
			if(!fileAttente.isEmpty())
			{
				test = false;
				testProcess = fileAttente.get(0);
				trierListeDuree(fileAttente);
				if(!testProcess.isEqual(fileAttente.get(0)) || newDelete) // Si il y a une nouvelle priorité ou une reprise
				{
					if(!newDelete)
					{
						testProcess.setHeureFin(temps, -1);
						historique.add(testProcess.getNom()+" fait une pause à  "+temps);
						listeFin.add(new Processus(testProcess));
					}
					fileAttente.get(0).setHeureArriveeReelle(temps);
					historique.add(fileAttente.get(0).getNom()+" est nouvellement traité à  "+temps);
					historique.add(etatFile());
					newDelete = false;
				}
				
				Processus procTest = fileAttente.get(0);
				if(procTest.getHeureArriveeReelle() == -1)
				{
					procTest.setHeureArriveeReelle(temps);
					historique.add(procTest.getNom()+" est traité la 1ère fois à  "+temps);
				}
				if(procTest.decrementer() == false)
				{
					processFini = new Processus(fileAttente.remove(0));
					processFini.setHeureFin(temps);
					processFini.setLast(true);
					historique.add(procTest.getNom()+" est achevé à  "+temps);
					historique.add(etatFile());
					listeFin.add(processFini);
					if(fileAttente.size() != 0)
						newDelete = true;
				}
			}
			temps++;
		}
		return new ProcessusTrie(listeFin, historique);
	}

	public static ProcessusTrie applySJFNot(List<Processus> listeProcessus)
	{
		List<Processus> liste = clonerListe(listeProcessus);
		List<Processus> listeFin = new ArrayList<Processus>();
		
		int numProcessAAjouterDansFile = 0;
		int temps = 0;
		boolean test = false;
		List<String> historique = new ArrayList<String>();
		boolean etatProcessEnCours = true;
		Processus processFini;

		while(!test)
		{
			if(numProcessAAjouterDansFile < liste.size())
			{
				if(temps == liste.get(numProcessAAjouterDansFile).getHeureArrivee())
				{
					fileAttente.add(new Processus(liste.get(numProcessAAjouterDansFile)));
					historique.add(liste.get(numProcessAAjouterDansFile).getNom()+" arrive en file d'attente à "+temps);
					historique.add(etatFile());
					numProcessAAjouterDansFile++;
				}
			}
			else
				test = true;
			if(!fileAttente.isEmpty())
			{
				test = false;
				if(etatProcessEnCours)
					trierListeDuree(fileAttente);
				Processus procTest = fileAttente.get(0);
				if(procTest.getHeureArriveeReelle() == -1)
				{
					procTest.setHeureArriveeReelle(temps);
					//procTest.setPremiereExec(temps);
					historique.add(procTest.getNom()+" est traité la 1ère fois à  "+temps);
				}
				if(procTest.decrementer() == false)
				{
					processFini = new Processus(fileAttente.remove(0));
					processFini.setHeureFin(temps);
					processFini.setLast(true);
					historique.add(procTest.getNom()+" est achevé à  "+temps);
					historique.add(etatFile());
					listeFin.add(processFini);
					etatProcessEnCours = true;
				}
				else
					etatProcessEnCours = false;
			}
			temps++;
		}
		return new ProcessusTrie(listeFin, historique);
	}

	public static ProcessusTrie applyRoundRobin(List<Processus> listeProcessus, int nbQuantum)
	{
		List<Processus> liste = clonerListe(listeProcessus);
		List<Processus> listeFin = new ArrayList<Processus>();
		
		int numProcessAAjouterDansFile = 0;
		int temps = 0;
		int countQuantum = 0;
		boolean test = false;
		boolean newDelete = false;
		List<String> historique = new ArrayList<String>();
		Processus testProcess;
		Processus processFini;

		while(!test)
		{
			if(numProcessAAjouterDansFile < liste.size())
			{
				if(temps == liste.get(numProcessAAjouterDansFile).getHeureArrivee())
				{
					fileAttente.add(new Processus(liste.get(numProcessAAjouterDansFile)));
					historique.add(liste.get(numProcessAAjouterDansFile).getNom()+" arrive en file d'attente à "+temps);
					historique.add(etatFile());
					numProcessAAjouterDansFile++;
				}
			}
			else
				test = true;
			if(!fileAttente.isEmpty())
			{
				test = false;
				testProcess = fileAttente.get(0);
				if((countQuantum >= nbQuantum && fileAttente.size() > 1) || newDelete) // Si il y a une nouvelle priorité
				{
					countQuantum = 0;
					if(!newDelete)
					{
						rotateProcess();
						testProcess.setHeureFin(temps, -1);
						historique.add(testProcess.getNom()+" fait une pause à  "+temps);
						listeFin.add(new Processus(testProcess));
					}
					fileAttente.get(0).setHeureArriveeReelle(temps);
					historique.add(fileAttente.get(0).getNom()+" est nouvellement traité à  "+temps);
					historique.add(etatFile());
					newDelete = false;
				}
				
				Processus procTest = fileAttente.get(0);
				if(procTest.getHeureArriveeReelle() == -1)
				{
					procTest.setHeureArriveeReelle(temps);
					historique.add(procTest.getNom()+" est traité la 1ère fois à  "+temps);
				}
				if(procTest.decrementer() == false)
				{
					processFini = new Processus(fileAttente.remove(0));
					processFini.setHeureFin(temps);
					processFini.setLast(true);
					historique.add(procTest.getNom()+" est achevé à  "+temps);
					historique.add(etatFile());
					listeFin.add(processFini);
					if(fileAttente.size() != 0)
						newDelete = true;
				}
				countQuantum++;
			}
			temps++;
		}
		return new ProcessusTrie(listeFin, historique);
	}

	/*-----------------------------------------------------------------*\
	|*						Méthodes privées						   *|
	\*-----------------------------------------------------------------*/
	/**
	 * Décale tout les processus dans la file d'attente. Le 1er devient le dernier.
	 * Utilisé lors de l'algorithme Round Robbin
	 */
	private static void rotateProcess()
	{
		for(int i = 0; i < fileAttente.size()-1; i++)
			switchProcess(fileAttente, i, i+1);
	}
	
	private static void switchProcess(List<Processus> listeProcessus, int index1, int index2)
	{
		Processus temp = listeProcessus.get(index1);
		listeProcessus.set(index1, listeProcessus.get(index2));
		listeProcessus.set(index2, temp);
	}
	
	/**
	 * Renvoie une String de l'état de la file d'attente
	 * Canevas : Nom1[Temps_restant1], Nom2[Temps_restant2], ... 
	 * Exemple : P1[4], P5[2], P8[1]
	 */
	private static String etatFile()
	{
		String historique = new String("File :");
		for(Processus pFile:fileAttente)
			historique = historique.concat(pFile.getNom()+"["+pFile.getTempsRestant()+"]"+", ");
		if(fileAttente.size() != 0 && historique.length() > 0)
			historique = historique.substring(0, historique.length()-2); // -2 car ne prend pas les dernier ", "
		return historique;
	}
	
	/**
	 * Trie la liste donnée en paramètre par heure d'arrivée. De la 1ère à la dernière
	 */
	private static void trierListeArrivee(List<Processus> liste)
	{
		int longueur = liste.size();
		boolean test;
		int i, j;
		do
		{
			test = false;
			for(i = 0; i < longueur - 1; i++)
			{
				j = i+1;
				if (liste.get(i).getHeureArrivee() > liste.get(j).getHeureArrivee())
				{
					echanger(liste, i, j);
					test = true;
				}
			}
			longueur--;
		}
		while(test);
	}
	
	/**
	 * Trie la liste donnée en paramètre par durée. De la plus courte à la plus longue
	 */
	private static void trierListeDuree(List<Processus> liste)
	{
		int longueur = liste.size();
		boolean test;
		int i, j;
		do
		{
			test = false;
			for(i = 0; i < longueur - 1; i++)
			{
				j = i+1;
				if(liste.get(i).getTempsRestant() > liste.get(j).getTempsRestant())
				{
					echanger(liste, i, j);
					test = true;
				}
			}
			longueur--;
		}
		while(test);
	}
	
	private static void viderFileAttente()
	{
		fileAttente.clear();
	}
	
	/**
	 * Retourne un clone de la liste donnée en paramètre.
	 * Vide aussi la file d'attente. Ceci est fait ici car la méthode clonerListe est toujours appelée
	 * en début d'algorithme.
	 */
	private static List<Processus> clonerListe(List<Processus> listeInit)
	{
		List<Processus> listeFinale = new ArrayList<Processus>(listeInit);
		trierListeArrivee(listeFinale);
		viderFileAttente();
		return listeFinale;
	}
	
	/**
	 * Echange 2 valeurs (indicées par i et j) dans une liste (listeProcessus).
	 */
	private static void echanger(List<Processus> listeProcessus, int i, int j)
	{
		Processus temp = listeProcessus.get(i);
		listeProcessus.set(i, listeProcessus.get(j));
		listeProcessus.set(j, temp);
	}
	
	/*-----------------------------------------------------------------*\
	|*						Attributs privés						   *|
	\*-----------------------------------------------------------------*/
	/*-----------------------------------------*\
	|*				Statiques				   *|
	\*-----------------------------------------*/
	private static List<Processus> fileAttente = new ArrayList<Processus>();
}
