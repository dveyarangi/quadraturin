package yar.quadraturin;

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
	 * @param newScene - current scene.
	 */
	public void sceneChanged(Scene newScene);
}
