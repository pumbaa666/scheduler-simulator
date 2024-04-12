package ch.correvon.scheduler.moo.myObjects;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

import ch.correvon.scheduler.gui.algorithm.CheckSliderChangeListener;
import ch.correvon.scheduler.gui.algorithm.SliderChangeListener;
import ch.correvon.scheduler.gui.algorithm.TextFieldSliderListener;
import ch.correvon.scheduler.gui.myInterface.PanelScaleable;

public class MyJSlider extends JPanel// implements ComponentListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public MyJSlider(JPanel panelToDraw, PanelScaleable panelScaleable)
	{
		this(0, 10, panelToDraw, panelScaleable);
	}	
	
	public MyJSlider(int min, int max, JPanel panelToDraw, PanelScaleable panelScaleable)
	{
		this(min, max, (min+max)/2, panelToDraw, panelScaleable);
	}
	
	public MyJSlider(int value, JPanel panelToDraw, PanelScaleable panelScaleable)
	{
		this(0, 1000, value, panelToDraw, panelScaleable);
	}
	
	public MyJSlider(int min, int max, int value, JPanel panelToDraw, PanelScaleable panelScaleable)
	{
		this.checkBox = new JCheckBox("Auto");
		this.checkBox.setToolTipText("Coché : choisi une échelle automatique pour le dessin des diagrammes de Gantt");
		this.checkBox.setEnabled(true);
		
		this.slider = new JSlider(min, max, value);
		this.slider.setMinorTickSpacing(50);
		this.slider.setMajorTickSpacing(150);
		this.slider.setPaintTicks(true);
		this.slider.setPaintLabels(true);
		
		this.txtField = new JTextField(3);
		this.txtField.setPreferredSize(new Dimension(this.txtField.getPreferredSize().width, 20));
		this.txtField.setText(""+this.slider.getValue());
		this.txtField.addKeyListener(new TextFieldSliderListener(this.txtField, this.slider));
		
		CheckSliderChangeListener checkListener = new CheckSliderChangeListener(this.checkBox, this.slider, this.txtField, panelToDraw);
		this.checkBox.addChangeListener(checkListener);
		checkListener.stateChanged(new ChangeEvent(this));
		
		SliderChangeListener sliderListener = new SliderChangeListener(this.slider, this.txtField, panelScaleable);
		this.slider.addChangeListener(sliderListener);
		sliderListener.stateChanged(new ChangeEvent(this));
		
		this.setLayout(new BorderLayout());
		this.add(this.checkBox, BorderLayout.WEST);
		this.add(this.slider, BorderLayout.CENTER);
		this.add(this.txtField, BorderLayout.EAST);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/	
	/*---------------------------------*\
	|*				Get				   *|
	\*---------------------------------*/
	public JCheckBox getCheckBox()
	{
		return this.checkBox;
	}
	
	public JSlider getSlider()
	{
		return this.slider;
	}
	
	public JTextField getTxtField()
	{
		return this.txtField;
	}
	
	public int getValue()
	{
		return this.slider.getValue();
	}
	
	/*---------------------------------*\
	|*				Set				   *|
	\*---------------------------------*/
	public void setValue(int value)
	{
		this.slider.setValue(value);
	}
	
	public void setSelected(boolean value)
	{
		this.checkBox.setSelected(value);
	}
	
	/*---------------------------------*\
	|*				Is				   *|
	\*---------------------------------*/
	public boolean isSelected()
	{
		return this.checkBox.isSelected();
	}
	
	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private JCheckBox checkBox;
	private JSlider slider;
	private JTextField txtField;
}
