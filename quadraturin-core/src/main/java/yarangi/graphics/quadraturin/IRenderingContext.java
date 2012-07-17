package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.ILook;

/**
 * Holds some rendering properties, provided to {@link ILook} methods
 * TODO: maybe it should have a brighter future, encapsulating the GL object and 
 * becoming a real renderer.
 * 
 * @author dveyarangi
 */
public interface IRenderingContext 
{
	
	/**
	 * @return current user {@link ViewPort}.
	 */
	public ViewPort getViewPort();

	/**
	 * Retrieves plugin by name.
	 * Note: The left side type for this expression is on the user's conscience.
	 * @param name
	 * @return
	 */
	public <T> T getPlugin(String name);
	
	public GL gl();
	
	public void setDefaultBlendMode(GL gl);
}
