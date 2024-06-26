package ch.correvon.scheduler.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MPanelPrinter implements Printable
{

	public MPanelPrinter(JPanel panel)
	{
		documentTitle = "";
		this.panel = panel;
		initPrintablePanel();
	}

	public void initPrintablePanel()
	{
		fitIntoPage = false;
		wrapComponent = false;
		printJob = PrinterJob.getPrinterJob();
		pageFormat = printJob.defaultPage();
		pageFormat.setOrientation(PORTRAIT);
	}

	public void setOrientation(int orientation)
	{
		pageFormat.setOrientation(orientation);
	}

	public void setWrapComponent(boolean status)
	{
		wrapComponent = status;
	}

	public void setFitIntoPage(boolean status)
	{
		fitIntoPage = status;
	}

	public int getPageWidth()
	{
		return (int) pageFormat.getImageableWidth();
	}

	public double getMarginTop()
	{
		return pageFormat.getImageableY();
	}

	public double getMarginLeft()
	{
		return pageFormat.getImageableX();
	}

	public void setLRMargins(int margin)
	{
		Paper paper = pageFormat.getPaper();
		paper.setImageableArea(paper.getImageableX() - (margin / 2.0),
				paper.getImageableY(), paper.getImageableWidth()
						+ (margin / 2.0), paper.getImageableHeight());
		pageFormat.setPaper(paper);
	}

	public void setTBMargins(int margin)
	{
		Paper paper = pageFormat.getPaper();
		paper.setImageableArea(paper.getImageableX(), paper.getImageableY()
				- (margin / 2.0), paper.getImageableWidth(), paper
				.getImageableHeight()
				+ (margin / 2.0));
		pageFormat.setPaper(paper);
	}

	public void setDocumentTitle(String title)
	{
		documentTitle = title;
	}

	public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException
	{
		Dimension tailleDoc = panel.getSize();
		double hauteurDocu = tailleDoc.getHeight();
		double hauteurPage = pf.getImageableHeight();
		double largeurDocu = tailleDoc.getWidth();
		double largeurPage = pf.getImageableWidth();
		int totalNumPages = (int) Math.ceil(hauteurDocu / hauteurPage);
		if (wrapComponent)
		{
			this.calculatePages();
			totalNumPages = taillePages.size();
		}
		else if (fitIntoPage)
			totalNumPages = 1;
		double scaleX = largeurPage / largeurDocu;
		double scaleY = hauteurPage / hauteurDocu;
		if (pageIndex >= totalNumPages)
			return 1;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		if (fitIntoPage)
		{
			double ratio = Math.min(scaleX, scaleY);
			g2d.scale(ratio, ratio);
		}
		else if (wrapComponent)
		{
			if (pageIndex > 0)
				g2d.translate(0.0D, -(taillePages.get(pageIndex - 1)).doubleValue());
		}
		else
		{
			g2d.translate(0.0D, (-pageIndex) * hauteurPage);
		}
		panel.paint(g2d);
		if (wrapComponent)
		{
			double hauteurBlanc = (taillePages.get(pageIndex)).doubleValue();
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, (int) hauteurBlanc, (int) largeurPage, (int) hauteurBlanc + (int) hauteurPage);
		}
		if (wrapComponent)
		{
			if (pageIndex > 0)
				g2d.translate(0.0D, (taillePages.get(pageIndex - 1)).doubleValue());
		}
		else
		{
			g2d.translate(0.0D, pageIndex * hauteurPage);
		}
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Verdanna", 2, 10));
		g2d.drawString(documentTitle + " - [" + (pageIndex + 1) + "/"
				+ totalNumPages + "]", 0, (int) pf.getImageableHeight() - 20);
		return 0;
	}

	public void print()
	{
		printJob.setPrintable(this, pageFormat);
		try
		{
			if (printJob.printDialog())
			{
				if (wrapComponent)
					calculatePages();
				Paper paper = pageFormat.getPaper();
				Paper save = pageFormat.getPaper();
				paper.setImageableArea(paper.getImageableX(), paper
						.getImageableY(), paper.getWidth()
						- paper.getImageableX(), paper.getHeight()
						- paper.getImageableY());

				pageFormat.setPaper(paper);
				printJob.setPrintable(this, pageFormat);
				printJob.print();
				pageFormat.setPaper(save);
			}
		}
		catch (PrinterException pe)
		{
			JOptionPane.showMessageDialog(null, "Erreur lors de l'impression du document: "+this.documentTitle, "Erreur", JOptionPane.OK_OPTION);
			pe.printStackTrace();
		}
	}

	private void calculatePages()
	{
		double hauteurPage = pageFormat.getImageableHeight();
		double hauteurTotal = 0;
		double hauteurCumul = 0;

		taillePages = new Vector<Double>();

		for (int i = 0; i < panel.getComponentCount(); i++)
		{
			int gridBagInsets = 0;
			if (panel.getLayout() instanceof GridBagLayout)
				gridBagInsets = ((GridBagLayout) panel.getLayout()).getConstraints(panel.getComponent(i)).insets.bottom + ((GridBagLayout) panel.getLayout()).getConstraints(panel.getComponent(i)).insets.top;
			double hauteurComponent = panel.getComponent(i).getSize().getHeight() + gridBagInsets;
			if (hauteurComponent > hauteurPage)
			{
				wrapComponent = false;
				return;
			}
			hauteurTotal += hauteurComponent;
			if (hauteurTotal > hauteurPage)
			{
				hauteurTotal -= hauteurComponent;
				hauteurCumul += hauteurTotal;
				taillePages.add(new Double(hauteurCumul));
				hauteurTotal = hauteurComponent;
			}
		}

		hauteurCumul += hauteurTotal;
		taillePages.add(new Double(hauteurCumul));
	}

	private JPanel panel;
	private boolean fitIntoPage;
	private boolean wrapComponent;
	private PageFormat pageFormat;
	private PrinterJob printJob;
	private Vector<Double> taillePages;
	private String documentTitle;
	public static final int PORTRAIT = 1;
	public static final int LANDSCAPE = 0;
}
