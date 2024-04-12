package ch.hearc.scheduler.moo;

import java.awt.Font;
import java.awt.Toolkit;

/**
 * Contient plusieurs informations qui ne changent pas lors de l'exécution du programme.
 */
public class Options
{
	public static final String IMAGES_PATH = new String("..\\ressources\\images\\");
	public static final String FILE_PATH =   new String("..\\ressources\\data");
	public static final Font FONT_LISTBOX = new Font("Courier New", Font.PLAIN,12);
	public static final int WIDTH_FENETRE = 500;
	public static final int HEIGHT_FENETRE = 600;
	public static final int WIDTH_ECRAN = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int HEIGHT_ECRAN = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static final int NB_PROC_MAX = 200;
}
