package yarangi.graphics.quadraturin.simulations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import yarangi.graphics.quadraturin.terrain.ITerrain;
import yarangi.physics.IPhysicalObject;
import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialSensor;
import yarangi.spatial.ISpatialSetIndex;
import yarangi.spatial.ITileMap;

public class RoughCollider <B extends IPhysicalObject, T extends ITerrain> implements ICollider <B>
{
	
	/**
	 * Index of entities.
	 */
	private final ISpatialSetIndex <B> indexer;
	
	/**
	 * Optional: terrain
	 */
	private final ITileMap <T> terrain;
	
	private final Map <Class<? extends IPhysicalObject>, ICollisionHandler<B>> handlers = 
				new HashMap <Class<? extends IPhysicalObject>, ICollisionHandler<B>> ();
	
	/**
	 * Spatial query processor for broad phase of collision/interaction test
	 */
	private final WorldProximitySensor worldSensor;
	private final TerrainProximitySensor terrainSensor;
	
	public RoughCollider(ISpatialSetIndex < B> indexer, ITileMap <T> terrain)
	{
		this.indexer = indexer;
		this.terrain = terrain;
		
		worldSensor = new WorldProximitySensor();
		terrainSensor = new TerrainProximitySensor();
	}
	
	@Override
	public boolean collide(B source, IPhysicalObject target) 
	{
		// todo:
		ICollisionHandler<B> handler = handlers.get( source.getClass() );
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
	public void registerHandler(Class <? extends IPhysicalObject> _class, ICollisionHandler<B> handler)
	{
		handlers.put( _class, handler );
	}

	@Override
	public void query(B entity)
	{
		worldSensor.clear();
		terrainSensor.clear();
		worldSensor.setSource( entity );
		terrainSensor.setSource( entity );
		AABB aabb = (AABB)entity.getArea();
//		System.out.println("RoughCollider: " + entity + " : " + entity.getArea());
		indexer.queryAABB( worldSensor, (float)aabb.getCenterX(), (float)aabb.getCenterY(), (float)aabb.getRX(), (float)aabb.getRY());
		if(terrain != null)
			terrain.queryAABB(terrainSensor, (float)aabb.getCenterX(), (float)aabb.getCenterY(), (float)aabb.getRX(), (float)aabb.getRY());
		
	}

	@Override
	public Set<B> getPhysicalEntities()
	{
		return indexer.keySet();
	}
	
	
	
	public class WorldProximitySensor implements ISpatialSensor <B>
	{
		protected B source;
		
		@Override
		public final boolean objectFound(B target) 
		{
//			if(!target.isAlive())
//				return false; 
			if(!source.isAlive())
				return true;
			
			return collide(source, target);
			
		}

		public final void setSource(B source) { this.source = source; }

		@Override
		public void clear() { }
	}
	public class TerrainProximitySensor implements ISpatialSensor <T>
	{
		protected B source;
		
		@Override
		public final boolean objectFound(T target) 
		{
			if(!target.isAlive())
				return false; 
			if(!source.isAlive())
				return true;
			
			// 
//			System.out.println(source + " : " + target);
			if(	target.overlaps( (AABB)source.getArea() ) )
					return collide(source, target);
			return false;
		}

		public final void setSource(B source) { this.source = source; }

		@Override
		public void clear() { }
	}
}
