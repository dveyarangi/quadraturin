package yarangi.graphics.quadraturin.simulations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import yarangi.physics.IPhysicalObject;
import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialSensor;
import yarangi.spatial.ISpatialSetIndex;
import yarangi.spatial.ITileMap;

public class RoughCollider <O extends IPhysicalObject> implements ICollider <O>
{
	
	/**
	 * Index of entities.
	 */
	private final ISpatialSetIndex <O> indexer;
	
	/**
	 * Optional: terrain
	 */
	private final ITileMap <O> terrain;
	
	private final Map <Class<? extends IPhysicalObject>, ICollisionHandler<O>> handlers = 
				new HashMap <Class<? extends IPhysicalObject>, ICollisionHandler<O>> ();
	
	/**
	 * Spatial query processor for broad phase of collision/interaction test
	 */
	private final IProximitySensor <O> worldSensor;
	private final IProximitySensor <O> terrainSensor;
	
	public RoughCollider(ISpatialSetIndex < O> indexer, ITileMap <O> terrain)
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
		if(target.getArea() instanceof AABB)
		{
			if(source.getArea().overlaps( (AABB)target.getArea() ))
				return handler.setImpactWith( source, target );
			else
				return false;
		}
		
		//throw new IllegalArgumentException("Collision with areas of type [" + target.getClass() + "] is yet supported.");
		return false;
	}

	@Override
	public void registerHandler(Class <? extends IPhysicalObject> _class, ICollisionHandler<O> handler)
	{
		handlers.put( _class, handler );
	}

	@Override
	public void query(O entity)
	{
		worldSensor.clear();
		terrainSensor.clear();
		worldSensor.setSource( entity );
		terrainSensor.setSource( entity );
		AABB aabb = (AABB)entity.getArea();
//		System.out.println("RoughCollider: " + entity + " : " + entity.getArea());
		indexer.queryAABB( worldSensor, aabb.getCenterX(), aabb.getCenterY(), aabb.getRX(), aabb.getRY());
		if(terrain != null)
			terrain.queryAABB(terrainSensor, aabb.getCenterX(), aabb.getCenterY(), aabb.getRX(), aabb.getRY());
		
	}

	@Override
	public Set<O> getPhysicalEntities()
	{
		return indexer.keySet();
	}
	
	
	/**
	 * Spatial query processor
	 */
	public interface IProximitySensor <O extends IPhysicalObject> extends ISpatialSensor <O>
	{
		/**
		 * Defines the reference entity for proximity tests.
		 * @param source
		 */
		public void setSource(O source);
	}
	
	public class WorldProximitySensor implements IProximitySensor <O>
	{
		protected O source;
		
		@Override
		public final boolean objectFound(O target) 
		{
//			if(!target.isAlive())
//				return false; 
			if(!source.isAlive())
				return true;
			
			return collide(source, target);
			
		}

		@Override
		public final void setSource(O source) { this.source = source; }

		@Override
		public void clear() { }
	}
	public class TerrainProximitySensor implements IProximitySensor <O>
	{
		protected O source;
		
		@Override
		public final boolean objectFound(O target) 
		{
			if(!target.isAlive())
				return false; 
			if(!source.isAlive())
				return true;
			
			return collide(source, target);
		}

		@Override
		public final void setSource(O source) { this.source = source; }

		@Override
		public void clear() { }
	}
}
