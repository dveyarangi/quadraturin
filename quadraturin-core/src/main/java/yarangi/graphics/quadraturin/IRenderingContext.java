package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.graphics.quadraturin.objects.IVisible;

/**
 * Holds some rendering properties and methods for use in {@link ILook} methods
 * TODO: maybe it should have a brighter future, encapsulating the GL object and 
 * becoming a real renderer.
 * 
 * Aims to be rendering tools class.
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
	 * Provides 
	 * @return
	 */
	public Camera2D getCamera();

	/** 
	 * Removes an overlay from rendering queue
	 * Entities added to scene and implementing IVisible will be automatically removed.
	 *   
	 * @param entity
	 */
	void removeVisible(IVisible entity);

	/**
	 * Adds visible overlay to rendering queue.
	 * Entities added to scene and implementing IVisible will be automatically added.
	 * @param entity
	 */
	void addVisible(IVisible entity);

	/**
	 * Hints at entity associated with specified look.
	 * If look is used by multiple entities, any one of them will be returned.
	 * @return null, in case no entity found.
	 */
	<K> K getAssociatedEntity(ILook <K>look);

}
