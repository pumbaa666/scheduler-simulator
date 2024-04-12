
//Specification:
package ch.correvon.scheduler.moo;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import ch.correvon.scheduler.moo.myObjects.MyClonable;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.ProcessType;
import ch.correvon.scheduler.moo.process.Statistic;
import ch.correvon.scheduler.moo.process.UsedType;
import ch.correvon.scheduler.plugin.Algo_I;
import ch.correvon.scheduler.plugin.Statistic_I;


/**
 * Différents outils utilisés réguliérement dans ce projet
 */
public class Tools
{
	/**
	 * Sélectionne une partie du texte d'une textBox.
	 */
	public static void selectText(JTextField txt, int start, int end)
	{
		txt.setSelectionStart(start);
		txt.setSelectionEnd(end);
		//return txt;
	}
	
	/**
	 * Sélectionne tout le texte d'une textBox.
	 */
	public static void selectAllText(JTextField txt)
	{
		selectText(txt, 0, txt.getText().length());
	}
	
	/**
	 * Ré-évalue la priorité de chaque ProcessType donné en paramètre dans listType pour que la somme de toutes les priorités donne totalPriorityTime.
	 */
	public static void normalizePriority(Hashtable<String, UsedType> listType, int totalPriorityTime)
	{
		int total = 0;
    	String key;
    	UsedType element;
		for(Enumeration<String> e = listType.keys() ; e.hasMoreElements() ; )
		{
			key = e.nextElement();
        	element = listType.get(key);
			
			total += element.getType().getPriority();
		}
		double ratio = totalPriorityTime / (double)total;
		
		int newPriority;
		double differential = 0.0;
		total = 0;
		ProcessType type = null;
		int count = 0;

		for(Enumeration<String> e = listType.keys() ; e.hasMoreElements() ; )
		{
			key = e.nextElement();
        	element = listType.get(key);
			type = element.getType();
			newPriority = (int)(type.getPriority()*ratio);
			if(newPriority < 0)
				newPriority = 1;
			differential += type.getPriority()*ratio - newPriority;
			if(differential >= 1.0)
			{
				newPriority += (int)differential;
				differential = type.getPriority()*ratio - newPriority;
			}
			type.setPriority(newPriority);
			total += newPriority;
			count++;
		}
		type.setPriority(type.getPriority()+(totalPriorityTime - total));
	}
	
	private static Point drawGantt(Graphics2D g2D, ArrayList<Process> listProcess, ArrayList<String> selectedProcess, int panelWidth, int totalProcessLength, double ratio, int xStart, int yStart, int xDifferential, int yDifferential)
	{
		int x;
		int xTest;
		int w;
		double wTest;
		int h = 20;
		yStart = yStart - yDifferential + 25;
		int hTextTime = yStart-10;
		int hProcessName = yStart+h/2;
		int lastHourDraw = 0;
		int hTimeDifferential = 0;
		Stroke tempStroke = g2D.getStroke();
		Font tempFont = g2D.getFont();
		g2D.setFont(new Font("Times new roman", Font.PLAIN, 12));
		String endingTime;
		
		int y = yStart;
		int yMax = 0;
		for(Process process:listProcess)
		{
			// Rectangle
			xTest = (int)(process.getRealIncomingTime()/(double)totalProcessLength*panelWidth*ratio)-xDifferential + xStart;
			wTest = (int)(process.getDuration()/(double)totalProcessLength*panelWidth*ratio);
			y = process.getCPUInCharge()*(h+10)+yStart;
			process.setBounds(xTest, y, (int)wTest, h);
			
			if(xTest > xDifferential+panelWidth+100)
				break;
			else if(xTest + wTest > 0)
			{
				// Couleur
				g2D.setColor(process.getType().getColor());
				
				x = xTest;
				w = (int)wTest;
				for(String s:selectedProcess)
					if(process.getName().compareTo(s) == 0)
					{
						tempStroke = g2D.getStroke();
						g2D.setStroke(LARGE_LINE_STROKE);
					}
				g2D.drawRect(x, y, w, h);
				g2D.setStroke(tempStroke);
				
				// Heure début
				if(x+1 >= lastHourDraw) // +1 compenser les erreurs d'arrondi
					lastHourDraw = Tools.drawString(g2D, process.getRealIncomingTime()+"", x+5, hTextTime+hTimeDifferential, HorizontalAlignement.LEFT, VerticalAlignement.TOP);
				
				// Heure fin
				endingTime = process.getEndingTime()+"";
				if(w > 40 && Math.abs(x+w-g2D.getFontMetrics().stringWidth(endingTime) - lastHourDraw) > 50)
					lastHourDraw = Tools.drawString(g2D, endingTime, w+x, hTextTime, HorizontalAlignement.RIGHT, VerticalAlignement.TOP);
				
				// Nom
				if(w > 20)
				{
					hProcessName = y+h/2;
					Tools.drawString(g2D, process.getName(), x+w/2, hProcessName, HorizontalAlignement.CENTER, VerticalAlignement.CENTER); // Affiche le nom
				}
			}
			if(y > yMax)
				yMax = y;
		}
		Process lastProcess = listProcess.get(listProcess.size()-1);
		xTest = (int)(lastProcess.getRealIncomingTime()/(double)totalProcessLength*panelWidth*ratio)-xDifferential + xStart;
		wTest = (int)(lastProcess.getDuration()/(double)totalProcessLength*panelWidth*ratio);
		g2D.setFont(tempFont);

		
		return new Point((int)(xTest + wTest), yMax+20);
	}

