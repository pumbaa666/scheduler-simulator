package ch.correvon.scheduler.gui.algorithm;

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.correvon.scheduler.gui.myInterface.PanelScaleable;


public class SliderChangeListener implements ChangeListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public SliderChangeListener(JSlider slider, JTextField txtField, PanelScaleable panel)
	{
		this.panel = panel;
		this.slider = slider;
		this.txtField = txtField;
	}

	/*-----------------------------------------------------------------*\
	|*							Methodes publiques					   *|
	\*-----------------------------------------------------------------*/
	@Override public void stateChanged(ChangeEvent arg0)
	{
		int value = this.slider.getValue();
		if(value <= 1)
			value = 1;
		this.txtField.setText(""+value);
		this.panel.setScaleValue(value/10.0);
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private JSlider slider;
	private JTextField txtField;
	private PanelScaleable panel;
}