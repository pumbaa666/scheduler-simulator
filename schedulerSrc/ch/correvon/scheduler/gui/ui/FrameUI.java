// Specification:
package ch.correvon.scheduler.gui.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import ch.correvon.scheduler.moo.Manager;
import ch.correvon.scheduler.moo.Menus;
import ch.correvon.scheduler.moo.myObjects.MyJFrame;

public class FrameUI extends MyJFrame implements ComponentListener, WindowListener, WindowStateListener, Runnable
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public FrameUI(String title, Manager manager)
	{
		super();
		this.manager1 = manager;
		this.setState();
		this.setLocation();
		this.setSize();
		this.panelUI = new PanelUI(manager, this);
		this.chrono = new Chronometer(this); // Fait office de verrou (exclusion mutuelle) pour que la frame ai le temps de se rendre compte d'un évenement de changement d'état (maximisation par exemple) alors que les évenements de changement de position arrivent avant
		this.tryToUpdate = false;
		
		this.addComponentListener(this);
		this.addWindowListener(this);
		this.addWindowStateListener(this);

		this.setResizable(true);
		this.setTitle(title);

		ArrayList<ArrayList<String>> structure = new ArrayList<ArrayList<String>>(1);
		
		ArrayList<String> file = new ArrayList<String>(9);
		file.add("Fichier");
			file.add("Nouveau");
			file.add("Ouvrir");
			file.add("Enregistrer");
			file.add(" - ");
			file.add("Imprimer");
			file.add(" - ");
			file.add("Préférences");
			file.add(" - ");
			file.add("A propos");
			file.add(" - ");
			file.add("Quitter");
			structure.add(file);
			
		Menus menu = new Menus(structure, new InterfaceMenuListener(manager));
		this.setMenuBar(menu);
		
		this.setIconImage(getToolkit().getImage("ressources\\Images\\chip1.jpg"));
		
		this.setVisible(false);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes publiques					   *|
	\*-----------------------------------------------------------------*/
	public void run()
	{
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setExtendedState(this.state);
	}
	
	@Override public int getState()
	{
		return this.state;
	}
	
	public PanelUI getPanelUI()
	{
		return this.panelUI;
	}
	
	public Point getLastLocation()
	{
		return this.lastLocation;
	}
	
	public Dimension getLastSize()
	{
		return this.lastSize;
	}
	
	@Override public void setLocation(int x, int y)
	{
		super.setLocation(x, y);
		this.lastLocation = new Point(x, y);
	}
	
	@Override public void setLocation(Point point)
	{
		this.setLocation(point.x, point.y);
	}
	
	@Override public void setSize(int w, int h)
	{
		Dimension dimension = new Dimension(w, h);
		super.setSize(w, h);
		this.lastSize = dimension;
	}
	
	@Override public void setSize(Dimension dimension)
	{
		this.setSize(dimension.width, dimension.height);
	}
	
	@Override public void close()
	{
	}

	/*-----------------------------------------------------------------*\
	|*							Ecouteurs							   *|
	\*-----------------------------------------------------------------*/
	@Override public void componentHidden(ComponentEvent e)
	{
	}

	@Override public void componentMoved(ComponentEvent e)
	{
		this.changed();
	}

	@Override public void componentResized(ComponentEvent e)
	{
		this.changed();
	}

	@Override public void componentShown(ComponentEvent e)
	{
	}
	
	@Override public void windowDeiconified(WindowEvent e)
	{
	}
	
	@Override public void windowActivated(WindowEvent e)
	{
	}
	
	@Override public void windowIconified(WindowEvent e)
	{
	}
	
	@Override public void windowDeactivated(WindowEvent e)
	{
	}
	
	@Override public void windowClosing(WindowEvent e)
	{
		this.manager1.quit();
	}
	
	@Override public void windowClosed(WindowEvent e)
	{
	}
	
	@Override public void windowOpened(WindowEvent e)
	{
	}
	
	@Override public void windowStateChanged(WindowEvent e)
	{
		this.state = e.getNewState();
		this.setState(this.state);
		this.chrono.stop();
		if(this.tryToUpdate)
		{
			this.updateLastLocation();
			this.tryToUpdate = false;
		}
	}
	
	public void updateLocation()
	{
		this.tryToUpdate = false;
		
			this.chrono.stop();
		this.updateLastLocation();
	}
	
	/*-----------------------------------------------------------------*\
	|*							Méthodes privées					   *|
	\*-----------------------------------------------------------------*/
	private void changed()
	{
		if(!this.chrono.isRunning())
		{
			this.chrono.start();
			this.updateLastLocation();
		}
	}
	
	private void setState()
	{
		String temp = this.manager1.getPreferances("frameUI_state");
		if(temp.compareTo("NaN") != 0)
		{
			try
			{
				this.state = new Integer(temp).intValue();
			}
			catch(NumberFormatException e)
			{
				this.state = -1;
			}
		}
		this.setState(this.state);
	}
	
	private void setLocation()
	{
		String temp = this.manager1.getPreferances("frameUI_location");
		if(temp.compareTo("NaN") != 0)
		{
			try
			{
				String[] split = temp.split(",");
				int x = new Integer(split[0]).intValue();
				int y = new Integer(split[1]).intValue();
				this.setLocation(x, y);
			}
			catch(Exception e)
			{
				this.setLocation(0, 0);
			}
		}
	}

	private void setSize()
	{
		String temp = this.manager1.getPreferances("frameUI_size");
		if(temp.compareTo("NaN") != 0)
		{
			try
			{
				String[] split = temp.split(",");
				int w = new Integer(split[0]).intValue();
				int h = new Integer(split[1]).intValue();
				this.setSize(w, h);
			}
			catch(Exception e)
			{
				this.setDefaultSize();
			}
		}
		else
			this.setDefaultSize();
	}
	
	private void setDefaultSize()
	{
		this.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height));
		this.setState(JFrame.MAXIMIZED_BOTH);
	}
	
	private void updateLastLocation()
	{
		if(!this.chrono.isRunning())
		{
			if(this.getState() != JFrame.MAXIMIZED_BOTH)
			{
				this.lastLocation = this.getLocation();
				this.lastSize = this.getSize();
			}
		}
		else
		{
			this.tryToUpdate = true;
		}
	}

	/*-----------------------------------------------------------------*\
	|*						Attritbuts privés						   *|
	\*-----------------------------------------------------------------*/
	private Manager manager1;
	private int state;
	private PanelUI panelUI;
	private Point lastLocation;
	private Dimension lastSize;
	private Chronometer chrono;
	private boolean tryToUpdate;
}
