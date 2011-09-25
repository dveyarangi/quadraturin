package yarangi.graphics.quadraturin.simulations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import yarangi.graphics.quadraturin.terrain.ITerrainMap;
import yarangi.spatial.Area;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialIndex;
import yarangi.spatial.ISpatialSensor;

public class RoughCollider <K extends IPhysicalObject> implements ICollider <K>
{
	private ISpatialIndex <K> indexer;
	
	private ITerrainMap <K> terrain;
	
	private Map <Class<? extends IPhysicalObject>, ICollisionHandler<K>> handlers = 
				new HashMap <Class<? extends IPhysicalObject>, ICollisionHandler<K>> ();
	
	/**
	 * Spatial query processor for broad phase of collision/interaction test
	 */
	private IProximitySensor <K> worldSensor;
	private IProximitySensor <K> terrainSensor;
	
	public RoughCollider(ISpatialIndex <K> indexer, ITerrainMap <K> terrain)
	{
		this.indexer = indexer;
		this.terrain = terrain;
		
		worldSensor = new WorldProximitySensor();
		terrainSensor = new TerrainProximitySensor();
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
	public void query(K entity, Area area)
	{
		worldSensor.clear();
		terrainSensor.clear();
		worldSensor.setSource( entity );
		terrainSensor.setSource( entity );
		indexer.query( worldSensor, area );
		if(terrain != null)
			terrain.query(terrainSensor, area);
		
	}

	@Override
	public Set<K> getPhysicalEntities()
	{
		return indexer.keySet();
	}
	/**
	 * Spatial query processor
	 */
	public interface IProximitySensor <T extends IPhysicalObject> extends ISpatialSensor <T>
	{
		/**
		 * Defines the reference entity for proximity tests.
		 * @param source
		 */
		public void setSource(T source);
	}
	
	public class WorldProximitySensor implements IProximitySensor <K>
	{
		protected K source;
		
		public final boolean objectFound(IAreaChunk chunk, K target) 
		{
//			if(!target.isAlive())
//				return false; 
			if(!source.isAlive())
				return true;
			
			return collide(source, target);
		}

		public final void setSource(K source) { this.source = source; }

		@Override
		public void clear() { }
	}
	public class TerrainProximitySensor implements IProximitySensor <K>
	{
		protected K source;
		
		public final boolean objectFound(IAreaChunk chunk, K target) 
		{
			if(!target.isAlive())
				return false; 
			if(!source.isAlive())
				return true;
			
			return collide(source, target);
		}

		public final void setSource(K source) { this.source = source; }

		@Override
		public void clear() { }
	}
}
