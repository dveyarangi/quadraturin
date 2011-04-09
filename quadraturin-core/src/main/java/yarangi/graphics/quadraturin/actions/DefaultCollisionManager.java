package yarangi.graphics.quadraturin.actions;

import yarangi.graphics.quadraturin.SceneVeil;
import yarangi.graphics.quadraturin.simulations.ICollisionManager;
import yarangi.graphics.quadraturin.simulations.IPhysicalObject;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.SpatialIndexer;

public class DefaultCollisionManager implements ICollisionManager 
{
	private SpatialIndexer <ISpatialObject> indexer;
	
	public DefaultCollisionManager(SceneVeil veil)
	{
		this.indexer = veil.getEntityIndex();
	}
	
	public void collide(ISpatialObject e1, ISpatialObject e2) {
		if(!(e1 instanceof IPhysicalObject))
			return;
		if(!(e2 instanceof IPhysicalObject))
			return;

		IPhysicalObject source = (IPhysicalObject) e1;
		IPhysicalObject target = (IPhysicalObject) e2;
		
		target.setImpactWith(source);
	}

	public SpatialIndexer<ISpatialObject> getObjectIndex() { return indexer; }

}
