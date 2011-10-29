package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;

/**
 * Generic graphics interface for {@link IEntity}-s; implements entity view aspect.
 * 
 * Can be shared between different entities (in this case it should not have modifiable state).
 * 
 * @author Dve Yarangi
 */
public interface Look <An>
{
	public static final int DEFAULT_BUFFER = 0;
	
	/**
	 * Initiate graphics (textures, meshes, etc.)
	 * @param gl
	 * @param entity
	 * @param context some global rendering properties
	 */
	public void init(GL gl, An entity, IRenderingContext context);
	
	/**
	 * Renders this entity.
	 * @param gl
	 * @param entity entity to be rendered
	 * @param time scene time for animation
	 * @param context some global rendering properties
	 */
	public void render(GL gl, double time, An entity, IRenderingContext context);

	/**
	 * Invoked on entity deconstruction, should implement cleanup logic.
	 * @param gl
	 * @param entity
	 * @param context some global rendering properties
	 */
	public void destroy(GL gl, An entity, IRenderingContext context);

	/**
	 * Defines look visual priority (looks with priority closer to 0 will override looks below.)
	 * Ranges from {@link Quad2DController#MIN_DEPTH_PRIORITY} to {@link Quad2DController#MAX_DEPTH_PRIORITY}
	 * TODO: move to Area?
	 * @return
	 */
	public float getPriority();
	
	/**
	 * @return true, if this look should be considered opaque for lighting calculations.
	 */
	public boolean isCastsShadow();

	/**
	 * Can be used to set special rendering environment for the look; null if default veil is used
	 * @return
	 */
	public IVeil getVeil();

}
