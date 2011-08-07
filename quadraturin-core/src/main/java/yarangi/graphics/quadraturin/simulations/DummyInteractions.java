package yarangi.graphics.quadraturin.simulations;

/**
 * Dummy physics engine implementation for scenes that do not require any physics.
 */
public class DummyInteractions implements IPhysicsEngine {

	
	private ICollisionManager manager;
	/**
	 * {@inheritDoc}
	 * Does nothing.
	 */
	public void init() { }

	/**
	 * {@inheritDoc}
	 * Does nothing.
	 */
	public void calculate(double time) { }

	/**
	 * {@inheritDoc}
	 * Does nothing.
	 */	
	public void setCollisionManager(ICollisionManager manager) { this.manager = manager;}

	@Override
	public ICollisionManager getCollisionManager()
	{
		return manager;
	}

}
