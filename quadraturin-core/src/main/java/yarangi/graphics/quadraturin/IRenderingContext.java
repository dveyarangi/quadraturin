package yarangi.graphics.quadraturin;

import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.plugin.IPluginFactory;

/**
 * Holds some rendering properties, provided to {@link Look} methods
 * TODO: maybe it should have a brighter future, encapsulating the GL object and 
 * becoming a real renderer.
 * 
 * @author dveyarangi
 */
public interface IRenderingContext 
{
	/**
	 * Marks the scope of rendering.
	 * @return
	 */
	public boolean isForEffect();
	
	/**
	 * @return current user viewpoint.
	 */
	public IViewPoint getViewPoint();

	/**
	 * Retrieves plugin by name.
	 * Note: The left side type for this expression is on the user's conscience.
	 * @param name
	 * @return
	 */
	public <T extends IPluginFactory> T getPlugin(String name);
}
