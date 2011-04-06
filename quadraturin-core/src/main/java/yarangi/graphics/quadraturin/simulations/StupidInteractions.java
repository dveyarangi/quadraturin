package yarangi.graphics.quadraturin.simulations;

import java.util.Iterator;
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
	
	private double frameLength;
	private ICollisionManager manager;
	
	private IProximityProcessor sensor;
	
	public StupidInteractions(int frameLength)
	{
		this.frameLength = frameLength;
	}
	
	public void setCollisionManager(ICollisionManager man)
	{
		this.manager = man;
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
			Iterator <SceneEntity> iterator = manager.getObjectIndex().iterator(minx, miny, maxx, maxy);
			while(iterator.hasNext())
				manager.collide(entity, iterator.next());
		}
	}
	

	public void runPreUnLock() { }

	public void runBody() 
	{
		calculate(frameLength);
	}

	public void runPostLock() { }

	
	public interface IProximityProcessor extends ISpatialSensor <ISpatialObject>
	{
		public void setSource(IPhysicalObject source);
	}
	public class DefaultProximityProcessor implements IProximityProcessor
	{
		protected IPhysicalObject source;
		
		protected ICollisionManager manager;
		
		public DefaultProximityProcessor(ICollisionManager manager)
		{
			this.manager = manager;
		}
		
		public void objectFound(ISpatialObject target) {
			manager.collide(source, target);
		}

		public void setSource(IPhysicalObject source) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
