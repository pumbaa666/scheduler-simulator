
//Specification:
package ch.hearc.scheduler.moo;

import java.util.ArrayList;
import java.util.List;

/**
 * Contient une liste de processus (trié selon un algorithme) et l'historique correspondant.
 */
public class ProcessusTrie
{
	/*----------------------------------------------------------------*\
	|*							Constructeurs						  *|
	\*----------------------------------------------------------------*/

	ProcessusTrie(List<Processus> listeProcessusTrie, List<String> historique)
	{
		this.listeProcessusTrie = listeProcessusTrie;
		this.historique = historique;
	}
	
	ProcessusTrie()
	{
		this(new ArrayList<Processus>(), new ArrayList<String>());
	}
	
	/*----------------------------------------------------------------*\
	|*						Methodes publiques						  *|
	\*----------------------------------------------------------------*/
	/*--------------------------------*\
	|*				Get				  *|
	\*--------------------------------*/
	public List<Processus> getListeProcessusTrie()
	{
		return this.listeProcessusTrie;
	}
	
	public List<String> getHistorique()
	{
		return this.historique;
	}
	
	/*----------------------------------------------------------------*\
	|*							Attributs Privés					  *|
	\*----------------------------------------------------------------*/
	private List<Processus> listeProcessusTrie;
	private List<String> historique;
}

