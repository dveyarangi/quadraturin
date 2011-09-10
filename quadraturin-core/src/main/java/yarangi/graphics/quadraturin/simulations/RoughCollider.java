package yarangi.graphics.quadraturin.simulations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import yarangi.graphics.quadraturin.terrain.ITerrainMap;
import yarangi.spatial.Area;
import yarangi.spatial.ISpatialIndex;
import yarangi.spatial.ISpatialSensor;

public class RoughCollider <K extends IPhysicalObject> implements ICollider <K>
{
	private ISpatialIndex <K> indexer;
	
	private ITerrainMap <K> terrain;
	
	private Map <Class<? extends IPhysicalObject>, ICollisionHandler<K>> handlers = 
				new HashMap <Class<? extends IPhysicalObject>, ICollisionHandler<K>> ();
	
	public RoughCollider(ISpatialIndex <K> indexer, ITerrainMap <K> terrain)
	{
		this.indexer = indexer;
		this.terrain = terrain;
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
	public void registerHandler(Class <? extends IPhysicalObject> _class, ICollisionHandler<K> handler)
	{
		handlers.put( _class, handler );
	}

	@Override
	public ISpatialSensor<K> query(ISpatialSensor<K> sensor, Area area)
	{
		sensor.clear();
		indexer.query( sensor, area );
		if(terrain != null)
			terrain.query(sensor, area);
		
		return sensor;
	}

	@Override
	public Set<K> getPhysicalEntities()
	{
		return indexer.keySet();
	}

}
