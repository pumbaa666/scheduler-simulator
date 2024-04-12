package ch.correvon.scheduler.gui.algorithm;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CheckSliderChangeListener implements ChangeListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public CheckSliderChangeListener(JCheckBox checkBox, JSlider slider, JTextField txtField, JPanel panelToRepaint)
	{
		this.checkBox = checkBox;
		this.slider = slider;
		this.txtField = txtField;
		this.panelToRepaint = panelToRepaint;
	}

	/*-----------------------------------------------------------------*\
	|*							Methodes publiques					   *|
	\*-----------------------------------------------------------------*/
	@Override public void stateChanged(ChangeEvent arg0)
	{
		boolean value = this.checkBox.isSelected();
		this.setEnable(!value);
		this.panelToRepaint.repaint();
	}
	
	private void setEnable(boolean value)
	{
		this.slider.setEnabled(value);
		this.txtField.setEnabled(value);
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private JCheckBox checkBox;
	private JSlider slider;
	private JTextField txtField;
	private JPanel panelToRepaint;
}
