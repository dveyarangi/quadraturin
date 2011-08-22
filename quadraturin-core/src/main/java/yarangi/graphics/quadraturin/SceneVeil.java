package yarangi.graphics.quadraturin;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.IVeilEntity;
import yarangi.graphics.quadraturin.objects.IVeilOverlay;
import yarangi.graphics.quadraturin.objects.WorldEntity;
import yarangi.spatial.Area;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialSensor;
import yarangi.spatial.SpatialIndexer;

/**
 * Provides means to manage lifecycles of {@link WorldEntity} objects. 
 * Also provides {@link SpatialIndexer} to allow object picking and other
 * location-based interactions. 
 * 
 * @author Dve Yarangi
 */
public abstract class SceneVeil <K extends IVeilEntity>
{

	private int width, height;
	
	private Set <K> entities = new HashSet <K> ();
	
	/**
	 * Indexes the object's locations
	 */
	private SpatialIndexer <K> indexer;

	/**
	 * Queue of entites waitong to be added to the veil.
	 */
	private Queue <K> bornEntities = new LinkedList<K> ();

	/**
	 * Queue of dead entities to be cleaned up.
	 */
	private Queue <K> deadEntities = new LinkedList <K> ();
	
	private IVeilOverlay veilEffect;
	
//	private SetSensor <ISpatialObject> clippingSensor = new SetSensor<ISpatialObject>();
	/**
	 * 
	 */
	public SceneVeil(int width, int height, SpatialIndexer <K> indexer)
	{
		this.indexer = indexer;
		
		this.width = width;
		this.height = height;
	}
	
	public void setOverlayEffect(IVeilOverlay effect)
	{
		this.veilEffect = effect;
	}
	

	public SpatialIndexer <K> getEntityIndex() { return indexer; }


	protected Set <K> getEntities()
	{
		return entities;
	}
	/**
	 * Initializes the scene. 
	 * @param gl
	 * @throws SceneException
	 */
	public void init(GL gl)
	{
		if(veilEffect != null)
			veilEffect.init(gl, this);
	}
	
	public void destroy(GL gl)
	{
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
			K born = bornEntities.poll();
			born.getLook().init( gl, born );
			entities.add(born);
			if(born.getArea() != null)
				indexer.add(born.getArea(), born);
		}
		
		while(!deadEntities.isEmpty())
		{
			K dead = deadEntities.poll();
			dead.getLook().destroy( gl, dead );
			if(dead.getArea() != null)
			{
				indexer.remove(dead);
				entities.remove(dead);
			}
		}
		
//		System.out.println(entities.size() + " : " + indexer.size());
		if(veilEffect == null)
		{
//			ISpatialSensor <SceneEntity> clippingSensor = new ClippingSensor(gl, time, context);
//			getEntityIndex().query(clippingSensor, new AABB(0, 0, Math.max(viewPoint.getPortWidth(), viewPoint.getPortHeight()), 0));
//			System.out.println(Math.max(viewPoint.getPortWidth(), viewPoint.getPortHeight()));
			// TODO: do the clipping already, you lazy me!
//			System.out.println("BEGIN ======================================================");
			for(K entity : entities)
			{
				render(gl, time, entity, context);
			}
//			System.out.println("Total " + entities.size() + " entities rendered.");
//			root.display(gl, time, context);
		}
/*		else
		{
			veilEffect.render(gl, time, entities, context);
		}*/
	}
	
	public abstract void animate(double time);


	/**
	 * Adds an entity to the injection queue. The entity will be inserted into scene
	 * on the next iteration of animation loop.
	 * @param entity
	 */
	public void addEntity(K entity) 
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
	public void removeEntity(K entity)
	{
		deadEntities.add(entity);
	}


	public int getWidth() {return width; }
	public int getHeight() {return height; }

	public class ClippingSensor extends HashSet <K> implements ISpatialSensor <K> 
	{
		private GL gl;
		private double time;
		private RenderingContext context;
		
		public ClippingSensor(GL gl, double time, RenderingContext context)
		{
			super();
			this.gl = gl;
			this.time = time;
			this.context = context;
		}
		@Override
		public boolean objectFound(IAreaChunk chunk, K object)
		{
			if(!contains( object ))
			{
				render(gl, time, object, context);
				add(object);
			}
			
			return false;
		}
		
	}
	
	protected void render(GL gl, double time, K entity, RenderingContext context)
	{
		Area area = entity.getArea();
		
		// storing transformation matrix:
		gl.glPushMatrix();
		
		// transforming into entity coordinates:
		gl.glTranslatef((float)area.getRefPoint().x(), (float)area.getRefPoint().y(), 0/* TODO: entity.getLook().getPriority() */);
		gl.glRotatef((float)area.getOrientation(), 0, 0, 1 );
		
		// rendering this entity:
		entity.getLook().render(gl, time, entity, context);
		
		gl.glPopMatrix();

	}

}
