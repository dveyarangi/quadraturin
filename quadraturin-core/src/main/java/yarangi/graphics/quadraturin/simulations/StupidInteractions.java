package yarangi.graphics.quadraturin.simulations;

import java.util.Set;

import yarangi.physics.Body;
import yarangi.physics.IPhysicalObject;
import yarangi.spatial.Area;

import com.spinn3r.log5j.Logger;

public class StupidInteractions <K extends IPhysicalObject> implements IPhysicsEngine <K>
{
	/**
	 * Stupid's logger.
	 */	
	private final Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Calculates narrow phase of collision handling.
	 */
	private final ICollider <K> manager;
	
	
	public StupidInteractions(ICollider <K> man)
	{
		manager = man;
	}
	
	@Override
	public ICollider <K> getCollisionManager() { return manager; }
	
	@Override
	public void init() { }
	@Override
	public void destroy() { }
	
	// TODO: make it double sided:
	@Override
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
			manager.query(entity);  
			
			////////////////////////////////
			// inert mass point adjustment:
			
			// TODO: add volume and rotation :)
			
			if(body.isInert()) 
			{
				
				if(body.getFrictionCoef() != 0)
				{
					
					double vabs = body.getVelocity().abs();
					if(vabs != 0) {
						double fricoef = body.getFrictionCoef() * time;
						double fvdx = -body.getVelocity().x()/vabs * fricoef;
						double fvdy = -body.getVelocity().y()/vabs * fricoef;
//						System.out.println("friction: " + fvdx + " : " + fvdy);
						body.addForce( fvdx, fvdy );
					}
				}
				
				
				// TODO: probably should use some Runga-Kutta. 
				body.addVelocity(body.getForce().x() / body.getMass() * time, body.getForce().y() / body.getMass() * time);
				
				

				
				// TODO: add limits for forces and velocities
				// TODO: warn about potentially destabilizing limit overflows (more than some percentage of limit)
				
				body.moveMassCenter(area, body.getVelocity().x() * time, body.getVelocity().y() * time);
			}
			
		}
	}
	

}
