package yarangi.graphics.quadraturin;

/**
 * Interface for {@link Scene} change listener.
 * 
 * @author dveyarangi
 *
 */
public interface StageListener 
{
	/**
	 * Handles scene swap.
	 * @param oldScene - previous scene, can be null
	 * @param newScene - current scene.
	 */
	public void sceneChanged(Scene oldScene, Scene newScene);
}
