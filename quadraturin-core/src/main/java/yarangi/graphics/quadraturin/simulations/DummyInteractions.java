package yarangi.graphics.quadraturin.simulations;

/**
 * Dummy physics engine implementation for scenes that do not require any physics.
 */
public class DummyInteractions implements IPhysicsEngine {

	
	private ICollider manager;
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
	public void setCollisionManager(ICollider manager) { this.manager = manager;}

	@Override
	public ICollider getCollisionManager()
	{
		return manager;
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}

}
