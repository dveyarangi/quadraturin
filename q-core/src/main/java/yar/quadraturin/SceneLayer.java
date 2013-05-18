package yar.quadraturin;

import yar.quadraturin.objects.Entity;
import yar.quadraturin.objects.ILayerObject;
import yar.quadraturin.objects.IVisible;
import yar.quadraturin.ui.Overlay;
import yarangi.spatial.Area;
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

	/**
	 * Indexes the object's locations
	 */
	protected SpatialIndexer <K> indexer;

	/**
	 * Manager for visual components of objects.
	 */
	private LookManager looks;
	
	
	private final Logger log = Q.structure;
	
	/**
	 * 
	 */
	public SceneLayer(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		this.looks = new LookManager();
	}

	public SpatialIndexer <K> getEntityIndex() { return indexer; }
	
	protected LookManager getLooks() { return looks; }
	
	protected void setEntityIndex(SpatialIndexer <K> indexer)
	{
		this.indexer = indexer;
	}

	/**
	 * Initializes the scene. 
	 * @param gl
	 * @throws SceneException
	 */
/*	public void init(IRenderingContext context)
	{
		this.context = context;
	}
	
	public void destroy(IRenderingContext context)
	{
	}*/

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
				looks.addVisible(visible);
		}
		
		if(entity.isIndexed())
			indexer.add(entity.getArea(), entity);

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
			looks.removeVisible((IVisible) entity);
	}


	public int getWidth() {return width; }
	public int getHeight() {return height; }

	protected boolean testEntity(K entity)
	{
		return true;
	}
}