	public static Point drawGantt(Graphics2D g2D, ArrayList<Process> listProcess, ArrayList<String> selectedProcess, int panelWidth, int xStart, int yStart, int xDifferential, int yDifferential)
	{
		int totalProcessLength = listProcess.get(listProcess.size()-1).getEndingTime();
		
		FontMetrics metric = g2D.getFontMetrics(g2D.getFont());
		double minimalTextSize = metric.stringWidth(" "+listProcess.get(listProcess.size()-1).getName()+" ");
		
		double ratio = minimalTextSize;
		double min = listProcess.get(0).getDuration()/(double)totalProcessLength*panelWidth;
		double wTest;
		
		for(Process process:listProcess)
		{
			wTest = process.getDuration()/(double)totalProcessLength*panelWidth;
			if(wTest < min)
				min = wTest;
		}
		if(min < minimalTextSize && min > 0)
			ratio = minimalTextSize/min;
		else
			ratio = 0.96;

		return drawGantt(g2D, listProcess, selectedProcess, panelWidth, totalProcessLength, ratio, xStart, yStart, xDifferential, yDifferential);
	}

	public static Point drawGantt(Graphics2D g2D, ArrayList<Process> listProcess, ArrayList<String> selectedProcess, int panelWidth, double primalSpace, int xStart, int yStart, int xDifferential, int yDifferential)
	{
		return drawGantt(g2D, listProcess, selectedProcess, panelWidth, 1, primalSpace/panelWidth, xStart, yStart, xDifferential, yDifferential);
	}

	/**
	 * Calcul des statistiques sur la liste de processus listProcess.
	 * Renvoie le résultat sous forme d'une liste de String.
	 */
	public static ArrayList<Statistic> averagecalcul(ArrayList<Process> listProcess, ArrayList<Statistic_I> listStats, Hashtable<String, String> listEnabledStat, ArrayList<Statistic> statNames)
	{
		ArrayList<Process> clonedList = deepClone(listProcess); // Copie profonde pour pas que les programmeurs de stats puissent modifier les processus internes au simulateur
		clonedList = factorListForStat(clonedList);
		ArrayList<Statistic> result = new ArrayList<Statistic>(listStats.size());
		double value = 0;
		String hashStat;
		
		for(Statistic_I stat:listStats)
		{
			hashStat = listEnabledStat.get("Stat_"+stat.getName());
			if(hashStat == null || hashStat.compareTo("false") != 0)
			{
				value = stat.calcul(clonedList); 
				result.add(new Statistic(stat.getName(), value, findStatistic(stat.getName(), statNames).getColor()));
			}
		}
		
		return result;
	}
	
	/**
	 * "Factorise" les processus. Si un processus a été fragmenté en plusieurs partie par un algorithme préemptif, chaque fragment possède ses propres temps d'attente et de rotation.
	 * Cette fonction regroupe tout les fragement de processus en un seul, avec la somme des ses statistiques.
	 */
	public static ArrayList<Process> factorListForStat(ArrayList<Process> listProcess)
	{
		ArrayList<Process> result = new ArrayList<Process>(listProcess.size());
		ArrayList<String> alreadyCounted = new ArrayList<String>(listProcess.size());
		String processName;
		Process editedProcess;
		
		for(Process process:listProcess)
		{
			processName = process.getName();
			
			if(alreadyCounted.indexOf(processName) != -1)
			{
				editedProcess = result.remove(searchProcess(processName, result));
				editedProcess.setWaitingTime(editedProcess.getWaitingTime()+process.getWaitingTime());
				editedProcess.setRotateTime(process.getRotateTime());
				result.add(editedProcess);
			}
			else
			{
				alreadyCounted.add(processName);
				result.add(process);
			}
		}
		
		return result;
	}
	
