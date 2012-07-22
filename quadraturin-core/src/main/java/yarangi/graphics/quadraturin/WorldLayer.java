package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.math.Vector2D;
import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialFilter;
import yarangi.spatial.ITileMap;
import yarangi.spatial.PickingSensor;
import yarangi.spatial.SpatialHashMap;

import com.spinn3r.log5j.Logger;

public class WorldLayer extends SceneLayer <IEntity> 
{

	private IPhysicsEngine <IEntity> engine;
	
	private double layerTime;
	
	private EntityShell <? extends ITileMap > terrain;
	
	private final Logger log = Logger.getLogger("q-world");
	
	public static final double CURSOR_PICK_SPAN = 5;

	
	public WorldLayer(int width, int height) 
	{ 
//		super(new SpatialHashMap<ISpatialObject>(100, 10, width, height));
		super(width, height);
		
		setEntityIndex( new SpatialHashMap	<IEntity>(width*height/10, 10, width, height) );
		
		log.debug("Allocated " + width*height/10 + " cells.");
	}
	

	public final IPhysicsEngine <IEntity> getPhysicsEngine() 
	{
		return engine; 
	}
	
	/**
	 * @param engine
	 * @param collide
	 */
	public final void setPhysicsEngine(IPhysicsEngine <IEntity> engine)
	{
		this.engine = engine;
	}

	public void addTerrain(EntityShell <? extends ITileMap> terrain)
	{
		this.terrain = terrain;
	}
	
	/**
	 * {@inheritDoc}
	 * Initializes physics engine.
	 */
	@Override
	public void init(GL gl, IRenderingContext context)
	{
		super.init( gl, context );
		
		if(engine != null)
			engine.init();
		if(terrain != null)
			terrain.getLook().init( gl, terrain.getEssence(), context );
	}
	
	/**
	 * @param gl
	 * @deprecated Move this stuff to {@link IGraphicsPlugin#preRender(GL, IRenderingContext)} when needed
	 */
	@Deprecated
	public void preDisplay(GL gl) {}
	
	/**
	 * @param gl
	 * @deprecated Move this stuff to {@link IGraphicsPlugin#preRender(GL, IRenderingContext)} when needed
	 */
	@Deprecated
	public void postDisplay(GL gl) {}
	
	@Override
	public void display( GL gl, IRenderingContext context)
	{
		if(terrain != null)
			terrain.render( gl, context );
		
		super.display( gl, context );
	}
	
	@Override
	public void animate(double time)
	{
//		boolean changePending = false;
		
		// running physics:
		if(engine != null)
			engine.calculate(time);
		layerTime += time;
		Vector2D refPoint;
		
		if(terrain != null)
			terrain.behave( time, true );
		// TODO: no control on order of executions
		for(IEntity entity : getEntities())
		{
			
			if (!entity.isAlive())
			{   // scheduling for removal:
				removeEntity(entity);
				continue;
			}
			
			if(entity.behave(time, true))
			{   
				if(entity.getArea() != null)
					getEntityIndex().update(entity.getArea(), entity);
//				changePending = true;
			}
			
			if(entity.getSensor() != null)
			{   // filling world objects in sensor range:
				// TODO: scheduling, perhaps?

				if(entity.getSensor().isSensingNeeded( layerTime ))
				{
					refPoint = entity.getArea().getAnchor();
					// this implementation extracts live entity objects, entity locations thus updated regardless of sensing frequency
					entity.getSensor().clear();
					double radiusSquare = entity.getSensor().getRadius() * entity.getSensor().getRadius();
					getEntityIndex().query(entity.getSensor(), refPoint.x(), refPoint.y(), radiusSquare);
					if(terrain != null && entity.getSensor().isSenseTerrain())
						terrain.getEssence().query(entity.getSensor(), refPoint.x(), refPoint.y(), radiusSquare);
				}
			}
		}
		
	}
	/**
	 * {@inheritDoc}
	 * Stops physics engine.
	 */
	@Override
	public void destroy(GL gl, IRenderingContext context)
	{
		super.destroy( gl, context );
		if(terrain != null)
			terrain.getLook().destroy( gl, terrain.getEssence(), context );
		if(engine != null)
			engine.destroy();
	}


	@Override
	protected boolean testEntity(IEntity entity)
	{
		boolean test = super.testEntity(entity);
		
		if(entity.getBehavior() == null) {
			log.warn( "Entity [" + entity + "] must define behavior aspect." );
			test = false;
		}
		
		return test;
	}


	public <T> ITileMap <T> getTerrain()
	{
		return  terrain == null ? null : (ITileMap <T> )terrain.getEssence();
	}


	public ILayerObject processPick(Vector2D worldLocation, ISpatialFilter <IEntity> filter)
	{
		PickingSensor <IEntity> sensor = new PickingSensor <IEntity> (filter);
		getEntityIndex().query(sensor, AABB.createSquare(worldLocation.x(), worldLocation.y(), CURSOR_PICK_SPAN, 0));
		
		IEntity entity = sensor.getObject();
		if(entity == null)
			return null;
		
		
		return entity;
	}


}
