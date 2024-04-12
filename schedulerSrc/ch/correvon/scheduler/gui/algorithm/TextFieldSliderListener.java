package ch.correvon.scheduler.gui.algorithm;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JSlider;
import javax.swing.JTextField;

public class TextFieldSliderListener implements KeyListener
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public TextFieldSliderListener(JTextField txtField, JSlider slider)
	{
		this.txtField = txtField;
		this.slider = slider;
		this.released = true;
	}

	/*-----------------------------------------------------------------*\
	|*							Methodes publiques					   *|
	\*-----------------------------------------------------------------*/
	@Override public void keyPressed(KeyEvent arg0)
	{
		if(this.released)
		{
			this.originalText = this.txtField.getText();
			this.released = false;
		}
	}

	@Override public void keyReleased(KeyEvent arg0)
	{
		this.released = true;
		int value = -1;
		
		try
		{
			value = new Integer(this.txtField.getText()).intValue();
		}
		catch(NumberFormatException e)
		{
			if(this.txtField.getText().compareTo("") != 0)
				this.txtField.setText(this.originalText);
			return;
		}
		
		if(value >= this.slider.getMinimum() && value <= this.slider.getMaximum())
		{
			this.slider.setValue(value);
			this.slider.updateUI();
		}
		else
			this.txtField.setText(this.originalText);
	}

	@Override public void keyTyped(KeyEvent arg0)
	{
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private JTextField txtField;
	private JSlider slider;
	private String originalText;
	private boolean released;
}
