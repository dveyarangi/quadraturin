package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.graphics.quadraturin.terrain.ITerrainMap;
import yarangi.math.Vector2D;
import yarangi.spatial.SpatialHashMap;

public class WorldVeil extends SceneVeil <IEntity> 
{

	private IPhysicsEngine <IEntity> engine;
	
	private double veilTime;
	
	private EntityShell <? extends ITerrainMap >terrain;
	
	public WorldVeil(int width, int height) 
	{ 
//		super(new SpatialHashMap<ISpatialObject>(100, 10, width, height));
		super(width, height, new SpatialHashMap	<IEntity>(width*height/10, 10, width, height));
		
		System.out.println("Allocated " + width*height/10 + " cells.");
	}
	

	public final IPhysicsEngine <IEntity> getPhysicsEngine() 
	{
		return engine; 
	}
	
	/**
	 * TODO: restore configurable engine and add terrain configuration.
	 * TODO: collisions on/off
	 * @param engine
	 * @param collide
	 */
	public final void setPhysicsEngine(IPhysicsEngine <IEntity> engine)
	{
		this.engine = engine;
	}

	public void addTerrain(EntityShell <? extends ITerrainMap> terrain)
	{
		this.terrain = terrain;
	}
	
	/**
	 * {@inheritDoc}
	 * Initializes physics engine.
	 */
	public void init(GL gl)
	{
		super.init( gl );
		
		if(engine != null)
			engine.init();
		
		terrain.getLook().init( gl, terrain.getEssence() );
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
	
	public void display( GL gl, double time, RenderingContext context)
	{
		terrain.render( gl, time, context );
		super.display( gl, time, context );
	}
	
	public void animate(double time)
	{
//		boolean changePending = false;
		
		// running physics:
		if(engine != null)
			engine.calculate(time);
		veilTime += time;
		Vector2D refPoint;
		
		terrain.behave( time, true );
		// TODO: no control on order of executions
		for(IEntity entity : getEntities())
		{
			
			if(entity.behave(time, true))
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
					entity.getSensor().clear();
					getEntityIndex().query(entity.getSensor(), refPoint.x(), refPoint.y(), entity.getSensor().getSensorRadiusSquare());
					if(terrain != null && entity.getSensor().isSenseTerrain())
						terrain.getEssence().query(entity.getSensor(), refPoint.x(), refPoint.y(), entity.getSensor().getSensorRadiusSquare());
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
		if(terrain != null)
			terrain.getLook().destroy( gl, terrain.getEssence() );
		if(engine != null)
		engine.destroy();
	}


	@Override
	protected boolean testEntity(IEntity entity)
	{
		
		return entity.getLook() != null && entity.getBehavior() != null;
	}


	public ITerrainMap getTerrain()
	{
		return terrain.getEssence();
	}


}
