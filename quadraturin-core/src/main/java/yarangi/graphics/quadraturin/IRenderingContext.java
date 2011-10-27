package yarangi.graphics.quadraturin;

import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;

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
	 * @return current user viewpoint.
	 */
	public int getScreenWidth();
	public int getScreenHeight();

	/**
	 * Retrieves plugin by name.
	 * Note: The left side type for this expression is on the user's conscience.
	 * @param name
	 * @return
	 */
	public <T extends IGraphicsPlugin> T getPlugin(String name);
}
