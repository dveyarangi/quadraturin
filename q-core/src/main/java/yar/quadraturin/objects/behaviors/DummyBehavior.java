package yar.quadraturin.objects.behaviors;

import yar.quadraturin.objects.IBehavior;

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
