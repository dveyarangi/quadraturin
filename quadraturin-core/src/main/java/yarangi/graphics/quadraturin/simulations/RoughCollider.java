package yarangi.graphics.quadraturin.simulations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import yarangi.graphics.quadraturin.terrain.ITileMap;
import yarangi.spatial.AABB;
import yarangi.spatial.Area;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialIndex;
import yarangi.spatial.ISpatialSensor;
import yarangi.spatial.Tile;

public class RoughCollider <O extends IPhysicalObject> implements ICollider <O>
{
	private ISpatialIndex <IAreaChunk, O> indexer;
	
	private ITileMap <O> terrain;
	
	private Map <Class<? extends IPhysicalObject>, ICollisionHandler<O>> handlers = 
				new HashMap <Class<? extends IPhysicalObject>, ICollisionHandler<O>> ();
	
	/**
	 * Spatial query processor for broad phase of collision/interaction test
	 */
	private IProximitySensor <IAreaChunk, O> worldSensor;
	private IProximitySensor <Tile<O>, O> terrainSensor;
	
	public RoughCollider(ISpatialIndex <IAreaChunk, O> indexer, ITileMap <O> terrain)
	{
		this.indexer = indexer;
		this.terrain = terrain;
		
		worldSensor = new WorldProximitySensor();
		terrainSensor = new TerrainProximitySensor();
	}
	
	@Override
	public boolean collide(O source, IPhysicalObject target) 
	{
		// todo:
		ICollisionHandler<O> handler = handlers.get( source.getClass() );
		if(handler == null)
			return false;
		if(!(target.getArea() instanceof AABB))
			return false; //throw new IllegalArgumentException("Collision with non-AABB areas is not yet supported (target: " + target + ")");
		if(source.getArea().overlaps( (AABB)target.getArea() ))
			return handler.setImpactWith( source, target );
		
		return false;
	}

	@Override
	public void registerHandler(Class <? extends IPhysicalObject> _class, ICollisionHandler<O> handler)
	{
		handlers.put( _class, handler );
	}

	@Override
	public void query(O entity, Area area)
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
	public Set<O> getPhysicalEntities()
	{
		return indexer.keySet();
	}
	/**
	 * Spatial query processor
	 */
	public interface IProximitySensor <T, O extends IPhysicalObject> extends ISpatialSensor <T, O>
	{
		/**
		 * Defines the reference entity for proximity tests.
		 * @param source
		 */
		public void setSource(O source);
	}
	
	public class WorldProximitySensor implements IProximitySensor <IAreaChunk, O>
	{
		protected O source;
		
		public final boolean objectFound(IAreaChunk chunk, O target) 
		{
//			if(!target.isAlive())
//				return false; 
			if(!source.isAlive())
				return true;
			
			return collide(source, target);
			
		}

		public final void setSource(O source) { this.source = source; }

		@Override
		public void clear() { }
	}
	public class TerrainProximitySensor implements IProximitySensor <Tile<O>, O>
	{
		protected O source;
		
		public final boolean objectFound(Tile<O> chunk, O target) 
		{
			if(!target.isAlive())
				return false; 
			if(!source.isAlive())
				return true;
			
			if(chunk.overlaps( source.getArea().getRefPoint().x()-source.getArea().getMaxRadius(),
					source.getArea().getRefPoint().y()-source.getArea().getMaxRadius(),
					source.getArea().getRefPoint().x()+source.getArea().getMaxRadius(),
					source.getArea().getRefPoint().y()+source.getArea().getMaxRadius()))
				return collide(source, target);
			
			return false;
		}

		public final void setSource(O source) { this.source = source; }

		@Override
		public void clear() { }
	}
}
