
//Specification:
package ch.correvon.scheduler.plugin;

import java.util.ArrayList;

public class PluginsAlgoLoader extends PluginsLoader
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public PluginsAlgoLoader(String path)
	{
		super("Algo_I", path);
	}

	/*-----------------------------------------------------------------*\
	|*						Méthodes publiques						   *|
	\*-----------------------------------------------------------------*/
	/**
	 * Fonction de chargement de tout les plugins de type Algo_I
	 * @return Une collection de Algo_I contenant les instances des plugins
	 * @throws Exception si file = null ou file.length = 0
	 */
	@Override public Algo_I[] loadAllInterface_I() throws Exception
	{
		super.initializeLoader();
		ArrayList<Algo_I> listAlgo = new ArrayList<Algo_I>();
		for(int index = 0 ; index < super.getClassInterface().size(); index ++ )
		{
			try
			{
				listAlgo.add((Algo_I)((Class)super.getClassInterface().get(index)).newInstance());
			}
			catch(ClassCastException e)
			{
				System.err.println("Impossible de charger l'algorithme "+((Class)super.getClassInterface().get(index)).newInstance()+" il ne respecte pas l'interface Algo_I");
				e.printStackTrace();
			}
		}

		int i = 0;
		Algo_I[] tmpPlugins = new Algo_I[listAlgo.size()];
		for(Algo_I algo:listAlgo)
		{
			tmpPlugins[i] = algo;
			i++;
		}

		return tmpPlugins;
	}
}