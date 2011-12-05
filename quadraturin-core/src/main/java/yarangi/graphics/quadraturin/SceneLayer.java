package yarangi.graphics.quadraturin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.objects.Entity;
import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialSensor;
import yarangi.spatial.SpatialIndexer;

/**
 * Provides means to manage lifecycles of {@link Entity} objects. 
 * Also provides {@link SpatialIndexer} to allow object picking and other
 * location-based interactions. 
 * 
 * @author Dve Yarangi
 */
public abstract class SceneLayer <K extends ILayerObject>
{

	private int width, height;
	
	private List <K> entities = new ArrayList <K> (100); 
		
/*		new TreeSet <K> (
			new Comparator <K> () {
				@Override
				public int compare(K o1, K o2)
				{
					return (int)((o1.getLook().getPriority() - o2.getLook().getPriority()) * 1000f);
				}
	}); */
	/**
	 * Indexes the object's locations
	 */
	private SpatialIndexer <K> indexer;

	/**
	 * Queue of entities waiting to be added to the veil.
	 */
	private Queue <K> bornEntities = new LinkedList<K> ();

	/**
	 * Queue of dead entities to be cleaned up.
	 */
	private Queue <K> deadEntities = new LinkedList <K> ();
	
	private Logger log = Q.structure;
	
//	private SetSensor <ISpatialObject> clippingSensor = new SetSensor<ISpatialObject>();
	/**
	 * 
	 */
	public SceneLayer(int width, int height, SpatialIndexer <K> indexer)
	{
		this.indexer = indexer;
		
		this.width = width;
		this.height = height;
	}

	public SpatialIndexer <K> getEntityIndex() { return indexer; }

	protected List <K> getEntities()
	{
		return entities;
	}
	/**
	 * Initializes the scene. 
	 * @param gl
	 * @throws SceneException
	 */
	public void init(GL gl, IRenderingContext context)
	{
	}
	
	public void destroy(GL gl, IRenderingContext context)
	{
	}
	
	/**
	 * Displays the entirety of entities in this scene for one scene animation frame.
	 * Also handles the decomposition of newly created and oldly dead entities.
	 * @param gl
	 * @param time scene frame time
	 * @param context
	 */
	public void display(GL gl, double time, IRenderingContext context) 
	{	
		// injecting new entities
		while(!bornEntities.isEmpty())
		{
			K born = bornEntities.poll();
			born.init( gl, context );
			entities.add(born);
			
			
			if(born.isIndexed())
				indexer.add(born.getArea(), born);
		}
		
		while(!deadEntities.isEmpty())
		{
			K dead = deadEntities.poll();
			dead.destroy( gl, context );
			if(dead.isIndexed())
			{
				indexer.remove(dead);
				entities.remove(dead);
			}
		}
		
		IVeil veil;
//		System.out.println(entities.size() + " : " + indexer.size());
//			ISpatialSensor <SceneEntity> clippingSensor = new ClippingSensor(gl, time, context);
//			getEntityIndex().query(clippingSensor, new AABB(0, 0, Math.max(viewPoint.getPortWidth(), viewPoint.getPortHeight()), 0));
//			System.out.println(Math.max(viewPoint.getPortWidth(), viewPoint.getPortHeight()));
			// TODO: do the clipping already, you lazy me!
//			System.out.println("BEGIN ======================================================");
			for(K entity : entities)
			{
				veil = entity.getLook().getVeil();
				if(veil == null)
					veil = IVeil.ORIENTING;
//				System.out.println(entity + " : " + entity.getLook() + " : " + entity.getLook().getVeil());
				
				veil.weave( gl, entity, context );
				entity.render( gl, time, context );
				veil.tear( gl );
			}
//			System.out.println("Total " + entities.size() + " entities rendered.");
//			root.display(gl, time, context);
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

//		if(entity.getLook() == null)
//			throw new IllegalArgumentException("Entity look cannot be null.");
//		if(entity.getBehavior() == null)
//			throw new IllegalArgumentException("Entity behavior cannot be null.");
//		if(entity.getAABB() == null)
//			throw new IllegalArgumentException("Entity AABB bracket cannot be null.");
		
		if(testEntity(log, entity))
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

	public class ClippingSensor implements ISpatialSensor <K> 
	{
		private GL gl;
		private double time;
		private IRenderingContext context;
		
		public ClippingSensor(GL gl, double time, IRenderingContext context)
		{
			super();
			this.gl = gl;
			this.time = time;
			this.context = context;
		}
		@Override
		public boolean objectFound(IAreaChunk chunk, K entity)
		{
			entity.render(gl, time, context);
			
			return false;
		}
		
		@Override
		public void clear() {}
		
	}
	

	protected boolean testEntity(Logger logger, K entity) 
	{
		boolean test = true;
		if(entity.getLook() == null) {
			log.warn( "Entity [" + entity + "] must define look object." );
			test = false;
		}
		
		return test;

	}
}
