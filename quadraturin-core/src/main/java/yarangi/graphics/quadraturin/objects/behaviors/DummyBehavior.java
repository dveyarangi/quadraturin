package yarangi.graphics.quadraturin.objects.behaviors;

import yarangi.graphics.quadraturin.objects.IBehavior;

public class DummyBehavior <K> implements IBehavior <K>  
{	
	public boolean behave(double time, K entity, boolean isVisible) { return false; } 
}
