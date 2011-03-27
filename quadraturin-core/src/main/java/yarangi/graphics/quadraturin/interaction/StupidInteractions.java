package yarangi.graphics.quadraturin.interaction;

import java.util.Iterator;
import java.util.Set;

import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.interaction.spatial.AABB;
import yarangi.graphics.quadraturin.objects.SceneEntity;

public class StupidInteractions implements IPhysicsEngine
{
	/**
	 * Stupid's logger.
	 */	
//	private Logger log = Logger.getLogger(this.getClass());
	
	private double frameLength;
	private ICollisionManager manager;
	
	public StupidInteractions()
	{
		this.frameLength = QuadConfigFactory.getStageConfig().getFrameLength();
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
		
		Set <SceneEntity> entities = manager.getObjectIndex().getLocations().keySet();
		for(SceneEntity entity : entities)
		{
			if(!manager.isCollidable(entity))
				continue;
			aabb = entity.getAABB();
			if(aabb == null)
				continue;
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

}
