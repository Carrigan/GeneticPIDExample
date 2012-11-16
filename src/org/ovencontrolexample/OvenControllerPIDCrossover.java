package org.ovencontrolexample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

public class OvenControllerPIDCrossover extends AbstractCrossover<OvenController>{

	public OvenControllerPIDCrossover(Probability chance)
	{
		super(3, chance);
	}
	

	@Override
	protected List<OvenController> mate(OvenController parent1,
			OvenController parent2, int crossoverPoints, Random random) {
		
		double randomDouble = random.nextDouble();
		ArrayList<OvenController> children = new ArrayList<OvenController>();
		
		// Swap at first point
		if(randomDouble < .333)
		{
			children.add(new OvenController(parent1.getKd(), parent2.getKi(), parent2.getKp(), 1, parent2.getThresholdOpen() , false));
			children.add(new OvenController(parent2.getKd(), parent1.getKi(), parent1.getKp(), 1, parent1.getThresholdOpen() , false));
			return children;
		}
		
		// Swap at second point
		if(randomDouble < .666)
		{
			children.add(new OvenController(parent1.getKd(), parent1.getKi(), parent2.getKp(), 1, parent2.getThresholdOpen() , false));
			children.add(new OvenController(parent2.getKd(), parent2.getKi(), parent1.getKp(), 1, parent1.getThresholdOpen() , false));
			return children;
		}
		
		// Swap at third point
		children.add(new OvenController(parent1.getKd(), parent1.getKi(), parent1.getKp(), 1, parent2.getThresholdOpen() , false));
		children.add(new OvenController(parent2.getKd(), parent2.getKi(), parent2.getKp(), 1, parent1.getThresholdOpen() , false));
		
		return children;
	}

}
