package yarangi.graphics.quadraturin.objects;

/**
 * Interface for {@link IBeing} behavior - change of internal state.
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
	 * @return true, if behavior changes the {@link IEntity#getArea()}. 
	 * TODO: invalidate entity's Area instead, to remove this illicit dependency on return type.
	 */
	public boolean behave(double time, An entity, boolean isVisible);
	
}
