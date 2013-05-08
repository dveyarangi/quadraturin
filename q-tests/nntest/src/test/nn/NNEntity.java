package test.nn;

import yar.quadraturin.objects.Entity;
import yarangi.ai.nn.init.InitializerFactory;
import yarangi.ai.nn.init.RandomWeightsInitializer;
import yarangi.ai.nn.init.WeightsInitializer;
import yarangi.ai.nn.numeric.BackpropNetwork;
import yarangi.ai.nn.numeric.TanHAF;

public class NNEntity extends Entity
{

	private int trainingSetsize = 1000;
	private double [] inputSet, outputSet;
	private double [] calculatedSet;
	
	static {
		InitializerFactory.setWeightsInitializer( new RandomWeightsInitializer( -1, 1 ) );
	}
	
	public NNEntity() 
	{
		NNBehavior beh = new NNBehavior();
		beh.setNetwork( createNetwork( 1 ) );
		
		inputSet = new double [trainingSetsize];
		outputSet = new double [trainingSetsize];
		calculatedSet = new double [trainingSetsize];
		
		double min = -5;
		double max = 5;
		double step = (max - min) / trainingSetsize;
		double curr = min;
		
		for(int idx = 0; idx < trainingSetsize; idx ++) {
			inputSet[idx] = curr;
			outputSet[idx] = Math.sin( curr );
			curr += step;
		}	
		
		setBehavior( beh );

		setLook( new NNLook() );
	}
	
	private BackpropNetwork createNetwork(int inputArrLength) 
	{
		return new BackpropNetwork( new int [] {1, 50, 50, 1}, new TanHAF() );
		
	}

	public int getTrainingSetSize()
	{
		
		return trainingSetsize;
	}
	
	public double [] getInputSet() { return inputSet; }
	public double [] getOutputSet() { return outputSet; }

	public void setCalculatedOutput(int idx, double value)
	{
		calculatedSet[idx] = value;
	}

	public double[] getCalculatedSet()
	{
		return calculatedSet;
	}
}
