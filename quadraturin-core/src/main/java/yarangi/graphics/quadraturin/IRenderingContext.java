package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.graphics.quadraturin.objects.IVisible;

/**
 * Holds some rendering properties for use in {@link ILook} methods
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
	
	/**
	 * TODO: use it in {@link ILook}-s or delete it.
	 * 
	 * @return OpenGL renderer object
	 */
	public GL gl();
	
	/**
	 * Restores default blending methods
	 * TODO: this is because I am too lazy to find appropriate gl.glPushAttrib() argument
	 * @param gl
	 */
	public void setDefaultBlendMode(GL gl);
	
	public float getFrameLength();

	public void addVisible(IVisible entity);
	
	public void removeVisible(IVisible entity);

	public ViewPoint2D getViewPoint();

}
