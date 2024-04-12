package ch.correvon.scheduler.test;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Properties;

import javax.swing.RepaintManager;

public class PrintUtilities implements Printable
{
	/** le composant à imprimer */
	private Component componentToBePrinted;

	/**
	 * imprimer composant
	 * 
	 * @param c
	 *            le composant
	 */
	public static void printComponent(Component c)
	{
		new PrintUtilities(c).print();
	}

	/**
	 * constructeur
	 * 
	 * @param componentToBePrinted
	 *            compsant à imprimer
	 */
	public PrintUtilities(Component componentToBePrinted)
	{
		this.componentToBePrinted = componentToBePrinted;
	}

	/** print method * */
	public void print()
	{
		PrinterJob printJob = PrinterJob.getPrinterJob();
		
		
		Properties howPrint = new Properties();
		howPrint.put( "awt.print.destination", "printer" );
		// could be "printer" or "file", default "printer"

		howPrint.put( "awt.print.fileName", "TEMP/TEMP.PRN");
		// file to receive the PostScript or other physical printer commands

		howPrint.put( "awt.print.numCopies", "1" );
		// default 1

		howPrint.put( "awt.print.orientation", "landscape" );
		// could be "portrait" or "landscape", default "portrait"

		howPrint.put( "awt.print.paperSize", "a4" );
		// could be "letter","legal","executive " or "a4". default "letter"

		//howPrint.put( "awt.print.printer", "lp" );
		// name of command/utility that will do the printing

		//howPrint.put( "awt.print.options", "" );
		// options to pass to the print command/utility
		
		PageFormat pf = printJob.pageDialog(printJob.defaultPage()); //printJob.defaultPage();
		Paper papier = new Paper();
		double gauche = 10.0 * 72 / 25.4;
		papier.setImageableArea(gauche, 0.0, papier.getWidth() - 2*gauche, papier.getHeight());
		pf.setPaper(papier);
		printJob.setPrintable(this, pf);
		
		
		//printJob.setPrintable(this);
		if (printJob.printDialog())
			try
			{
				printJob.print();
			}
			catch (PrinterException pe)
			{
				System.out.println("Error printing: " + pe);
			}
	}

	/**
	 * print method
	 * 
	 * @param g
	 *            graphisme
	 * @param pageFormat
	 *            format
	 * @param pageIndex
	 *            index
	 * @return valeur de retour de l'impression
	 */

	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
	{
		if (pageIndex > 0)
			return (NO_SUCH_PAGE);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		disableDoubleBuffering(componentToBePrinted);
		componentToBePrinted.paint(g2d);
		enableDoubleBuffering(componentToBePrinted);
		return (PAGE_EXISTS);
	}

	public static void disableDoubleBuffering(Component c)
	{
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}

	/**
	 * @param c
	 *            composant
	 */

	public static void enableDoubleBuffering(Component c)
	{
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
}
