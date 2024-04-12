package ch.correvon.scheduler.gui.algorithm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import ch.correvon.scheduler.moo.myObjects.MyJFrame;
import ch.correvon.scheduler.moo.process.Process;

public class ToolTipFrame extends MyJFrame implements FocusListener
{
	/*----------------------------------------------------------------*\
	|*							Constructeurs						  *|
	\*----------------------------------------------------------------*/
	public ToolTipFrame(PanelAlgoView panel)
	{
		super(0, 0, 160, 210);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setBackground(new Color(0, 255, 0, 50));
		
		this.panel = panel;
		
		this.toolTipPanel = new JPanel();
		this.toolTipPanel.setLayout(new BorderLayout());
		this.toolTipPanel.setOpaque(false);
		this.toolTipPanel.setBackground(new Color(255, 255, 255, 50));
		
		this.pane = new JTextPane();
		this.pane.setContentType("text/html");
		this.pane.setEditable(false);
		
		Box boxH = Box.createHorizontalBox();
		boxH.add(this.pane);
		this.toolTipPanel.add(boxH, BorderLayout.WEST);
		
		this.add(this.toolTipPanel);
		
		this.pane.addFocusListener(this);
		
		this.setVisible(false);
	}

	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	@Override public void close()
	{
		this.setVisible(false);
	}
	
	
	public void selectText()
	{
		this.pane.setSelectionStart(0);
		this.pane.setSelectionEnd(this.endSelection-178);
	}
	
	/*-----------------------------------------------------*\
	|*						Ecouteur 					   *|
	\*-----------------------------------------------------*/
	@Override public void focusGained(FocusEvent arg0)
	{
	}

	@Override public void focusLost(FocusEvent arg0)
	{
		this.panel.setAnchored(false);
		this.dispose();
	}
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setProcess(Process process)
	{
		String utilText = "<html>" +
		"<font size = \"5\">"+process.getName()+"<br><font size = \"3\">"+
		"Arrivée : "+process.getIncomingTime()+"<br>"+
		"Durée : "+process.getDuration()+"<br>"+
		"Arrivée réelle : "+process.getRealIncomingTime()+"<br>"+
		"Fin : "+process.getEndingTime()+"<br>"+
		"Attente : "+process.getWaitingTime()+"<br>"+
		"Réponse : "+process.getResponseTime()+"<br>"+
		"Rotation : "+process.getRotateTime()+"<br>"+
		"Affecté au CPU : "+process.getCPUInCharge()+"<br><br>";
		
		this.pane.setText(utilText);
		this.endSelection = this.pane.getText().length();
		
		String focusText = "<i>Pressez F2 pour donner le focus</i></html>";
		
		
		this.pane.setText(utilText+focusText);

		this.toolTipPanel.repaint();
	}
	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public JTextPane getTxtPane()
	{
		return this.pane;
	}
	
	/*-----------------------------------------------------------------*\
	|*						Attributs privés						   *|
	\*-----------------------------------------------------------------*/
	private JPanel toolTipPanel;
	private JTextPane pane;
	private int endSelection;
	private PanelAlgoView panel;
}
