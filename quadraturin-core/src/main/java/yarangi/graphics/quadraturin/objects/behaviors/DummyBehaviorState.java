package yarangi.graphics.quadraturin.objects.behaviors;

public class DummyBehaviorState <K> implements IBehaviorState <K> 
{

	@Override
	public boolean behave(double time, K entity, boolean isVisible) { return false; }

}
