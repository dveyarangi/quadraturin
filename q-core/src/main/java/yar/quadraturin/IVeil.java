package yar.quadraturin;

import javax.media.opengl.GL;

import yar.quadraturin.objects.ILook;
import yar.quadraturin.objects.IVisible;

/**
 * Represents a rendering space, that may be customized for 
 * different {@link ILook} rendering modes.
 * 
 * TODO: consider (merging/replacing/making difference explicit) with {@link IRenderingContext}
 * @author dveyarangi
 *
 */
public interface IVeil
{
	
	/**
	 * Marks start of the veil rendering.
	 * @param gl
	 * @param entity
	 * @param context
	 */
	public void weave(GL gl, IRenderingContext context);
	
	/**
	 * Marks end of the veil rendering.
	 * @param gl
	 */
	public void tear(GL gl);

}