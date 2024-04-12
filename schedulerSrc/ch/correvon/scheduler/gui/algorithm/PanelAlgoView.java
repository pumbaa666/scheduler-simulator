package ch.correvon.scheduler.gui.algorithm;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ch.correvon.scheduler.algorithms.ButtonAlgorithm;
import ch.correvon.scheduler.gui.myInterface.PanelScaleable;
import ch.correvon.scheduler.gui.myInterface.PanelScrollable;
import ch.correvon.scheduler.gui.myInterface.PanelSelectionable;
import ch.correvon.scheduler.gui.painting.InputTableListener;
import ch.correvon.scheduler.gui.painting.ScrollBarListener;
import ch.correvon.scheduler.gui.statistic.FrameStatistic;
import ch.correvon.scheduler.moo.HorizontalAlignement;
import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.Tools;
import ch.correvon.scheduler.moo.VerticalAlignement;
import ch.correvon.scheduler.moo.jTable.Abstract_ProcessTableModel;
import ch.correvon.scheduler.moo.jTable.SimpleProcessRowSorter;
import ch.correvon.scheduler.moo.jTable.SimpleProcessTableModel;
import ch.correvon.scheduler.moo.myObjects.MyJSlider;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.SortedProcess;
import ch.correvon.scheduler.moo.process.Statistic;
import ch.correvon.scheduler.moo.simulation.GanttDiagram;

