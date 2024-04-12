//Specification:
package ch.correvon.scheduler.moo;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ch.correvon.scheduler.algorithms.ButtonAlgorithm;
import ch.correvon.scheduler.gui.about.FrameAbout;
import ch.correvon.scheduler.gui.options.FrameOptions;
import ch.correvon.scheduler.gui.ui.FrameUI;
import ch.correvon.scheduler.gui.ui.PanelUI;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.ProcessType;
import ch.correvon.scheduler.moo.process.Statistic;
import ch.correvon.scheduler.plugin.Algo_I;
import ch.correvon.scheduler.plugin.PluginsAlgoLoader;
import ch.correvon.scheduler.plugin.PluginsStatLoader;
import ch.correvon.scheduler.plugin.Statistic_I;

/**
 * Une seule instance de ce Manager est créé lors du lancement du programme (dans le main : package ch.hearc.scheduler.use)
 * Une copie de la réferance du manager est passée à toutes les instances de presque chaque classes de ce projet.
 * Son but est de centraliser des informations indispensable à plusieurs classes et permet une 
 * communication propre, sans utiliser de copie profonde ou de variables globales. 
 */
public class Manager
{
	/*----------------------------------------------------------------*\
	|*							Constructeur						  *|
	\*----------------------------------------------------------------*/
	public Manager(String title, String rootFolder)
	{
		this.rootFolder = rootFolder;
		this.processTypePath = this.rootFolder+"ressources\\TypeDeProcessus.ini";
		this.statisticPath = this.rootFolder+"ressources\\Statistiques.ini";
		this.preferancesPath = this.rootFolder+"ressources\\Preferances.ini";
		DATA_PATH = this.rootFolder+"ressources\\Données\\";

		this.statistics = new ArrayList<Statistic>();
		this.processType = new ArrayList<ProcessType>();

		this.preferancesFileReading();
		
		this.createdHistory = new ArrayList<File>();
		this.algorithmPlugin = new ArrayList<Algo_I>();
		this.statPlugin = new ArrayList<Statistic_I>();
		this.listButtonAlgorithm = new ArrayList<ButtonAlgorithm>();
		this.pluginsAlgoLoader = new PluginsAlgoLoader(this.rootFolder+"\\ressources\\Plugins\\Algorithmes\\");
		this.pluginsStatLoader = new PluginsStatLoader(this.rootFolder+"\\ressources\\Plugins\\Statistiques\\");

		this.frameOptions = new FrameOptions(100, 100, 640, 480, this);
		this.frameUI = new FrameUI(title, this);
		this.panelUI = this.frameUI.getPanelUI();
		this.frameUI.add(this.panelUI.getBuilder());
		this.frameAbout = new FrameAbout();

		this.searchPluginsAlgorithm();
		this.searchPluginsStat();
		this.searchProcessType();
		this.searchStatistic();
		
		this.frameOptions.run();
	}
	
	/*----------------------------------------------------------------*\
	|*						Methodes publiques						  *|
	\*----------------------------------------------------------------*/
	public void updateListType()
	{
		this.panelUI.updateInterface();
	}
	
	/**
	 * Ajoute un chemin de fichier à la liste lorsqu'un historique est créé.
	 * Ceci en vue de supprimer tous les historiques à la fin du programme. 
	 */
	public void addHistorique(String path)
	{
		this.createdHistory.add(new File(path));
	}
	
	/**
	 * Affiche l'interface principale
	 */
	public void run(ArrayList<String> files)
	{
		for(String file:files)
			if(file != null && file.compareTo(DATA_PATH) != 0)
				this.readFile(file, true);
		this.frameUI.run();
	}
	
	private void fillAlgo_I(Algo_I[] plugins)
	{
		for(int index = 0 ; index < plugins.length; index ++ )
			this.addAlgo(plugins[index]);
		this.panelUI.setListAlgorithm(this.listButtonAlgorithm);
	}
	
	private void fillStat_I(Statistic_I[] plugins)
	{
		for(int index = 0 ; index < plugins.length; index ++ )
			this.addStat(plugins[index]);
		}
	
	/**
	 * Méthode invoquée lorsqu'on quitte l'application (nottemment lors d'un clique sur Fichier / Quitter (voir EcouteurMenuInterface))
	 */
	public void quit()
	{
		if(this.askForSave())
		{
			this.panelUI.quit();
			this.savePreferancesFile();
			this.frameUI.dispose();
			for(File path:this.createdHistory)
		       if(path.exists() && path.getName().startsWith("_"))
		        	path.delete();
			System.exit(0);
		}
	}
	
