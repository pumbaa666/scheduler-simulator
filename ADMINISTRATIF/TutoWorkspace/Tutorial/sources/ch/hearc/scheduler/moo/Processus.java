
//Specification:
package ch.hearc.scheduler.moo;

public class Processus
{
	/*----------------------------------------------------------------*\
	|*							Constructeurs						  *|
	\*----------------------------------------------------------------*/
	public Processus(String nom, int heureArrivee, int duree, int heureArriveeReelle, int premiereExec, int heureFinReelle)
	{
		this.nom = nom;
		this.heureArrivee = heureArrivee;
		this.heureArriveeReelle = heureArriveeReelle;
		this.duree = duree;
		this.tempsRestant = duree;
		this.premiereExec = premiereExec;
		this.heureFinReelle = heureFinReelle;
		this.last = false;
	}
	
	public Processus(String nom, int heureArrivee, int duree)
	{
		this(nom, heureArrivee, duree, -1, -1, 0); // TODO 0 au lieu de -1 à premiereExec ??
	}
	
	public Processus()
	{
		this("P0", 0, 1);
	}
	
	public Processus(Processus process)
	{
		this(process.getNom(), process.getHeureArrivee(), process.getDuree(), process.getHeureArriveeReelle(), process.getPremiereExec(), process.getHeureFinReelle());
	}
	
	/*----------------------------------------------------------------*\
	|*						Methodes publiques						  *|
	\*----------------------------------------------------------------*/
	public String toStringEspace()	
	{
		String str = new String(this.nom);
		for(int i = str.length(); i%tailleTabulation!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.heureArrivee);
		for(int i = str.length(); i%tailleTabulation!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.duree);
		return str;
	}
	
	public String toStringTab()	
	{
		return this.nom+caractereSeparationFichier+this.heureArrivee+caractereSeparationFichier+this.duree;
	}
	
	public String toString2Espace()	
	{
		String str = new String(this.nom);
		for(int i = str.length(); i%tailleTabulation!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.heureArriveeReelle);
		for(int i = str.length(); i%tailleTabulation!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.heureFinReelle);
		for(int i = str.length(); i%tailleTabulation!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.tempsAttente);
		for(int i = str.length(); i%tailleTabulation!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.tempsReponse);
		for(int i = str.length(); i%tailleTabulation!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.tempsRotation);
		return str;
	}
	
	public String toStringTiret()	
	{
		String str = new String(this.nom);
		for(int i = str.length(); i%tailleTabulation!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.heureArriveeReelle);
		for(int i = str.length(); i%tailleTabulation!=0; i++)
			str = str.concat(" ");
		str = str.concat(""+this.heureFinReelle);
		for(int i = str.length(); i%tailleTabulation!=0; i++)
			str = str.concat(" ");
		if(this.last)
		{
			str = str.concat(""+this.tempsAttente);
			for(int i = str.length(); i%tailleTabulation!=0; i++)
				str = str.concat(" ");
			str = str.concat(""+this.tempsReponse);
			for(int i = str.length(); i%tailleTabulation!=0; i++)
				str = str.concat(" ");
			str = str.concat(""+this.tempsRotation);
		}
		else
		{
			for(int j = 0; j < 3; j++)
			{
				for(int i = str.length(); i%tailleTabulation!=0; i++)
					str = str.concat(" ");
				str = str.concat("-");
			}
		}
		return str;
	}
	
	public String toString2Tab()
	{
		return this.nom+caractereSeparationFichier+this.heureArriveeReelle+caractereSeparationFichier+this.heureFinReelle+caractereSeparationFichier+this.tempsAttente+caractereSeparationFichier+this.tempsReponse+caractereSeparationFichier+this.tempsRotation;
	}
	
	/*--------------------------------*\
	|*				Set				  *|
	\*--------------------------------*/
	public void setNom(String nom)
	{
		this.nom = nom;
	}
	
	public void setHeureArrivee(int heureArrivee)
	{
		this.heureArrivee = heureArrivee;
	}
	
	public void setDuree(int duree)
	{
		this.duree = duree;
	}
	
	public void setHeureFin(int heureFin)
	{
		setHeureFin(heureFin, 0);
	}

