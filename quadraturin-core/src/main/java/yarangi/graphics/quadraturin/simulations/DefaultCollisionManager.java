package yarangi.graphics.quadraturin.simulations;

import yarangi.spatial.ISpatialObject;
import yarangi.spatial.SpatialIndexer;

public class DefaultCollisionManager implements ICollisionManager 
{
	private SpatialIndexer <ISpatialObject> indexer;
	
	public DefaultCollisionManager(SpatialIndexer <ISpatialObject> indexer)
	{
		this.indexer = indexer;
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
