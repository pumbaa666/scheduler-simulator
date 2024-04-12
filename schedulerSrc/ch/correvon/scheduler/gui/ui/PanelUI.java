// Specification:
package ch.correvon.scheduler.gui.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import ch.correvon.scheduler.algorithms.ButtonAlgorithm;
import ch.correvon.scheduler.gui.algorithm.PanelAlgorithm;
import ch.correvon.scheduler.gui.painting.PanelPainting;
import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.jTable.Abstract_ProcessTableModel;
import ch.correvon.scheduler.moo.jTable.SimpleProcessRowSorter;
import ch.correvon.scheduler.moo.jTable.SimpleProcessTableModel;
import ch.correvon.scheduler.moo.myObjects.MyJTextField;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.ProcessType;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Panneau contenant toute l'interface utilisateur
 */
public class PanelUI extends JPanel implements ComponentListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public PanelUI(Manager manager, FrameUI frameUI)
	{
		this.manager = manager;
		this.txtBoxListener = new TextBoxListener();
		this.creatComponents();
		this.fillComponents();
		this.frameUI = frameUI;
		this.frameUI.addComponentListener(this);
		this.setDataChanged(false);
	}

	/*-----------------------------------------------------------------*\
	|*						Méthodes Publiques						   *|
	\*-----------------------------------------------------------------*/
	public void addProcess(String name, int incomingTime, int duration, ProcessType type)
	{
		addProcess(new Process(name, incomingTime, duration, type));
	}
	
	public void addProcess(Process process)
	{
		this.table_model.addRow(process);
		this.setDataChanged(true);
	}
	
	public void removeProcess(int[] tab)
	{
	    for(int i = 0; i < tab.length; i++)
			this.table_model.removeRow(tab[i]-i);
		this.setDataChanged(true);
	}
	
	public void removeAllProcess()
	{
		this.table_model.removeAll();
		this.setDataChanged(true);
	}

	public void updateInterface()
	{
		this.table_model.update(); // met à jour les type dans la liste de processus
		if(this.tbpane.getTabCount() > 0)
		{
			PanelAlgorithm p = (PanelAlgorithm)this.tbpane.getSelectedComponent();
			p.repaint(); // redessine le panneau sélectionné pour mettre à jour la couleur des types
			p.getPanelAlgoView().getFrameStatistic().getPanelStatistic().update();
		}
	}
	
	public void updateTypeProcess()
	{
		this.comboType.setModel(new DefaultComboBoxModel(new Vector<ProcessType>(this.manager.getProcessType()))); // Ne pas faire un seul Vector pour les 2 combos, car ajouter un "Aléatoire" à 1 des 2, le rajoute aux 2.
		this.comboGenerateType.setModel(new DefaultComboBoxModel(new Vector<ProcessType>(this.manager.getProcessType())));
		this.comboGenerateType.addItem(new ProcessType("Aléatoire"));
		this.comboGenerateType.setSelectedIndex(this.comboGenerateType.getItemCount()-1);
	}
	
	public void addPanneauAlgo(PanelPainting panelPainting)
	{
		this.tbpane.add(panelPainting.getName(), panelPainting);
	}
	
	/*public void print()
	{
		PanelAlgorithm selectedPanel = (PanelAlgorithm)this.tbpane.getSelectedComponent();
		if(selectedPanel != null)
		{
			PanelPrintable panel = (PanelPrintable)selectedPanel.getTbpane().getSelectedComponent();
			if(panel != null)
				panel.print();
		}
		else
			JOptionPane.showMessageDialog(this, "Il n'y a rien à imprimer", "Erreur", JOptionPane.OK_OPTION);
	}*/
	
	/**
	 * Méthode appelé dans AddProcessButtonListener.
	 * Quand on ajoute manuellement un processus à la liste, ça incrémente
	 * automatiquement le numéro après le nom, pour plus de souplesse
	 */
	public void incrementNumberProcess()
	{
		int nb = -1;
		int i = this.txtName.getText().toString().length();
		int stringLength = i;
		boolean stop = false;
		String txtName = this.txtName.getText();
		String name = "";
		while(!stop && i != 0)
		{
			try
			{
				nb = Integer.parseInt(txtName.substring(i-1, stringLength-0));
			}
			catch (NumberFormatException e)
			{
				stop = true;
				name = txtName.substring(0, i);
			}
			i--;
		}
		if(nb != -1)
			this.txtName.setText(name+(nb+1));
	}
	
	public void valideInput()
	{
		for(ActionListener listener:this.buttonValidate.getActionListeners())
			listener.actionPerformed(new ActionEvent(this, 0, "AutoValidation"));
	}
	
	public void raz()
	{
		if(this.manager.askForSave())
			this.clean();
	}
	
	public void quit()
	{
		Hashtable<String, String> pref = this.manager.getPreferances();
		pref.put("checkShuffle", ""+this.checkShuffle.isSelected());
		pref.put("checkClear", ""+this.checkClear.isSelected());
		pref.put("txtGenerateNumber", this.txtGenerateNumber.getText());
		pref.put("txtGenerateIncoming", this.txtGenerateIncoming.getText());
		pref.put("txtGenerateDuration", this.txtGenerateDuration.getText());
		
		PanelAlgorithm panelAlgo = (PanelAlgorithm)this.tbpane.getSelectedComponent();
		if(panelAlgo != null)
		{
			pref.put("sliderValue", ""+panelAlgo.getPanelAlgoView().getSliderValue());
			pref.put("checkUseAutoScale", ""+panelAlgo.getPanelAlgoView().getCheckUseAutoScaleValue());
		}
	}

	public void showButtons(boolean value)
	{
		for(JButton button:this.tabButton)
			button.setVisible(value);
	}
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setDataChanged(boolean value)
	{
		this.dataChanged = value;
	}
	
	public void setListAlgorithm(ArrayList<ButtonAlgorithm> list)
	{
		this.listButtonAlgorithm = list;
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public boolean getDataChanged()
	{
		return this.dataChanged;
	}
	
	public JPanel getBuilder()
	{
		return this.builder.getPanel();
	}

	public JTextField[] getGenerateTxtField()
	{
		JTextField[] tabTxt = new JTextField[3];
		tabTxt[0] = this.txtGenerateIncoming;
		tabTxt[1] = this.txtGenerateDuration;
		tabTxt[2] = this.txtGenerateNumber;
		return tabTxt;
	}
	
	public String getProcess(int lastNumberInName)
	{
		String name = this.txtName.getText().trim();
		int time = 0;
		int duration = 0;
		
		if(name.length() == 0)
			name = this.manager.getPreferances("txtDefaultProcessName")+lastNumberInName;
		if(name.length() > 4)
		{
			JOptionPane.showMessageDialog(this, "Le nom est incorrect ! Il faut au entre 1 et 4 lettres", "Erreur", JOptionPane.OK_OPTION);
			return null;
		}
		
		try
		{
			time = Integer.parseInt(this.txtTime.getText());
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, "L'heure d'arrivée est incorrecte ! Il faut un nombre entier", "Erreur", JOptionPane.OK_OPTION);
			return null;
		}
		
		try
		{
			duration = Integer.parseInt(this.txtDuree.getText());
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, "La durée est incorrecte ! Il faut un nombre entier", "Erreur", JOptionPane.OK_OPTION);
			return null;
		}
		
		if(duration <= 0)
		{
			JOptionPane.showMessageDialog(this, "La durée est incorrecte ! Elle doit être entière positive", "Erreur", JOptionPane.OK_OPTION);
			return null;
		}
		
		Object selectedType = this.comboType.getSelectedItem();
		if(selectedType == null)
		{
			JOptionPane.showMessageDialog(this, "Choisissez un type.\nSi aucun type n'apparait dans la boite de types, allez en créer dans les préférances", "Erreur", JOptionPane.OK_OPTION);
			return null;
		}
		
		return name+", "+time+", "+duration+", "+this.comboType.getSelectedItem().toString();
	}
	
	public JScrollPane getInputTable()
	{
		return this.listScrollPane;
	}
	
	public Abstract_ProcessTableModel getTableModel()
	{
		return this.table_model;
	}
	
	public JTextField getTextBoxTime()
	{
		return this.txtTime;
	}
	
	public boolean getCheckboxClearValue()
	{
		return this.checkClear.isSelected();
	}

	public boolean getValueCheckboxShuffle()
	{
		return this.checkShuffle.isSelected();
	}
	
	public ProcessType getComboGenerateType()
	{
		return (ProcessType)this.comboGenerateType.getSelectedItem();
	}
	
	public PanelAlgorithm getSelectedAlgorithmPane()
	{
		return (PanelAlgorithm)this.tbpane.getSelectedComponent();
	}
	
	/*-------------------------------------------------*\
	|*					Listener					   *|
	\*-------------------------------------------------*/
	/*-----------------------------------------*\
	|*					Fenêtre			 	   *|
	\*-----------------------------------------*/
	public void componentShown(ComponentEvent e)
	{
		this.setColumnCombo(3);
		this.showButtons(this.manager.getPreferances("checkShowButtons").compareTo("true") == 0);
	}
	
	public void componentMoved(ComponentEvent e)
	{
	}
	
	public void componentResized(ComponentEvent e)
	{
	}
	
	public void componentHidden(ComponentEvent e)
	{
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes Privées					   *|
	\*-----------------------------------------------------------------*/
	private void creatComponents()
	{
		this.checkShuffle = new JCheckBox("Mélanger");
		this.checkShuffle.setToolTipText("Activé : Mélange les processus au lieu de les laisser triés par ordre d'arrivée");
		this.checkShuffle.setSelected(this.manager.getPreferances("checkShuffle").compareTo("true") == 0);

		AddProcessButtonListener ecouteurAjouter = new AddProcessButtonListener(this.manager); 
		
		this.txtName = new MyJTextField(3);
		this.txtName.setToolTipText("Nom");
		this.txtName.setPreferredSize(new Dimension((int)this.txtName.getPreferredSize().getWidth(), 25));
		this.txtName.addKeyListener(ecouteurAjouter);
		this.txtName.addFocusListener(txtBoxListener);
		
		this.txtTime = MyJTextField.cloneOf(this.txtName);
		this.txtTime.setToolTipText("Heure d'arrivée");
		this.txtTime.addKeyListener(ecouteurAjouter);
		this.txtTime.addFocusListener(txtBoxListener);
		
		this.txtDuree = MyJTextField.cloneOf(this.txtName);
		this.txtDuree.setToolTipText("Durée");
		this.txtDuree.addKeyListener(ecouteurAjouter);
		this.txtDuree.addFocusListener(txtBoxListener);
		
		this.comboType = new JComboBox();
		this.comboType.setToolTipText("Type de processus");
		this.comboType.setPreferredSize(new Dimension(65, this.comboType.getPreferredSize().height));

		this.comboGenerateType = new JComboBox();
		this.comboGenerateType.setToolTipText("Type de processus");
		this.comboGenerateType.setPreferredSize(new Dimension(65, this.comboType.getPreferredSize().height));

		JButton buttonAdd = new JButton("Ajouter");
		buttonAdd.addActionListener(ecouteurAjouter);
		
		JButton buttonDelete = new JButton("Supprimer");
		buttonDelete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
			    	if(checkClear.isSelected())
			    	{
						if(JOptionPane.showConfirmDialog(	null, 
															"Voulez-vous vraiment tout supprimer ?\n(Pour ne supprimer qu'un ou plusieurs éléments distincts, décochez la case Vider, title, optionType)", 
															"Tout vider", 
															JOptionPane.YES_NO_OPTION)
													== 0)
						{
								removeAllProcess();
						}
			    	}
			    	else
			    	{
					    int[] indices = tableProcess.getSelectedRows();
					    if(indices.length > 0)
					    	removeProcess(indices);
			    	}
				}
			}
		);		
		
		this.checkClear = new JCheckBox("Vider");
		this.checkClear.setToolTipText("Si coché, vide la liste avant de générer, avant d'importer, après avoir exporté ou en cliquant sur Supprimer");
		this.checkClear.setSelected(this.manager.getPreferances("checkClear").compareTo("true") == 0);

		GenerateButtonListener buttonGenerateListener = new GenerateButtonListener(this.manager);
		
		this.txtGenerateNumber = MyJTextField.cloneOf(this.txtName);
		this.txtGenerateNumber.addFocusListener(txtBoxListener);
		this.txtGenerateNumber.addKeyListener(buttonGenerateListener);
		this.txtGenerateNumber.setToolTipText("Nombre de processus à générer");

		this.txtGenerateIncoming = MyJTextField.cloneOf(this.txtName);
		this.txtGenerateIncoming.addFocusListener(txtBoxListener);
		this.txtGenerateIncoming.addKeyListener(buttonGenerateListener);
		this.txtGenerateIncoming.setToolTipText("Heure d'arrivée max");
		
		this.txtGenerateDuration = MyJTextField.cloneOf(this.txtName);
		this.txtGenerateDuration.addFocusListener(txtBoxListener);
		this.txtGenerateDuration.addKeyListener(buttonGenerateListener);
		this.txtGenerateDuration.setToolTipText("Durée max");
		

		this.tableProcess = new JTable();
		this.table_model = new SimpleProcessTableModel(this.tableProcess, this.manager);
		this.tableProcess.setModel(this.table_model);
		this.tableProcess.addKeyListener(new JTableListener(this.tableProcess));
		this.tableProcess.setRowSorter(new SimpleProcessRowSorter<Abstract_ProcessTableModel>(this.table_model));

		final JButton buttonTop = new JButton("Tout en haut");
		buttonTop.setToolTipText("Ctrl+Alt+HAUT");
		buttonTop.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					table_model.upTop();
				}
			}
		);
		
		final JButton buttonUp = new JButton("Monter");
		buttonUp.setToolTipText("Alt+HAUT");
		buttonUp.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					table_model.up();
				}
			}
		);
		
		final JButton buttonSwitch = new JButton("Echanger");
		buttonSwitch.setToolTipText("S");
		buttonSwitch.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					table_model.switchElements();
				}
			}
		);
		
		final JButton buttonDown = new JButton("Descendre");
		buttonDown.setToolTipText("Alt+BAS");
		buttonDown.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					table_model.down();
				}
			}
		);
		
		final JButton buttonBottom = new JButton("Tout en bas");
		buttonBottom.setToolTipText("Ctrl+Alt+BAS");
		buttonBottom.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					table_model.downBottom();
				}
			}
		);
		
		this.tabButton = new JButton[5];
		this.tabButton[0] = buttonTop;
		this.tabButton[1] = buttonUp;
		this.tabButton[2] = buttonSwitch;
		this.tabButton[3] = buttonDown;
		this.tabButton[4] = buttonBottom;

		JButton buttonGenerate = new JButton("Générer");
		buttonGenerate.addActionListener(buttonGenerateListener);
		
		this.buttonValidate = new JButton("Valider");
		this.buttonValidate.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(manager.getListProcess().size() != 0)
					{
						String name = "Algorithme "+nbPanelAlgorithm;
						PanelAlgorithm panelAlgorithm = new PanelAlgorithm(name, listButtonAlgorithm, manager);
				        tbpane.add(name, panelAlgorithm);
				        tbpane.setSelectedComponent(panelAlgorithm);
				        if(manager.getPreferances("checkAutoRunAlgo").compareTo("false") != 0)
				        	panelAlgorithm.runAlgorithms();
				        nbPanelAlgorithm++;
					}
				}
			}
		);
		
		this.tbpane = new JTabbedPane();
		this.tbpane.addMouseListener(new TBPaneListener(this.tbpane));
		
		FormLayout layout = new FormLayout(
				//1    2     3     4     5     6      7     8     9     10    11    12    13    14    15       
				"pref, pref, pref, pref, 10dlu,pref:grow", //columns
				"50dlu:grow, 25dlu,pref, pref, pref, 25dlu, pref, pref, 25dlu,pref, pref, 25dlu, pref, pref, pref, pref");//rows

	   layout.setColumnGroups(new int[][]{{1, 2, 3}});
	   layout.setRowGroups(new int[][]{{3, 4, 5, 7, 8, 10, 11, 13, 14, 15}, {2, 6, 9, 12}});
	   
	   this.builder = new PanelBuilder(layout);
	   
	   // Petite marge par rapport au Panel
	   this.builder.setDefaultDialogBorder();
	       
	   // Création de l'objet contrainte
	   CellConstraints cellConstraints = new CellConstraints();
	
		this.listScrollPane = new JScrollPane(this.tableProcess);
		this.listScrollPane.setPreferredSize(new Dimension(this.listScrollPane.getPreferredSize().width, Integer.MAX_VALUE));
	   // Ajout des champs dans le builder
	   this.builder.add(this.listScrollPane,        cellConstraints.xyw  (1  , 1 	, 4 	      ));
	   
	   this.builder.add(this.txtName,        		cellConstraints.xyw  (1  , 3 	, 1           ));
	   this.builder.add(this.txtTime,          		cellConstraints.xyw  (2  , 3 	, 1           ));
	   this.builder.add(this.txtDuree,          	cellConstraints.xyw  (3  , 3 	, 1           ));
	   this.builder.add(this.comboType,         	cellConstraints.xyw  (4  , 3 	, 1           ));
	   this.builder.add(buttonAdd,          		cellConstraints.xyw  (1  , 4 	, 3           ));
	   this.builder.add(this.checkClear,        	cellConstraints.xyw  (4  , 4	, 1           ));
	   this.builder.add(buttonDelete,        		cellConstraints.xyw  (1  , 5	, 3           ));
	   
	   this.builder.add(this.txtGenerateNumber,  	cellConstraints.xyw  (1  , 7 	, 1           ));
	   this.builder.add(this.txtGenerateIncoming,  	cellConstraints.xyw  (2  , 7 	, 1           ));
	   this.builder.add(this.txtGenerateDuration,  	cellConstraints.xyw  (3  , 7 	, 1           ));
	   this.builder.add(this.comboGenerateType,  	cellConstraints.xyw  (4  , 7 	, 1           ));
	   this.builder.add(buttonGenerate, 			cellConstraints.xyw  (1  , 8 	, 3           ));
	   this.builder.add(this.checkShuffle, 			cellConstraints.xyw  (4  , 8	, 1           ));
	   
	   this.builder.add(this.buttonValidate,		cellConstraints.xyw  (1  , 10	, 4           ));
	   
	   this.builder.add(buttonTop,					cellConstraints.xyw  (1  , 13	, 3           ));
	   this.builder.add(buttonUp,					cellConstraints.xyw  (4  , 13	, 1           ));
	   this.builder.add(buttonSwitch,				cellConstraints.xyw  (1  , 14	, 4           ));
	   this.builder.add(buttonDown,					cellConstraints.xyw  (1  , 15	, 3           ));
	   this.builder.add(buttonBottom,				cellConstraints.xyw  (4  , 15	, 1           ));
	   
	   this.builder.add(this.tbpane, 				cellConstraints.xywh (6  , 1 	, 1     , 15  ));
	}
	
	private void fillComponents()
	{
		String temp = this.manager.getPreferances("txtDefaultProcessName");
		if(temp.compareTo("NaN") == 0)
			temp = "P";
		this.txtName.setText(temp+"0");
		this.txtTime.setText("0");
		this.txtDuree.setText("1");
		temp = this.manager.getPreferances("txtGenerateNumber");
		if(temp.compareTo("NaN") == 0)
			temp = "10";
		this.txtGenerateNumber.setText(temp);
		temp = this.manager.getPreferances("txtGenerateIncoming");
		if(temp.compareTo("NaN") == 0)
			temp = "20";
		this.txtGenerateIncoming.setText(temp);
		temp = this.manager.getPreferances("txtGenerateDuration");
		if(temp.compareTo("NaN") == 0)
			temp = "20";
		this.txtGenerateDuration.setText(temp);
	}
	
	/**
	 * Fait de la colone 3 (celle des type) une combobox contenant tout les types
	 */
	private void setColumnCombo(int columnIndex)
	{
		TableColumn column = this.tableProcess.getColumnModel().getColumn(columnIndex);
		column.setCellEditor(new DefaultCellEditor(new JComboBox(this.manager.getProcessType().toArray())));
	}
	
	/**
	 * Supprime tout les onglets du tbpane et supprime tout les processus de la liste d'entrée (tableProcess)
	 */
	private void clean()
	{
		PanelAlgorithm panelAlgoView;
		for(Component component:this.tbpane.getComponents())
		{
			panelAlgoView = (PanelAlgorithm)component;
			panelAlgoView.getPanelAlgoView().getFrameStatistic().close();
		}

		this.fillComponents();
		this.tbpane.removeAll();
		this.table_model.removeAll();
		this.nbPanelAlgorithm = 0;
		this.dataChanged = false;
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
	private FrameUI frameUI;
	
	private JTable tableProcess;
	private Abstract_ProcessTableModel table_model;
	private JScrollPane listScrollPane;
	
	private MyJTextField txtName;
	private MyJTextField txtTime;
	private MyJTextField txtDuree;
	private JComboBox comboType;
	private JComboBox comboGenerateType;
	
	private MyJTextField txtGenerateIncoming;
	private MyJTextField txtGenerateDuration;
	private MyJTextField txtGenerateNumber;
	private TextBoxListener txtBoxListener;

	private JCheckBox checkClear;
	private JCheckBox checkShuffle;
	private JButton[] tabButton;
	private JButton buttonValidate;
	
	private PanelBuilder builder;
	
	private ArrayList<ButtonAlgorithm> listButtonAlgorithm;
	private JTabbedPane tbpane;
	private int nbPanelAlgorithm = 0;
	
	private boolean dataChanged;
}

