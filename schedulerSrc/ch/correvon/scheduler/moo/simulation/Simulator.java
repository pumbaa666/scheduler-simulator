package ch.correvon.scheduler.moo.simulation;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import ch.correvon.scheduler.algorithms.AlgoTools;
import ch.correvon.scheduler.moo.Tools;
import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.moo.process.ProcessType;
import ch.correvon.scheduler.moo.process.SortedProcess;
import ch.correvon.scheduler.moo.process.UsedType;
import ch.correvon.scheduler.plugin.Algo_I;


public class Simulator
{
	/*-----------------------------------------------------------------*\
	|*							Constructeurs						   *|
	\*-----------------------------------------------------------------*/
	public Simulator(Algo_I algo, ArrayList<Process> listProcess, int quantum, int nbCPU, int totalPriorityTime)
	{
		for(Process process:listProcess)
			process.getType().setAlgo(algo);
		this.containPreemptifAlgo = algo.isPreemptif();
			
		this.init(Tools.deepClone(listProcess), quantum, nbCPU, totalPriorityTime);
	}
	
	public Simulator(ArrayList<ProcessType> listType, ArrayList<Process> listProcess, int quantum, int nbCPU, int totalPriorityTime)
	{
		Algo_I algo;
		ArrayList<Process> newListProcess = Tools.deepClone(listProcess);
		this.containPreemptifAlgo = false;
		for(Process process:newListProcess)
		{
			algo = listType.get(Tools.searchType(process.getType(), listType)).getAlgo();
			process.getType().setAlgo(algo);
			if(algo.isPreemptif())
				this.containPreemptifAlgo = true;
		}
		
		this.init(newListProcess, quantum, nbCPU, totalPriorityTime);
	}
	
	/*-----------------------------------------------------------------*\
	|*							Methodes publiques					   *|
	\*-----------------------------------------------------------------*/
	private void init(ArrayList<Process> listProcess, int quantum, int nbCPU, int totalPriorityTime)
	{
		CPU.resetID();
		this.listProcess = listProcess;
		this.listSize = this.listProcess.size();
		
		this.quantum = quantum;
		this.nbCPU = nbCPU;
		
		this.queue = new ArrayList<Process>(listSize);
		this.time = 0;
		this.history = new ArrayList<String>(listSize*20);
		
		this.tabCPU = new CPU[nbCPU];
		for(int i = 0; i < this.nbCPU; i++)
			this.tabCPU[i] = new CPU();

		// Recherche de tout les type existant
		this.listType = new Hashtable<String, UsedType>();
		for(Process process:listProcess)
			this.addTypeIfNotExist(process.getType());
		
		Tools.normalizePriority(this.listType, totalPriorityTime);
		
		this.elements = this.listType.elements();
		this.typeInProgress = this.elements.nextElement().getType();
		
		AlgoTools.sortListByIncomingTime(this.listProcess);
	}
	
	public SortedProcess run()
	{
		this.result = new ArrayList<Process>(this.listProcess.size());
		Process processChoosen;
		boolean findAnotherProcess;
		Process processAdded;
		
		while(this.queue.size() != 0 || !this.listProcess.isEmpty() || !this.areEveryCPUFree()) // Boucle principale. Tourne jusqu'à que tout les processus aient été exécuté.
		{
			while(this.listProcess.size() > 0 && this.time == this.listProcess.get(0).getIncomingTime()) // Recherche de tout les processus arrivé à ce temps
			{
				processAdded = this.listProcess.remove(0); // Retire le processus de la liste initiale.
				this.queue.add(processAdded); // l'ajoute à la file d'attente.
				this.addToHistory(processAdded.getNameTypeTime()+" arrive en file d'attente au temps "+this.time, true);
				this.addToHistory(AlgoTools.queueState(this.queue), false);
			}
			
			this.increaseWaitingTime();
			
			if(this.isOneCPUFree())
			{
				this.chooseType();
				
				findAnotherProcess = true;
				while(findAnotherProcess)
				{
					processChoosen = this.choseProcess();
					if(processChoosen != null)
					{
						int numCPU = this.findFreeCPU(processChoosen.getAffinity());
	
						if(numCPU != -1)
						{
							this.tabCPU[numCPU].affectProcess(processChoosen);
							this.addToHistory(processChoosen.getNameTypeTime()+" est affecté au CPU "+numCPU+" au temps "+this.time, true);
						}
						else
						{
							this.queue.add(processChoosen);
							findAnotherProcess = false;
						}
					}
					else
						findAnotherProcess = false;
				}
			}
			
			this.runEveryCPU();
			
			this.time++;
		}
		
		this.result = this.fusionFragProcess(this.result);
		AlgoTools.sortListByRealIncomingTime(this.result);
		SortedProcess sortedProcess = new SortedProcess(this.result, this.history);
		return sortedProcess;
	}
	
