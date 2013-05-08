package yar.quadraturin;

import javax.media.opengl.GL;

import yar.quadraturin.objects.Entity;
import yar.quadraturin.objects.ILayerObject;
import yar.quadraturin.objects.IVisible;
import yarangi.spatial.SpatialIndexer;

import com.spinn3r.log5j.Logger;

/**
 * Aggregates and manages life-cycle of {@link Entity} objects. 
 * Also provides {@link SpatialIndexer} to allow object picking and other
 * location-based interactions (used automatically if entity has {@link Area}) 
 * 
 * @author Dve Yarangi
 */
public abstract class SceneLayer <K extends ILayerObject>
{

	private final int width, height;
		
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

	
	private final Logger log = Q.structure;
	
	private IRenderingContext context;
	
//	private SetSensor <ISpatialObject> clippingSensor = new SetSensor<ISpatialObject>();
	/**
	 * 
	 */
	public SceneLayer(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.context = Q.getRenderingContext();
	}

	public SpatialIndexer <K> getEntityIndex() { return indexer; }
	
	protected void setEntityIndex(SpatialIndexer <K> indexer)
	{
		this.indexer = indexer;
	}

	/**
	 * Initializes the scene. 
	 * @param gl
	 * @throws SceneException
	 */
	public void init(GL gl, IRenderingContext context)
	{
		this.context = context;
	}
	
	public void destroy(GL gl, IRenderingContext context)
	{
	}

	public abstract void animate(double time);


	/**
	 * Adds an entity to the injection queue. The entity will be inserted into scene
	 * on the next iteration of animation loop.
	 * @param entity
	 */
	public void addEntity(K entity) 
	{	
		if(entity == null)
			throw new IllegalArgumentException("Entity cannot be null.");
//		if(entity.getLook() == null)
//			throw new IllegalArgumentException("Entity look cannot be null.");
//		if(entity.getBehavior() == null)
//			throw new IllegalArgumentException("Entity behavior cannot be null.");
//		if(entity.getAABB() == null)
//			throw new IllegalArgumentException("Entity AABB bracket cannot be null.");
		
		if(entity instanceof IVisible) {
			IVisible visible = (IVisible) entity;
			if(visible.getLook() == null)
				log.debug("Entity [" + entity + "] have no look.");
			else
				context.addVisible(visible);
		}
		
		if(entity.isIndexed())
			indexer.add(entity.getArea(), entity);
		
//		if(testEntity(entity))
//			bornEntities.add(entity);
	}
	
	/**
	 * Removes the entity from the scene.
	 * @param entity
	 */
	public void removeEntity(K entity)
	{
		if(entity.isIndexed())
			indexer.remove(entity);
		if(entity instanceof IVisible)
			context.removeVisible((IVisible) entity);
	}


	public int getWidth() {return width; }
	public int getHeight() {return height; }

/*	public class ClippingSensor implements ISpatialSensor <K> 
	{
		private final GL gl;

		private final IRenderingContext context;
		
		public ClippingSensor(GL gl, IRenderingContext context)
		{
			super();
			this.gl = gl;

			this.context = context;
		}
		@Override
		public boolean objectFound(K entity)
		{
			entity.render(gl,context);
			
			return false;
		}
		
		@Override
		public void clear() {}
		
	}*/
	

	protected boolean testEntity(K entity) 
	{
		boolean test = true;
/*		if(entity.getLook() == null) {
			log.warn( "Entity [" + entity + "] must define look object." );
			test = false;
		}*/
		
		return test;

	}
}
