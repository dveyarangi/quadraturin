package yar.quadraturin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import yar.quadraturin.objects.EntityShell;
import yar.quadraturin.objects.IEntity;
import yar.quadraturin.objects.ILayerObject;
import yar.quadraturin.simulations.IPhysicsEngine;
import yar.quadraturin.terrain.ITerrain;
import yarangi.math.IVector2D;
import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialFilter;
import yarangi.spatial.ITileMap;
import yarangi.spatial.PickingSensor;
import yarangi.spatial.SpatialHashMap;

import com.spinn3r.log5j.Logger;

/**
 * This is the simulation-related subset of {@link Scene} features.
 * 
 * This class manages life-cycles of {@link IEntity} implementations, and 
 * provides physics and collision map.
 * 
 * 
 * 
 * @author dveyarangi
 *
 */
public class WorldLayer extends SceneLayer <IEntity> 
{

	private static String NAME = "q-world";
	
	/**
	 * Simulation-world time reference.
	 */
	private double layerTime;
	
	/**
	 * List of managed entities
	 */
	private final List <IEntity> entities = new ArrayList <IEntity> (100); 
	
	/**
	 * Physics engine
	 */
	private IPhysicsEngine <IEntity> engine;
	
	/**
	 * Collision map
	 */
	private EntityShell <? extends ITileMap<ITerrain>> terrain;
	
	/**
	 * Yes
	 */
	private final Logger log = Logger.getLogger(NAME);
	
	/**
	 * 
	 */
	public static final float CURSOR_PICK_SPAN = 5;
	
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
		super(width, height);
		
		
		// creating index for world entities:
		setEntityIndex( new SpatialHashMap	<IEntity>( NAME, width*height/10, 10, width, height) );
		
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

	public void addTerrain(EntityShell <? extends ITileMap<ITerrain>> terrain)
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
		IVector2D refPoint;
		
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
			
			if(entity.getEntitySensor() != null)
			{   // filling world objects in sensor range:
				// TODO: scheduling, perhaps?

				if(entity.getEntitySensor().isSensingNeeded( layerTime ))
				{
					refPoint = entity.getArea().getAnchor();
					// this implementation extracts live entity objects, entity locations thus updated regardless of sensing frequency
					entity.getEntitySensor().clear();

					getEntityIndex().queryRadius(entity.getEntitySensor(), (float)refPoint.x(), (float)refPoint.y(), (float)entity.getEntitySensor().getRadius());

				}
			}
			if(entity.getTerrainSensor() != null && terrain != null)
			{   // filling world objects in sensor range:
				// TODO: scheduling, perhaps?

				if(entity.getTerrainSensor().isSensingNeeded( layerTime ))
				{
					refPoint = entity.getArea().getAnchor();
					// this implementation extracts live entity objects, entity locations thus updated regardless of sensing frequency
					entity.getTerrainSensor().clear();
					
					terrain.getEssence().queryRadius(entity.getTerrainSensor(), (float)refPoint.x(), (float)refPoint.y(), (float)entity.getTerrainSensor().getRadius());

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
		if(deadEntities.contains( entity ))
			deadEntities.remove(entity);
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
		if(bornEntities.contains( entity ))
			bornEntities.remove(entity);
		
//		AABB.release( (AABB)entity.getArea() );
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

	
	public <T extends ITileMap<ITerrain>> T getTerrain()
	{
		return  terrain == null ? null : (T)terrain.getEssence();
	}


	public ILayerObject processPick(IVector2D worldLocation, PickingSensor.Mode mode, ISpatialFilter <IEntity> filter)
	{
		PickingSensor <IEntity> sensor = new PickingSensor <IEntity> (worldLocation.x(), worldLocation.y(), mode, filter);
		getEntityIndex().queryAABB(sensor, (float)worldLocation.x(), (float)worldLocation.y(), CURSOR_PICK_SPAN, CURSOR_PICK_SPAN);
		IEntity entity = sensor.getObject();
//		System.out.println("picked: " + entity);
		if(entity == null)
			return null;
		
		
		return entity;
	}


}
