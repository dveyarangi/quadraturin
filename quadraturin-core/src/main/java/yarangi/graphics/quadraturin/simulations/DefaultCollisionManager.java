package yarangi.graphics.quadraturin.simulations;

import java.util.HashMap;
import java.util.Map;

import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.spatial.SpatialIndexer;

public class DefaultCollisionManager implements ICollisionManager 
{
	private SpatialIndexer <SceneEntity> indexer;
	
	private Map <Class, ICollisionHandler> handlers = new HashMap <Class, ICollisionHandler> ();
	
	public DefaultCollisionManager(SpatialIndexer <SceneEntity> indexer)
	{
		this.indexer = indexer;
	}
	
	@Override
	public void collide(SceneEntity source, SceneEntity target) 
	{
		// todo:
		ICollisionHandler handler = handlers.get( source.getClass() );
		if(handler == null)
			return;
		
		handler.setImpactWith( source, target );
	}

	@Override
	public SpatialIndexer<SceneEntity> getObjectIndex() { return indexer; }

	@Override
	public void registerHandler(Class<?> _class, ICollisionHandler<?> handler)
	{
		handlers.put( _class, handler );
	}

}
