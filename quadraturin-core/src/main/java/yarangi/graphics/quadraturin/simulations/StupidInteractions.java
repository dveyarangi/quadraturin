package yarangi.graphics.quadraturin.simulations;

import java.util.Set;

import org.apache.log4j.Logger;

import yarangi.spatial.Area;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialObject;
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
	
	public StupidInteractions(ICollisionManager man)
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

		Set <ISpatialObject> entities = manager.getObjectIndex().keySet();
//		log.debug("Entities in index: " + manager.getObjectIndex().size());
		for(ISpatialObject e : entities)
		{
			area = e.getArea();
			if(area == null) // bodyless entity:
				continue;
			
			// TODO: somehow remove the check and the casting
			if(!(e instanceof IPhysicalObject)) // non-physical entity:
				continue;
			
			IPhysicalObject entity = (IPhysicalObject) e;
					
			////////////////////////////////
			// collision detection broad phase: 
			sensor.setSource(entity);
			
			manager.getObjectIndex().query(sensor, area); // 
			
			////////////////////////////////
			// inert mass point adjustment:
			
			// TODO: add volume, rotation and collision response :)
			// TODO: probably should use some Runga-Kutta (research it). 
			entity.addVelocity(entity.getForce().x / entity.getMass() * time, entity.getForce().y / entity.getMass() * time);
			
			// TODO: add limits for forces and velocities
			// TODO: warn about potentially destabilizing limit overflows (more than some percentage of limit)
			
			entity.moveMassCenter(entity.getVelocity().x * time, entity.getVelocity().y * time);
			
		}
	}
	
	/**
	 * Spatial query processor
	 */
	public interface IProximitySensor extends ISpatialSensor <ISpatialObject>
	{
		/**
		 * Defines the reference entity for proximity tests.
		 * @param source
		 */
		public void setSource(IPhysicalObject source);
	}
	
	public class DefaultProximitySensor implements IProximitySensor
	{
		protected IPhysicalObject source;
		
		protected ICollisionManager manager;
		
		public DefaultProximitySensor(ICollisionManager manager)
		{
			this.manager = manager;
		}
		
		public boolean objectFound(IAreaChunk chunk, ISpatialObject target) {
			manager.collide(source, target);
			
			return true;
		}

		public void setSource(IPhysicalObject source) {
			this.source = source;
		}
		
	}
}