	/*-----------------------------------------------------------------*\
	|*							Methodes privées					   *|
	\*-----------------------------------------------------------------*/
	/*-------------------------------------------------*\
	|*						CPU						   *|
	\*-------------------------------------------------*/
	private void runEveryCPU() 
	{
		this.verifyAffinity();
		
		Process clone;
		for(int i = 0; i < this.nbCPU; i++)
		{
			if(this.tabCPU[i].isWorking())
			{
				if(this.tabCPU[i].getRealIncomingTime() == -1)
				{
					this.tabCPU[i].setRealIncomingTime(this.time);
					this.addToHistory(this.tabCPU[i].getProcess().getNameTypeTime()+" est traité la 1ère fois au temps "+this.time , true);
				}
				this.listType.get(this.tabCPU[i].getProcessType().toString()).increaseUsedTime(); // incrémente le temps d'utilisatin du type
				if(!this.tabCPU[i].decrement())
				{
					this.addToHistory(this.tabCPU[i].getProcessName()+" est terminé au temps "+this.time, true);
					clone = this.tabCPU[i].removeProcess();
					clone.setEndingTime(this.time);
					this.result.add(clone);
				}

				if(this.tabCPU[i].getProcessType() != null)
					this.stopRunningProcess(this.tabCPU[i]);
			}
		}
	}
	
	/**
	 * Une fois que les processeur on été rempli par des processus, on vérifie qu'il n'est pas possible d'améliorer le déroulement du simulateur en attribuant réellement chaque processus à son processeur préféré (en effet, lors de l'attribution du 1er processus du "tour", il choisit sa place, mais si il n'a pas d'affinité, il prend le 1er libre, qui est peut-être le processeur préféré d'un autre processus.
	 * Note : lorsque 2 processus se battent pour 1 processeur, c'est le 1er processus qui gagne, même si il aurait été plus logique de l'attribuer à un processus qui est en train de tourner.
	 */
	private void verifyAffinity()
	{
		ArrayList<Process> listSwap = new ArrayList<Process>(this.tabCPU.length);
		Process processToTest;
		
		// Recherche des processus en cours d'exécution
		for(int i = 0; i < this.tabCPU.length; i++)
		{
			processToTest = this.tabCPU[i].getProcess();
			if(processToTest != null && this.tabCPU[i].getProcessType().getAlgo().isPreemptif() && processToTest.waitSince() < 2) // un processus est suseptible d'être affecté à son processeur préféré si : il existe, il est traité par un algorithm préemptif, il est justement en cours d'exécution
				listSwap.add(processToTest);
		}
		
		if(listSwap.size() > 1) // Si il n'y en a qu'un, il n'y a rien à faire, car il est automatiquement affecté à son processeur favoris. Si il n'y en a aucun, il n'y a rien à faire non plus,
		{
			int i = 0;
			int affinity;
			Process processI;
			Process processAffinity;

			for(Process process:listSwap)
			{
				affinity = process.getAffinity();
				if(affinity != -1 && affinity != i) // Si le processus n'a pas d'affinité, ou si il est déjà sur son processeur favori, il n'y a rien besoin de faire
				{
					processI = this.tabCPU[i].removeProcess();
					processAffinity = this.tabCPU[affinity].removeProcess();
					if(processI == null || processAffinity == null)
					{
						this.tabCPU[i].clear();
						if(processI == null)
							this.tabCPU[affinity].affectProcess(processAffinity);
						else
							this.tabCPU[affinity].affectProcess(processI);
					}
					else
					{
						this.tabCPU[i].affectProcess(processAffinity);
						this.tabCPU[affinity].affectProcess(processI);
					}
					//break;
				}
				i++;
			}
		}
	}
	
	private int findFreeCPU(int affinity)
	{
		// Commence par vérifier si le processeur préféré du processus est libre
		if(affinity != -1)
			if(!this.tabCPU[affinity].isWorking())
				return affinity;
		
		// Sinon, cherche le 1er processeur libre
		for(int i = 0; i < this.nbCPU; i++)
			if(!this.tabCPU[i].isWorking())
				return i;
		
		// Si aucun processeur n'est libre
		return -1;
	}
	
	private boolean areEveryCPUFree()
	{
		for(int i = 0; i < this.nbCPU; i++)
			if(this.tabCPU[i].isWorking())
				return false;
		
		return true;
	}
	
	private boolean isOneCPUFree()
	{
		for(int i = 0; i < this.nbCPU; i++)
			if(!this.tabCPU[i].isWorking())
				return true;
		
		return false;
	}

