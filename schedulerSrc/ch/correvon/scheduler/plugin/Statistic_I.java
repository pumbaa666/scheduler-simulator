package ch.correvon.scheduler.plugin;

import java.util.ArrayList;

import ch.correvon.scheduler.moo.process.Process;

public interface Statistic_I extends PluginInterface_I
{
	/**
	 * Calcul une statistique sur une liste de processus (listProcess)
	 * Attention ! Prenez en compte ou non les processus non-finis (détéctables par la méthodes public boolean isLast())
	 */
	public double calcul(ArrayList<Process> listProcess);
	public String getName();
}
