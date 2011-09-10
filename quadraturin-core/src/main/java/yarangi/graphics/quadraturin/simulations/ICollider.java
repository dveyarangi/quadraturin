package yarangi.graphics.quadraturin.simulations;

import java.util.Set;

import yarangi.graphics.quadraturin.terrain.ITerrainMap;
import yarangi.spatial.Area;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.ISpatialSensor;

/**
 * Handles the collision responses.
 * 
 * @author Dve Yarangi
 */
public interface ICollider <K extends IPhysicalObject>
{
	
	/**
	 * Invoked on coarse collision detection stage.
	 * @param e1
	 * @param e2
	 * @return true, if no further processing of source collisions is required.
	 */
	public boolean collide(K source, IPhysicalObject target); 
	
	/**
	 * Registers a collision handler for specific object type.
	 * @param _class
	 * @param agentCollider
	 */
	public void registerHandler(Class<? extends IPhysicalObject> _class, ICollisionHandler<K> agentCollider);
	
	/**
	 * Iterates over specified area, reporting fully or partially fitting objects in index. 
	 * {@link ISpatialSensor} must implement object observing logic. They can also modify 
	 * the sensed objects, proving that measurements are just a set of observations that 
	 * reduce uncertainty where the result is expressed as a quantity. One could also say that a 
	 * measurement is the collapse of the wavefunction.
	 * 
	 * Nevertheless, query terminates if {@link ISpatialSensor#objectFound(IAreaChunk, ISpatialObject)} returns true.
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	public ISpatialSensor <K> query(ISpatialSensor <K> sensor, Area area);

	public Set<K> getPhysicalEntities();

}
