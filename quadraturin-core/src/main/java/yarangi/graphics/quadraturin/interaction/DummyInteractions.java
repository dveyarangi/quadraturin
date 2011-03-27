package yarangi.graphics.quadraturin.interaction;

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
	public void addEntity(PhysicalEntity entity) { }

	/**
	 * {@inheritDoc}
	 * Does nothing.
	 */
	public void removeEntity(PhysicalEntity entity) { }

	/**
	 * {@inheritDoc}
	 * Does nothing.
	 */
	public void calculate(double time) { }

	/**
	 * {@inheritDoc}
	 * Also does nothing.
	 */
	public void updateEntity(PhysicalEntity entity) { }

	public void runPreUnLock() {
	}

	
	public void runBody() {
	}

	
	public void runPostLock() {
	}


	public void setCollisionManager(ICollisionManager manager) {
	}

}
