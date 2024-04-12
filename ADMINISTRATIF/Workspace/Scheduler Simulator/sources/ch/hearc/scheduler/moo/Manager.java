//Specification:
package ch.hearc.scheduler.moo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ch.hearc.scheduler.gui.FrameAPropos;
import ch.hearc.scheduler.gui.FrameDessin;
import ch.hearc.scheduler.gui.FrameInterface;
import ch.hearc.scheduler.gui.PanneauInterface;
import ch.hearc.scheduler.gui.tools.Tools;

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
	public Manager()
	{
		this.listeProcessus = new ArrayList<Processus>();
		
		this.frameInterface = new FrameInterface(0, 0, Options.WIDTH_FENETRE, Options.HEIGHT_FENETRE, this);
		this.panneauInterface = new PanneauInterface(this, Options.WIDTH_FENETRE, Options.HEIGHT_FENETRE, this.frameInterface);
		this.frameInterface.add(this.panneauInterface);
		this.frameAPropos = new FrameAPropos(100, 100, 300, 200);
		
		this.historiqueCree = new ArrayList<File>();
	}
	
	/*----------------------------------------------------------------*\
	|*						Methodes publiques						  *|
	\*----------------------------------------------------------------*/
	/**
	 * Ajoute un chemin de fichier à la liste lorsqu'un historique est créé.
	 * Ceci en vue de supprimer tous les historiques à la fin du programme. 
	 */
	public void addHistorique(String path)
	{
		this.historiqueCree.add(new File(path));
	}
	
	/**
	 * Affiche l'interface principale
	 */
	public void run()
	{
		this.frameInterface.run();
	}
	
	/**
	 * Méthode invoquée lorsqu'on clique sur Fichier / Quitter
	 * (voir EcouteurMenuInterface)
	 */
	public void quit()
	{
		this.frameInterface.dispose();
		for(File path:this.historiqueCree)
		{
	        boolean resultat = true; 
	        
	        if(path.exists() && path.getName().startsWith("_"))
	        	resultat &= path.delete();
		}
		System.exit(0);
	}
	
	/**
	 * Méthode invoquée lorsqu'on clique sur Importer ou Fichier / Importer
	 * Si erase vaut true, la liste d'entrée est vidée avant d'ouvrir le fichier.
	 * Si erase vaut false, le programme tente d'incorporer les données du fichier dans la liste.
	 * Si il y a un conflit, une boite de dialogue demande à l'utilisateur ce qu'il veut faire.
	 * (voir méthode lireFichier(String, boolean)
	 */
	public void ouvrirFichier(boolean erase)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(Options.FILE_PATH));
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			String filePath = new String(chooser.getSelectedFile().getAbsolutePath());
			lireFichier(filePath, erase);
		}
	}

	/**
	 * Méthode invoquée lorsqu'on clique sur Exporter ou Fichier / Exporter
	 * Si erase vaut true, la liste d'entrée est vidée après l'ouverture du fichier
	 */
	public void enregistrerFichier(boolean erase)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Enregistrer");
		chooser.setCurrentDirectory(new File(Options.FILE_PATH));
		chooser.setApproveButtonText("Enregistrer");
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			String path = new String(chooser.getSelectedFile().getAbsolutePath());
			enregistrerFichier(path, erase);
		}
	}
	
	public void addProcessus()
	{
		addProcessus(new Processus());
	}
	
	public void addProcessus(String nom, int heureArrivee, int duree)
	{
		addProcessus(new Processus(nom, heureArrivee, duree));
	}
	
	public void addProcessus(Processus processus)
	{
		DefaultListModel model = (DefaultListModel)this.panneauInterface.getListBoxInput().getModel();
		this.listeProcessus.add(processus);
		model.addElement(processus.toStringEspace());
	}
	
	public void removeProcessus(int[] tab)
	{
	    for(int i = 0; i < tab.length; i++)
			removeProcessus(tab[i]-i);
	}
	
	public void removeAllProcessus()
	{
		DefaultListModel model = (DefaultListModel)this.panneauInterface.getListBoxInput().getModel();
		model.removeAllElements();
		this.listeProcessus.clear();
	}
	
	public void applyFCFS()
	{
		if(this.listeProcessus.size() > 0)
		{
			ProcessusTrie processusTrie = Algorithmes.applyFCFS(this.listeProcessus);
			FrameDessin dessin = new FrameDessin("FCFS", Options.WIDTH_FENETRE, 0, wFrameDessin, Options.HEIGHT_ECRAN, processusTrie, this);
			dessin.run();
		}
	}
	
	public void applySJF()
	{
		if(this.listeProcessus.size() > 0)
		{
			ProcessusTrie processusTrie = Algorithmes.applySJF(this.listeProcessus);
			FrameDessin dessin = new FrameDessin("SJF", Options.WIDTH_FENETRE, 0, wFrameDessin, Options.HEIGHT_ECRAN, processusTrie, this);
			dessin.run();
		}
	}
	
	public void applySJFNot()
	{
		if(this.listeProcessus.size() > 0)
		{
			ProcessusTrie processusTrie = Algorithmes.applySJFNot(this.listeProcessus);
			FrameDessin dessin = new FrameDessin("SJFNot", Options.WIDTH_FENETRE, 0, wFrameDessin, Options.HEIGHT_ECRAN, processusTrie, this);
			dessin.run();
		}

	}
	
	public void applyRoundRobin()
	{
		int nbQuantum = this.panneauInterface.getQuantum();
		if(nbQuantum <= 0)
			JOptionPane.showMessageDialog(null, "Le nombre de quantum doit être un nombre supérieur à zéro", "Erreur", JOptionPane.OK_OPTION);
		else
		{
			if(this.listeProcessus.size() > 0)
			{
				ProcessusTrie processusTrie = Algorithmes.applyRoundRobin(this.listeProcessus, nbQuantum);
				FrameDessin dessin = new FrameDessin("Round Robin", Options.WIDTH_FENETRE, 0, wFrameDessin, Options.HEIGHT_ECRAN, processusTrie, this);
				dessin.run();
			}
		}
	}
	
	public void upProcess()
	{
		int tabIndice[] = this.panneauInterface.getListBoxInput().getSelectedIndices();
		for(int i = 0; i < tabIndice.length ; i++)
		{
			int indice1 = tabIndice[i];
			int indice2 = indice1 - 1;
			if(indice1 > 0)
			{
				switchProcess(indice1, indice2);
				refreshListIn();
			}
		}
		increaseTabIndice(tabIndice);
		this.panneauInterface.getListBoxInput().setSelectedIndices(tabIndice);
	}
	
	public void downProcess()
	{
		int tabIndice[] = this.panneauInterface.getListBoxInput().getSelectedIndices();
		for(int i = tabIndice.length-1; i >= 0 ; i--)
		{
			int indice1 = tabIndice[i];
			int indice2 = indice1 + 1;
			if(indice1 != -1 && indice1 < this.panneauInterface.getListBoxInput().getModel().getSize()-1)
			{
				switchProcess(indice1, indice2);
				refreshListIn();
			}
		}
		decreaseTabIndice(tabIndice);
		this.panneauInterface.getListBoxInput().setSelectedIndices(tabIndice);
	}
	
	public void upProcessTop()
	{
		int tabIndice[] = this.panneauInterface.getListBoxInput().getSelectedIndices();
		for(int j = 0; j < tabIndice.length ; j++)
			for(int i = tabIndice[j]; i > 0; i--)
			{
				this.panneauInterface.getListBoxInput().setSelectedIndex(i);
				upProcess();
			}
		
		/* Permute les process */
		for(int i = 0; i < tabIndice.length/2; i++)
			switchProcess(i, tabIndice.length-i-1);
		refreshListIn();
		
		/* Nouvelle sélection */
		for(int i = 0; i < tabIndice.length; i++)
			tabIndice[i] = tabIndice.length-i-1;
		this.panneauInterface.getListBoxInput().setSelectedIndices(tabIndice);
	}
	
	public void downProcessBottom()
	{
		int tabIndice[] = this.panneauInterface.getListBoxInput().getSelectedIndices();
		int tailleListBox = this.panneauInterface.getListBoxInput().getModel().getSize();
		for(int j = tabIndice.length-1; j >= 0 ; j--)
			for(int i = tabIndice[j]; i < this.panneauInterface.getListBoxInput().getModel().getSize()-1; i++)
			{
				this.panneauInterface.getListBoxInput().setSelectedIndex(i);
				downProcess();
			}
		
		/* Permute les process */
		for(int i = 0; i < tabIndice.length/2; i++)
			switchProcess(tailleListBox-1-i, tailleListBox-tabIndice.length+i);
		refreshListIn();
		
		/* Nouvelle sélection */
		for(int i = 0; i < tabIndice.length; i++)
			tabIndice[i] = tailleListBox-i-1;
		this.panneauInterface.getListBoxInput().setSelectedIndices(tabIndice);
	}
	
	public void switchProcess()
	{
		int tabIndice[] = this.panneauInterface.getListBoxInput().getSelectedIndices();
		
		if(tabIndice.length > 1)
			for(int i = tabIndice.length-1; i > 0 ; i--)
				switchProcess(tabIndice[i], tabIndice[i-1]);
		
		refreshListIn();
		this.panneauInterface.getListBoxInput().setSelectedIndices(tabIndice);
	}

	/*--------------------------------*\
	|*				Get				  *|
	\*--------------------------------*/
	public PanneauInterface getPanneauInterface()
	{
		return this.panneauInterface;
	}
	
	public FrameAPropos getFrameAPropos()
	{
		return this.frameAPropos;
	}
	
	public List<Processus> getListeProcessus()
	{
		return this.listeProcessus;
	}
	
	/*------------------------------------------------------------------------------*\
	|*							Methodes privées									*|
	\*------------------------------------------------------------------------------*/
	private void removeProcessus(int index)
	{
		DefaultListModel model = (DefaultListModel)this.panneauInterface.getListBoxInput().getModel();
		model.removeElementAt(index);
		this.listeProcessus.remove(index);
	}
	/**
	 * Echange de place 2 processus dans la liste des processus ( d'entrée )
	 */
	private void switchProcess(int index1, int index2)
	{
		Processus temp = this.listeProcessus.get(index1);
		this.listeProcessus.set(index1, this.listeProcessus.get(index2));
		this.listeProcessus.set(index2, temp);
	}
	
	private void refreshListIn()
	{
		DefaultListModel model = (DefaultListModel)this.panneauInterface.getListBoxInput().getModel();
		model.clear();
		for(Processus process:this.listeProcessus)
		{
			model.addElement(process.toStringEspace());
		}
	}
	
	private void decreaseTabIndice(int[] tab)
	{
		for(int i = 0; i < tab.length; i++)
			tab[i]++;
	}
	
	private void increaseTabIndice(int[] tab)
	{
		for(int i = 0; i < tab.length; i++)
			tab[i]--;
	}

	private void lireFichier(String path, boolean erase)
	{
	    try
	    {
	        FileReader fichier = new FileReader(path);
	        BufferedReader buffer = new BufferedReader(fichier);
	        
	        String ligne = buffer.readLine();
	        if(erase)
	        	removeAllProcessus();
	        while(ligne != null)
	        {
	    		Processus temp = Processus.convert(ligne);
	    		if(temp != null && Tools.verifierExistance(temp, this.listeProcessus))
	    			addProcessus(temp);
	    		else
	    		{
	    	        int reponse = JOptionPane.showConfirmDialog(null, "Fichier corrompu, continuer ?\nAnnuler = tout vider", "Erreur", JOptionPane.YES_NO_CANCEL_OPTION);
	    	        if(reponse == 1)
	    	        	break;
	    	        else if(reponse == 2)
	    	        {
	    	        	removeAllProcessus();
	    	        	break;
	    	        }
	    		}
	        	
	        	ligne = buffer.readLine();
	        }
	        buffer.close();
	        fichier.close();
	    }
	    catch(IOException e)
	    {
       		System.out.println("erreur lors de la lecture du fichier");
       		e.printStackTrace();
	    }
	}
	
	private void enregistrerFichier(String path, boolean erase)
	{
		/**
		 * Test si le fichier existe déjà.
		 * Si il existe, on demande à l'utilisateur si il veut l'écraser.
		 * Sinon, le fichier est créé
		 */
	    try
	    {
	        FileReader fichier = new FileReader(path);
	        int reponse = JOptionPane.showConfirmDialog(null, "Ce fichier existe déjà. Voulez-vous l'ecraser ?", "Ecraser", JOptionPane.YES_NO_OPTION);
	        if(reponse == 0)
	        {
	        	ecrireFichier(path);
	        	if(erase)
	        		removeAllProcessus();
	        }
	        fichier.close();
	    }
	    catch(IOException ioe)
	    {
	    	ecrireFichier(path);
	    }
	}
	
	private void ecrireFichier(String path)
	{
        try
        {
            FileWriter fichier = new FileWriter(path);
            String putStr;
            for(Processus process:this.listeProcessus)
            {
            	putStr = new String(process.toStringTab() + carFinLigne);
            	fichier.write(putStr);
            }
            fichier.close();
       	 }
        catch(IOException e)
        {
       		System.out.println("erreur lors de l'écriture du fichier");
       		e.printStackTrace();
        }		
	}
	
	/*----------------------------------------------------------------*\
	|*							Attributs Privés					  *|
	\*----------------------------------------------------------------*/
	private PanneauInterface panneauInterface;
	private FrameInterface frameInterface;
	private FrameAPropos frameAPropos;
	
	private List<Processus> listeProcessus;
	
	private List<File> historiqueCree;
	
	/*-----------------------------------------*\
	|*			Statiques finaux			   *|
	\*-----------------------------------------*/
	private static final String carFinLigne = new String(new Character((char)13).toString()  + new Character((char)10).toString());
	private static final int wFrameDessin = Options.WIDTH_ECRAN-Options.WIDTH_FENETRE;
}