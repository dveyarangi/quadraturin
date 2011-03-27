package yarangi.graphics.quadraturin.interaction;

import yarangi.graphics.quadraturin.thread.Loopy;


/**
 * Physics engine interface. See {@link StupidInteractions}
 * 
 */
public interface IPhysicsEngine extends Loopy
{
	/**
	 * Initializes whatever needs to be initialized.
	 */
	public void init();
	
	public void setCollisionManager(ICollisionManager manager);
	
	/**
	 * Calculates whatever needs to be calculated.
	 * Called in the engine loop.
	 * @param time
	 */
	public void calculate(double time);
}
