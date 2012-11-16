package org.ovencontrolexample;

import java.util.ArrayList;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.examples.EvolutionLogger;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

public class OvenPID_TestBench {

	// Set this true for the genetic algorithm, false for brute force.
	private static final boolean geneticAlgorithm = true;
	
	// Spit out the health of every generation?
	private static final boolean verbosity = false;
	
	// The desired heat profile
	private static double desiredProfile[];
	
    public static void main(String[] args)
    {
    	desiredProfile = new double[121];
    	OvenController fittestController;
    	long startTime, endTime;
    	
    	// Create the desired heat profile
    	for(int i = 0; i < 13; i++)
    	{
    		desiredProfile[i] = 100;
    	}
    	
    	for(int i = 13; i < 37; i++)
    	{
    		desiredProfile[i] = -50.0 + 25/2.0*i;
    	}
    	
    	for(int i = 37; i < 61; i++)
    	{
    		desiredProfile[i] = 400;
    	}
    	
    	for(int i = 61; i < 85; i++)
    	{
    		desiredProfile[i] = 1225 - i*330/24.0;
    	}
    	
    	for(int i = 85; i <= 120; i++)
    	{
    		desiredProfile[i] = 70;
    	}
    	
    	startTime = System.currentTimeMillis();
    	
    	if(geneticAlgorithm)
    	{
    		// Genetic Algorithm
    		// Build a list of possible mutations with the following probabilities of occuring
	    	ArrayList<EvolutionaryOperator<OvenController>> mutations = new ArrayList<EvolutionaryOperator<OvenController>>();
	    	mutations.add(new OvenControllerPIDMutation(new Probability(.03d), .2));
	    	mutations.add(new OvenControllerPIDCrossover(new Probability(.2d)));
	    	EvolutionaryOperator<OvenController> pipeline = new EvolutionPipeline<OvenController>(mutations);

	    	// Set the fitness function
	    	OvenControllerFitnessFunction fit = new OvenControllerFitnessFunction(desiredProfile);
	    	
	    	// Create the engine
	        GenerationalEvolutionEngine<OvenController> engine = new GenerationalEvolutionEngine<OvenController>(new OvenControllerFactory(),
	                pipeline,
	                fit,
	                new RouletteWheelSelection(),
	                new MersenneTwisterRNG());
	    	
	        engine.setSingleThreaded(true);
	        
	        // If verbose, display the health of each generation
	        if(verbosity)
	        {
	        	engine.addEvolutionObserver(new EvolutionLogger<OvenController>());
	        }
	        
	        // Go!
	        fittestController = engine.evolve(100, // individuals per generation
	                      						2, // Elites per generation
	                      						new GenerationCount(5000)); // Continue for 5000 generations
    	} else {
    		// dummy initialization
    		fittestController = new OvenController(0, 0, 0, 0, 0, false);
    		fittestController.operateOverSequence(5.0, 600.0, desiredProfile);
    		
    		// Brute Force
    		for(int i = 0; i < 40; i++)
    		{
    			for(int j = 0; j < 40; j++)
    			{
    				for(int k = 0; k < 40; k++)
    				{
    					for(int l = 0; l < 40; l++)
    					{
    						OvenController tempController = new OvenController(i*.025, j*.025, k*.025, 1, l*(-.1), false);
    						tempController.operateOverSequence(5.0, 600.0, desiredProfile);
    						if(tempController.getWeightedScore() < fittestController.getWeightedScore())
    						{
    							fittestController = tempController;
    						}
    					}
    				}
    			}
    		}	
    	}
    	
    	endTime = System.currentTimeMillis();
    	
    	// Print our results
        System.out.println("\n----------------- Results ----------------- ");
        System.out.println("Score:\t\t\t" + fittestController.getWeightedScore());
        System.out.println("Kp:\t\t\t" + fittestController.getKp());
        System.out.println("Ki:\t\t\t" + fittestController.getKi());
        System.out.println("Kd:\t\t\t" + fittestController.getKd());
        System.out.println("Oven ON Threshold:\t" + fittestController.getThresholdHeat());
        System.out.println("Vent OPEN Threshold:\t" + fittestController.getThresholdOpen());
        System.out.println("Time taken (ms):\t" + (endTime-startTime));
        System.out.println("------------------------------------------- ");
    }	
}
