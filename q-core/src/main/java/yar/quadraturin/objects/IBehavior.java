package yar.quadraturin.objects;

/**
 * Interface for entity behavior..
 * Used to animate an engine entity.
 * 
 * Invoked in {@link QAnimator} loop.  
 */
public interface IBehavior <An>
{
	
	/**
	 * Makes it behave.
	 * 
	 * @param time "real world" time to calculate the behavior for (see {@link QAnimator})
	 * @param entity Entity this behavior should be applied to
	 * @param isVisible TODO: states, if the entity is currently visible to invoke simpler behavior
	 * 
	 * @return true, if behavior changes the animated entity. 
	 * TODO: invalidate entity's state instead (Area or Body, in case of IEntity}, to remove this illicit dependency on return value.
	 */
	public boolean behave(double dt, An entity, boolean isVisible);
	
}