	public void setHeureFin(int heureFin, int decalage)
	{
		this.heureFinReelle = heureFin + decalage;
		this.tempsAttente = this.heureArriveeReelle-this.heureArrivee;
		this.tempsRotation = this.heureFinReelle-this.premiereExec + 1;
		this.tempsReponse = this.premiereExec-this.heureArrivee;
		this.duree = this.heureFinReelle - this.heureArriveeReelle + 1;
	}
	
	public void setHeureArriveeReelle(int heureArrivee)
	{
		this.heureArriveeReelle = heureArrivee;
		if(this.premiereExec == -1)
			this.premiereExec = heureArrivee;
	}
	
	public boolean decrementer()
	{
		this.tempsRestant--;
		if(this.tempsRestant == 0)
			return false;
		else
			return true;
	}
	
	public void setLast(boolean value)
	{
		this.last = value;
	}
	
	/*--------------------------------*\
	|*				Get				  *|
	\*--------------------------------*/
	public String getNom()
	{
		return nom;
	}
	
	public int getHeureArrivee()
	{
		return this.heureArrivee;
	}
	
	public int getDuree()
	{
		return this.duree;
	}
	
	public int getHeureFinReelle()
	{
		return this.heureFinReelle;
	}
	
	public int getTempsRestant()
	{
		return this.tempsRestant;
	}
	
	public int getTempsAttente()
	{
		return this.tempsAttente;
	}
	
	public int getHeureArriveeReelle()
	{
		return this.heureArriveeReelle;
	}
	
	public int getTempsRotation()
	{
		return this.tempsRotation;
	}
	
	public int getTempsReponse()
	{
		return this.tempsReponse;
	}
	
	public int getPremiereExec()
	{
		return this.premiereExec;
	}
	
	/*--------------------------------*\
	|*				Is				  *|
	\*--------------------------------*/
	public boolean isEqual(Processus p)
	{
		boolean value = false;
		
		if(this.nom.compareTo(p.getNom()) == 0)
			value = true;
		
		if(this.heureArrivee == p.getHeureArrivee())
			value = true;
		else
			value = false;
		
		if(this.duree == p.getDuree())
			value = true;
		else
			value = false;
		
		return value;
	}
	
	public boolean isLast()
	{
		return this.last;
	}
	
	/*----------------------------------------*\
	|*				Statiques				  *|
	\*----------------------------------------*/
	public static Processus convert(String process)
	{
		String[] str = process.split(caractereSeparationFichier);
		
		if(str.length != 3)
		{
			//System.out.println("Processus.convert : Pas le bon nombre d'argument (3). Donné : "+str.length);
			return null;
		}
		
		String nom = str[0];
		int heure = 0;
		int duree = 0;
		
		if(nom.length() == 0 || nom.length() > 4)
		{
			//System.out.println("Processus.convert : Le nom n'est pas conforme (1-4 car). Donné : "+nom.length());
			return null;
		}
		
		try
		{
			heure = Integer.parseInt(str[1]);
		}
		catch (NumberFormatException e)
		{
			//System.out.println("Processus.convert : Impossible de lire l'heure d'arrivée. Donné : "+str[1]);
			return null;
		}
		
		try
		{
			duree = Integer.parseInt(str[2]);
		}
		catch (NumberFormatException e)
		{
			//System.out.println("Processus.convert : Impossible de lire la durée. Donné : "+str[2]);
			return null;
		}

		return new Processus(nom, heure, duree);
	}
	
	/*----------------------------------------------------------------*\
	|*						Attributs Privés						  *|
	\*----------------------------------------------------------------*/
	private String nom;
	private int heureArrivee;
	private int duree;
	
	private int heureArriveeReelle;
	private int heureFinReelle;
	private int tempsAttente; // Temps total passé à attendre
	private int tempsRotation; // Fin réelle - Arrivée réelle
	private int tempsReponse; // temps entre Arrivée et 1ère execution
	private int premiereExec;
	private int tempsRestant;
	
	private boolean last;
	
	/*-----------------------------------------*\
	|*				Statiques				   *|
	\*-----------------------------------------*/
	private static int tailleTabulation = 5;
	private static final String caractereSeparationFichier = ", ";
}