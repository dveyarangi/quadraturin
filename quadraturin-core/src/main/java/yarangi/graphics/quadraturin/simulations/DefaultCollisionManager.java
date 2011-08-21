package yarangi.graphics.quadraturin.simulations;

import java.util.HashMap;
import java.util.Map;

import yarangi.spatial.SpatialIndexer;

public class DefaultCollisionManager <K extends IPhysicalObject> implements ICollisionManager <K>
{
	private SpatialIndexer <K> indexer;
	
	private Map <Class<? extends IPhysicalObject>, ICollisionHandler<K>> handlers = 
				new HashMap <Class<? extends IPhysicalObject>, ICollisionHandler<K>> ();
	
	public DefaultCollisionManager(SpatialIndexer <K> indexer)
	{
		this.indexer = indexer;
	}
	
	@Override
	public boolean collide(K source, IPhysicalObject target) 
	{
		// todo:
		ICollisionHandler<K> handler = handlers.get( source.getClass() );
		if(handler == null)
			return false;
		
		return handler.setImpactWith( source, target );
	}

	@Override
	public SpatialIndexer<K> getObjectIndex() { return indexer; }

	@Override
	public void registerHandler(Class <? extends IPhysicalObject> _class, ICollisionHandler<K> handler)
	{
		handlers.put( _class, handler );
	}

}
