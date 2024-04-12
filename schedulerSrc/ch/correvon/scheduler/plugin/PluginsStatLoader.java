
//Specification:
package ch.correvon.scheduler.plugin;

import java.util.ArrayList;

public class PluginsStatLoader extends PluginsLoader
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public PluginsStatLoader(String path)
	{
		super("Statistic_I", path);
	}

	/*-----------------------------------------------------------------*\
	|*						Méthodes publiques						   *|
	\*-----------------------------------------------------------------*/

	/**
	 * Fonction de chargement de tout les plugins de type Stats_I
	 * @return Une collection de Stats_I contenant les instances des plugins
	 * @throws Exception si file = null ou file.length = 0
	 */
	@Override public Statistic_I[] loadAllInterface_I() throws Exception
	{
		super.initializeLoader();
		ArrayList<Statistic_I> listStat = new ArrayList<Statistic_I>();
		for(int index = 0 ; index < super.getClassInterface().size(); index ++ )
		{
			try
			{
				listStat.add((Statistic_I)((Class)super.getClassInterface().get(index)).newInstance());
			}
			catch(ClassCastException e)
			{
				System.err.println("Impossible de charger la statistique "+((Class)super.getClassInterface().get(index)).newInstance()+" il ne respecte pas l'interface Statistic_I");
				e.printStackTrace();
			}
		}
			
		int i = 0;
		Statistic_I[] tmpPlugins = new Statistic_I[listStat.size()];
		for(Statistic_I stat:listStat)
		{
			tmpPlugins[i] = stat;
			i++;
		}
		
		return tmpPlugins;
	}
	
}