	/*-------------------------------------------------*\
	|*			Choix du processus à éxecuter		   *|
	\*-------------------------------------------------*/
	private Process choseProcess()
	{
		int testEveryType = 0;
		int size = this.listType.size();
		ArrayList<Process> listProcessOfType = null;
		ProcessType typeInProgress = this.typeInProgress; // Sauvegarde le type en cours, car si aucun processus de ce type n'est en file d'attente, il faudra aller chercher dans les autres types, et donc remettre le bon type en cours à la fin du choix
		
		while(testEveryType < size) // Cherchons chaque type
		{
			listProcessOfType = this.subListOfTypeInProgress();
			if(listProcessOfType.isEmpty()) // Si aucun processus n'a été trouvé...
				this.nextType(false); //... on change de type
			else // Si au moins un processus du bon type a été trouvé...
				testEveryType = size; //... on fait en sorte de sortir de la boucle
			testEveryType++;
		}
		
		Process processChoosen = null;
		if(!listProcessOfType.isEmpty()) // Si on a trouvé au moins un processus à élir
		{
			processChoosen = listProcessOfType.get(0).getType().getAlgo().runAlgorithm(listProcessOfType, this.quantum); // on peut lancer l'algo !
			this.queue.remove(processChoosen); // et retirer le processus élu de la file d'attente
		}

		while(!typeInProgress.isEquals(this.typeInProgress.toString())) // Remettons le bon type
			this.nextType(false);
		
		if(processChoosen != null)
		{
			this.addToHistory(processChoosen.getNameTypeTime()+" a été choisi au temps "+this.time, true);
			this.addToHistory(AlgoTools.queueState(this.queue), false);
		}
		
		return processChoosen;
	}
	
	/**
	 * Rempli une liste avec tout les processus en file d'attente qui sont du type en cours 
	 */
	private ArrayList<Process> subListOfTypeInProgress()
	{
		ArrayList<Process> list = new ArrayList<Process>(this.queue.size());
		
		for(Process process:this.queue)
			if(process.getType().isEquals(this.typeInProgress.toString()))
				list.add(process);
		
		return list;
	}
	
	private void increaseWaitingTime()
	{
		for(Process process:this.queue)
			process.increaseWaitSince();
	}
	
	/*-------------------------------------------------*\
	|*					Choix du type				   *|
	\*-------------------------------------------------*/
	private void chooseType()
	{
		UsedType usedType = this.listType.get(this.typeInProgress.toString()); 
		if(usedType.getUsedTime() >= this.typeInProgress.getPriority())
		{
			ProcessType typeToClear = new ProcessType(this.typeInProgress);
			this.listType.get(this.typeInProgress.toString()).clearUsedTime();
			this.nextType(true);
			
			this.clearAllCPU(typeToClear);
		}
	}
	
	private void nextType(boolean notifyHistory)
	{
		String string = "Changement de type de '"+this.typeInProgress;
		if(this.elements.hasMoreElements())
			this.typeInProgress = this.elements.nextElement().getType();
		else
		{
			this.elements = this.listType.elements();
			this.typeInProgress = this.elements.nextElement().getType();
		}
		string = string.concat("' à '"+this.typeInProgress+"' au temps "+this.time);
		if(notifyHistory)
			this.addToHistory(string, true);
	}
	
	/**
	 * Enlève proprement les processus du type typeToClear des processeurs 
	 */
	private void clearAllCPU(ProcessType typeToClear)
	{
		for(int i = 0; i < this.nbCPU; i++)
			if(this.tabCPU[i].isWorkingSinceOneTime())
				if(this.tabCPU[i].getProcessType().isEquals(typeToClear.toString()))
				{
					if(!this.stopRunningProcess(this.tabCPU[i]))
						this.addToHistory(this.tabCPU[i].getProcess().getNameTypeTime()+" n'a pas été interrompu au temps "+this.time+" car il n'est pas traîté par un algorithme pré-emptif, malgré que le type en cours ai changé.", false);
				}
	}
	
	/*-------------------------------------------------*\
	|*						Divers					   *|
	\*-------------------------------------------------*/
	/**
	 * Quand un processus est retiré d'un processeur, il faut le cinder en 2 parties : 
	 * 1) la faite, que l'on ajoute au résultat this.listResult
	 * 2) le reste que l'on remet dans la file d'attente this.queue
	 */
	private boolean stopRunningProcess(CPU cpu)
	{
		if(cpu.getProcess().isAlgoInChargePreemptif())
		{
			Process rest;
			Process done;
			
			rest = cpu.removeProcess();
			done = new Process(rest.getName(), rest.getIncomingTime(), this.time - rest.getRealIncomingTime() + 1, rest.getRealIncomingTime(), rest.getFirstExecution(), this.time, rest.getType());
				
			done.setCPUInCharge(rest.getCPUInCharge());
			
			done.setEndingTime(this.time);
			this.result.add(done);
			
			rest.setRealncomingTime(-1);
			rest.setDuration(rest.getDuration() - done.getDuration());
			rest.setIncomingTime(this.time+1);
			this.queue.add(rest);
			
			this.addToHistory(done.getNameTypeTime()+" à été interrompu au temps "+this.time, false);
			return true;
		}
		return false;
	}
	
