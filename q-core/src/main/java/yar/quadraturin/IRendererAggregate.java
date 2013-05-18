package yar.quadraturin;

/**
 * package interface to set up rendering context scene-dependent values.
 * 
 * @author dveyarangi
 */
interface IRendererAggregate extends IRenderingContext
{

	/**
	 * Sets Scene's camera
	 */
	void setViewPoint(Camera2D camera);

	/**
	 * Sets world renderables
	 */
	void setWorldLookManager(LookManager man);
	
	/**
	 * Sets UI renderables
	 */
	void setUILookManager(LookManager man);

}
