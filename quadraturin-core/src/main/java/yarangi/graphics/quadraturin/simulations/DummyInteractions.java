package yarangi.graphics.quadraturin.simulations;

/**
 * Dummy physics engine implementation for scenes that do not require any physics.
 */
public class DummyInteractions implements IPhysicsEngine {

	/**
	 * {@inheritDoc}
	 * Does nothing.
	 */
	public void init() { }

	/**
	 * {@inheritDoc}
	 * Does nothing.
	 */
	public void addEntity(IPhysicalObject entity) { }

	/**
	 * {@inheritDoc}
	 * Does nothing.
	 */
	public void removeEntity(IPhysicalObject entity) { }

	/**
	 * {@inheritDoc}
	 * Does nothing.
	 */
	public void calculate(double time) { }

	/**
	 * {@inheritDoc}
	 * Also does nothing.
	 */
	public void updateEntity(IPhysicalObject entity) { }

	/**
	 * {@inheritDoc}
	 * Does nothing.
	 */	
	public void setCollisionManager(ICollisionManager manager) { }

}
