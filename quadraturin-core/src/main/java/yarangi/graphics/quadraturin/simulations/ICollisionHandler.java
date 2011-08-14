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
	 * Implements collision logic. 
	 * @param e
	 */
	public void setImpactWith(K source, IPhysicalObject target);

}
