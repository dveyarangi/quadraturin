package yarangi.graphics.quadraturin.simulations;

import yarangi.spatial.SpatialIndexer;

/**
 * Handles the collision responses.
 * 
 * @author Dve Yarangi
 */
public interface ICollisionManager <K extends IPhysicalObject>
{
	
	/**
	 * Invoked on coarse collision detection stage.
	 * @param e1
	 * @param e2
	 */
	public void collide(K source, IPhysicalObject target); 
	
	/**
	 * Registers a collision handler for specific object type.
	 * @param _class
	 * @param agentCollider
	 */
	public void registerHandler(Class<? extends IPhysicalObject> _class, ICollisionHandler<K> agentCollider);
	
	/**
	 *  
	 * @return
	 */
	public SpatialIndexer <K> getObjectIndex();

}
