package ch.correvon.scheduler.plugin.stat;

import java.util.ArrayList;

import ch.correvon.scheduler.moo.process.Process;
import ch.correvon.scheduler.plugin.Statistic_I;

public class RotateTime implements Statistic_I
{
	public double calcul(ArrayList<Process> listProcess)
	{
		double result = 0;
		
		for(Process process:listProcess)
			result += process.getRotateTime();
		
		return result / listProcess.size();
	}
	
	public String getName()
	{
		return "Temps de rotation";
	}
}
