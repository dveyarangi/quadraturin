package yarangi.graphics.quadraturin.simulations;

import java.util.Set;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.objects.Body;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.spatial.Area;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialSensor;

public class StupidInteractions implements IPhysicsEngine
{
	/**
	 * Stupid's logger.
	 */	
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Calculates narrow phase of collision handling.
	 */
	private ICollisionManager manager;
	
	/**
	 * Spatial query processor for broad phase of collision/interaction test
	 */
	private IProximitySensor sensor;
	
	public StupidInteractions()
	{
		
	}
	
	public void setCollisionManager(ICollisionManager man)
	{
		this.manager = man;
		this.sensor = new DefaultProximitySensor(man);
		
	}
	
	public void init() { }
	
	// TODO: make it double sided:
	public void calculate(double time) 
	{
		if(time <= 0)
			throw new IllegalArgumentException("Time must be bigger than zero.");
			
		// no collision manager found
		if(manager == null)
			return;
		
		Area area;

		Set <SceneEntity> entities = manager.getObjectIndex().keySet();
//		log.debug("Entities in index: " + manager.getObjectIndex().size());
		// TODO: redo! this is highly stupid!
		// query only to narrow the search, use polygon+velocity collision methods instead
		// http://www.codeproject.com/KB/GDI-plus/PolygonCollision.aspx
		for(SceneEntity entity : entities)
		{
			if(!entity.isAlive()) // sanity check
				continue; 
			
			area = entity.getArea();
			if(area == null) // bodyless entity:
				continue;
			
			Body body = entity.getBody();
			if(body == null) // non-physical entity:
				continue;
			
			////////////////////////////////
			// collision detection broad phase: 
			sensor.setSource(body);
			
			// TODO: querying by area is inefficient (polygon iterator is slow)
			manager.getObjectIndex().query(sensor, area);  
			
			////////////////////////////////
			// inert mass point adjustment:
			
			// TODO: add volume, rotation and collision response :)
			// TODO: probably should use some Runga-Kutta (research it). 
			body.addVelocity(body.getForce().x / body.getMass() * time, body.getForce().y / body.getMass() * time);
			
			// TODO: add limits for forces and velocities
			// TODO: warn about potentially destabilizing limit overflows (more than some percentage of limit)
			
			body.moveMassCenter(area, body.getVelocity().x * time, body.getVelocity().y * time);
			
		}
	}
	
	/**
	 * Spatial query processor
	 */
	public interface IProximitySensor extends ISpatialSensor <SceneEntity>
	{
		/**
		 * Defines the reference entity for proximity tests.
		 * @param source
		 */
		public void setSource(Body source);
	}
	
	public class DefaultProximitySensor implements IProximitySensor
	{
		protected Body source;
		
		protected ICollisionManager manager;
		
		public DefaultProximitySensor(ICollisionManager manager)
		{
			this.manager = manager;
		}
		
		public boolean objectFound(IAreaChunk chunk, SceneEntity target) 
		{
			if(!target.isAlive())
				return false; 
			
			manager.collide(source, target);
			
			return true;
		}

		public void setSource(Body source) {
			this.source = source;
		}
		
	}
}
