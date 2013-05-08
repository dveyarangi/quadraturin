package yar.quadraturin.objects;

import yar.quadraturin.SceneLayer;
import yarangi.spatial.AABB;
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
	 * {@inheritDoc}
	 */
	@Override
	public AABB getArea();

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


}