package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.SceneLayer;
import yarangi.spatial.Area;
import yarangi.spatial.ISpatialObject;


/**
 * A renderable entity that is managed by {@link SceneLayer} container.
 * 
 * @author dveyarangi
 */
public interface ILayerObject extends ISpatialObject
{

	/**
	 * How the object looks.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public abstract ILook getLook();	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Area getArea();

	/**
	 * Alive flag, for collection of dead entities.
	 * @return
	 */
	public abstract boolean isAlive();

	/**
	 * Marks entity "dead", forcing it's disposal at the start of the next rendering cycle.
	 */
	public abstract void markDead();
	
	/**
	 * Defines either the entity needs to be added to spatial indexer in corresponding layer.
	 * @return
	 */
	public boolean isIndexed();


	public void init(GL gl, IRenderingContext context);
	public void render(GL gl, IRenderingContext context);
	public void destroy(GL gl, IRenderingContext context);

}