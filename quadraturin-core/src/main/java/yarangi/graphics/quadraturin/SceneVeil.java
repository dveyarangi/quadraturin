package yarangi.graphics.quadraturin;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.IVeilOverlay;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.math.Vector2D;
import yarangi.spatial.SpatialIndexer;

/**
 * Provides means to manage lifecycles of {@link SceneEntity} objects. 
 * Also provides {@link SpatialIndexer} to allow object picking and other
 * location-based interactions. 
 * 
 * @author Dve Yarangi
 */
public abstract class SceneVeil
{

	private int width, height;
	
	private Set <SceneEntity> entities = new HashSet <SceneEntity> ();
	
	/**
	 * Indexes the object's locations
	 */
	private SpatialIndexer <SceneEntity> indexer;

	/**
	 * Queue of entites waitong to be added to the veil.
	 */
	private Queue <SceneEntity> bornEntities = new LinkedList<SceneEntity> ();

	/**
	 * Queue of dead entities to be cleaned up.
	 */
	private Queue <SceneEntity> deadEntities = new LinkedList <SceneEntity> ();
	
	private IVeilOverlay veilEffect;
	
	
//	private SetSensor <ISpatialObject> clippingSensor = new SetSensor<ISpatialObject>();
	/**
	 * 
	 */
	public SceneVeil(int width, int height, SpatialIndexer <SceneEntity> indexer)
	{
		this.indexer = indexer;
		
		this.width = width;
		this.height = height;

	}
	
	public void setOverlayEffect(IVeilOverlay effect)
	{
		this.veilEffect = effect;
	}
	

	public SpatialIndexer <SceneEntity> getEntityIndex() { return indexer; }


	/**
	 * Initializes the scene. 
	 * @param gl
	 * @throws SceneException
	 */
	public void init(GL gl)
	{
		for(SceneEntity entity : entities)
			entity.init(gl);
		if(veilEffect != null)
			veilEffect.init(gl, this);
	}
	
	public void destroy(GL gl)
	{
		for(SceneEntity entity : entities)
			entity.destroy(gl);
		if(veilEffect != null)
			veilEffect.destroy(gl, this);
	}
	
	/**
	 * Displays the entirety of entities in this scene.
	 * @param gl
	 * @param time
	 * @param pushNames
	 */
	public void display(GL gl, double time, RenderingContext context) 
	{	
		// injecting new entities
		while(!bornEntities.isEmpty())
		{
			SceneEntity born = bornEntities.poll();
			born.init(gl);
			entities.add(born);
			if(born.getArea() != null)
				indexer.add(born.getArea(), born);
		}
		
		while(!deadEntities.isEmpty())
		{
			SceneEntity dead = deadEntities.poll();
			dead.destroy(gl);
			if(dead.getArea() != null)
			{
				indexer.remove(dead);
				entities.remove(dead);
			}
		}
		
//		System.out.println(entities.size() + " : " + indexer.size());
		if(veilEffect == null)
		{

//			SetSensor <ISpatialObject> clipped = new SetSensor<ISpatialObject>();
//			getEntityIndex().query(clipped, new AABB(0, 0, 100, 0));
			// TODO: do the clipping already, you lazy me!
//			System.out.println("BEGIN ======================================================");
			for(SceneEntity entity : entities)
			{
//				System.out.println(entity);
				entity.display(gl, time, context);
			}
//			root.display(gl, time, context);
		}
		else
		{
//			veilEffect.render(gl, time, root, context);
		}
	}
	
	public void animate(double time)
	{
//		boolean changePending = false;
		// TODO: no control on order of executions
		Vector2D refPoint;
		for(SceneEntity entity : entities)
		{
			
			if(entity.behave(time, true))
			{
				if(entity.getArea() != null)
					indexer.update(entity.getArea(), entity);
//				changePending = true;
			}
			
			if (!entity.isAlive())
			{
				removeEntity(entity);
				continue;
			}
			
			if(entity.getSensor() != null)
			{
				refPoint = entity.getArea().getRefPoint();
				entity.getSensor().clearSensor();
				indexer.query(entity.getSensor(), refPoint.x(), refPoint.y(), entity.getSensor().getSensorRadiusSquare());
			}
		}
		
		
//		if(engine != null)
		
//		return changePending;
	}


	/**
	 * Adds an entity to the injection queue. The entity will be inserted into scene
	 * on the next iteration of animation loop.
	 * @param entity
	 */
	public void addEntity(SceneEntity entity) 
	{	

		// TODO: perhaps children of SceneEntities shall be added here also
//		if(entity.getLook() == null)
//			throw new IllegalArgumentException("Entity look cannot be null.");
//		if(entity.getBehavior() == null)
//			throw new IllegalArgumentException("Entity behavior cannot be null.");
//		if(entity.getAABB() == null)
//			throw new IllegalArgumentException("Entity AABB bracket cannot be null.");
		
		bornEntities.add(entity);
	}
	
	/**
	 * Removes the entity from the scene.
	 * @param entity
	 */
	public void removeEntity(SceneEntity entity)
	{
		deadEntities.add(entity);
	}


	public int getWidth() {return width; }
	public int getHeight() {return height; }

}
