package yarangi.graphics.quadraturin.simulations;



/**
 * Physics engine interface. See {@link StupidInteractions}
 * 
 */
public interface IPhysicsEngine <K extends IPhysicalObject>
{
	/**
	 * Initializes whatever needs to be initialized.
	 */
	public void init();
	
	/**
	 * Calculates whatever needs to be calculated.
	 * Called in the engine loop.
	 * @param time
	 */
	public void calculate(double time);

	public ICollider <K> getCollisionManager();

	public void destroy();
}