	/**
	 * Crée une nouvelle key dans this.listType si elle n'existe pas encore
	 */
	private void addTypeIfNotExist(ProcessType newType)
	{
		boolean alreadyExist = false;
		Enumeration<UsedType> elements = this.listType.elements(); 
		while(elements.hasMoreElements())
		{
			UsedType elem = elements.nextElement();
			if(elem.isEquals(newType.toString()))
			{
				alreadyExist = true;
				break;
			}
		}
		if(!alreadyExist)
			this.listType.put(newType.toString(), new UsedType(new ProcessType(newType)));
	}
	
	/**
	 * Fusionne les fragments de processus résultant d'algorithme préemptif. En effet, un processus s'étalant sur plusieurs unité de temps est fragmenté en plusieurs processus d'une seule unité de temps 
	 * (Note : pourrait être parallèlisé)
	 */
	private ArrayList<Process> fusionFragProcess(ArrayList<Process> listSource)
	{
		Process last;
		Process running;
		Process fusion;
		Process solo;
		ArrayList<Process> listResult = new ArrayList<Process>(listSource.size());
		int count;
		int listSize = listSource.size();
		boolean emptyList;
		Hashtable<String, Integer> nameResponseTime = new Hashtable<String, Integer>(listSource.size()/Math.max(5, this.quantum));
		
		for(int cpuID = 0; cpuID < this.nbCPU; cpuID++)
		{
			count = 0;
			emptyList = false;
			
			// Cherche le 1er processus du bon CPU in charge
			do
			{
				last = listSource.get(count);
				count++;
				if(count >= listSize)
				{
					emptyList = true;
					break;
				}
			}
			while(last.getCPUInCharge() != cpuID);
			if(emptyList)
				continue;
			solo = last;
			
			
			for(int i = count; i < listSize; i++)
			{
				running = listSource.get(i);
				if(running.getCPUInCharge() == cpuID && last.getCPUInCharge() == cpuID)
				{
					if(solo != null && solo.getName().compareTo(running.getName()) != 0)
					{
						solo.setCPUInCharge(cpuID);
						if(solo.getResponseTime() != -1)
							nameResponseTime.put(solo.getName(), solo.getResponseTime());
						
						listResult.add(new Process(solo));
						solo = null;
					}
						
					fusion = null;
					if(running.getName().compareTo(last.getName()) == 0)
					{
						fusion = new Process(last);
						fusion.setDuration(last.getDuration()+running.getDuration());
						fusion.setEndingTime(running.getEndingTime());

						solo = fusion;
						last = fusion;
					}
					else
					{
						solo = new Process(running);
						last = solo;
					}
				}
			}
			if(solo != null)
			{
				solo.setCPUInCharge(cpuID);
				listResult.add(new Process(solo));
				if(solo.getResponseTime() != -1)
					nameResponseTime.put(solo.getName(), solo.getResponseTime());
			}
		}
		
		// Affectation des temps de réponses corrects
		String name;
		
		ArrayList<Process> lastProcess = new ArrayList<Process>(listResult.size() / this.quantum);
		int index;
		for(Process process:listResult)
		{
			name = process.getName();
			process.setResponseTime(nameResponseTime.get(name).intValue());
			
			index = Tools.searchProcess(process.getName(), lastProcess);
			if(index == -1)
				lastProcess.add(process);
			else
				lastProcess.set(index, process);
			
		}

		// Vérifie que chaque dernier processus est bien marqué comme dernier (isLast == true)
		for(Process process:lastProcess)
			process.setLast(true);
		
		return listResult;
	}
	
	/**
	 * Ajoute du texte à l'historique.
	 * Si force vaut true, il est de toute façon ajouté. Sinon, il n'est ajouté que si le nombre de processus passé au simulateur est inférieur à 100 (sinon il y a risque de Stack Overflow), ou si l'algorithme n'est pas préemptif.
	 */
	private void addToHistory(String text, boolean force)
	{
		if(force || !this.containPreemptifAlgo || this.listSize < 100)
			this.history.add(text);
	}

	/*-----------------------------------------------------------------*\
	|*							Attributs privés					   *|
	\*-----------------------------------------------------------------*/
	private ArrayList<Process> listProcess;
	private int listSize;
	private boolean containPreemptifAlgo;
	private int quantum;

	private ArrayList<Process> result;
	private ArrayList<String> history;
	
	private int time;
	private ArrayList<Process> queue;
	
	private Hashtable<String, UsedType> listType;
	private ProcessType typeInProgress;
	private Enumeration<UsedType> elements;
	
	private CPU[] tabCPU;
	private int nbCPU;
}