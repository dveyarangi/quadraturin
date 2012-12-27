package yarangi.graphics.quadraturin.objects.behaviors;

import yarangi.graphics.quadraturin.objects.IBehavior;

/**
 * Dummy behavior for dummies.
 * 
 * @author dveyarangi
 *
 * @param <K>
 */
public class DummyBehavior <K> implements IBehavior <K>  
{	
	@Override
	public boolean behave(double time, K entity, boolean isVisible) { return false; } 
}
