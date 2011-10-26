package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.spatial.Area;
import yarangi.spatial.ISpatialObject;


/**
 * A renderable entity that is managed by {@link SceneVeil} container.
 * 
 * @author dveyarangi
 */
public interface ILayerEntity extends ISpatialObject
{

	/**
	 * How the object looks.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public abstract Look getLook();	
	
	/** 
	 * Sets entity's look.
	 * @param look
	 */
	public abstract void setLook(Look<?> look);

	/**
	 * {@inheritDoc}
	 */
	public Area getArea();
	
	/**
	 * Sets entity area span.
	 * @param empty
	 */
	public abstract void setArea(Area area);

	/**
	 * Alive flag, for collection of dead entities.
	 * @return
	 */
	public abstract boolean isAlive();

	/**
	 * Marks entity "dead", forcing it's disposal at the start of the next rendering cycle.
	 */
	public abstract void markDead();


	public void init(GL gl, IRenderingContext context);
	public void render(GL gl, double time, IRenderingContext context);
	public void destroy(GL gl, IRenderingContext context);

}