	public static Statistic findStatistic(String name, ArrayList<Statistic> listStat)
	{
		for(Statistic stat:listStat)
			if(stat.getName().compareTo(name) == 0)
				return stat;
		return new Statistic("Defaut");
	}
	
	public static int drawString(Graphics g, String string, int x, int y)
	{
		return drawString(g, string, x, y, HorizontalAlignement.LEFT, VerticalAlignement.TOP);
	}
	
	public static int drawString(Graphics g, String string, int x, int y, HorizontalAlignement horizontalAlign, VerticalAlignement verticalAlign)
	{
		FontMetrics fontMetrics = g.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth(string);
		int stringHeight = fontMetrics.getHeight();
		int finalX;
		int finalY;
		
		if(horizontalAlign == HorizontalAlignement.CENTER)
			finalX = (x - stringWidth / 2);
		else if(horizontalAlign == HorizontalAlignement.LEFT)
			finalX = x;
		else
			finalX = x - stringWidth;
		
		if(verticalAlign == VerticalAlignement.CENTER)
			finalY = (y + stringHeight / 3);
		else if(verticalAlign == VerticalAlignement.TOP)
			finalY = y;
		else
			finalY = y + stringHeight;
		
		g.drawString(string, finalX, finalY);
		
		return stringWidth+finalX;
	}
	
	public static void drawStringZone(Graphics g, String string, Point start, Rectangle bounds)
	{
		if(bounds.contains(start))
			g.drawString(string, start.x, start.y);
	}
	
	/**
	 * Construit et renvoie une chaine de caractère composée de chaque élements de list, séparé par autant d'espace que définit par tabulationSize .
	 */
	public static String buildStringTab(int tabulationSize, ArrayList<String> list)
	{
		return buildStringTab(tabulationSize, (String[])list.toArray());
	}
	
	/**
	 * Construit et renvoie une chaine de caractère composée de chaque élements de list, séparé par autant d'espace que définit par tabulationSize .
	 */
	public static String buildStringTab(int tabulationSize, String[] list)
	{
		String result = "";
		for(int j = 0; j < list.length; j++)
		{
			result = result.concat(list[j]);
			for(int i = result.length(); i%tabulationSize!=0; i++)
				result = result.concat(" ");
		}
		return result;
	}
	
	/**
	 * Fait de la colonne sélectionnée (columnIndex) une colonne de CheckBox, avec la taille minimale.
	 */
	public static void setColumnCheck(JTable table, int columnIndex)
	{
		TableColumn column = table.getColumnModel().getColumn(columnIndex);
		column.setCellEditor(new DefaultCellEditor(new JCheckBox()));
		column.setMaxWidth(20);
	}
	
	/**
	 * Renvoie une copie profonde élément par élément de la liste source .
	 */
	public static ArrayList deepClone(ArrayList<? extends MyClonable> source)
	{
		ArrayList<MyClonable> clone = new ArrayList<MyClonable>(source.size());

		for(MyClonable o:source)
			clone.add(o.myClone());
		
		return clone;
	}
	
	
	public static void printProcess(ArrayList<Process> listProcess)
	{
		for(Process p:listProcess)
			System.out.println(p.getName()+" / Wait : "+p.getWaitingTime()+" / Rotate : "+p.getRotateTime()+" / Response : "+p.getResponseTime());
	}
	
	/**
	 * Renvoie l'indice du type cherché par searchedType dans la liste listType.
	 * Renvoie -1 si aucune occurence n'est trouvée.
	 */
	public static int searchType(String searchedType, ArrayList<ProcessType> listType)
	{
		int i = 0;
		
		for(ProcessType type:listType)
		{
			if(type.isEquals(searchedType))
				return i;
			i++;
		}
		
		System.err.println("Impossible de trouver le type '"+searchedType.toString() +"' dans la liste");
		return -1;
	}
	
	/**
	 * Renvoie l'indice du type cherché par searchedType dans la liste listType.
	 * Renvoie -1 si aucune occurence n'est trouvée.
	 */
	public static int searchType(ProcessType searchedType, ArrayList<ProcessType> listType)
	{
		return searchType(searchedType.toString(), listType);
	}

