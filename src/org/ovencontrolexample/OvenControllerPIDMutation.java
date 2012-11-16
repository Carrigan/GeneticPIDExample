package org.ovencontrolexample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class OvenControllerPIDMutation implements EvolutionaryOperator<OvenController>{

	private Probability mutationProbability;
	private double maxMutation;
	
	public OvenControllerPIDMutation(Probability pProbability, double pMutation)
	{
		maxMutation = pMutation;
		this.mutationProbability = pProbability;
	}
	
	@Override
	public List<OvenController> apply(List<OvenController> list, Random rnd) {
		List<OvenController> newList = new ArrayList<OvenController>(); 
		
		for(OvenController OC : list)
		{
			OvenController newController = mutateController(OC, rnd);
			newList.add(newController);
		}
		
		return newList;
	}
	
	public OvenController mutateController(OvenController selectedMutant, Random rando)
	{
		double Kp = selectedMutant.getKp();
		double Ki = selectedMutant.getKi();
		double Kd = selectedMutant.getKd();
		double ThreshHeat = selectedMutant.getThresholdHeat();
		double ThreshOpen = selectedMutant.getThresholdOpen();
		
		if(mutationProbability.nextEvent(rando))
		{
			Kp += rando.nextDouble() * maxMutation - maxMutation/2;
			if(Kp > 1.0)
			{
				Kp = 1;
			}
			
			if(Kp < 0)
			{
				Kp = 0;
			}
		}
		
		if(mutationProbability.nextEvent(rando))
		{
			Ki += rando.nextDouble() * maxMutation - maxMutation/2;
			if(Ki > 1.0)
			{
				Ki = 1;
			}
			
			if(Ki < 0)
			{
				Ki = 0;
			}
		}
		
		if(mutationProbability.nextEvent(rando))
		{
			Kd += rando.nextDouble() * maxMutation - maxMutation/2;
			if(Kd > 1.0)
			{
				Kd = 1;
			}
			
			if(Kd < 0)
			{
				Kd = 0;
			}
		}
		
		if(mutationProbability.nextEvent(rando))
		{
			ThreshOpen += rando.nextDouble() * maxMutation - maxMutation/2;
			if(ThreshOpen < -10.0)
			{
				ThreshOpen = -10;
			}
			
			if(ThreshOpen > 0)
			{
				ThreshOpen = 0;
			}
		}
		
	
		if(mutationProbability.nextEvent(rando))
		{
			Kp = rando.nextDouble();
			Ki = rando.nextDouble();
			Kd = rando.nextDouble();
			ThreshOpen = rando.nextDouble() * -10;
			ThreshHeat = 1;
		}
		
		return new OvenController(Kp, Ki, Kd, ThreshHeat, ThreshOpen, false);
	}
	
}
