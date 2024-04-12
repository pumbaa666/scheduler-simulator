
//Specification:
package ch.correvon.scheduler.plugin;

import java.util.ArrayList;

import ch.correvon.scheduler.moo.process.Process;


public interface Algo_I extends PluginInterface_I
{
	public Process runAlgorithm(ArrayList<Process> listProcess, int quantum);
	
	/**
	 * ATTENTION, il faut que getName() appel toString() pour que le passage d'un algorithme à une JList affiche le bon nom (l'affichage se fait grâce à la méthode toString de l'algorithme 
	 */
	public String getName();
	public Algo_I cloneOf();
	public boolean isPreemptif();
}