	/**
	 * Renvoie l'indice du processus cherché par searchedProcess dans la liste listProcess.
	 * Renvoie -1 si aucune occurence n'est trouvée.
	 */
	public static int searchProcess(Process searchedProcess, ArrayList<Process> listProcess)
	{
		return searchProcess(searchedProcess.getName(), listProcess);
	}

	/**
	 * Renvoie l'indice du processus cherché par searchedProcess dans la liste listProcess.
	 * Renvoie -1 si aucune occurence n'est trouvée.
	 */
	public static int searchProcess(String searchedProcess, ArrayList<Process> listProcess)
	{
		int i = 0;
		
		for(Process algo:listProcess)
		{
			if(algo.getName().compareTo(searchedProcess) == 0)
				return i;
			i++;
		}
		
		return -1;
	}

	/**
	 * Renvoie l'indice de l'algorithme cherché par searchedAlgo dans la liste listAlgo.
	 * Renvoie -1 si aucune occurence n'est trouvée.
	 */
	public static int searchAlgo(Algo_I searchedAlgo, ArrayList<Algo_I> listAlgo)
	{
		return searchAlgo(searchedAlgo.toString(), listAlgo);
	}

	/**
	 * Renvoie l'indice de l'algorithme cherché par searchedAlgo dans la liste listAlgo.
	 * Renvoie -1 si aucune occurence n'est trouvée
	 */
	public static int searchAlgo(String searchedAlgo, ArrayList<Algo_I> listAlgo)
	{
		int i = 0;
		
		for(Algo_I algo:listAlgo)
		{
			if(algo.getName().compareTo(searchedAlgo) == 0)
				return i;
			i++;
		}
		
		return -1;
	}
	
	/**
	 * Recherche le 1er nombre positif dans la chaine de caractère text.
	 * Ce nombre peut être composé de plusieurs chiffres.
	 * 
	 * Renvoie -1 si aucun nombre n'est trouvé.
	 */
	public static int searchNumber(String text)
	{
		int number = 0;
		String figure = "";
		char[] string = text.toCharArray();
		boolean alreadyFounded = false;
		
		for(int i = 0; i < string.length; i++)
		{
			try
			{
				number = new Integer(new Character(string[i]).toString());
				figure = figure.concat(""+number);
				alreadyFounded = true;
			}
			catch (NumberFormatException e)
			{
				if(alreadyFounded)
					break;
			}
		}
		
		if(figure.compareTo("") == 0)
			figure = "-1";
		
		return new Integer(figure).intValue();
	}
	
	/**
	 * Renvoie true si il est possible d'ajouter le processus process à la liste listExist.
	 * Renvoie false si il y a un conflit (nom ou heure d'arrivée déjà existants).
	 */
	public static boolean notYetExist(Process newProcess, ArrayList<Process> listExist, boolean verifyName, boolean verifyArrival)
	{
		int arrivee = newProcess.getIncomingTime();
		String nom = newProcess.getName();
		for(Process process:listExist)
		{
			if(verifyName && process.getName().toLowerCase().compareTo(nom.toLowerCase()) == 0)
			{
				JOptionPane.showMessageDialog(null, "Impossible d'ajouter le processus.\nIl y a un conflit dans le nom", "Erreur", JOptionPane.OK_OPTION);
				return false;
			}
			else if(verifyArrival && process.getIncomingTime() == arrivee)
			{
				JOptionPane.showMessageDialog(null, "Impossible d'ajouter le processus.\nIl y a un conflit dans l'heure d'arrivée", "Erreur", JOptionPane.OK_OPTION);
				return false;
			}
		}
		
		return true;
	}
	
	public static double calculRatio(ArrayList<Process> listProcess, int panelWidth, int maxScale)
	{
		double wTest;
		double min = 10000;
		double ratio;
		int totalProcessLength = listProcess.get(listProcess.size()-1).getEndingTime();
		
		for(Process process:listProcess)
		{
			wTest = process.getDuration()/(double)totalProcessLength*panelWidth;
			if(wTest < min)
				min = wTest;
		}
		if(min < maxScale && min > 0)
			ratio = maxScale/min;
		else
			ratio = 0.96;
		
		return ratio;
	}
	


	private static final Stroke LARGE_LINE_STROKE = new BasicStroke(5);
}

