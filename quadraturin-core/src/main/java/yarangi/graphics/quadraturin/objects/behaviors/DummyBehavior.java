package yarangi.graphics.quadraturin.objects.behaviors;

import yarangi.graphics.quadraturin.objects.Behavior;

public class DummyBehavior <K> implements Behavior <K>  
{	
	public boolean behave(double time, K entity, boolean isVisible) { return false; } 
}
