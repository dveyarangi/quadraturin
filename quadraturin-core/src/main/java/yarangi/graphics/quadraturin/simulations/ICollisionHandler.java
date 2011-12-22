package yarangi.graphics.quadraturin.simulations;


/**
 * Implements collision reaction for objects of type K.
 * @author dveyarangi
 *
 * @param <K>
 */
public interface ICollisionHandler <K>
{
	/**
	 * Implements collision logic
		TODO: add time. and make rendering and behavior/physics parallel
	 * @param e
	 * @return true, if no further processing of source collision is required.
	 */
	public boolean setImpactWith(K source, IPhysicalObject target);

}
