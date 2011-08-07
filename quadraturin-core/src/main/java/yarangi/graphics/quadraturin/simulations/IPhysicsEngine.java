package yarangi.graphics.quadraturin.simulations;



/**
 * Physics engine interface. See {@link StupidInteractions}
 * 
 */
public interface IPhysicsEngine
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
	
	public void setCollisionManager(ICollisionManager man);

	public ICollisionManager getCollisionManager();
}
