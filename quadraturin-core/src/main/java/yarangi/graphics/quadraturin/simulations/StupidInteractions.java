package yarangi.graphics.quadraturin.simulations;

import java.util.Set;

import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.ISpatialSensor;

public class StupidInteractions implements IPhysicsEngine
{
	/**
	 * Stupid's logger.
	 */	
//	private Logger log = Logger.getLogger(this.getClass());
	
	private ICollisionManager manager;
	
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
	
	public void calculate(double time) 
	{
		AABB aabb;

//		long startTime = System.nanoTime();
		// TODO: make it double sided:
		if(manager == null)
			return;
		
		Set <ISpatialObject> entities = manager.getObjectIndex().keySet();
		for(ISpatialObject e : entities)
		{
			aabb = e.getAABB();
			if(aabb == null)
				continue;
			
			if(!(e instanceof IPhysicalObject))
				continue;
			
			IPhysicalObject entity = (IPhysicalObject) e;
					
			double minx = aabb.x-aabb.r;
			double miny = aabb.y-aabb.r;
			double maxx = aabb.x+aabb.r;
			double maxy = aabb.y+aabb.r;
			
			sensor.setSource(entity);
			
			manager.getObjectIndex().query(sensor,minx, miny, maxx, maxy);
			
			
			// inert mass point
			// TODO: add volume, rotation and collision response :) 
			entity.getVelocity().x += entity.getForce().x / entity.getMass() * time;
			entity.getVelocity().x += entity.getForce().y / entity.getMass() * time;
			
			entity.getAABB().x += entity.getVelocity().x * time;
			entity.getAABB().y += entity.getVelocity().y * time;
			
		}
	}
	
	public interface IProximitySensor extends ISpatialSensor <ISpatialObject>
	{
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
		
		public void objectFound(ISpatialObject target) {
			manager.collide(source, target);
		}

		public void setSource(IPhysicalObject source) {
			this.source = source;
		}
		
	}
}
