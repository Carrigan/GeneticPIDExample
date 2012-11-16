package org.ovencontrolexample;

public class Oven {
	// Booleans
	private boolean isOn;
	private boolean doorOpen;
	
	// mTemperatures
	private double mTemperature;
	private double mTemperatureAmbient;
	
	// Insulations
	private double mInsulationClosed;
	private double mInsulationOpen;
	
	// Heating capacity of the oven
	private double mHeatPerMinute;
	
	public Oven(double pAmbient, double pInOpen, double pInClosed, double pHeatPerMinute)
	{
		isOn = false;
		doorOpen = false;

		mHeatPerMinute = pHeatPerMinute;
		mInsulationClosed = pInClosed;
		mInsulationOpen = pInOpen;
		
		mTemperatureAmbient = pAmbient;
		mTemperature = mTemperatureAmbient;
	}
	
	public double updateTemperature(double step)
	{
		// Cool off
		double mTemperature_difference = this.mTemperature - this.mTemperatureAmbient;
		if(doorOpen)
		{
			mTemperature = mTemperature - mTemperature_difference * mInsulationOpen * step;
		} else {
			mTemperature = mTemperature - mTemperature_difference * mInsulationClosed * step;
		}
		
		// Heat up
		if(isOn)
		{
			mTemperature += mHeatPerMinute / 60.0 * step;
		}
		
		return mTemperature;
	}
	
	public void turnOn()
	{
		isOn = true;
	}
	
	public void turnOff()
	{
		isOn = false;
	}
	
	public void openDoor()
	{
		doorOpen = true;
	}
	
	public void closeDoor()
	{
		doorOpen = false;
	}
	
	public double getTemperature()
	{
		return this.mTemperature;
	}
	
	public boolean isOvenOn()
	{
		return this.isOn;
	}
	
	public boolean isDoorOpen()
	{
		return this.doorOpen;
	}
	
}
