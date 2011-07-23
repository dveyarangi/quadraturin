package yarangi.graphics.quadraturin.simulations;

import yarangi.graphics.quadraturin.objects.Body;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.spatial.SpatialIndexer;

public class DefaultCollisionManager implements ICollisionManager 
{
	private SpatialIndexer <SceneEntity> indexer;
	
	public DefaultCollisionManager(SpatialIndexer <SceneEntity> indexer)
	{
		this.indexer = indexer;
	}
	
	public void collide(Body e1, SceneEntity e2) 
	{
		// todo:
		e1.setImpactWith(e2);
	}

	public SpatialIndexer<SceneEntity> getObjectIndex() { return indexer; }

}
