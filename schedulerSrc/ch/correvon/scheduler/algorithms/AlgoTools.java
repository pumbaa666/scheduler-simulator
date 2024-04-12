
//Specification:
package ch.correvon.scheduler.algorithms;

import java.util.ArrayList;

import ch.correvon.scheduler.moo.process.Process;


public class AlgoTools
{
	/**
	 * Décale tout les processus dans la file d'attente. Le 1er devient le dernier.
	 */
	public static void rotateProcess(ArrayList<Process> queue)
	{
		for(int i = 0; i < queue.size()-1; i++)
			switchProcess(queue, i, i+1);
	}
	
	/**
	 * Echange les processus indicés "index1" et "index2" dans la liste listProcess
	 */
	public static void switchProcess(ArrayList<Process> listProcess, int index1, int index2)
	{
		Process temp = listProcess.get(index1);
		listProcess.set(index1, listProcess.get(index2));
		listProcess.set(index2, temp);
	}
	
	/**
	 * Renvoie une String de l'état de la file d'attente
	 * Canevas : Nom1[Temps_restant1], Nom2[Temps_restant2], ... 
	 * Exemple : P1[4], P5[2], P8[1]
	 */
	public static String queueState(ArrayList<Process> queue)
	{
		String history = new String("Etat de la file : ");
		for(Process process:queue)
			history = history.concat(process.getNameTypeTime()+"; ");
		if(queue.size() != 0 && history.length() > 0)
			history = history.substring(0, history.length()-2); // -2 car ne prend pas les dernier "; "
		else
			history = history.concat("vide !");
		return history;
	}
	
	/**
	 * Trie la liste donnée en paramètre par heure d'arrivée. De la plus petite (ancienne) à la plus grande (récente).
	 */
	public static void sortListByIncomingTime(ArrayList<Process> list)
	{
		int listSize = list.size();
		boolean test;
		int i, j;
		do
		{
			test = false;
			for(i = 0; i < listSize - 1; i++)
			{
				j = i+1;
				if(list.get(i).getIncomingTime() > list.get(j).getIncomingTime())
				{
					switchProcess(list, i, j);
					test = true;
				}
			}
			listSize--;
		}
		while(test);
	}
	
	/**
	 * Trie la liste donnée en paramètre par heure d'arrivée réelle. De la plus petite (ancienne) à la plus grande (récente).
	 */
	public static void sortListByRealIncomingTime(ArrayList<Process> list)
	{
		int listSize = list.size();
		boolean test;
		int i, j;
		do
		{
			test = false;
			for(i = 0; i < listSize - 1; i++)
			{
				j = i+1;
				if(list.get(i).getRealIncomingTime() > list.get(j).getRealIncomingTime())
				{
					switchProcess(list, i, j);
					test = true;
				}
			}
			listSize--;
		}
		while(test);
	}
	
	/**
	 * Trie la liste donnée en paramètre par durée. De la plus petite (courte) à la plus grande (longue)
	 */
	public static void sortListByDuration(ArrayList<Process> list)
	{
		int listeSize = list.size();
		boolean test;
		int i, j;
		do
		{
			test = false;
			for(i = 0; i < listeSize - 1; i++)
			{
				j = i+1;
				if(list.get(i).getRemainingTime() > list.get(j).getRemainingTime())
				{
					switchProcess(list, i, j);
					test = true;
				}
			}
			listeSize--;
		}
		while(test);
	}
}

