package yarangi.graphics.quadraturin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.math.Vector2D;
import yarangi.spatial.ISpatialFilter;
import yarangi.spatial.ITileMap;
import yarangi.spatial.PickingSensor;
import yarangi.spatial.SpatialHashMap;

import com.spinn3r.log5j.Logger;

public class WorldLayer extends SceneLayer <IEntity> 
{

	
	private final List <IEntity> entities = new ArrayList <IEntity> (100); 
	
	private IPhysicsEngine <IEntity> engine;
	
	private double layerTime;
	
	private EntityShell <? extends ITileMap > terrain;
	
	private final Logger log = Logger.getLogger("q-world");
	
	public static final double CURSOR_PICK_SPAN = 5;
	
	/**
	 * Queue of entities waiting to be initialized.
	 */
	private final Queue <IEntity> bornEntities = new LinkedList<IEntity> ();

	/**
	 * Queue of dead entities to be cleaned up.
	 */
	private final Queue <IEntity> deadEntities = new LinkedList <IEntity> ();

	
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
		addEntity( terrain );
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
		
//		if(terrain != null)
//			terrain.behave( time, true );
		

		
		while(!bornEntities.isEmpty())
		{
			IEntity born = bornEntities.poll();
			entities.add( born );
		}

		// TODO: no control on order of executions
		for(IEntity entity : entities)
		{
			
			if (!entity.isAlive())
			{   // scheduling for removal:
				removeEntity(entity);
				
				continue;
			}
			
			if(entity.behave(time, true))
			{   
				if(entity.isIndexed())
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
					getEntityIndex().queryRadius(entity.getSensor(), refPoint.x(), refPoint.y(), radiusSquare);
					if(terrain != null && entity.getSensor().isSenseTerrain())
						terrain.getEssence().queryRadius(entity.getSensor(), refPoint.x(), refPoint.y(), radiusSquare);
				}
			}
			

		}
		
		while(!deadEntities.isEmpty())
		{
			IEntity dead = deadEntities.poll();
			entities.remove( dead );
			super.removeEntity( dead );
		}


	}
	
	@Override
	public void addEntity(IEntity entity) 
	{	
//		log.trace( "Entity %s is being added.", entity );
//		if(entity.getLook() == null)
//			throw new IllegalArgumentException("Entity look cannot be null.");
//		if(entity.getBehavior() == null)
//			throw new IllegalArgumentException("Entity behavior cannot be null.");
//		if(entity.getAABB() == null)
//			throw new IllegalArgumentException("Entity AABB bracket cannot be null.");
		
		super.addEntity( entity );
		
		bornEntities.add(entity);
		
//		if(testEntity(entity))
//			bornEntities.add(entity);
	}
	
	/**
	 * Removes the entity from the scene.
	 * @param entity
	 */
	@Override
	public void removeEntity(IEntity entity)
	{
		if(entity == null)
			throw new IllegalArgumentException("Entity cannon be null.");
//		log.trace( "Entity %s is being removed.", entity );
		deadEntities.add( entity );
		
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
		getEntityIndex().queryAABB(sensor, worldLocation.x(), worldLocation.y(), CURSOR_PICK_SPAN, CURSOR_PICK_SPAN);
		
		IEntity entity = sensor.getObject();
		if(entity == null)
			return null;
		
		
		return entity;
	}


}
