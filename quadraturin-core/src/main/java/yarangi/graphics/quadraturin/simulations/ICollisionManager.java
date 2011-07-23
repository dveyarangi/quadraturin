package yarangi.graphics.quadraturin.simulations;

import yarangi.graphics.quadraturin.objects.Body;
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
	public void collide(Body e1, SceneEntity e2); 
	
	/**
	 * 
	 * @return
	 */
	public SpatialIndexer <SceneEntity> getObjectIndex();
}