public class PanelAlgoView extends JPanel implements ComponentListener, PanelSelectionable, PanelScrollable, PanelScaleable, /*PanelPrintable, */KeyListener
{
	/*----------------------------------------------------------------*\
	|*							Constructeurs						  *|
	\*----------------------------------------------------------------*/
	public PanelAlgoView(String name, Manager manager, ArrayList<ButtonAlgorithm> listButtonAlgorithm, PanelAlgorithm panelAlgorithm, ArrayList<Process> listProcess)
	{
		this.manager = manager;
		this.panelAlgorithm = panelAlgorithm;
		this.listProcess = new ArrayList<Process>(listProcess);
		this.setLayout(new BorderLayout());	
		this.selectedProcess = new ArrayList<String>();
		
		this.yDifferential = 0;
		this.xDifferential = 0;
		this.xCursor = -1;
		this.isDragging = false;
		
		this.add(leftPanel(name, listButtonAlgorithm), BorderLayout.WEST);
		this.createPaintedPanel();
		
		this.mainScroll = new JScrollPane(this.paintedPanel);
		ScrollBarListener scrollListener = new ScrollBarListener(this); 
		this.mainScroll.getHorizontalScrollBar().addAdjustmentListener(scrollListener);
		this.mainScroll.getVerticalScrollBar().addAdjustmentListener(scrollListener);
		
		// Evite le scintillement des noms des algorithmes, de leur statistiques ainsi que de l'échelle
		ScrollBarMousListener scrollMouseListener = new ScrollBarMousListener(this); 
		this.mainScroll.getHorizontalScrollBar().addMouseListener(scrollMouseListener);
		this.mainScroll.getVerticalScrollBar().addMouseListener(scrollMouseListener);
		this.add(this.mainScroll, BorderLayout.CENTER);
		this.add(this.creatScalePanel(), BorderLayout.SOUTH);
		
		for(Process process:this.listProcess)
			this.table_model.addRow(process);

		ScrollMouseListener mouseListener = new ScrollMouseListener(this);
		this.paintedPanel.addMouseMotionListener(mouseListener);
		this.paintedPanel.addMouseListener(mouseListener);
		
		this.panelAlgorithm.addComponentListener(this);
		this.listSortedProcess = new ArrayList<GanttDiagram>(this.manager.getAlgoPlugin().size());
		
		this.toolTipFrame = new ToolTipFrame(this);
		this.toolTipFrame.getTxtPane().addKeyListener(this);
		this.isAnchored = false;
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void changeCursorPosition(int x)
	{
		if(this.checkShowScale.isSelected())
		{
			int newPos = x-2*this.widthCursor;
			if(newPos > this.getWidth()+this.xDifferential)
				this.xCursor = this.getWidth()+this.xDifferential;
			else if(newPos < 10+this.xDifferential)
				this.xCursor = 10+this.xDifferential;
			else
				this.xCursor = newPos;
			repaint();
		}
		this.isAnchored = false;
	}

	public void isCursorInProcess(Point point, Point locationOnScreen)
	{
		if(!this.isAnchored)
			if(this.manager.getPreferances("checkShowToolTipFrame").compareTo("false") != 0)
			{
				for(GanttDiagram diagram:this.listSortedProcess)
					for(Process process:diagram.getSortedProcess().getListSortedProcess())
						if(process.getBounds().contains(point))
						{
							this.toolTipFrame.setProcess(process);
							this.toolTipFrame.setLocation(locationOnScreen.x+10, locationOnScreen.y+10);
							this.toolTipFrame.setTitle(process.getName());
							this.toolTipFrame.setVisible(true);
							return;
						}
				
				this.toolTipFrame.setVisible(false);
			}
	}
	
	/*@Override public void print()
	{
		MPanelPrinter printer = new MPanelPrinter(this);
		printer.setDocumentTitle("Vue");
		printer.setOrientation(MPanelPrinter.LANDSCAPE);
		printer.print();
	}*/

	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public int getXCursor()
	{
		return this.xCursor;
	}
	
	public int getYCursor()
	{
		return this.yCursor;
	}
	
	public int getWidthCursor()
	{
		return this.widthCursor;
	}

	public Graphics2D getG2D()
	{
		return this.g2D;
	}

	public FrameStatistic getFrameStatistic()
	{
		return this.frameStat;
	}
	
	public ButtonAlgorithm getPimpedButton()
	{
		return this.pimpedButton;
	}
	
	public int getSliderValue()
	{
		return this.slider.getValue();
	}
	
	public boolean getCheckUseAutoScaleValue()
	{
		return this.checkUseAutoScale.isSelected();
	}
	
	public ArrayList<Process> getInputList()
	{
		return new ArrayList<Process>(this.listProcess);
	}
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setAnchored(boolean value)
	{
		this.isAnchored = value;
	}
	
	public void setDataToDraw(String name, ArrayList<Process> listSortedProcess, ArrayList<Statistic> stats/*, int x, int y*/)
	{
		int i = 0;
		int index = -1;
		for(GanttDiagram diagram:this.listSortedProcess)
		{
			if(diagram.getName().compareTo(name) == 0)
			{
				index = i;
				break;
			}
			i++;
		}

		if(index == -1)
			this.listSortedProcess.add(new GanttDiagram(name, new SortedProcess(listSortedProcess, new ArrayList<String>(), stats)));
		else
			this.listSortedProcess.set(index, new GanttDiagram(name, new SortedProcess(listSortedProcess, new ArrayList<String>(), stats)));
		
		repaint();
	}

	public void setDragging(boolean value)
	{
		this.isDragging = value;
	}
	
	public void setScaleValue(double value)
	{
		this.scale = value;
		this.paintedPanel.repaint();
	}

	/*-----------------------------------------------------*\
	|*						Ecouteur 					   *|
	\*-----------------------------------------------------*/
	/*---------------------------------------------*\
	|*					Fenetre 				   *|
	\*---------------------------------------------*/
	@Override public void componentResized(ComponentEvent e)
	{
		this.slider.setPreferredSize(new Dimension(this.panelAlgorithm.getSize().width-200, this.slider.getPreferredSize().height));
		this.paintedPanel.repaint();
	}

	@Override public void componentShown(ComponentEvent e)
	{
	}

	@Override public void componentHidden(ComponentEvent e)
	{
	}

	@Override public void componentMoved(ComponentEvent e)
	{
	}
	
	/*---------------------------------------------*\
	|*					Clavier 				   *|
	\*---------------------------------------------*/
	@Override public void keyPressed(KeyEvent arg0)
	{
		int code = arg0.getKeyCode(); 
		switch(code)
		{
			case 113 /* [F2] */ : 
				this.toolTipFrame.getTxtPane().requestFocus();
				this.toolTipFrame.selectText();
				this.isAnchored = true;
				break;
			case 27 /* [ESC] */ :
				this.toolTipFrame.setVisible(false);
				this.isAnchored = false;
				break;
		}
	}

	@Override public void keyReleased(KeyEvent arg0)
	{
	}

	@Override public void keyTyped(KeyEvent arg0)
	{
	}
	
	/*---------------------------------------------*\
	|*				Sélectionnable 				   *|
	\*---------------------------------------------*/
	@Override public void setSelectedProcess(ArrayList<String> listName)
	{
		this.selectedProcess = listName;
	}

	/*---------------------------------------------*\
	|*					Scrollable 				   *|
	\*---------------------------------------------*/
	@Override public JScrollPane getScroll()
	{
		return this.mainScroll;
	}

	@Override public void setXDifferential(int x)
	{
		this.xDifferential = x;
	}

	@Override public void setYDifferential(int y) 
	{
		this.yDifferential = y;
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes privées					   *|
	\*-----------------------------------------------------------------*/
	private Container leftPanel(String name, ArrayList<ButtonAlgorithm> listButtonAlgorithm)
	{
		// Initialisation
		Box boxV = Box.createVerticalBox();
		Box boxButton = Box.createVerticalBox();
		Dimension dimension = new Dimension(200, this.getSize().height/2); 
		
		// Table
		this.inputList = new JTable();
		this.table_model = new SimpleProcessTableModel(this.inputList, this.manager);
		this.inputList.setModel(this.table_model);
		this.table_model.setCellEditable(false);
		this.inputList.setRowSorter(new SimpleProcessRowSorter<Abstract_ProcessTableModel>(this.table_model));
		
		InputTableListener listener = new InputTableListener(this, this.inputList); 
		this.inputList.addKeyListener(listener);
		this.inputList.addMouseListener(listener);
		this.inputList.addMouseMotionListener(listener);
		
		JScrollPane scrollList = new JScrollPane(this.inputList);
		scrollList.setPreferredSize(dimension);
		
		// Liste de bouttons algorithme
		for(int i = 0; i  < 1; i++)
		for(ButtonAlgorithm button:listButtonAlgorithm)
		{
			button.setPanelAlgorithm(this.panelAlgorithm);
			boxButton.add(button.getButton());
		}
		boxButton.add(Box.createVerticalStrut(20));
		
		this.pimpedButton = new ButtonAlgorithm("Personnalisé", null, this.manager);
		this.pimpedButton.getButton().setToolTipText("Exécute le simulateur avec les algorithmes par défaut de chaque type");
		this.pimpedButton.setPanelAlgorithm(this.panelAlgorithm);
		boxButton.add(this.pimpedButton.getButton());
		
		JScrollPane scrollButton = new JScrollPane(boxButton);
		scrollButton.setPreferredSize(dimension);
		
		// Reste : checkBox et boutton Statistique
		int frameStatHeight = 300;
		this.frameStat = new FrameStatistic(this.manager, 0, Toolkit.getDefaultToolkit().getScreenSize().height - frameStatHeight, Toolkit.getDefaultToolkit().getScreenSize().width, frameStatHeight);
		this.frameStat.setTitle("Statistiques de "+name);
		
		JButton buttonStat = new JButton("Statistiques");
		buttonStat.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					frameStat.showUI();
				}
			}
		);
		
