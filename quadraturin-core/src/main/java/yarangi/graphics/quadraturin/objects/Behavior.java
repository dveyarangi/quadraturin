package yarangi.graphics.quadraturin.objects;

/**
 * Interface for {@link CompositeSceneEntity} behavior - change of internal state.
 * Invoked in {@link StageAnimator} loop.  
 */
public interface Behavior <An>
{
	
	/**
	 * Makes it behave.
	 * 
	 * @param time "real world" time to calculate the behavior for.
	 * @param entity Entity this behavior should be applied to
	 * @param isVisible states, if the entity is currently visible to invoke simpler behavior
	 * 
	 * @return true, if the underlying entity has been relocated and needs spatial update.
	 */
	public boolean behave(double time, An entity, boolean isVisible);
	
}
