package yarangi.graphics.quadraturin.interaction;

import yarangi.graphics.quadraturin.interaction.spatial.SpatialIndexer;
import yarangi.graphics.quadraturin.objects.SceneEntity;

public interface ICollisionManager
{
	public void collide(SceneEntity e1, SceneEntity e2);
	
	public boolean isCollidable(SceneEntity e1);

	public SpatialIndexer <SceneEntity> getObjectIndex();
}
