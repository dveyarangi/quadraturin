package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;

/**
 * TODO: add look init and destroy functions (for GL mesh lists, textures and such)
 * 
 * @author Dve Yarangi
 */
public interface Look <An>
{
	/**
	 * Initiate graphics (textures, meshes, etc.)
	 * @param gl
	 * @param entity
	 */
	public void init(GL gl, An entity);
	
	/**
	 * Renders this entity.
	 * @param gl
	 * @param time
	 */
	public void render(GL gl, double time, An entity, RenderingContext context);

	public void destroy(GL gl, An entity);

	/**
	 * Defines look visual priority (looks with priority closer to 0 will override looks below.)
	 * Ranges from 0 to {@link -Quad2DController#MAX_DEPTH_PRIORITY}
	 * TODO: move to Area?
	 * @return
	 */
	public float getPriority();
	
	/**
	 * @return true, if this look should be considered opaque for lighting calculations.
	 */
	public boolean isCastsShadow();

}
