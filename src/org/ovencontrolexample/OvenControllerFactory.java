package org.ovencontrolexample;

import java.util.Random;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class OvenControllerFactory extends AbstractCandidateFactory<OvenController>{

	@Override
	public OvenController generateRandomCandidate(Random rnd) {
		double mKp = rnd.nextDouble();
		double mKi = rnd.nextDouble();
		double mKd = rnd.nextDouble();
		double mThreshOpen = rnd.nextDouble() * -10;
		double mThreshHeat = 1;
				
		return new OvenController(mKp, mKi, mKd, mThreshHeat, mThreshOpen, false);
	}
}
