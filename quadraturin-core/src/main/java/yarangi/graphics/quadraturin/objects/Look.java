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
	
	public void init(GL gl, An entity);
	
	/**
	 * Renders this entity.
	 * @param gl
	 * @param time
	 */
	public void render(GL gl, double time, An entity, RenderingContext context);

	public void destroy(GL gl, An entity);

	
	public boolean isCastsShadow();
}
