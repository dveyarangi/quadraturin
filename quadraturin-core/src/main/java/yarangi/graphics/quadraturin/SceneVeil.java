package yarangi.graphics.quadraturin;

import java.util.LinkedList;
import java.util.Queue;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.CompositeSceneEntity;
import yarangi.graphics.quadraturin.objects.DummyEntity;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.graphics.quadraturin.objects.IVeilOverlay;
import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialObject;
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
	
	/**
	 * Scene tree root.
	 */
	private CompositeSceneEntity root = new DummyEntity(new AABB(0,0,0,0));
	
	/**
	 * Indexes the object's locations
	 */
	private SpatialIndexer <ISpatialObject> indexer;

	/**
	 * Queue of entites waitong to be added to the veil.
	 */
	private Queue <SceneEntity> bornEntities = new LinkedList<SceneEntity> ();

	/**
	 * Queue of dead entities to be cleaned up.
	 */
	private Queue <SceneEntity> deadEntities = new LinkedList <SceneEntity> ();
	
	private IVeilOverlay veilEffect;
	
	/**
	 * 
	 */
	public SceneVeil(SpatialIndexer <ISpatialObject> indexer)
	{
		this.indexer = indexer;

	}
	
	public void setOverlayEffect(IVeilOverlay effect)
	{
		this.veilEffect = effect;
	}
	

	public SpatialIndexer <ISpatialObject> getEntityIndex() { return indexer; }
	
	/**
	 * 
	 * @return Root entity of the scene for object tree traversal.
	 */
	protected SceneEntity getSceneRoot() 
	{
		return root;
	}

	/**
	 * Initializes the scene. 
	 * @param gl
	 * @throws SceneException
	 */
	public void init(GL gl)
	{
		root.init(gl);
		
		if(veilEffect != null)
			veilEffect.init(gl, root);
	}
	
	public void destroy(GL gl)
	{
		root.destroy(gl);
		if(veilEffect != null)
			veilEffect.destroy(gl, root);
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
			root.addChild(born);
			if(born.getAABB() != null)
				indexer.add(born);
		}
		
		while(!deadEntities.isEmpty())
		{
			SceneEntity dead = deadEntities.poll();
//			System.out.println("dead: " + dead.getId());
			dead.destroy(gl);
			if(dead.getAABB() != null)
			indexer.remove(dead);
			root.removeChild(dead);
		}
		
		if(veilEffect == null)
		{
			root.display(gl, time, context);
		}
		else
		{
			veilEffect.render(gl, time, root, context);
		}
	}

	/**
	 * 
	 */
	public void preAnimate() { }
	
	public boolean animate(double time)
	{
		boolean changePending = false;
		// TODO: no control on order of executions
		for(SceneEntity entity : root.getChildren())
		{
			if(entity.behave(time, true))
			{
				indexer.update(entity);
				changePending = true;
			}
			
			if (!entity.isAlive())
			{
				removeEntity(entity);
			}
		}
		
		return changePending;
	}
	
	public void postAnimate() { }


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

	
}
