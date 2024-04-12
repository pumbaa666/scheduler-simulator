
//Specification:
package ch.hearc.scheduler.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import ch.hearc.scheduler.gui.tools.PanelCardsGraphics;
import ch.hearc.scheduler.moo.Manager;
import ch.hearc.scheduler.moo.Options;
import ch.hearc.scheduler.moo.Processus;
import ch.hearc.scheduler.moo.ProcessusTrie;

/**
 * C'est dans ce panneau que seront déssinés tout les processus lors de l'application
 * d'un algorithme à une liste de processus
 */
public class PanneauDessin extends PanelCardsGraphics
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public PanneauDessin(String nom, ProcessusTrie procesTrie, Manager manager)
	{
		this.manager = manager;
		this.procesTrie = procesTrie;
		this.output = new ArrayList<String>();
		this.nom = nom;
		
		genererNomFichier();
		this.add(zoneCentre());
		inputInListBoxOut();
		calculerMoyenne();
		repaint();

	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes Public						   *|
	\*-----------------------------------------------------------------*/
	@Override public void paintComponent(Graphics g)
	{
		super.paintComponent(g);	
		this.g2D = (Graphics2D)g;
		dessin();
	}

	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public List<String> getListBoxOutput()
	{
		return this.output;
	}
	
	 /*-----------------------------------------------------------------*\
	 |*							Méthodes Privées						*|
	 \*-----------------------------------------------------------------*/
	private Box zoneCentre()
	{
		Box boxV = Box.createVerticalBox();
		boxV.add(Box.createVerticalStrut(200));
		this.setLayout(new FlowLayout());

		/* CheckBox Afficher */
		this.checkAfficher = new JCheckBox("Afficher Historique", false);
		this.checkAfficher.setToolTipText("A n'utiliser qu'avec une entrée de 10 processus maximum");
		this.checkAfficher.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								if(checkAfficher.isSelected())
									afficherHisto = true;
								else
									afficherHisto = false;
								repaint();
							}
						}
					);
		boxV.add(this.checkAfficher);

		/* Bouton Ouvrir */
		JButton boutonAfficherHisto = new JButton("Ouvrir Historique");
		boutonAfficherHisto.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								String filePath = Options.FILE_PATH+"\\"+fileName;
								String progPath = "notepad.exe";
								try
								{
									Runtime.getRuntime().exec(progPath+" "+filePath);
								}
								catch(IOException e1)
								{
									System.out.println("Impossible d'ouvrir le fichier\n"+filePath+"\navec le programme\n"+progPath);
									e1.printStackTrace();
								}
							}
						}
					);
		boxV.add(boutonAfficherHisto);
		return boxV;
	}
	
	/**
	 * Génère le nom du fichier temporaire qui va contenir l'historique.
	 * Ce nom dépend de la date et l'heure du jour, ainsi que du type d'algorithme employé
	 */
	private void genererNomFichier()
	{
		Calendar calendrier = Calendar.getInstance();
		String dateHeure = calendrier.getTime().toString().replace(" ", "_").replace(":", "-");
		String deleteHisto = "";
		if(this.manager.getPanneauInterface().getValueCheckHisto())
			deleteHisto = "_";
		this.fileName = deleteHisto+"historique_"+this.nom+"-"+dateHeure+".txt";
	}
	
	private void dessin()
	{
		this.g2D.setColor(new Color(10,155,200));
		this.g2D.setFont(new Font("Times new roman", Font.PLAIN, 24));
		super.drawCenteredString(this.g2D, this.nom, this.getWidth()/2, 20);
		
		int x = 0;
		int y = 75;
		int w = 0;
		int h = 50;

		int marge = 75;
		int widthPanneau = this.getWidth()-marge; // Taille de la zone de dessin totale
		
		int hEcritureNom = y+h/2;
		int hEcritureHeure = y+h+15;

		this.g2D.setFont(new Font("Times new roman", Font.PLAIN, 12));
		
		int alpha;
		
		int lastHeureFin = 0;
		int tailleTotale;
		/* ----------------------- Dessin des processus ----------------------- */
		tailleTotale = this.procesTrie.getListeProcessusTrie().get(this.procesTrie.getListeProcessusTrie().size()-1).getHeureFinReelle()/* - procesTrie.getListeProcessusTrie().get(0).getHeureArriveeReelle()*/;
		alpha = 160;
		for(Processus p:this.procesTrie.getListeProcessusTrie())
		{
			/* Couleur */
			this.g2D.setColor(Color.getHSBColor((alpha) / 360f, 1.0f, 1.0f));
			//alpha += 360/(nbProcessParLigne+5); // pour le multi-ligne
			alpha += 360/(this.procesTrie.getListeProcessusTrie().size()+5);
			
			/* Rectangle */
			x = (int)((p.getHeureArriveeReelle()-lastHeureFin)/(float)tailleTotale*widthPanneau)+marge/4;//-10;
			w = (int)(p.getDuree()/(float)tailleTotale*widthPanneau);
			this.g2D.drawRect(x, y, w, h);
			
			/* Heure début */
			this.g2D.drawString(p.getHeureArriveeReelle()+"", x+5, hEcritureHeure);
			
			/* Heure fin */
			if(w > 40)
				this.g2D.drawString(p.getHeureFinReelle()+"", w+x-15, hEcritureHeure);
			
			/* Nom */
			if(w < 20)
			{
				hEcritureHeure += 15;
				if(hEcritureHeure > y+h+45)
					hEcritureHeure = y+h+15;
				if(hEcritureNom == y-h/9)
					hEcritureNom -= 15;
				else
					hEcritureNom = y-h/9;
			}
			else
			{
				hEcritureNom = y+h/2;
				hEcritureHeure = y+h+15;
			}
			super.drawCenteredString(this.g2D, p.getNom(), x+w/2, hEcritureNom); // Affiche le nom
		}
		lastHeureFin = this.procesTrie.getListeProcessusTrie().get(this.procesTrie.getListeProcessusTrie().size()-1).getHeureFinReelle();
		y += espacementVertical;
		hEcritureHeure = y+h+15;
				
		/* ----------------------- Ecriture ----------------------- */
		this.g2D.setFont(Options.FONT_LISTBOX);
		this.g2D.setColor(Color.black);
		
		/* Output */
		int yInit = 300;
		int yDecalage = 15;
		x = 10;
		y = yInit;
		for(String s:this.output)
		{
			this.g2D.drawString(s, x, y);
			y += yDecalage;
			if(y > this.getHeight()-50)
			{
				y = yInit+yDecalage;
				x += 250;
			}
		}
		
		/* Historique */
        try
        {
        	String path = Options.FILE_PATH+"\\"+this.fileName;
        	this.manager.addHistorique(path);
            FileWriter fichier = new FileWriter(path);
            for(String s:this.procesTrie.getHistorique())
            	fichier.write(s+carFinLigne);
            if(this.afficherHisto)
            {
            	y += 25;
                for(String s:this.procesTrie.getHistorique())
                {
                	this.g2D.drawString(s+carFinLigne, x, y);
        			y += yDecalage;
        			if(y > this.getHeight()-50)
        			{
        				y = yInit+yDecalage;
        				x += 300;
        			}
                }
            }
            fichier.close();
       	 }
        catch(IOException e)
        {
       		System.out.println("erreur lors de l'écriture du fichier");
       		e.printStackTrace();
        }
	}
	
	private void inputInListBoxOut(/*List<Processus> liste*/)
	{
		this.output.add("Nom  Arr  Fin  Att  Rep  Rot");
		for(Processus process:this.procesTrie.getListeProcessusTrie())
			this.output.add(process.toStringTiret());
	}

	private void calculerMoyenne()
	{
		double tempsAttenteMoy = 0; // Temps total passé à attendre
		double tempsRotationMoy = 0; // Fin réelle - Arrivée réelle
		double tempsReponseMoy = 0; // temps entre Arrivée et 1ère execution
		int nbProcess = 0;// = this.procesTrie.getListeProcessusTrie().size();
		for(Processus process:this.procesTrie.getListeProcessusTrie())
		{
			if(process.isLast())
			{
				tempsAttenteMoy += process.getTempsAttente();
				tempsRotationMoy += process.getTempsRotation();
				tempsReponseMoy += process.getTempsReponse();
				nbProcess++;
			}
		}
		tempsAttenteMoy /= nbProcess;
		tempsRotationMoy /= nbProcess;
		tempsReponseMoy /= nbProcess;
		
		this.output.add("");
		this.output.add("temps attente moyen  : "+(int)(tempsAttenteMoy*100)/100.0);
		this.output.add("temps rotation moyen : "+(int)(tempsRotationMoy*100)/100.0);
		this.output.add("temps réponse moyen  : "+(int)(tempsReponseMoy*100)/100.0);
	}
	
	 /*-----------------------------------------------------------------*\
	 |*							Attributs Private						*|
	 \*-----------------------------------------------------------------*/
	private Manager manager;
	private Graphics2D g2D;
	private ProcessusTrie procesTrie;
	private List<String> output;
	private String nom;
	
	private JCheckBox checkAfficher;
	private boolean afficherHisto = false;
	private String fileName;

	/*-----------------------------------------*\
	|*				Statiques				   *|
	\*-----------------------------------------*/
	//private static int nbProcessParLigne = 7; // Pour le multi-ligne
	private static int espacementVertical = 100;
	private static String carFinLigne = new String(new Character((char)13).toString()  + new Character((char)10).toString());
}

