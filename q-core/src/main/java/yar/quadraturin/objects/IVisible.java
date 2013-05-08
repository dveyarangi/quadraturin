package yar.quadraturin.objects;

import javax.media.opengl.GL;

import yar.quadraturin.IRenderingContext;
import yarangi.spatial.Area;

/**
 * Defines requirements for renderable layer object
 * @author dveyarangi
 *
 */
public interface IVisible
{

	/**
	 * Retrieves the looks of this object.
	 * If several IVisibles share the same ILook entities, it will not be destroyed until the last of the sharing entities is dropped.
	 * 
	 * @return
	 */
	public abstract ILook getLook();

	/**
	 * Area that marks perimeter for all rendered features of this entity
	 * @return
	 */
	public abstract Area getArea();

	/**
	 * Called to render this entity; basic implementation just invokes {@link ILook#render(GL, Object, IRenderingContext)},
	 * but customized procedures are sometimes required (see implementations)
	 * 
	 * @param context
	 */
	public void render(IRenderingContext ctx);
}
