// Specification:
package ch.hearc.scheduler.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ch.hearc.scheduler.gui.ecouteur.EcouteurAjouter;
import ch.hearc.scheduler.gui.ecouteur.EcouteurBoutonGenerer;
import ch.hearc.scheduler.gui.ecouteur.EcouteurListBoxInput;
import ch.hearc.scheduler.gui.ecouteur.EcouteurTextBox;
import ch.hearc.scheduler.gui.tools.PanelCardsGraphics;
import ch.hearc.scheduler.moo.Manager;
import ch.hearc.scheduler.moo.Options;

/**
 * Panneau contenant toute l'interface utilisateur
 */
public class PanneauInterface extends PanelCardsGraphics
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public PanneauInterface(Manager manager, int w, int h, JFrame frame)
	{
		this.manager = manager;
		this.hauteurPanneau = h;
		
		this.dimensionTxtBox = new Dimension(100, hauteurGénérale);
		this.dimensionBouton = new Dimension(150, hauteurGénérale);
		
		this.ecouteurTextBox = new EcouteurTextBox();

		/**
		 *  ________________
		 * |    ||    ||    |
		 * |    || 2a || 3a |
		 * | 1  ||____||____|
		 * |    ||    ||    |
		 * |    || 2b || 3b |
		 * |    ||    ||    |
		 *  ----------------
		 * 
		 */
		this.setLayout(new GridLayout(1,3, 10, 1));
		
		this.add(panneauGauche()); // Panneau 1
		this.add(panneauCentre()); // Panneau 2
		this.add(panneauDroite()); // Panneau 3
	}
	
	/*-----------------------------------------------------------------*\
	|*						Méthodes Publiques						   *|
	\*-----------------------------------------------------------------*/
	/**
	 * Méthode appelé dans EcouteurAjouter.
	 * Quand on ajoute manuellement un processus à la liste, ça incrémente
	 * automatiquement le numéro après le nom, pour plus de souplesse
	 * 
	 * TODO ajouter une option pour activer / désactiver
	 */
	public void incrementerNumProcess()
	{
		int num = 0;
		int i = this.textNom.getText().toString().length();
		int strLen = i;
		boolean stop = false;
		String txtNom = this.textNom.getText();
		String name = "";
		while(!stop && i != 0)
		{
			try
			{
				num = Integer.parseInt(txtNom.substring(i-1, strLen-0));
			}
			catch (NumberFormatException e)
			{
				stop = true;
				name = txtNom.substring(0, i);
			}
			i--;
		}
		if(num != 0)
			this.textNom.setText(name+(num+1));
	}

	/**
	 * Cette méthode, appelée dans EcouteurListBoxInput active ou désactive
	 * le bouton Modifier. En effet, il ne faut pouvoir modifier un processus
	 * que si il y en a un et un seul de sélectionné
	 * 
	 */
	public void enableBoutonModifier(boolean value)
	{
		this.boutonModifier.setEnabled(value);
	}

	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public JTextField[] getTxtFieldGenerer()
	{
		JTextField[] tabTxt = new JTextField[3];
		tabTxt[0] = this.txtGenArrivee;
		tabTxt[1] = this.txtGenDuree;
		tabTxt[2] = this.txtGenNombre;
		return tabTxt;
	}
	
	public String getProcessus()
	{
		String nom = this.textNom.getText().trim();
		int heure = 0;
		int duree = 0;
		
		if(nom.length() == 0 || nom.length() > 4)
		{
			JOptionPane.showMessageDialog(this, "Le nom est incorrect ! Il faut au entre 1 et 4 lettres", "Erreur", JOptionPane.OK_OPTION);
			return null;
		}
		
		try
		{
			heure = Integer.parseInt(this.textHeure.getText());
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, "L'heure d'arrivée est incorrecte ! Il faut un nombre entier", "Erreur", JOptionPane.OK_OPTION);
			return null;
		}
		try
		{
			duree = Integer.parseInt(this.textDuree.getText());
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, "La durée est incorrecte ! Il faut un nombre entier", "Erreur", JOptionPane.OK_OPTION);
			return null;
		}
		if(duree <= 0)
		{
			JOptionPane.showMessageDialog(this, "La durée est incorrecte ! Elle doit être entière positive", "Erreur", JOptionPane.OK_OPTION);
			return null;
		}
		
		return nom+", "+heure+", "+duree;
	}
	
	public JList getListBoxInput()
	{
		return this.listeBoxInput;
	}
	
	public int getQuantum()
	{
		int num;
		try
		{
			num = Integer.parseInt(this.textQuantum.getText());
		}
		catch (NumberFormatException e)
		{
			num = 0;
		}
		return num;
	}
	
	public JTextField getTextBoxHeure()
	{
		return this.textHeure;
	}
	
	public boolean getValueCheckVider()
	{
		return this.checkVider.isSelected();
	}
	
	public boolean getValueCheckHisto()
	{
		return this.checkSupprimerHisto.isSelected();
	}
	
	public boolean getValueCheckShuffle()
	{
		return this.checkShuffle.isSelected();
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes Privées					   *|
	\*-----------------------------------------------------------------*/
	private JPanel panneauGauche() // Panneau 1
	{
		JPanel panneau = new JPanel();
		
		panneau.setLayout(new BorderLayout());
		Box boxH = Box.createHorizontalBox();
		
		/* Labels */
		boxH.add(new JLabel(" Nom    "));
		boxH.add(new JLabel("Arr     "));
		boxH.add(new JLabel("Dur   "));
		panneau.add(boxH, BorderLayout.NORTH);
		
		/* ListBox */
		DefaultListModel model = new DefaultListModel();
		this.listeBoxInput = new JList(model);
		this.listeBoxInput.setFont(Options.FONT_LISTBOX);
		EcouteurListBoxInput ecouteurListBox = new EcouteurListBoxInput(this.manager);
		this.listeBoxInput.addKeyListener(ecouteurListBox);
		this.listeBoxInput.addListSelectionListener(ecouteurListBox);
		this.listeBoxInput.setBounds(0, hauteurGénérale, wListe, hauteurPanneau - hauteurGénérale);

		JScrollPane listScrollPane = new JScrollPane(this.listeBoxInput);
		listScrollPane.setBounds(0, hauteurGénérale, wListe, hauteurPanneau - hauteurGénérale);

		
		panneau.add(listScrollPane);

		
		return panneau;
	}

	private JPanel panneauCentre() // Panneau 2
	{
		JPanel panneau = new JPanel();
		panneau.setLayout(new GridLayout(2, 1));
		panneau.add(panneauCentreHaut()); // Panneau 2a
		panneau.add(panneauCentreBas()); // Panneau 2b
		return panneau;
	}
	
	private JPanel panneauCentreHaut() // Panneau 2a
	{
		JPanel panneau = new JPanel();
		panneau.setLayout(new FlowLayout());
		
		Box boxVerticalPrincipal = Box.createVerticalBox();
		
		JButton bouttonTop = new JButton("Tout en haut");
		bouttonTop.setToolTipText("Ctrl+Alt+HAUT");
		bouttonTop.setPreferredSize(this.dimensionBouton);
		bouttonTop.setMaximumSize(this.dimensionBouton);
		bouttonTop.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				    manager.upProcessTop();
				}
			}
		);
		boxVerticalPrincipal.add(bouttonTop);
		
		JButton bouttonUp = new JButton("Monter");
		bouttonUp.setToolTipText("Alt+HAUT");
		bouttonUp.setPreferredSize(this.dimensionBouton);
		bouttonUp.setMaximumSize(this.dimensionBouton);
		bouttonUp.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				    manager.upProcess();
				}
			}
		);
		boxVerticalPrincipal.add(bouttonUp);		
		
		JButton bouttonSwitch = new JButton("Switcher");
		bouttonSwitch.setToolTipText("S");
		bouttonSwitch.setPreferredSize(this.dimensionBouton);
		bouttonSwitch.setMaximumSize(this.dimensionBouton);
		bouttonSwitch.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				    manager.switchProcess();
				}
			}
		);
		boxVerticalPrincipal.add(Box.createVerticalStrut(10));
		boxVerticalPrincipal.add(bouttonSwitch);		
		boxVerticalPrincipal.add(Box.createVerticalStrut(10));
		
		JButton bouttonDown = new JButton("Descendre");
		bouttonDown.setToolTipText("Alt+BAS");
		bouttonDown.setPreferredSize(this.dimensionBouton);
		bouttonDown.setMaximumSize(this.dimensionBouton);
		bouttonDown.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				    manager.downProcess();
				}
			}
		);
		boxVerticalPrincipal.add(bouttonDown);	
		
		JButton bouttonBottom = new JButton("Tout en bas");
		bouttonBottom.setToolTipText("Ctrl+Alt+BAS");
		bouttonBottom.setPreferredSize(this.dimensionBouton);
		bouttonBottom.setMaximumSize(this.dimensionBouton);
		bouttonBottom.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				    manager.downProcessBottom();
				}
			}
		);
		boxVerticalPrincipal.add(bouttonBottom);

		panneau.add(boxVerticalPrincipal);
		
		return panneau;
	}
	
	private JPanel panneauCentreBas() // Panneau 2b
	{
		JPanel panneau = new JPanel();
		panneau.setLayout(new BorderLayout());
		
		EcouteurAjouter ecouteurAjouter = new EcouteurAjouter(this.manager); 
		Box boxHAjouter = Box.createHorizontalBox();
		
		this.textNom = new JTextField("P1");
		this.textNom.setToolTipText("Nom");
		this.textNom.setPreferredSize(this.dimensionTxtBox);
		this.textNom.setMaximumSize(this.dimensionTxtBox);
		this.textNom.addKeyListener(ecouteurAjouter);
		this.textNom.addFocusListener(ecouteurTextBox);
		boxHAjouter.add(this.textNom);
		
		this.textHeure = new JTextField("0");
		this.textHeure.setToolTipText("Heure d'arrivée");
		this.textHeure.setPreferredSize(this.dimensionTxtBox);
		this.textHeure.setMaximumSize(this.dimensionTxtBox);
		this.textHeure.addKeyListener(ecouteurAjouter);
		this.textHeure.addFocusListener(ecouteurTextBox);
		boxHAjouter.add(this.textHeure);
		
		this.textDuree = new JTextField("1");
		this.textDuree.setToolTipText("Durée");
		this.textDuree.setPreferredSize(this.dimensionTxtBox);
		this.textDuree.setMaximumSize(this.dimensionTxtBox);
		this.textDuree.addKeyListener(ecouteurAjouter);
		this.textDuree.addFocusListener(ecouteurTextBox);
		boxHAjouter.add(this.textDuree);

		
		JButton boutonAjouter = new JButton("Ajouter");
		boutonAjouter.setPreferredSize(this.dimensionBouton);
		boutonAjouter.setMaximumSize(this.dimensionBouton);
		boutonAjouter.addActionListener(ecouteurAjouter);
		
		Box boxVAjouter = Box.createVerticalBox();
		boxVAjouter.add(boxHAjouter);
		boxVAjouter.add(boutonAjouter);
		
		this.boutonModifier = new JButton("Modifier");
		this.boutonModifier.setPreferredSize(this.dimensionBouton);
		this.boutonModifier.setMaximumSize(this.dimensionBouton);
		this.boutonModifier.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					/**
					 * S'assure de ne créer qu'une seule instance de la fenêtre Modifier Processus
					 * Il n'y a pas d'autre altérnative à ce choix d'implémentation.
					 * On ne peut pas créer une FrameModifierProcessus dans le constructeur de cette classe
					 * car elle a besoin de l'existance complète du PanneauInterface.
					 */
					if(!FrameModifierProcessus.existance)
						frameModifier = new FrameModifierProcessus(100, 100, 200, 160, manager);
				    frameModifier.run();
				}
			}
		);
		this.boutonModifier.setEnabled(false);

		boxVAjouter.add(Box.createVerticalStrut(20));
		boxVAjouter.add(this.boutonModifier);
		
		JButton boutonSupprimer = new JButton("Supprimer");
		boutonSupprimer.setPreferredSize(this.dimensionBouton);
		boutonSupprimer.setMaximumSize(this.dimensionBouton);
		boutonSupprimer.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				    int[] indices = manager.getPanneauInterface().getListBoxInput().getSelectedIndices();
			    	if(checkVider.isSelected())
			    		manager.removeAllProcessus();
			    	else
					    if(indices.length > 0)
						    manager.removeProcessus(indices);
				}
			}
		);		
		boxVAjouter.add(Box.createVerticalStrut(20));
		boxVAjouter.add(boutonSupprimer);
		
		this.checkVider = new JCheckBox("Vider", true); // TODO faire un fichier ini avec les préférences 
		this.checkVider.setToolTipText("Si coché, vide la liste avant de générer, avant d'importer, après exporter ou en cliquant sur Supprimer");

		boxVAjouter.add(Box.createVerticalStrut(20));
		boxVAjouter.add(this.checkVider);

		panneau.add(boxVAjouter, BorderLayout.CENTER);
		
		return panneau;
	}
	
	private JPanel panneauDroite() // Panneau 3
	{
		JPanel panneau = new JPanel();
		panneau.setLayout(new GridLayout(2, 1));
		panneau.add(panneauDroiteHaut());
		panneau.add(panneauDroiteBas());
		return panneau;
	}

	private JPanel panneauDroiteHaut() // Panneau 3a
	{
		JPanel panneau = new JPanel();
		panneau.setLayout(new BorderLayout());
		
		Box boxV = Box.createVerticalBox();
		
		JButton boutonFCFS = new JButton("FCFS");
		boutonFCFS.setPreferredSize(this.dimensionBouton);
		boutonFCFS.setMaximumSize(this.dimensionBouton);
		boutonFCFS.setToolTipText("1er arrivé, 1er servi");
		boutonFCFS.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					manager.applyFCFS();
				}
			}
		);
		boxV.add(Box.createVerticalStrut(5));
		boxV.add(boutonFCFS);
		
		JButton boutonSJF = new JButton("SJF");
		boutonSJF.setToolTipText("Le plus court en 1er");
		boutonSJF.setPreferredSize(this.dimensionBouton);
		boutonSJF.setMaximumSize(this.dimensionBouton);
		boutonSJF.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					manager.applySJF();
				}
			}
		);
		boxV.add(boutonSJF);
		
		JButton boutonSJFNot = new JButton("SJFNot");
		boutonSJFNot.setPreferredSize(this.dimensionBouton);
		boutonSJFNot.setMaximumSize(this.dimensionBouton);
		boutonSJFNot.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					manager.applySJFNot();
				}
			}
		);
		boxV.add(boutonSJFNot);
		
		JButton boutonRoundRobin = new JButton("RoundRobin");
		boutonRoundRobin.setPreferredSize(this.dimensionBouton);
		boutonRoundRobin.setMaximumSize(this.dimensionBouton);
		boutonRoundRobin.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										manager.applyRoundRobin();
									}
								}
		);
		boxV.add(boutonRoundRobin);
		
		this.textQuantum = new JTextField("5");
		this.textQuantum.setPreferredSize(this.dimensionBouton);
		this.textQuantum.setMaximumSize(this.dimensionBouton);
		this.textQuantum.setToolTipText("Nombre de quantum");
		this.textQuantum.addKeyListener(new KeyListener()
					{
						public void  keyPressed(KeyEvent e)
						{
							if(e.getKeyCode() == 10) // Enter
								manager.applyRoundRobin();
						}
						
						public void keyReleased(KeyEvent e)
						{
							
						}
						
						public void keyTyped(KeyEvent e)
						{
			
						}
			
					}
		);
		boxV.add(this.textQuantum);
		
		panneau.add(boxV, BorderLayout.CENTER);

		
		return panneau;
	}

	private JPanel panneauDroiteBas() // Panneau 3b
	{
		JPanel panneau = new JPanel();
		panneau.setLayout(new BorderLayout());
		
		EcouteurBoutonGenerer ecouteurGenerer = new EcouteurBoutonGenerer(this.manager);
		Box boxVTxtGen = Box.createVerticalBox();
		Box boxHTxtGen = Box.createHorizontalBox();
		
		this.txtGenNombre = new JTextField();
		this.txtGenNombre.setPreferredSize(this.dimensionTxtBox);
		this.txtGenNombre.setMaximumSize(this.dimensionTxtBox);
		this.txtGenNombre.addFocusListener(ecouteurTextBox);
		this.txtGenNombre.addKeyListener(ecouteurGenerer);
		this.txtGenNombre.setToolTipText("Nombre de processus à générer");
		this.txtGenNombre.setText("10");
		boxHTxtGen.add(this.txtGenNombre);
		
		this.txtGenArrivee = new JTextField();
		this.txtGenArrivee.setPreferredSize(this.dimensionTxtBox);
		this.txtGenArrivee.setMaximumSize(this.dimensionTxtBox);
		this.txtGenArrivee.addFocusListener(ecouteurTextBox);
		this.txtGenArrivee.addKeyListener(ecouteurGenerer);
		this.txtGenArrivee.setToolTipText("Heure d'arrivée max");
		this.txtGenArrivee.setText("50");
		boxHTxtGen.add(this.txtGenArrivee);
		
		this.txtGenDuree = new JTextField();
		this.txtGenDuree.setPreferredSize(this.dimensionTxtBox);
		this.txtGenDuree.setMaximumSize(this.dimensionTxtBox);
		this.txtGenDuree.addFocusListener(ecouteurTextBox);
		this.txtGenDuree.addKeyListener(ecouteurGenerer);
		this.txtGenDuree.setToolTipText("Durée max");
		this.txtGenDuree.setText("30");
		boxHTxtGen.add(this.txtGenDuree);
		
		JButton boutonGenerer = new JButton("Générer");
		boutonGenerer.setPreferredSize(this.dimensionBouton);
		boutonGenerer.setMaximumSize(this.dimensionBouton);
		boxVTxtGen.add(boxHTxtGen);
		boxVTxtGen.add(boutonGenerer);


		boutonGenerer.addActionListener(ecouteurGenerer);
		
		JButton boutonImporter = new JButton("Importer");
		boutonImporter.setPreferredSize(this.dimensionBouton);
		boutonImporter.setMaximumSize(this.dimensionBouton);

		boutonImporter.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					manager.ouvrirFichier(checkVider.isSelected());
				}
			}
		);
		
		JButton boutonExporter = new JButton("Exporter");
		boutonExporter.setPreferredSize(this.dimensionBouton);
		boutonExporter.setMaximumSize(this.dimensionBouton);
		boutonExporter.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					manager.enregistrerFichier(checkVider.isSelected());
				}
			}
		);
	
		JButton boutonSupprimer = new JButton("Supprimer");
		boutonSupprimer.setPreferredSize(this.dimensionBouton);
		boutonSupprimer.setMaximumSize(this.dimensionBouton);
		boutonSupprimer.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				    int[] indices = manager.getPanneauInterface().getListBoxInput().getSelectedIndices();
			    	if(checkVider.isSelected())
			    		manager.removeAllProcessus();
			    	else
					    if(indices.length > 0)
						    manager.removeProcessus(indices);
				}
			}
		);		
		
		Box boxV = Box.createVerticalBox();
		
		boxV.add(boxHTxtGen);
		boxV.add(boutonGenerer);
		this.checkShuffle = new JCheckBox("Mélanger", true); // TODO faire ça dans Options / Parametres
		this.checkShuffle.setToolTipText("Activé : Mélange les processus au lieu de les laisser trié par ordre d'arrivée");
		
		boxV.add(this.checkShuffle);
		boxV.add(Box.createVerticalStrut(20));
		boxV.add(boutonImporter);
		boxV.add(boutonExporter);
		boxV.add(Box.createVerticalStrut(20));
		
		this.checkSupprimerHisto = new JCheckBox("Supprimer Historique", true); // TODO faire ça dans Options / Parametres TODO faire un fichier ini avec les préférences
		this.checkSupprimerHisto.setToolTipText("Activé : Supprime automatiquement les historiques à la fin de l'exécution du programme");
		
		boxV.add(this.checkSupprimerHisto);
		
		panneau.add(boxV);
		
		return panneau;
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private JList listeBoxInput;
	private Manager manager;
	private FrameModifierProcessus frameModifier;
	
	private JTextField textNom;
	private JTextField textHeure;
	private JTextField textDuree;
	private JTextField textQuantum;
	
	private JTextField txtGenArrivee;
	private JTextField txtGenDuree;
	private JTextField txtGenNombre;

	private JCheckBox checkVider;
	private JCheckBox checkSupprimerHisto;
	private JCheckBox checkShuffle;
	
	private Dimension dimensionBouton;
	private Dimension dimensionTxtBox;
	
	private JButton boutonModifier;
	
	private int wListe;
	private int hauteurPanneau;
	private EcouteurTextBox ecouteurTextBox ;

	/*-----------------------------------------*\
	|*				Statiques				   *|
	\*-----------------------------------------*/
	private static int hauteurGénérale = 30; // Taille en Y (hauteur) de la plupart des composants (bouton, textBox, ...)
}
