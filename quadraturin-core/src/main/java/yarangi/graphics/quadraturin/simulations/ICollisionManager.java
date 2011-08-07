package yarangi.graphics.quadraturin.simulations;

import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.spatial.SpatialIndexer;

/**
 * Handles the collision responses.
 * 
 * @author Dve Yarangi
 */
public interface ICollisionManager
{
	/**
	 * Invoked on coarse collision detection stage.
	 * @param e1
	 * @param e2
	 */
	public void collide(SceneEntity e1, SceneEntity e2); 
	
	/**
	 * Registers a collision handler for specific object type.
	 * @param _class
	 * @param agentCollider
	 */
	public void registerHandler(Class<?> _class, ICollisionHandler<?> agentCollider);
	
	/**
	 *  
	 * @return
	 */
	public SpatialIndexer <SceneEntity> getObjectIndex();

}
