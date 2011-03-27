package yarangi.graphics.quadraturin.interaction;

import yarangi.graphics.quadraturin.SceneVeil;
import yarangi.graphics.quadraturin.interaction.spatial.SpatialIndexer;
import yarangi.graphics.quadraturin.objects.SceneEntity;

public class DefaultCollisionManager implements ICollisionManager
{
	
	private SceneVeil veil;

	public DefaultCollisionManager(SceneVeil veil)
	{
		this.veil = veil;
	}

	public void collide(SceneEntity e1, SceneEntity e2) 
	{
		((PhysicalEntity) e1).setImpactWith(e2);
		((PhysicalEntity) e2).setImpactWith(e1);
	}

	public boolean isCollidable(SceneEntity e) {
		// TODO: must be something smarter:
		return (e instanceof PhysicalEntity);
	}

	public SpatialIndexer <SceneEntity> getObjectIndex() {
		return veil.getEntityIndex();
	}

}