		this.checkShowScale = new JCheckBox("Voir l'echelle");
		this.checkShowScale.setSelected(true);
		this.checkShowScale.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						repaint();
					}
				}
		);
		
		// Finalisation
		boxV.add(scrollList);
		boxV.add(scrollButton);
		boxV.add(this.checkShowScale);
		boxV.add(buttonStat);
		
		return boxV;
	}
	
	private void createPaintedPanel()
	{
		this.paintedPanel = new JPanel()
		{
			@Override public void paintComponent(Graphics g)
			{
				super.paintComponent(g);	
				g2D = (Graphics2D)g;
				paintPanel();
			}
		};
	}
	
	private JPanel creatScalePanel()
	{
		JPanel panel = new JPanel();
		int sliderValue;
		try
		{
			sliderValue = new Integer(this.manager.getPreferances("sliderValue")).intValue();
			if(sliderValue == 0)
				sliderValue = 1;
		}
		catch(NumberFormatException e)
		{
			sliderValue = 5;
		}
		this.slider = new MyJSlider(sliderValue, this, this);
		this.checkUseAutoScale = this.slider.getCheckBox();
		
		boolean checkValue = this.manager.getPreferances("checkUseAutoScale").compareTo("false") != 0;
		this.slider.setSelected(checkValue);
		
		panel.add(this.slider);
		
		return panel;
	}

	private void paintPanel()
	{
		if(this.checkUseAutoScale.isSelected())
			calculScale();
		int yInit = 10;
		int x = 10;
		int y = yInit;
		Font titleFont = new Font("Times new roman", Font.PLAIN, 20);
		Font statFont = new Font("Times new roman", Font.PLAIN, 12);
		String position;
		int xMax = 0;
		int xTest;
		int algoWithStatsHeight = 0;
		Point lastCoo;
		
		for(GanttDiagram list:this.listSortedProcess)
		{
			this.g2D.setFont(titleFont);
			// ----------------------- Diagramme de Gantt -----------------------
			this.g2D.setColor(Color.black);
			Tools.drawString(this.g2D, list.getName(), x+this.xDifferential, y, HorizontalAlignement.LEFT, VerticalAlignement.CENTER);
			y += this.yDifferential + 12;
			lastCoo = Tools.drawGantt(this.g2D, list.getSortedProcess().getListSortedProcess(), this.selectedProcess, this.mainScroll.getVisibleRect().width, this.scale, x, y, this.xDifferential, this.yDifferential); 
			xTest = lastCoo.x;
			if(xTest > xMax)
				xMax = xTest;
			
			// ----------------------- Statistiques -----------------------
			this.g2D.setFont(statFont); 
			y = lastCoo.y+20;
			
			this.g2D.setColor(Color.black);
			for(Statistic string:list.getSortedProcess().getStats())
			{
				this.g2D.drawString(string.toString(1), x+this.xDifferential, y);
				y += 17;
			}
			
			y += 10;
			
			if(algoWithStatsHeight == 0)
				algoWithStatsHeight = y;
			
			if(y - this.yDifferential + 85 > this.getHeight() - algoWithStatsHeight)
				break;
		}
		
		// ----------------------- Echelle -----------------------
		if(this.checkShowScale.isSelected() && this.listSortedProcess.size() > 0)
		{
			int xStart = x;
			int yScale = this.getHeight()-100+this.yDifferential;
			int hDash = 5;
			if(this.xCursor == -1)
			{
				this.xCursor = xStart;
				this.widthCursor = 2*hDash;
			}
			this.yCursor = yScale-hDash;
			double xDash = xStart;
			int panelWidth = this.getWidth()+this.xDifferential;
			
			this.g2D.setColor(Color.black);
			this.g2D.drawLine(xStart, yScale, panelWidth, yScale);
			double scaleDash = this.scale;
			
			if(scaleDash > 0)
			{
				if(scaleDash < 10)
					scaleDash = 10;
				int stringCounter = 1;
				while(xDash < panelWidth)
				{
					if(stringCounter % 5 == 0)
					{
						this.g2D.drawLine((int)xDash, yScale-(int)(1.75*hDash), (int)xDash, yScale+(int)(1.75*hDash));
						Tools.drawString(this.g2D, ""+(int)((xDash+this.xDifferential)/this.scale), (int)xDash, yScale-15, HorizontalAlignement.CENTER, VerticalAlignement.CENTER);
					}
					this.g2D.drawLine((int)xDash, yScale-hDash, (int)xDash, yScale+hDash);
					xDash += scaleDash;
					stringCounter++;
				}
			}
			
			this.g2D.fillRect(this.xCursor-this.widthCursor/2, this.yCursor, this.widthCursor, this.widthCursor);
			
			Stroke savedStroke = this.g2D.getStroke();
			this.g2D.setStroke(DASH_STROKE);
			this.g2D.drawLine(this.xCursor, yScale, this.xCursor, 0);
			this.g2D.setStroke(savedStroke);
			position = ""+(int)((this.xCursor-this.widthCursor+this.xDifferential)/this.scale);
			Tools.drawString(this.g2D, position, this.xCursor, yScale+13, HorizontalAlignement.CENTER, VerticalAlignement.CENTER);
		}

		int newPanelHeight = algoWithStatsHeight*this.listSortedProcess.size()+100;
		if(this.paintedPanel.getPreferredSize().width < xMax || this.paintedPanel.getPreferredSize().height < newPanelHeight || this.isDragging)
		{
			this.paintedPanel.setPreferredSize(new Dimension(xMax+1000, newPanelHeight));
			this.paintedPanel.updateUI();
			repaint();
		}
	}
	
	private void calculScale()
	{
		int xFinMax = 0;
		int xFin;
		int maxSize = 0;
		int xTest;
		int wTest;
		int totalProcessLength;
		double ratio;
		int panelWidth = this.getWidth();
		ArrayList<Process> listProcess;
		int lastElement;
		
		for(GanttDiagram list:this.listSortedProcess)
		{
			listProcess = list.getSortedProcess().getListSortedProcess();
			lastElement = listProcess.size() -1 ;
			totalProcessLength = listProcess.get(lastElement).getEndingTime();
			ratio = Tools.calculRatio(listProcess, this.getWidth(), 20);
			Process lastProcess = listProcess.get(lastElement);
			xTest = (int)(lastProcess.getRealIncomingTime()/(double)totalProcessLength*panelWidth*ratio);
			wTest = (int)(lastProcess.getDuration()/(double)totalProcessLength*panelWidth*ratio);
			xFin = xTest + wTest;
			if(xFin > xFinMax)
			{
				xFinMax = xFin;
				maxSize = listProcess.get(lastElement).getEndingTime();
			}
		}

		this.scale = xFinMax / (double)maxSize;
		if(this.scale > this.maxScale)
			this.scale = this.maxScale;
		this.slider.setValue((int)(this.scale*10));
	}
	
	/*-----------------------------------------------------------------*\
	|*						Attributs privés						   *|
	\*-----------------------------------------------------------------*/
	private Manager manager;
	private Graphics2D g2D;
	private ArrayList<Process> listProcess;
	private PanelAlgorithm panelAlgorithm;
	private ArrayList<String> selectedProcess;

	private JPanel paintedPanel;
	private JScrollPane mainScroll;
	private ArrayList<GanttDiagram> listSortedProcess;
	private int xDifferential;
	private int yDifferential;
	private double scale;
	private int xCursor;
	private int yCursor;
	private int widthCursor;
	private JCheckBox checkShowScale;
	
	private JTable inputList;
	private Abstract_ProcessTableModel table_model;
	
	private FrameStatistic frameStat;
	
	private boolean isDragging;
	
	private MyJSlider slider;
	private int maxScale = 20;
	private JCheckBox checkUseAutoScale;
	
	private ToolTipFrame toolTipFrame;
	private boolean isAnchored;
	
	private ButtonAlgorithm pimpedButton;
	
	/*-----------------------------------------*\
	|*				Statiques				   *|
	\*-----------------------------------------*/
	private static final float[] LINE_SYLE = {10,5}; // les pointillés seront 2 fois plus long que les blancs
	private static final BasicStroke DASH_STROKE = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, LINE_SYLE, 0);
}
