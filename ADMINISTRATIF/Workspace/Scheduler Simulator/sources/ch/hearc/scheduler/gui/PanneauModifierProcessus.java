
//Specification:
package ch.hearc.scheduler.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ch.hearc.scheduler.gui.ecouteur.EcouteurModifier;
import ch.hearc.scheduler.gui.tools.PanelCardsGraphics;
import ch.hearc.scheduler.moo.Manager;
import ch.hearc.scheduler.moo.Processus;


public class PanneauModifierProcessus extends PanelCardsGraphics
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public PanneauModifierProcessus(int w, int h, FrameModifierProcessus parent, Manager manager)
	{
		this.manager = manager;
		this.dimTxtBox = new Dimension(60, 30);
		this.dimBouton = new Dimension((int)(3*this.dimTxtBox.getWidth()/2), 30);
		this.add(zoneCentre());
		this.parent = parent;
	}
	
	/*-----------------------------------------------------------------*\
	|*						Méthodes Publiques						   *|
	\*-----------------------------------------------------------------*/
	public void run()
	{
		this.indice = this.manager.getPanneauInterface().getListBoxInput().getSelectedIndex();
		this.process = this.manager.getListeProcessus().get(this.indice);
		this.txtNom.setText(this.process.getNom());
		this.txtArrivee.setText(""+this.process.getHeureArrivee());
		this.txtDuree.setText(""+this.process.getDuree());
	}
	
	/*-------------------------*\
	|*			Get			   *|
	\*-------------------------*/
	public Processus getProcess()
	{
		return this.process;
	}
	
	public int getIndice()
	{
		return this.indice;
	}
	
	public Processus getNewProcess()
	{
		return new Processus(this.txtNom.getText(), Integer.parseInt(this.txtArrivee.getText()), Integer.parseInt(this.txtDuree.getText()));
	}
	
	public FrameModifierProcessus getFrameParent()
	{
		return this.parent;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes Privées					   *|
	\*-----------------------------------------------------------------*/
	private Container zoneCentre()
	{
		Box boxV = Box.createVerticalBox();
		Box boxHLabel = Box.createHorizontalBox();
		Box boxHTxt = Box.createHorizontalBox();
		Box boxHBouton = Box.createHorizontalBox();
		
		JLabel labelNom = new JLabel("Nom");
		labelNom.setPreferredSize(this.dimTxtBox);
		labelNom.setMaximumSize(this.dimTxtBox);
		boxHLabel.add(labelNom);
		
		JLabel labelArrivée = new JLabel("Arrivée");
		labelArrivée.setPreferredSize(this.dimTxtBox);
		labelArrivée.setMaximumSize(this.dimTxtBox);
		boxHLabel.add(labelArrivée);
		
		JLabel labelDuree = new JLabel("Durée");
		labelDuree.setPreferredSize(this.dimTxtBox);
		labelDuree.setMaximumSize(this.dimTxtBox);
		boxHLabel.add(labelDuree);
				
		EcouteurModifier ecouteur = new EcouteurModifier(manager, this);

		this.txtNom = new JTextField();
		this.txtNom.setPreferredSize(this.dimTxtBox);
		this.txtNom.setMaximumSize(this.dimTxtBox);
		this.txtNom.addKeyListener(ecouteur);
		boxHTxt.add(this.txtNom);
		
		this.txtArrivee = new JTextField();
		this.txtArrivee.setPreferredSize(this.dimTxtBox);
		this.txtArrivee.setMaximumSize(this.dimTxtBox);
		this.txtArrivee.addKeyListener(ecouteur);
		boxHTxt.add(this.txtArrivee);
		
		this.txtDuree = new JTextField();
		this.txtDuree.setPreferredSize(this.dimTxtBox);
		this.txtDuree.setMaximumSize(this.dimTxtBox);
		this.txtDuree.addKeyListener(ecouteur);
		boxHTxt.add(this.txtDuree);
		
		JButton boutonModifier = new JButton("Modifier");
		boutonModifier.setPreferredSize(this.dimBouton);
		boutonModifier.setMaximumSize(this.dimBouton);
		boutonModifier.addActionListener(ecouteur);
		boxHBouton.add(boutonModifier);
		
		JButton boutonAnnuler = new JButton("Annuler");
		boutonAnnuler.setPreferredSize(this.dimBouton);
		boutonAnnuler.setMaximumSize(this.dimBouton);
		boutonAnnuler.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								parent.close();
							}
						}
		);
		boxHBouton.add(boutonAnnuler);
		
		boxV.add(boxHLabel);
		boxV.add(boxHTxt);
		boxV.add(boxHBouton);
		
		return boxV;
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs Privés					   *|
	\*-----------------------------------------------------------------*/
	private JTextField txtNom;
	private JTextField txtArrivee;
	private JTextField txtDuree;
	
	private Dimension dimTxtBox;
	private Dimension dimBouton;
	
	private FrameModifierProcessus parent;
	
	private Processus process;
	private int indice;
	
	private Manager manager;
}

