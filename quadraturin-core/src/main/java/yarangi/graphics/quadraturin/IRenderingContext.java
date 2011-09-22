package yarangi.graphics.quadraturin;

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
}
