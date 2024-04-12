package ch.correvon.scheduler.use;

import java.util.ArrayList;

import javax.swing.JApplet;

import ch.correvon.scheduler.moo.Manager;

public class Main extends JApplet
{
	@Override public void init()
	{
		Manager manager;
		try
		{
			manager = new Manager("Scheduler Simulator", FOLDER);
		}
		catch(NoClassDefFoundError e)
		{
			if(e.getMessage().contains("FormLayout"))
				System.err.println("Erreur, impossible de charger le FormLayout. Vérifiez la place de vos jar externes");
			else
			{
				System.err.println("Erreur innatendue");
				e.printStackTrace();
			}
			return;
		}
		manager.run(searchFiles());
	}
	
	public static void main(String[] args)
	{
		String folder = ReadInstallationPath.getPath();
		if(folder == null || folder.compareTo("") == 0)
		{
			FOLDER = System.getProperty("user.dir")+"\\";
			System.err.println("Pensez à ré-installer le Scheduler-Simulator");
		}
		else
			FOLDER = folder+"\\";
				
		ARGS = args;
		new Main().init();
	}
	
	private static ArrayList<String> searchFiles()
	{
		ArrayList<String> files = new ArrayList<String>();

		if(ARGS != null)
			for(int i = 0; i < ARGS.length; i++)
				files.add(Manager.DATA_PATH+ARGS[i]);
		
		return files;
	}

	private static String FOLDER = "..\\";
	private static String[] ARGS;
}