package org.ovencontrolexample;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class OvenControllerFitnessFunction implements FitnessEvaluator<OvenController>{

	private double profile[];
	
	public OvenControllerFitnessFunction(double pProfile[])
	{
		profile = pProfile;
	}
	
	@Override
	public double getFitness(OvenController candidate,
			List<? extends OvenController> population) {

		candidate.operateOverSequence(5.0,  600.0, profile);		
		return candidate.getWeightedScore();
	}

	@Override
	public boolean isNatural() {
		// TODO Auto-generated method stub
		return false;
	}

}
