package yar.quadraturin;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.objects.ILook;

/**
 * Holds some rendering properties and methods for use in {@link ILook} methods
 * TODO: maybe it should have a brighter future, encapsulating the GL object and 
 * becoming a real renderer.
 * 
 * Aims to be rendering tools API.
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
	 * @return OpenGL renderer object
	 */
	public GL2 gl();
	
	/**
	 * Restores default blending methods
	 * TODO: this is because I am too lazy to find appropriate gl.glPushAttrib() argument
	 * @param gl
	 */
	public void setDefaultBlendMode(GL gl);
	
	/**
	 * animation time allocated for current animation frame 
	 * @return
	 */
	public float getFrameLength();

	/**
	 * Provides camera properties
	 * @return
	 */
	public Camera2D getCamera();

	
}
