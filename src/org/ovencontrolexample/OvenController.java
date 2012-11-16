package org.ovencontrolexample;

import java.text.DecimalFormat;

public class OvenController {

	// PID and controls
	private double Ki;
	private double Kp;
	private double Kd;
	private double mThresholdHeat;
	private double mThresholdOpen;
	
	// Oven being controlled
	private Oven mOven;
	
	// Verbosity
	private boolean mVerbose;
	
	// Stats
	double mTotalError;
	double mHeatUsed;
	
	// Weight per energy use unit when evaluating fitness
	private final int heatingCost = 0;
	
	public OvenController(double pKp, double pKi, double pKd, double pThresholdHeat, double pThresholdOpen, boolean pVerbose)
	{
		this.Ki = pKi;
		this.Kp = pKp;
		this.Kd = pKd;
		this.mThresholdHeat = pThresholdHeat;
		this.mThresholdOpen = pThresholdOpen;
		this.mVerbose = pVerbose;
		
		this.mOven = new Oven(68.0, 0.04, 0.004, 200.0);
	}
	
	// The simulation
	public void operateOverSequence(double step, double end, double objective[])
	{
		if(end/step + 1 != objective.length)
		{
			System.out.println("Target function is of improper length.");
			return;
		}
		
		// Initial PID things;
		double previousError = 0;
		double integralTotal = 0;
		double error = 0;
		double u = 0;
		
		// Stats
		mTotalError = 0;
		mHeatUsed = 0;
		
		if(mVerbose)
		{
			System.out.println("Time\tDesired\t\tTemperature\tOn?\tOpen?");
		}
		
		for(int i = 0; i < objective.length; i++)
		{
			// Update temperature
			double temp = mOven.updateTemperature(step);
			
			// PID Math
			error = objective[i] - mOven.getTemperature();
			integralTotal += Ki * error * step;
			u = error * Kp + integralTotal + Kd * (error - previousError)/step;
			previousError = error;
			mTotalError += step*Math.abs(error);
			if(mOven.isOvenOn())
			{
				mHeatUsed += 5;
			}
			
			// Controls
			if(u > mThresholdHeat)
			{
				mOven.turnOn();
			} else {
				mOven.turnOff();
			}
			
			if(u < mThresholdOpen)
			{
				mOven.openDoor();
			} else {
				mOven.closeDoor();
			}
			
			// Format the output
			if(mVerbose)
			{
				DecimalFormat standardFormat = new DecimalFormat("###.000");
				String temperatureString = standardFormat.format(temp);
				String desiredString = standardFormat.format(objective[i]);
				System.out.println(Double.toString(step*i) + '\t' + desiredString + "\t\t" + temperatureString + "\t\t" + Boolean.toString(mOven.isOvenOn()) + '\t' + Boolean.toString(mOven.isDoorOpen()));
			}
		}
		
		// End stuff
		if(mVerbose)
		{
			System.out.println(Double.toString(mTotalError));
		}
	}
	
	public double getTotalError()
	{
		return this.mTotalError;
	}
	
	public double getWeightedScore()
	{
		return this.mTotalError + heatingCost*this.mHeatUsed;
	}
	
	public double getKp()
	{
		return this.Kp;
	}
	
	public double getKi()
	{
		return this.Ki;
	}
	public double getKd()
	{
		return this.Kd;
	}
	
	public double getThresholdOpen()
	{
		return this.mThresholdOpen;
	}
	
	public double getThresholdHeat()
	{
		return this.mThresholdHeat;
	}
	
	
	public void setKp(double pKp)
	{
		this.Kp = pKp;
	}
	
	public void setKi(double pKi)
	{
		this.Ki = pKi;
	}
	public void setKd(double pKd)
	{
		this.Kd = pKd;
	}
	
	public void setThresholdOpen(double pThreshOpen)
	{
		this.mThresholdOpen = pThreshOpen;
	}
	
	public void setThresholdHeat(double pThreshHeat)
	{
		this.mThresholdHeat = pThreshHeat;
	}	
	
}
