package yarangi.graphics.quadraturin.simulations;

import java.util.Set;

import org.apache.log4j.Logger;

import yarangi.spatial.Area;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialSensor;

public class StupidInteractions <K extends IPhysicalObject> implements IPhysicsEngine <K>
{
	/**
	 * Stupid's logger.
	 */	
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Calculates narrow phase of collision handling.
	 */
	private ICollisionManager <K> manager;
	
	/**
	 * Spatial query processor for broad phase of collision/interaction test
	 */
	private IProximitySensor <K> sensor;
	
	public StupidInteractions()
	{
		
	}
	
	public void setCollisionManager(ICollisionManager <K> man)
	{
		this.manager = man;
		this.sensor = new DefaultProximitySensor <K>(man);
		
	}
	
	public ICollisionManager <K> getCollisionManager() { return manager; }
	
	public void init() { }
	public void destroy() { }
	
	// TODO: make it double sided:
	public void calculate(double time) 
	{
		if(time <= 0)
			throw new IllegalArgumentException("Time must be bigger than zero.");
			
		// no collision manager found
		if(manager == null)
			return;
		
		Area area;

		Set <K> entities = manager.getObjectIndex().keySet();
//		log.debug("Entities in index: " + manager.getObjectIndex().size());
		// TODO: redo! this is highly stupid!
		// query only to narrow the search, use polygon+velocity collision methods instead
		// http://www.codeproject.com/KB/GDI-plus/PolygonCollision.aspx
		for(K entity : entities)
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
			sensor.setSource(entity);
			
			// TODO: querying by area is inefficient (polygon iterator is slow)
			manager.getObjectIndex().query(sensor, area);  
			
			////////////////////////////////
			// inert mass point adjustment:
			
			// TODO: add volume, rotation and collision response :)
			// TODO: probably should use some Runga-Kutta (research it). 
			body.addVelocity(body.getForce().x() / body.getMass() * time, body.getForce().y() / body.getMass() * time);
			
			// TODO: add limits for forces and velocities
			// TODO: warn about potentially destabilizing limit overflows (more than some percentage of limit)
			
			body.moveMassCenter(area, body.getVelocity().x() * time, body.getVelocity().y() * time);
			
		}
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
	
	public class DefaultProximitySensor <T extends IPhysicalObject> implements IProximitySensor <T>
	{
		protected T source;
		
		protected ICollisionManager <T> manager;
		
		public DefaultProximitySensor(ICollisionManager <T> manager)
		{
			this.manager = manager;
		}
		
		public final boolean objectFound(IAreaChunk chunk, T target) 
		{
			if(!target.isAlive())
				return false; 
			if(!source.isAlive())
				return false;
			
			manager.collide(source, target);
			
			return true;
		}

		public final void setSource(T source) {
			this.source = source;
		}
		
	}
}
