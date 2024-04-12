
//Specification:
package ch.correvon.scheduler.gui.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import ch.correvon.scheduler.gui.ui.TextBoxListener;
import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.Tools;
import ch.correvon.scheduler.moo.jTable.CheckboxTableModel;
import ch.correvon.scheduler.moo.jTable.JTableButtonMouseListener;
import ch.correvon.scheduler.moo.jTable.JTableButtonRenderer;
import ch.correvon.scheduler.moo.jTable.SimplifiedCheckBox;
import ch.correvon.scheduler.moo.jTable.StatisticTableModel;
import ch.correvon.scheduler.moo.myObjects.MyJList;
import ch.correvon.scheduler.moo.myObjects.MyJTextField;
import ch.correvon.scheduler.moo.process.ProcessType;
import ch.correvon.scheduler.moo.process.Statistic;
import ch.correvon.scheduler.plugin.Algo_I;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class PanelOptions extends JPanel implements WindowListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public PanelOptions(Manager manager, FrameOptions frameOptions)
	{
		this.manager = manager;
		this.frameOptions = frameOptions;
		this.frameOptions.addWindowListener(this);
		
		this.buttonDimension = new Dimension(90, 25);
		this.txtDimension = new Dimension(40, 25);
		this.fillComponent = true;
	}
	
	/*-----------------------------------------------------------------*\
	|*						Méthodes publiques						   *|
	\*-----------------------------------------------------------------*/
	public void enableButtons(boolean value)
	{
		this.buttonColor.setEnabled(value);
		this.buttonEditType.setEnabled(value);
	}
	
	public void run()
	{
		this.createComponents();
		this.organizeComponents();
		this.fillComponents();
		this.frameOptions.pack();
	}
	
	/*---------------------------------------------*\
	|*					Listener				   *|
	\*---------------------------------------------*/
	public void windowActivated(WindowEvent e)
	{
		if(this.listBoxType != null)
		{
			if(this.fillComponent)
			{
				this.listBoxType.setSelectedIndices(new int[]{});
				fillComponents();
			}
			else
				this.fillComponent = true;
		}
	}

	public void windowClosed(WindowEvent e)
	{
	}

	public void windowClosing(WindowEvent e)
	{
	}

	public void windowDeactivated(WindowEvent e)
	{
	}

	public void windowDeiconified(WindowEvent e)
	{
	}

	public void windowIconified(WindowEvent e)
	{
	}

	public void windowOpened(WindowEvent e)
	{
	}
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setTxtBoxType()
	{
		int nbSelectedIndices = this.listBoxType.getSelectedIndices().length;
		if(nbSelectedIndices == 1)
		{
			ProcessType type = (ProcessType)this.listBoxType.getSelectedValue();
			this.txtType.setText(type.toString());
			this.txtType.setEnabled(true);

			this.txtPriority.setText(""+type.getPriority());
			this.txtPriority.setEnabled(true);

			this.buttonColor.setBackground(type.getColor());
			this.buttonColor.setEnabled(true);
			
			this.comboAlgo.setEnabled(true);
			DefaultComboBoxModel model = (DefaultComboBoxModel)this.comboAlgo.getModel();
			ArrayList<Algo_I> listAlgo = new ArrayList<Algo_I>();
			for(int i = 0; i < model.getSize(); i++)
				listAlgo.add((Algo_I)model.getElementAt(i));
			
			Algo_I algo = type.getAlgo();
			if(algo != null)
				this.comboAlgo.setSelectedIndex(Tools.searchAlgo(algo.getName(), listAlgo));
			else
				this.comboAlgo.setSelectedIndex(-1);
				
		}
		else if(nbSelectedIndices != 0)
		{
			this.txtType.setText("");
			this.txtType.setEnabled(false);
			
			this.txtPriority.setText("");
			this.txtPriority.setEnabled(false);
			
			this.buttonColor.setBackground(Color.white);
			this.buttonColor.setEnabled(false);
			
			this.comboAlgo.setEnabled(false);
		}
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public boolean getCheckboxDeleteHistoryValue()
	{
		return this.checkDeleteHistory.isSelected();
	}
	
	public boolean getCheckboxSameArrivalValue()
	{
		return this.checkSameArrival.isSelected();
	}
	
	public boolean getCheckboxSameAlgoValue()
	{
		return this.checkSameAlgo.isSelected();
	}
	
	public boolean getCheckboxShowButtonsValue()
	{
		return this.checkShowButtons.isSelected();
	}
	
	public boolean getCheckboxAutoRunAlgoValue()
	{
		return this.checkAutoRunAlgo.isSelected();
	}

	public boolean getCheckboxShowToolTipFrameValue()
	{
		return this.checkShowToolTipFrame.isSelected();
	}
	
	public boolean getCheckboxAutoValidFileReadValue()
	{
		return this.checkAutoValidFileRead.isSelected();
	}
	
	public boolean getCheckboxEnableMultiZeroValue()
	{
		return this.checkEnableMultiZero.isSelected();
	}
	
	public ArrayList<SimplifiedCheckBox> getCheckBoxesAlgo()
	{
		return this.checkAlgoTableModel.getData();
	}
	
	public ArrayList<Statistic> getCheckBoxesStat()
	{
		return this.statTableModel.getData();
	}
	
	public int getQuantum()
	{
		int num;
		try
		{
			num = Integer.parseInt(this.txtDefaultQuantum.getText());
		}
		catch (NumberFormatException e)
		{
			num = -1;
		}
		return num;
	}
	
	public ArrayList<ProcessType> getListType()
	{
		int nb = this.modelType.size();
		ArrayList<ProcessType> list = new ArrayList<ProcessType>(nb);
		for(int i = 0; i < nb; i++)
			list.add((ProcessType)this.modelType.get(i));
		return list;
	}
	
	public FrameOptions getFrameOptions()
	{
		return this.frameOptions;
	}
	
	public int getTotalPriorityTimeValue()
	{
		int value;
		try
		{
			value = new Integer(this.txtTotalPriorityTime.getText()).intValue();
		}
		catch (NumberFormatException e)
		{
			value = -1;
		}
		return value;
	}
	
	public String getDefaultProcessName()
	{
		return this.txtDefaultProcessName.getText();
	}
	
	public String getNbCPU()
	{
		return this.txtNbCPU.getText();
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes privées					   *|
	\*-----------------------------------------------------------------*/
	private boolean existIn(String element, DefaultListModel model)
	{
		ProcessType type;
		for(int i = 0; i < model.size(); i++)
		{
			type = (ProcessType)model.get(i);
			if(element.compareTo(type.toString()) == 0)
				return true;
		}
		return false;
	}
	
	private void createComponents()
	{
		TextBoxListener txtBoxSelectionListener = new TextBoxListener();
		ValidateButtonListener txtBoxValdiationListener = new ValidateButtonListener(this.manager ,this);
		
		this.txtDefaultQuantum = new MyJTextField(3);
		this.txtDefaultQuantum.setPreferredSize(this.txtDimension);
		this.txtDefaultQuantum.setToolTipText("Nombre de quantum");
		this.txtDefaultQuantum.addFocusListener(txtBoxSelectionListener);
		this.txtDefaultQuantum.addKeyListener(txtBoxValdiationListener);
		
		this.manager.searchProcessType();
		this.modelType = new DefaultListModel();
        this.listBoxType = new MyJList(this.modelType);
        this.listBoxType.setFont(new Font("Courier New", Font.PLAIN, 12));
        ListBoxInputListener ecouteurListBox = new ListBoxInputListener(this.listBoxType);
		this.listBoxType.addKeyListener(ecouteurListBox);
		ListTypeListener ecouteurType = new ListTypeListener(this);
		this.listBoxType.addListSelectionListener(ecouteurType);
		this.listBoxType.addMouseListener(ecouteurType);
		
		this.txtType = MyJTextField.cloneOf(this.txtDefaultQuantum);
		this.txtType.setToolTipText("Nom");

		this.buttonAdd = new JButton("Ajouter");
		this.buttonAdd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						int priority = 0;
						boolean ok = true;
						try
						{
							priority = new Integer(txtPriority.getText()).intValue();
						}
						catch(NumberFormatException e1)
						{
							JOptionPane.showMessageDialog(frameOptions, "La priorité est incorrecte, entrez un nombre supérieur à zéro", "Erreur", JOptionPane.ERROR_MESSAGE);
							ok = false;
						}
						
						if(priority < 0)
						{
							JOptionPane.showMessageDialog(frameOptions, "La priorité est incorrecte, entrez un nombre supérieur à zéro", "Erreur", JOptionPane.ERROR_MESSAGE);
							ok = false;
						}
						
						if(ok && comboAlgo.getSelectedIndex() == -1 )
						{
							JOptionPane.showMessageDialog(frameOptions, "Choisissez un type", "Erreur", JOptionPane.ERROR_MESSAGE);
							ok = false;
						}
							
						if(ok)
						{
							if(txtType.getText().compareTo("") != 0 && !existIn(txtType.getText(), modelType))
							{
								ProcessType newType = new ProcessType(txtType.getText(), priority);
								newType.setColor(buttonColor.getBackground());
								newType.setAlgo((Algo_I)comboAlgo.getModel().getSelectedItem());
								modelType.addElement(newType);
								manager.getProcessType().add(newType);
							}
						}
						else
							fillComponent = false;
					}
				}
		);
		
		this.buttonEditType = new JButton("Modifier");
		this.buttonEditType.setPreferredSize(this.buttonDimension);
		this.buttonEditType.setEnabled(false);
		this.buttonEditType.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(txtType.getText().compareTo("") != 0 && listBoxType.getSelectedIndex() != -1 && comboAlgo.getModel().getSelectedItem() != null)
						{
							ProcessType type = (ProcessType)listBoxType.getSelectedValue();
							type.setName(txtType.getText());
							int value = 0;
							boolean test = true;
							try 
							{
								value = new Integer(txtPriority.getText()).intValue();
								if(value < 0)
									test = false;
							}
							catch (NumberFormatException e1) 
							{
								test = false;
							}
							if(test)
							{
								type.setPriority(value);
								type.setAlgo((Algo_I)(comboAlgo.getModel().getSelectedItem()));
								type.setColor(buttonColor.getBackground());
								modelType.setElementAt(type, listBoxType.getSelectedIndex());							
							}
							else
								JOptionPane.showMessageDialog(frameOptions, "La priorité est incorrecte, entrez un nombre supérieur à zéro", "Erreur", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
		);
		
		this.buttonDeleteType = new JButton("Supprimer");
		this.buttonDeleteType.setPreferredSize(this.buttonDimension);
		this.buttonDeleteType.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(listBoxType.getSelectedIndex() != -1)
						{
							int[] tab = listBoxType.getSelectedIndices();
							for(int i = 0; i < tab.length; i++)
							{
								ProcessType removedType = (ProcessType)modelType.remove(tab[i]-i);
								manager.getProcessType().remove(removedType);
							}
						}
					}
				}
		);
		
		this.buttonColor = new JButton("");
		this.buttonColor.setPreferredSize(this.buttonDimension);
		this.buttonColor.setPreferredSize(new Dimension(buttonDeleteType.getPreferredSize().height, buttonDeleteType.getPreferredSize().height)); // 2x height pour avoir un carré
		this.buttonColor.setBackground(Color.white);
		this.buttonColor.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							Color bgColor = JColorChooser.showDialog(buttonColor, "Choisissez la couleur du type", getBackground());
							if(bgColor != null)
							{
								buttonColor.setBackground(bgColor);
						        fillComponent = false;
							}
						}
					}
		);
		
		this.txtPriority = MyJTextField.cloneOf(this.txtDefaultQuantum);
		this.txtPriority.setToolTipText("Priorité");
		
		this.comboAlgo = new JComboBox();
		this.comboAlgo.setToolTipText("Algorithme associé au type");
		this.comboAlgo.setModel(new DefaultComboBoxModel(new Vector<Algo_I>(this.manager.getAlgoPlugin()))); // Ne pas faire un seul Vector pour les 2 combos, car ajouter un "Aléatoire" à 1 des 2, le rajoute aux 2.

		
		this.txtTotalPriorityTime = MyJTextField.cloneOf(this.txtDefaultQuantum);
		this.txtTotalPriorityTime.addKeyListener(txtBoxValdiationListener);
		this.txtTotalPriorityTime.setToolTipText("Temps de rotations des files d'attentes.");
		
		this.txtDefaultProcessName = MyJTextField.cloneOf(this.txtDefaultQuantum);
		this.txtDefaultProcessName.setToolTipText("Nom de processus par défaut");
		this.txtDefaultProcessName.addFocusListener(txtBoxSelectionListener);
		this.txtDefaultProcessName.addKeyListener(txtBoxValdiationListener);

		
		this.txtNbCPU = MyJTextField.cloneOf(this.txtDefaultQuantum);
		this.txtNbCPU.setToolTipText("Nombre de processeurs à simuler");
		this.txtNbCPU.addFocusListener(txtBoxSelectionListener);
		this.txtNbCPU.addKeyListener(txtBoxValdiationListener);

		this.checkEnableMultiZero = new JCheckBox("Activer le multi-zéro");
		this.checkEnableMultiZero.setToolTipText("Activé : Ajoute des 0 avant les autres nombres dans le nom d'un processus pour qu'ils aient tous le même nombre de caractères");
		
		this.checkDeleteHistory = new JCheckBox("Supprimer les historiques", true);
		this.checkDeleteHistory.setToolTipText("Activé : Supprime automatiquement les historiques à la fin de l'exécution du programme");
		
		this.checkSameArrival = new JCheckBox("Autoriser les arrivées identiques", true);
		this.checkSameArrival.setToolTipText("Autorise ou non d'avoir plusieurs processus arrivant à la même heure");

		this.checkSameAlgo = new JCheckBox("Autoriser les algorithmes identiques", true);
		this.checkSameAlgo.setToolTipText("Autorise ou non d'avoir plusieurs fois le même onglet d'un même algorithme sur une page");

		this.checkShowButtons = new JCheckBox("Afficher les boutons");
		this.checkShowButtons.setToolTipText("Affiche ou masque les boutons de déplacement de processus");

		this.checkAutoRunAlgo = new JCheckBox("Executer les algorithmes");
		this.checkAutoRunAlgo.setToolTipText("Execute chaque algorithmes lors de la validation de la liste d'entrée des processus");
		
		this.checkShowToolTipFrame = new JCheckBox("Afficher la fenêtre d'information");
		this.checkShowToolTipFrame.setToolTipText("Affiche la fenêtre d'information lors d'un passage par la souris sur un processus dessiné");
		

		this.checkAutoValidFileRead = new JCheckBox("Valider automatiquement");
		this.checkAutoValidFileRead.setToolTipText("Valide la liste de processus en entrée lors de la lecture d'un fichier");

		this.buttonValid = new JButton("Valider");
		this.buttonValid.setPreferredSize(this.buttonDimension);
		this.buttonValid.addActionListener(txtBoxValdiationListener);

		this.buttonCancel = new JButton("Annuler");
		this.buttonCancel.setPreferredSize(this.buttonDimension);
		this.buttonCancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						fillWithOriginal();
						frameOptions.close();
					}
				}
		);
		
		this.listCheckAlgo = new JTable();
		String[] algoTitle = {"Algorithme", "[v]"};
		this.checkAlgoTableModel = new CheckboxTableModel(null, new Hashtable<String, Boolean>(0), this.manager.getAlgoPlugin().size(), algoTitle);
		this.listCheckAlgo.setModel(this.checkAlgoTableModel);
		this.listCheckAlgo.setRowSorter(new TableRowSorter<TableModel>(this.checkAlgoTableModel));
		
		for(Algo_I algo:this.manager.getAlgoPlugin())
			if(algo != null)
				this.checkAlgoTableModel.addRow(algo.getName());
		
		this.statTableModel = new StatisticTableModel(this.manager.getStatPlugin().size());
		this.listCheckStat = new JTable(this.statTableModel);
		this.listCheckStat.setDefaultRenderer(JButton.class, new JTableButtonRenderer(false));
		this.listCheckStat.addMouseListener(new JTableButtonMouseListener(this.listCheckStat));
		this.listCheckStat.setRowSorter(new TableRowSorter<TableModel>(this.statTableModel));
		
		for(Statistic stat:this.manager.getStatistics())
			this.statTableModel.addRow(stat);
		
		this.listCheckStat.getColumnModel().getColumn(1).setMaxWidth(50);
		this.originalListCheckStat = Tools.deepClone(this.statTableModel.getData());
	}
	
	private void fillComponents()
	{
		String temp = this.manager.getPreferances("txtQuantum");
		if(temp.compareTo("NaN") == 0)
			temp = "5";
		this.txtDefaultQuantum.setText(temp);
		
		this.txtPriority.setText("");
		
		temp = this.manager.getPreferances("txtTotalPriorityTime");
		if(temp.compareTo("NaN") == 0)
			temp = "100";
		this.txtTotalPriorityTime.setText(temp);
		
		temp = this.manager.getPreferances("txtDefaultProcessName");
		if(temp.compareTo("NaN") == 0)
			temp = "P";
		this.txtDefaultProcessName.setText(temp);
		
		temp = this.manager.getPreferances("txtNbCPU");
		if(temp.compareTo("NaN") == 0)
			temp = "1";
		this.txtNbCPU.setText(temp);
		
		this.txtType.setText("");
		
        this.modelType = (DefaultListModel)this.listBoxType.getModel();
        this.modelType.clear();
        for(ProcessType type:this.manager.getProcessType())
        	this.modelType.addElement(type);
        
        this.buttonColor.setBackground(Color.white);
		
		this.checkDeleteHistory.setSelected(this.manager.getPreferances("checkDeleteHistory").compareTo("true") == 0);
		this.checkSameArrival.setSelected(this.manager.getPreferances("checkSameArrival").compareTo("true") == 0);
		this.checkSameAlgo.setSelected(this.manager.getPreferances("checkSameAlgo").compareTo("true") == 0);
		this.checkShowButtons.setSelected(this.manager.getPreferances("checkShowButtons").compareTo("true") == 0);
		this.checkAutoRunAlgo.setSelected(this.manager.getPreferances("checkAutoRunAlgo").compareTo("true") == 0);
		this.checkShowToolTipFrame.setSelected(this.manager.getPreferances("checkShowToolTipFrame").compareTo("true") == 0);
		this.checkAutoValidFileRead.setSelected(this.manager.getPreferances("checkAutoValidFileRead").compareTo("true") == 0);
		this.checkEnableMultiZero.setSelected(this.manager.getPreferances("checkEnableMultiZero").compareTo("true") == 0);

		for(int i = 0; i < this.checkAlgoTableModel.getRowCount(); i++)
			this.checkAlgoTableModel.setValueAt(this.manager.getPreferances("Algo_"+this.checkAlgoTableModel.getValueAt(i, 0)).compareTo("false") != 0, i, 0);

		for(int i = 0; i < this.statTableModel.getRowCount(); i++)
			this.statTableModel.setValueAt(this.manager.getPreferances("Stat_"+this.statTableModel.getValueAt(i, 0)).compareTo("false") != 0, i, 2);
		
		this.comboAlgo.setSelectedIndex(-1);
		
		Tools.setColumnCheck(this.listCheckAlgo, 1);
		Tools.setColumnCheck(this.listCheckStat, 2);
	}
	
	private void fillWithOriginal()
	{
		this.statTableModel.removeAll();
		for(Statistic stat:this.originalListCheckStat)
			this.statTableModel.addRow(stat);
	}
	
	private void organizeComponents()
	{
		FormLayout layout = new FormLayout(
				//1    2     3     4     5     6      7     8     9       10    11    12    13           
				"pref, pref, pref, pref, pref, 10dlu, pref, pref, 70dlu", //columns
				"pref, pref, pref, pref, 10dlu, pref, pref, pref, pref, 10dlu, pref, 10dlu, pref");//rows
	   
	   layout.setColumnGroups(new int[][]{{2, 3, 4}});
	   layout.setRowGroups(new int[][]{{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13}});
	   
	   PanelBuilder builder = new PanelBuilder(layout);
	   
	   // Petite marge par rapport au Panel
	   builder.setDefaultDialogBorder();
	       
	   // Création de l'objet contrainte
	   CellConstraints cellConstraints = new CellConstraints();
	   
	   // Ajout des champs dans le builder
	   builder.addLabel("Nom par défaut",		cellConstraints.xy   (1  , 1                  ));
	   builder.add(this.txtDefaultProcessName,	cellConstraints.xy   (2  , 1                  ));
	   builder.add(this.checkEnableMultiZero,	cellConstraints.xyw  (1  , 2  , 2             ));
	   builder.addLabel("Quantum par défaut ",	cellConstraints.xy   (1  , 3                  ));
	   builder.add(this.txtDefaultQuantum,		cellConstraints.xy   (2  , 3                  ));
	   builder.add(this.checkDeleteHistory,		cellConstraints.xyw  (7  , 1  , 3             ));
	   builder.add(this.checkSameAlgo,			cellConstraints.xyw  (7  , 2  , 3             ));
	   builder.add(this.checkSameArrival,		cellConstraints.xyw  (7  , 3  , 3             ));
	   builder.add(this.checkShowButtons,		cellConstraints.xyw  (7  , 4  , 3             ));
	   builder.add(this.checkAutoRunAlgo,		cellConstraints.xyw  (7  , 5  , 3             ));
	   builder.add(this.checkAutoValidFileRead,	cellConstraints.xyw  (7  , 6  , 3             ));
	   builder.add(this.checkShowToolTipFrame,	cellConstraints.xyw  (7  , 7  , 3             ));
	   
	   JScrollPane scrollListType = new JScrollPane(this.listBoxType);
	   builder.add(scrollListType,				cellConstraints.xywh (1  , 6  , 1,    3		  ));
	   builder.add(this.txtTotalPriorityTime,	cellConstraints.xy   (1  , 9		          ));
	   builder.add(this.txtType,				cellConstraints.xy   (2  , 6                  ));
	   builder.add(this.buttonColor,			cellConstraints.xy   (3  , 6                  ));
	   builder.add(this.txtPriority,			cellConstraints.xy   (4  , 6                  ));
	   builder.add(this.comboAlgo,				cellConstraints.xy   (5  , 6                  ));
	   builder.add(this.buttonAdd,				cellConstraints.xyw  (2  , 7   , 4  		  ));
	   builder.add(this.buttonEditType,			cellConstraints.xyw  (2  , 8   , 4    		  ));
	   builder.add(this.buttonDeleteType,		cellConstraints.xyw  (2  , 9   , 4			  ));
	   builder.add(this.buttonValid,			cellConstraints.xyw  (1  , 13  , 1    		  ));
	   builder.add(this.buttonCancel,			cellConstraints.xyw  (2  , 13  , 3    		  ));
	   
	   builder.addLabel("Nombre de CPU ",		cellConstraints.xy   (7  , 8    			  ));
	   builder.add(this.txtNbCPU,				cellConstraints.xy   (8  , 8   			 	  ));
	   
	   JScrollPane scrollStat = new JScrollPane(this.listCheckStat);
	   scrollStat.setPreferredSize(new Dimension(scrollStat.getPreferredSize().width, 100));
	   builder.add(scrollStat,					cellConstraints.xywh (1  , 11  , 5,    1      ));
	   
	   JScrollPane scrollAlgo = new JScrollPane(this.listCheckAlgo);
	   scrollAlgo.setPreferredSize(new Dimension(this.buttonDimension.width*2, scrollStat.getPreferredSize().height));
	   builder.add(scrollAlgo,					cellConstraints.xywh (7  , 11  ,  3,   1      ));

	   this.add(builder.getPanel());
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private MyJList listBoxType;

	private MyJTextField txtDefaultQuantum;
	private MyJTextField txtType;
	private MyJTextField txtPriority;
	private MyJTextField txtTotalPriorityTime;
	private MyJTextField txtDefaultProcessName;
	private MyJTextField txtNbCPU;
	
	private DefaultListModel modelType;
	
	private JComboBox comboAlgo;
	
	private JCheckBox checkDeleteHistory;
	private JCheckBox checkSameArrival;
	private JCheckBox checkSameAlgo;
	private JCheckBox checkShowButtons;
	private JCheckBox checkEnableMultiZero;
	private JCheckBox checkAutoValidFileRead;
	private JCheckBox checkAutoRunAlgo;
	private JCheckBox checkShowToolTipFrame;
	
	private CheckboxTableModel checkAlgoTableModel;
	private StatisticTableModel statTableModel;
	private JTable listCheckAlgo;
	private JTable listCheckStat;
	private ArrayList<Statistic> originalListCheckStat;

	private JButton buttonAdd;
	private JButton buttonDeleteType;
	private JButton buttonValid;
	private JButton buttonCancel; 
	
	private boolean fillComponent;
	private JButton buttonColor;
	private JButton buttonEditType;
	
	private Manager manager;
	private FrameOptions frameOptions;
	
	private Dimension buttonDimension;
	private Dimension txtDimension;
}

