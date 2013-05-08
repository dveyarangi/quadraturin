package test.nn;

import yar.quadraturin.objects.IBehavior;
import yarangi.ai.nn.numeric.NeuralNetworkRunner;
import yarangi.numbers.RandomUtil;

public class NNBehavior  extends NeuralNetworkRunner <Double, Double> implements IBehavior <NNEntity>
{

	public NNBehavior()
	{
		super( "sin" );

	}

	@Override
	public boolean behave(double time, NNEntity entity, boolean isVisible)
	{
		for(int i = 0; i < 100; i ++) {
			int idx = RandomUtil.N( entity.getTrainingSetSize() );
			
			entity.setCalculatedOutput(idx, run(entity.getInputSet()[idx]));
			train(entity.getOutputSet()[idx] );
		}
		return true;
	}

	@Override
	protected double[] toInputArray(Double input)
	{
		return new double[] { input };
	}

	@Override
	protected double[] toOutputArray(Double input)
	{
		return new double[] { input };
	}

	@Override
	protected Double toOutput(double[] outputs)
	{
		// TODO Auto-generated method stub
		return outputs[0];
	}
	@Override
	protected double getLearningRate()
	{
		return RandomUtil.STD( 0.01, 0.01);
	}
}