	public boolean askForSave()
	{
		int result;
		
		if(this.panelUI.getDataChanged())
		{
			result = JOptionPane.showConfirmDialog(null, "Voulez-vous sauvegarder votre travail ?", "Sauvegarde", JOptionPane.YES_NO_CANCEL_OPTION);
			if(result != 2) // 2 : Annuler
			{
				if(result == 0) // 0 : Oui
				{
					if(this.saveFile(this.panelUI.getCheckboxClearValue()))
						return true;
				}
				else // 1 : Non
					return true;
			}
		}
		else
			return true;
		
		return false;
	}
	
	/**
	 * Méthode invoquée lorsqu'on clique sur Importer ou Fichier / Importer
	 * Si erase vaut true, la liste d'entrée est vidée avant d'ouvrir le fichier.
	 * Si erase vaut false, le programme tente d'incorporer les données du fichier dans la liste.
	 * Si il y a un conflit, une boite de dialogue demande à l'utilisateur ce qu'il veut faire.
	 * (voir méthode lireFichier(String, boolean)
	 */
	public void openFile(boolean erase)
	{
		if(this.askForSave())
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(DATA_PATH));
			if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				readFile(chooser.getSelectedFile().getAbsolutePath(), erase);
			this.panelUI.setDataChanged(false);
		}
	}

	/**
	 * Méthode invoquée lorsqu'on clique sur Exporter ou Fichier / Exporter
	 * Si erase vaut true, la liste d'entrée est vidée après l'ouverture du fichier
	 */
	public boolean saveFile(boolean erase)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Enregistrer");
		chooser.setCurrentDirectory(new File(DATA_PATH));
		chooser.setApproveButtonText("Enregistrer");
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			saveFile(chooser.getSelectedFile().getAbsolutePath(), erase);
			return true;
		}
		return false;
	}
	
	/**
	 * Ajoute un algorithme en mémoire et sur la page d'interface
	 */
	public void addAlgo(Algo_I algorithm)
	{
		ButtonAlgorithm newAlgo = new ButtonAlgorithm(algorithm.getName(), algorithm, this);
		this.algorithmPlugin.add(newAlgo.getAlgorithm());
		this.listButtonAlgorithm.add(newAlgo);
	}
	
	public void addStat(Statistic_I stat)
	{
		this.statPlugin.add(stat);
	}

	/*--------------------------------*\
	|*				Get				  *|
	\*--------------------------------*/
	public String getPreferances(String key)
	{
		String element = this.preferances.get(key);
		if(element == null || element.compareTo("") == 0)
			element = "NaN";
		return element;
	}
	
	public Hashtable<String, String> getPreferances()
	{
		return this.preferances;
	}
	
	public PanelUI getPanelUI()
	{
		return this.panelUI;
	}
	
	public FrameAbout getFrameAbout()
	{
		return this.frameAbout;
	}
	
	public FrameOptions getFrameOptions()
	{
		return this.frameOptions;
	}
	
	public ArrayList<Process> getListProcess()
	{
		return this.panelUI.getTableModel().getListProcess();
	}
	
	public PluginsAlgoLoader getPluginsAlgoLoader()
	{
		return this.pluginsAlgoLoader;
	}
	
	public PluginsStatLoader getPluginsStatLoader()
	{
		return this.pluginsStatLoader;
	}
	
	public ArrayList<Algo_I> getAlgoPlugin()
	{
		return this.algorithmPlugin;
	}
	
	public ArrayList<Statistic_I> getStatPlugin()
	{
		return this.statPlugin;
	}
	
	public ArrayList<ProcessType> getProcessType()
	{
		return this.processType;
	}
	
	public ArrayList<Statistic> getStatistics()
	{
		return this.statistics;
	}
	
	public void searchProcessType()
	{
		this.processType.clear();
	    try
	    {
	        FileReader fichier = new FileReader(this.processTypePath);
	        BufferedReader buffer = new BufferedReader(fichier);
	        
	        String line = buffer.readLine();
	        
	        String name;
	        Color color;
	        int priority = 1;
	        Algo_I algo = null;
	        String[] tabSplit;
	        String[] tabSplitColor;
	        int def = 0;
	        while(line != null)
	        {
	        	tabSplit = line.split(":");
	        	if(tabSplit.length != 4)
	        	{
	        		name = "Def"+def;
	        		def++;
	        		color = new Color(255, 255, 255);
	        		System.err.println("Erreur dans la lecture du fichier de type (Tout) - Génération d'un type par défaut");
	        	}
	        	else
	        	{
		        	name = tabSplit[0];
		        	tabSplitColor = tabSplit[1].split(",");
		        	if(tabSplitColor.length != 3)
		        	{
		        		color = new Color(255, 255, 255);
		        		System.err.println("Erreur dans la lecture du fichier de type (Couleur) - Génération d'une couleur par défaut");
		        	}
		        	else
		        		color = new Color(new Integer(tabSplitColor[0]).intValue(), new Integer(tabSplitColor[1]).intValue(), new Integer(tabSplitColor[2]).intValue());
		        	try
		        	{
						priority = new Integer(tabSplit[2]).intValue();
					} 
		        	catch(NumberFormatException e)
		        	{
		        		priority = 1;
		        		System.err.println("Erreur dans la lecture du fichier de type (Priorité) - Génération d'une priorité par défaut");
					}
		        	
		        	int index = Tools.searchAlgo(tabSplit[3], this.algorithmPlugin);
		        	if(index != -1)
		        		algo = this.algorithmPlugin.get(index);
	        	}
	        	ProcessType type = new ProcessType(name, color, priority);
	        	type.setAlgo(algo);
	        	this.processType.add(type);
	        	line = buffer.readLine();
	        }
	        buffer.close();
	        fichier.close();
	        this.panelUI.updateTypeProcess();
	    }
	    catch(IOException e)
	    {
	    	// Crée un fichier vide si il n'en existe pas encore un
	    	writeFileTypeProcess(new ArrayList<ProcessType>());
	    }
	}
	
	public void searchStatistic()
	{
		this.statistics.clear();
	    try
	    {
	        FileReader fichier = new FileReader(this.statisticPath);
	        BufferedReader buffer = new BufferedReader(fichier);
	        
	        String line = buffer.readLine();
	        
	        String name;
	        Color color;
	        String[] tabSplit;
	        String[] tabSplitColor;
	        int def = 0;
	        while(line != null)
	        {
	        	tabSplit = line.split(":");
	        	if(tabSplit.length != 2)
	        	{
	        		name = "Def"+def;
	        		def++;
	        		color = new Color(255, 255, 255);
	        		System.err.println("Erreur dans la lecture du fichier de statistiques (Tout) - Génération d'une statistique par défaut");
	        	}
	        	else
	        	{
		        	name = tabSplit[0];
		        	tabSplitColor = tabSplit[1].split(",");
		        	if(tabSplitColor.length != 3)
		        	{
		        		color = new Color(255, 255, 255);
		        		System.err.println("Erreur dans la lecture du fichier de statistiques (Couleur) - Génération d'une couleur par défaut");
		        	}
		        	else
		        		color = new Color(new Integer(tabSplitColor[0]).intValue(), new Integer(tabSplitColor[1]).intValue(), new Integer(tabSplitColor[2]).intValue());
	        	}
	        	this.statistics.add(new Statistic(name, 0.0, color));
	        	line = buffer.readLine();
	        }
	        buffer.close();
	        fichier.close();
	        
	        this.addEveryStatistic();
	        this.removeUnUsedStatistic();
	        
	        this.panelUI.updateTypeProcess();
	    }
	    catch(IOException e)
	    {
	    	// Crée un fichier vide si il n'en existe pas encore un
	    	writeFileStatProcess(new ArrayList<Statistic>());
	    }
	}
	
	public void writeFileTypeProcess(ArrayList<ProcessType> list)
	{
        try
        {
            FileWriter file = new FileWriter(this.processTypePath);
            String putStr;
            for(ProcessType type:list)
            {
            	putStr = type.toString() + ":" + type.getColor().getRed() + "," + type.getColor().getGreen() + "," + type.getColor().getBlue() + ":" + type.getPriority() + ":" + type.getAlgo().getName() + END_LINE_CHARACTER;
            	file.write(putStr);
            }
            file.close();
       	 }
        catch(IOException e)
        {
       		System.err.println("erreur lors de l'écriture du fichier de type de processus : "+this.processTypePath);
       		e.printStackTrace();
        }
	}
	
	public void writeFileStatProcess(ArrayList<Statistic> list)
	{
        try
        {
            FileWriter file = new FileWriter(this.statisticPath);
            String putStr;
            for(Statistic stat:list)
            {
            	putStr = stat.getName() + ":" + stat.getColor().getRed() + "," + stat.getColor().getGreen() + "," + stat.getColor().getBlue() + END_LINE_CHARACTER;
            	file.write(putStr);
            }
            file.close();
       	}
        catch(IOException e)
        {
       		System.err.println("erreur lors de l'écriture du fichier de statistique : "+this.statisticPath);
       		e.printStackTrace();
        }
	}
	
	/*------------------------------------------------------------------------------*\
	|*							Methodes privées									*|
	\*------------------------------------------------------------------------------*/
    /**
     *  Après avoir lu le fichier de statistiques, il faut comparer avec le dossier de Statistique dans les plugin
     */
    private void addEveryStatistic()
    {
        for(Statistic_I stat_i:this.getStatPlugin())
        {
        	boolean add = true;
        	for(Statistic stat:this.statistics)
        	{
	        	if(stat_i.getName().compareTo(stat.getName()) == 0)
	        	{
	        		add = false;
	        		break;
	        	}
        	}
        	if(add)
        		this.statistics.add(new Statistic(stat_i.getName()));
        }    
    }
    
    /**
     *  Il faut vérifier qu'il n'y a pas de statistiques en plus dans le fichier de statistiques
     */
    private void removeUnUsedStatistic()
    {
        for(Statistic stat:new ArrayList<Statistic>(Tools.deepClone(this.statistics)))
        {
        	boolean delete = true;
        	for(Statistic_I stat_i:this.getStatPlugin())
        	{
	        	if(stat_i.getName().compareTo(stat.getName()) == 0)
	        	{
	        		delete = false;
	        		break;
	        	}
        	}
        	if(delete)
        		this.statistics.remove(stat);
        }
    }

	private void searchPluginsAlgorithm()
	{
		File root = new File(this.rootFolder+"ressources\\Plugins\\Algorithmes");
		
		String[] list = root.list();
		
		if(list == null)
			System.err.println("Aucun algorithme n'a été détecté");
		else
		{
			for(String f:list)
				this.pluginsAlgoLoader.addFiles(f);
	
			try
			{
				this.fillAlgo_I(this.getPluginsAlgoLoader().loadAllInterface_I());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void searchPluginsStat()
	{
		File root = new File(this.rootFolder+"ressources\\Plugins\\Statistiques");
		String[] list = root.list();
		if(list != null)
		{
			for(String f:list)
				this.pluginsStatLoader.addFiles(f);
	
			try
			{
				this.fillStat_I(this.getPluginsStatLoader().loadAllInterface_I());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void preferancesFileReading()
	{
    	this.preferances = new Hashtable<String, String>();
	    try
	    {
	        FileReader file = new FileReader(this.preferancesPath);
	        BufferedReader buffer = new BufferedReader(file);
	        
	        String line = buffer.readLine();
	        String[] couple = new String[2];
	        while(line != null)
	        {
	        	couple = line.split(":");
	        	this.preferances.put(couple[0], couple[1]);
	        	line = buffer.readLine();
	        }
	        
	        buffer.close();
	        file.close();
	    }
	    catch(IOException e)
	    {
       		System.err.println("Aucun fichier de préférences n'a été trouvé. Il sera créé à la fin de l'exécution du programme.");
	    }
	}

	private void readFile(String path, boolean erase)
	{
	    try
	    {
	        FileReader file = new FileReader(path);
	        BufferedReader buffer = new BufferedReader(file);
	        
	        String line = buffer.readLine();
	        if(erase)
	        	this.panelUI.removeAllProcess();
	        Process temp;
	        ProcessType type;
	        while(line != null)
	        {
	    		temp = Process.convert(line);

	    		String error = null;
	    		if(temp == null)
	    			error = "Fichier corrompu\n";
	    		else if(!Tools.notYetExist(temp, this.panelUI.getTableModel().getListProcess(), true, this.getPreferances("checkSameArrival").compareTo("false") == 0))
	    			error = "";
	    		if(error != null)
	    		{
		    		int result = JOptionPane.showConfirmDialog(null, error+"Continuer ?\nAnnuler = tout vider", "Erreur", JOptionPane.YES_NO_CANCEL_OPTION);
	    	        if(result == 1)
	    	        	break;
	    	        else if(result == 2)
	    	        {
	    	        	this.panelUI.removeAllProcess();
	    	        	break;
	    	        }
	    		}
	    		else
	    		{
	    			type = this.findType(temp.getType().toString());
	    			if(type != null)
	    				temp.setType(type);
	    			this.panelUI.addProcess(temp);
	    		}
	        	line = buffer.readLine();
	        }
	        buffer.close();
	        file.close();
	        this.panelUI.setDataChanged(false);
	        if(this.getPreferances("checkAutoValidFileRead").compareTo("false") != 0)
	        	this.panelUI.valideInput();
	    }
	    catch(IOException e)
	    {
       		System.err.println("erreur lors de la lecture du fichier de données");
       		e.printStackTrace();
	    }
	}
	
	private ProcessType findType(String name)
	{
		for(ProcessType type:this.processType)
			if(type.toString().compareTo(name) == 0)
				return type;
		return null;
	}
	
	private void savePreferancesFile()
	{
        try
        {
            FileWriter file = new FileWriter(this.preferancesPath);
            String putStr;
        	String key = "";
        	String element;
            for(Enumeration<String> e = this.preferances.keys() ; e.hasMoreElements() ; key = e.nextElement())
	        {
	        	
	        	element = this.preferances.get(key);
	        	putStr = key+":"+element+END_LINE_CHARACTER;
	        	file.write(putStr);
	        }
            file.write("frameUI_location:"+this.frameUI.getLastLocation().x+","+this.frameUI.getLastLocation().y+END_LINE_CHARACTER);
            file.write("frameUI_size:"+this.frameUI.getLastSize().width+","+this.frameUI.getLastSize().height+END_LINE_CHARACTER);
            file.write("frameUI_state:"+this.frameUI.getState()+END_LINE_CHARACTER);

            file.close();
       	 }
        catch(IOException e)
        {
       		System.err.println("Erreur lors de l'écriture du fichier de préférances");
       		e.printStackTrace();
        }
	}
	
	private void saveFile(String path, boolean erase)
	{
		/*
		 * Test si le fichier existe déjà.
		 * Si il existe, on demande à l'utilisateur si il veut l'écraser.
		 * Sinon, le fichier est créé
		 */
		if(!path.endsWith(EXTENSION))
			path = path.concat(EXTENSION);
	    try
	    {
	        FileReader file = new FileReader(path);
	        if(JOptionPane.showConfirmDialog(null, "Ce fichier existe déjà. Voulez-vous l'écraser ?", "Ecraser", JOptionPane.YES_NO_OPTION) == 0)
	        {
	        	writeFile(path);
	        	if(erase)
	        		this.panelUI.removeAllProcess();
	        }
	        file.close();
	    }
	    catch(IOException ioe)
	    {
	    	writeFile(path);
	    }
        this.panelUI.setDataChanged(false);
	}
	
	private void writeFile(String path)
	{
        try
        {
            FileWriter fichier = new FileWriter(path);
            String putStr;
            for(Process process:this.panelUI.getTableModel().getListProcess())
            {
            	putStr = process.toStringTabulationIn() + END_LINE_CHARACTER;
            	fichier.write(putStr);
            }
            fichier.close();
       	 }
        catch(IOException e)
        {
       		System.err.println("erreur lors de l'écriture du fichier de données");
       		e.printStackTrace();
        }
	}
	
	/*----------------------------------------------------------------*\
	|*							Attributs Privés					  *|
	\*----------------------------------------------------------------*/
	private PanelUI panelUI;
	private FrameUI frameUI;
	private FrameAbout frameAbout;
	private FrameOptions frameOptions;
	
	private ArrayList<ButtonAlgorithm> listButtonAlgorithm;
	
	private ArrayList<ProcessType> processType;
	private ArrayList<Statistic> statistics;
	private Hashtable<String, String> preferances;
	
	private ArrayList<File> createdHistory;
	
	private PluginsAlgoLoader pluginsAlgoLoader;
	private PluginsStatLoader pluginsStatLoader;
	private ArrayList<Algo_I> algorithmPlugin;
	private ArrayList<Statistic_I> statPlugin;
	
	private String rootFolder;
	private String processTypePath;
	private String statisticPath;
	private String preferancesPath;

	/*-----------------------------------------*\
	|*			Statiques finaux			   *|
	\*-----------------------------------------*/
	public static String DATA_PATH;
	public static final String END_LINE_CHARACTER = new String(new Character((char)13).toString()  + new Character((char)10).toString());
	private final static String EXTENSION = ".sim";

}