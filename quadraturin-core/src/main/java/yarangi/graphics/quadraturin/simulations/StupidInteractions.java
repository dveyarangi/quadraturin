package yarangi.graphics.quadraturin.simulations;

import java.util.Set;

import com.spinn3r.log5j.Logger;

import yarangi.spatial.Area;
import yarangi.physics.Body;
import yarangi.physics.IPhysicalObject;

public class StupidInteractions <K extends IPhysicalObject> implements IPhysicsEngine <K>
{
	/**
	 * Stupid's logger.
	 */	
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Calculates narrow phase of collision handling.
	 */
	private ICollider <K> manager;
	
	
	public StupidInteractions(ICollider <K> man)
	{
		manager = man;
	}
	
	public ICollider <K> getCollisionManager() { return manager; }
	
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

		Set <K> entities = manager.getPhysicalEntities();
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
			
			// TODO: querying by area is inefficient (polygon iterator is slow)
			// TODO: collision prediction (expand area?)
			manager.query(entity, area);  
			
			////////////////////////////////
			// inert mass point adjustment:
			
			// TODO: add volume and rotation :)
			
			
			// TODO: probably should use some Runga-Kutta (research it). 
			body.addVelocity(body.getForce().x() / body.getMass() * time, body.getForce().y() / body.getMass() * time);
			
			// TODO: add limits for forces and velocities
			// TODO: warn about potentially destabilizing limit overflows (more than some percentage of limit)
			
			body.moveMassCenter(area, body.getVelocity().x() * time, body.getVelocity().y() * time);
			
		}
	}
	

}
