package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.IVeilEntity;

/**
 * Represents a rendering space, that may be customized for 
 * different {@link Look} rendering modes.
 * 
 * TODO: consider (merging/replacing/making difference explicit) with {@link IRenderingContext}
 * @author dveyarangi
 *
 */
public interface IVeil
{
	/**
	 * Default veil
	 */
	public static final IVeil ORIENTING = new OrientingVeil();
	
	/**
	 * Marks start of the veil rendering.
	 * @param gl
	 * @param entity
	 * @param context
	 */
	public void weave(GL gl, IVeilEntity entity, IRenderingContext context);
	
	/**
	 * Marks end of the veil rendering.
	 * @param gl
	 */
	public void tear(GL gl);

}
