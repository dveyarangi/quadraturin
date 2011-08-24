package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.IWorldEntity;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.math.Vector2D;
import yarangi.spatial.SpatialHashMap;

public class WorldVeil extends SceneVeil <IWorldEntity> 
{

	private IPhysicsEngine <IWorldEntity> engine;
	
	private double veilTime;
	
	public WorldVeil(int width, int height, IPhysicsEngine <IWorldEntity> engine) 
	{ 
//		super(new SpatialHashMap<ISpatialObject>(100, 10, width, height));
		super(width, height, new SpatialHashMap	<IWorldEntity>(width*height/10, 10, width, height));
		
		System.out.println("Allocated " + width*height/10 + " cells.");
			
		this.engine = engine;
	}

	public final IPhysicsEngine <IWorldEntity> getPhysicsEngine() 
	{
		return engine; 
	}
	
	/**
	 * {@inheritDoc}
	 * Initializes physics engine.
	 */
	public void init(GL gl)
	{
		super.init( gl );
		
		engine.init();
	}
	
	/**
	 * Any rendering preprocessing should be made here.
	 * @param gl
	 */
	public void preDisplay(GL gl) {}
	
	/**
	 * Any rendering postprocessing should be made here.
	 * @param gl
	 */
	public void postDisplay(GL gl) {}
	
	public void animate(double time)
	{
//		boolean changePending = false;
		
		// running physics:
		if(getPhysicsEngine() != null)
			getPhysicsEngine().calculate(time);
		veilTime += time;
		Vector2D refPoint;
		// TODO: no control on order of executions
		for(IWorldEntity entity : getEntities())
		{
			
			if(entity.getBehavior().behave(time, entity, true))
			{   // TODO: 
				if(entity.getArea() != null)
					getEntityIndex().update(entity.getArea(), entity);
//				changePending = true;
			}
			
			if (!entity.isAlive())
			{   // scheduling for removal:
				removeEntity(entity);
				continue;
			}
			
			if(entity.getSensor() != null)
			{   // filling world objects in sensor range:
				// TODO: scheduling, perhaps?

				if(entity.getSensor().isSensingNeeded( veilTime ))
				{
					refPoint = entity.getArea().getRefPoint();
					// this implementation extracts live entity objects, entity locations thus updated regardless of sensing frequency
					getEntityIndex().query(entity.getSensor(), refPoint.x(), refPoint.y(), entity.getSensor().getSensorRadiusSquare());
				}
			}
		}
//		return changePending;
	}

	/**
	 * {@inheritDoc}
	 * Stops physics engine.
	 */
	public void destroy(GL gl)
	{
		super.destroy( gl );
		engine.destroy();
	}

}
