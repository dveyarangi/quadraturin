package yarangi.graphics.quadraturin.simulations;

import yarangi.spatial.ISpatialObject;
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
	public void collide(ISpatialObject e1, ISpatialObject e2);
	
	/**
	 * 
	 * @return
	 */
	public SpatialIndexer <ISpatialObject> getObjectIndex();
}
