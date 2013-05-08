package yar.quadraturin.objects.behaviors;

public class DummyBehaviorState <K> implements IBehaviorState <K> 
{

	
	
	@Override
	public double behave(double time, K entity) { return 0; }

	@Override
	public int getId()
	{
		return Integer.MIN_VALUE; 
	}

}
