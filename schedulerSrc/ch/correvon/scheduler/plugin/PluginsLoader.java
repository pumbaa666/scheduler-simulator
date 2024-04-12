
//Specification:
package ch.correvon.scheduler.plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;


/**
 * Classe gérant le chargement et la validation des plugins.
 * Est concrétisée par PluginsAlgoLoader et PluginsStatLoader.
 */
public abstract class PluginsLoader
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public PluginsLoader(String name, String path)
	{
		this.name = name;
		this.path = path;
		this.classInterface_I = new ArrayList();
		this.files = new ArrayList<String>();
	}

	/*-----------------------------------------------------------------*\
	|*						Méthodes publiques						   *|
	\*-----------------------------------------------------------------*/
	public void addFiles(String file)
	{
		this.files.add(file);
	}
	
	/**
	 * Fonction de chargement de tout les plugins de type Algo_I
	 * @return Une collection de Interface_I contenant les instances des plugins
	 * @throws Exception si file == null ou file.length == 0
	 */
	public abstract PluginInterface_I[] loadAllInterface_I() throws Exception;

	
	/*-----------------------------------------------------------------*\
	|*						Méthodes privées						   *|
	\*-----------------------------------------------------------------*/
	public void initializeLoader() throws Exception
	{
		//On vérifie que la liste des plugins à charger à été initialisé

		if(this.files == null || this.files.size() == 0 )
			throw new Exception("Aucun plugin ("+this.name+") trouvé");

		//Pour éviter le double chargement des plugins
		if(this.classInterface_I.size() != 0 )
			return ;

		File[] f = new File[this.files.size()];
		
		// Pour charger le .jar en memoire
		URLClassLoader loader;
		
		//Pour la comparaison de chaines
		String tmp = "";
		
		//Pour le contenu de l'archive jar
		Enumeration enumeration;
		
		//Pour déterminer quels sont les interfaces implémentées
		Class tmpClass = null;

		for(int index = 0 ; index < f.length ; index ++ )
		{
			f[index] = new File(this.path+this.files.get(index));

			if(!f[index].exists())
				break;
			
			URL u = f[index].toURI().toURL();

			// On créer un nouveau URLClassLoader pour charger le jar qui se trouve ne dehors du CLASSPATH
			loader = new URLClassLoader(new URL[] {u}); 
			
			// On charge le jar en mémoire
			JarFile jar = new JarFile(f[index].getPath());
			
			//On récupére le contenu du jar
			enumeration = jar.entries();

			while(enumeration.hasMoreElements())
			{
				tmp = enumeration.nextElement().toString();

				//On vérifie que le fichier courant est un .class (et pas un fichier d'informations du jar )
				if(tmp.length() > 6 && tmp.substring(tmp.length()-6).equals(".class"))
				{
					tmp = tmp.substring(0,tmp.length()-6).replaceAll("/",".");
					
					try
					{
						tmpClass = Class.forName(tmp, true, loader); // FIXME Plante ici lorsqu'on lance le programme par applet. On obtient une ClassNotFoundException
					}
					catch(NoClassDefFoundError e)
					{
						System.err.println("Impossible de charger l'algorithme, il ne respect pas la bonne version de l'interface");
						e.printStackTrace();
					}
					
					for(int i = 0; i < tmpClass.getInterfaces().length; i++ )
						//Si la classe trouvée respect l'interface imposée
						if(tmpClass.getInterfaces()[i].getName().toString().contains(this.name) )
							this.classInterface_I.add(tmpClass);
				}
			}
		}
	}
	
	public ArrayList getClassInterface()
	{
		return this.classInterface_I;
	}
	
	/*-----------------------------------------------------------------*\
	|*						Attributs privés						   *|
	\*-----------------------------------------------------------------*/
	private ArrayList<String> files;
	private ArrayList classInterface_I;
	private String name;
	private String path;
}