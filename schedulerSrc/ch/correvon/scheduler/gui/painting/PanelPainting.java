
//Specification:
package ch.correvon.scheduler.gui.painting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import ch.correvon.scheduler.algorithms.ButtonAlgorithm;
import ch.correvon.scheduler.gui.algorithm.PanelAlgorithm;
import ch.correvon.scheduler.gui.myInterface.PanelScaleable;
import ch.correvon.scheduler.gui.myInterface.PanelScrollable;
import ch.correvon.scheduler.gui.myInterface.PanelSelectionable;
import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.Tools;
import ch.correvon.scheduler.moo.jTable.Abstract_ProcessTableModel;
import ch.correvon.scheduler.moo.jTable.FullProcessRowSorter;
import ch.correvon.scheduler.moo.jTable.FullProcessTableModel;
import ch.correvon.scheduler.moo.jTable.SimpleProcessRowSorter;
import ch.correvon.scheduler.moo.jTable.SimpleProcessTableModel;
import ch.correvon.scheduler.moo.myObjects.MyJSlider;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.SortedProcess;
import ch.correvon.scheduler.moo.process.Statistic;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * C'est dans ce panneau que seront déssinés tout les processus lors de l'application
 * d'un algorithme à une liste de processus
 */
public class PanelPainting extends JPanel implements ComponentListener, PanelSelectionable, PanelScrollable, PanelScaleable/*, PanelPrintable*/
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public PanelPainting(PanelAlgorithm panelAlgorithm, ButtonAlgorithm buttonAlgorithm, Manager manager, ArrayList<Process> listProcess, int sliderValue, boolean sliderEnabled)
	{
		this.manager = manager;
		this.panelAlgorithm = panelAlgorithm;
		this.listProcess = Tools.deepClone(listProcess);
		this.name = buttonAlgorithm.getName();
		this.result = buttonAlgorithm.getResult();
		this.selectedProcess = new ArrayList<String>();
		this.xDifferential = 0;
		this.yDifferential = 0;

		this.scale = sliderValue/10.0;
		
		this.fileNameGenerate();
		this.setLayout(new BorderLayout());
		
		this.panelScroll = new JPanel();
		this.panelScroll.setBackground(new Color(0, 0, 0, 0));
		this.panelScroll.setOpaque(true);
		this.scroll = new JScrollPane(this.panelScroll);
		this.scroll.getVerticalScrollBar().setEnabled(true);
		
		this.historyButton = new JButton("Ouvrir l'historique");
		this.historyButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					openHistory();
				}
			}
		);
		
		this.scroll.setBackground(new Color(0, 0, 0, 0));
		this.scroll.getViewport().setOpaque(false);
		ScrollBarListener scrollListener = new ScrollBarListener(this); 
		this.scroll.getHorizontalScrollBar().addAdjustmentListener(scrollListener);
		this.scroll.getVerticalScrollBar().addAdjustmentListener(scrollListener);
		this.scroll.setPreferredSize(new Dimension((int)(this.panelAlgorithm.getSize().width*0.91), (int)(this.panelAlgorithm.getSize().height*0.33)));
		
		Box boxV = Box.createVerticalBox();
		Box boxH = Box.createHorizontalBox();
		JLabel lblName = new JLabel(this.name);
		lblName.setFont(new Font("Times new roman", Font.PLAIN, 24));
		boxH.add(lblName);
		boxV.add(boxH);
		boxV.add(this.scroll);
		this.add(boxV, BorderLayout.NORTH);
		this.add(southArea(sliderValue, sliderEnabled), BorderLayout.CENTER);
		this.panelAlgorithm.addComponentListener(this);
		
		this.fillTables();
		this.history();
		
		this.repaint();
	}
	
	/*-----------------------------------------------------------------*\
	|*						Méthodes Publiques						   *|
	\*-----------------------------------------------------------------*/
	@Override public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.g2D = (Graphics2D)g;
		this.paint();
	}
	
	@Override public void setScaleValue(double value)
	{
		this.scale = value;
		this.resized = true;
		this.repaint();
	}
	
	/*@Override public void print()
	{
		MPanelPrinter printer = new MPanelPrinter(this);
		printer.setDocumentTitle(this.name);
		printer.setOrientation(MPanelPrinter.LANDSCAPE);
		printer.print();
	}*/
	
	/*----------------------------------------------------------*\
	|*						Ecouteur 				   		    *|
	\*----------------------------------------------------------*/
	/*--------------------------------------*\
	|*				Resizing 	   		    *|
	\*--------------------------------------*/
	public void componentResized(ComponentEvent e)
	{
		this.resized();
	}

	public void componentShown(ComponentEvent e)
	{
		this.resized();
	}

	public void componentHidden(ComponentEvent e)
	{
		
	}

	public void componentMoved(ComponentEvent e)
	{
		
	}

	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	@Override public String getName()
	{
		return this.name;
	}
	
	public JScrollPane getScroll()
	{
		return this.scroll;
	}
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setXDifferential(int xDifferential)
	{
		this.xDifferential = xDifferential;
	}

	public void setYDifferential(int yDifferential)
	{
		this.yDifferential = yDifferential;
	}
	
	@Override public void setSelectedProcess(ArrayList<String> listName)
	{
		this.selectedProcess = listName;
		ListSelectionModel outputSelectionModel = this.outputList.getSelectionModel();
		outputSelectionModel.clearSelection();
		
		int[] selectedRow = this.inputList.getSelectedRows();
		for(int j = 0; j < selectedRow.length; j++)
		{
			String name = (String)this.inputList.getModel().getValueAt(this.inputList.convertRowIndexToModel(selectedRow[j]), 0);
			String processName;
			for(int i = 0; i < this.outputList.getModel().getRowCount(); i++)
			{
				processName = (String)this.outputList.getModel().getValueAt(this.outputList.convertRowIndexToModel(i), 0);
				if(processName.compareTo(name) == 0)
					outputSelectionModel.addSelectionInterval(i, i);
			}
		}
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes Privées					   *|
	\*-----------------------------------------------------------------*/
	/**
	 * Génère le nom du fichier temporaire qui va contenir l'historique.
	 * Ce nom dépend de la date et l'heure du jour, ainsi que du type d'algorithme employé
	 */
	private void fileNameGenerate()
	{
		String deleteHisto = "";
		if(this.manager.getPreferances("checkboxDeleteHistory").compareTo("false") != 0)
			deleteHisto = "_";
		
		DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy - HH'h'mm'm'ss's'");
		String dateHeure = dateFormat.format(Calendar.getInstance().getTime());
		this.fileName = deleteHisto+this.name+" - "+dateHeure+".txt";
	}
	
	private void paint()
	{
		if(this.slider.isSelected())
			calculScale();
		this.g2D.setColor(new Color(10,155,200));
		
		this.g2D.setFont(FONT);
		Point lastCoo = Tools.drawGantt(this.g2D, this.result.getListSortedProcess(), this.selectedProcess, (int)(this.scroll.getSize().width*0.9), this.scale, 0, 50, this.xDifferential, this.yDifferential); 
		int lastX = lastCoo.x + this.xDifferential;
		if(this.resized)
		{
			this.panelScroll.setPreferredSize(new Dimension(lastX+40, lastCoo.y+50));
			this.panelScroll.updateUI();
			this.resized = false;
		}
	}
	
	private void resized()
	{
		this.resized = true;
		this.scroll.setPreferredSize(new Dimension((int)(this.panelAlgorithm.getSize().width*0.95), /*heightScroll*/ (int)(panelAlgorithm.getSize().height*0.33)));
		this.scroll.updateUI();
		this.repaint();
	}

	private void history()
	{
    	String path = Manager.DATA_PATH+this.fileName;
        try
        {
        	this.manager.addHistorique(path);
            FileWriter file = new FileWriter(path);
            for(String s:this.result.getHistory())
            	file.write(s+Manager.END_LINE_CHARACTER);
            file.close();
       	 }
        catch(IOException e)
        {
       		System.err.println("erreur lors de l'écriture du fichier "+path);
       		e.printStackTrace();
        }
	}
	
	private void fillTables()
	{
		this.fillInputTable();
		this.fillOutputTable();
		this.fillStatTable();
		this.setSorters();
	}
	
	public void setSorters()
	{
		this.inputList.setRowSorter(new SimpleProcessRowSorter<Abstract_ProcessTableModel>(this.inputList_model));
		this.outputList.setRowSorter(new FullProcessRowSorter<Abstract_ProcessTableModel>(this.outputList_model));
	}
	
	private void fillInputTable()
	{
		for(Process process:this.listProcess)
			this.inputList_model.addRow(process);
	}
	
	private void fillOutputTable()
	{
		for(Process process:this.result.getListSortedProcess())
			this.outputList_model.addRow(process);
	}
	
	private void fillStatTable()
	{
		DefaultListModel model = (DefaultListModel)this.statList.getModel();
		for(Statistic stat:this.result.getStats())
			model.addElement(stat.toString(2));
	}
	
	private void openHistory()
	{
		String filePath = Manager.DATA_PATH+this.fileName;
		String progPath = "notepad.exe";

		try
		{
			Runtime.getRuntime().exec(progPath+" "+filePath);
		}
		catch(IOException e)
		{
			System.err.println("Impossible d'ouvrir le fichier\n"+filePath+"\navec le programme\n"+progPath);
			e.printStackTrace();
		}
	}
	
	private Container southArea(int sliderValue, boolean sliderEnabled)
	{
		this.slider = new MyJSlider(sliderValue, this, this);
		this.slider.setSelected(sliderEnabled);
		
		this.inputList = new JTable();
		this.inputList_model = new SimpleProcessTableModel(this.inputList, this.manager);
		this.inputList.setModel(this.inputList_model);
		this.inputList_model.setCellEditable(false);

		
		this.outputList = new JTable();
		this.outputList_model = new FullProcessTableModel(this.outputList);
		this.outputList.setModel(this.outputList_model);
		this.outputList_model.setCellEditable(false);
		
		InputTableListener listener = new InputTableListener(this, this.inputList); 
		this.inputList.addKeyListener(listener);
		this.inputList.addMouseListener(listener);
		this.inputList.addMouseMotionListener(listener);
		
		this.statList = new JList(new DefaultListModel());
		
		FormLayout layout = new FormLayout(
				//1    2     3
				"pref, 10dlu, pref, pref:grow", //columns
				"pref, 10dlu, top:pref:grow, 10dlu, 10dlu, top:pref");//rows

		layout.setColumnGroups(new int[][]{{}});
		layout.setRowGroups(new int[][]{{}});
	   
		PanelBuilder builder = new PanelBuilder(layout);
	   
		// Petite marge par rapport au Panel
		builder.setDefaultDialogBorder();
			       
		// Création de l'objet contrainte
		CellConstraints cellConstraints = new CellConstraints();
	

		JScrollPane scrollInput = new JScrollPane(this.inputList);
		scrollInput.setPreferredSize(new Dimension(200, 300));
		
		JScrollPane scrollOutput = new JScrollPane(this.outputList);
		scrollOutput.setPreferredSize(new Dimension(300, 300));
		
		JScrollPane scrollStat = new JScrollPane(this.statList);
		scrollStat.setPreferredSize(new Dimension(300, 100));
	
		// Ajout des champs dans le builder 
		builder.add(this.slider,         	cellConstraints.xywh (1  , 1 	,  4,  1      ));
		builder.add(scrollInput,         	cellConstraints.xywh (1  , 3 	,  1,  2      ));
		builder.add(scrollOutput,         	cellConstraints.xywh (3  , 3 	,  1,  2      ));
		builder.add(this.historyButton,     cellConstraints.xy   (1  , 6 	    	      ));
		builder.add(scrollStat,         	cellConstraints.xy   (3  , 6 	    	      ));
		
		return builder.getContainer();
	}
	
	private void calculScale()
	{
		int xFin;
		int xTest;
		int wTest;
		int totalProcessLength;
		double ratio;
		int panelWidth = this.getWidth();
		int lastElement;
		int maxSize;
		ArrayList<Process> listProcess = this.outputList_model.getListProcess();
		
		lastElement = listProcess.size() - 1 ;
		totalProcessLength = listProcess.get(lastElement).getEndingTime();
		ratio = Tools.calculRatio(listProcess, this.scroll.getPreferredSize().width , 20);
		Process lastProcess = listProcess.get(lastElement);
		xTest = (int)(lastProcess.getRealIncomingTime()/(double)totalProcessLength*panelWidth*ratio);
		wTest = (int)(lastProcess.getDuration()/(double)totalProcessLength*panelWidth*ratio);
		xFin = xTest + wTest;
		maxSize = listProcess.get(lastElement).getEndingTime();

		this.scale = xFin / (double)maxSize;
		this.slider.setValue((int)(this.scale*10));
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
	private ArrayList<Process> listProcess;
	private Graphics2D g2D;
	private String name;
	private SortedProcess result;
	
	private int xDifferential;
	private int yDifferential;
	private String fileName;
	private JScrollPane scroll;
	private JPanel panelScroll; // Panneau invisible. Il est là pour donner une "consistance" au scrollPane
	
	private JTable inputList;
	private JTable outputList;
	private JList statList;
	private Abstract_ProcessTableModel inputList_model;
	private Abstract_ProcessTableModel outputList_model;
	private ArrayList<String> selectedProcess;
	
	private PanelAlgorithm panelAlgorithm;
	private MyJSlider slider;
	private double scale;
	
	private boolean resized = true;
	
	private JButton historyButton;
	
	/*-----------------------------------------*\
	|*				Statiques				   *|
	\*-----------------------------------------*/
	private static final Font FONT = new Font("Times new roman", Font.PLAIN, 12);
}

