package yarangi.spatial;

import java.util.HashMap;
import java.util.Set;

/**
 * Common interface for spatial indexing data structures.
 * 
 * @param <T> stored element type.
 */
public abstract class SpatialIndexer <K extends ISpatialObject>
{
	
	/**
	 * Id to location mapping, to keep and eye on object updates.
	 * TODO: switch to primitive int key hash (Trove?)
	 */
	private HashMap <K, AABB> locations = new HashMap <K, AABB> ();
	
//	private TIntObjectMap <AABB> locations = new TIntObjectHashMap <AABB> ();

	/**
	 * Adds an {@link AABB} box, containing the object.
	 * @param aabb
	 * @param object
	 */
	public void add(K object)
	{
		AABB center = new AABB(object.getAABB());
		locations.put(object, center);
		addObject(center, object);
	}
	
	/**
	 * Removes an {@link AABB} box, containing the object.
	 * @param aabb
	 * @param object
	 */
	public K remove(K object)
	{
		AABB oldLocation = locations.get(object);
		if(oldLocation == null)
			throw new IllegalArgumentException("The object [" + object + "] was not added to the indexer.");
		locations.remove(object);
		return removeObject(oldLocation, object);
	}
	
	/**
	 * Updates an {@link AABB} box location.
	 * @param aabb
	 * @param object
	 */
	public void update(K object)
	{
		AABB oldLocation = locations.get(object);
		if(oldLocation == null)
			throw new IllegalArgumentException("The object [" + object + "] was not added to the indexer.");
		AABB newLocation = new AABB(object.getAABB());
		
		locations.put(object, newLocation);
		updateObject(oldLocation, newLocation, object);
	}
	
	/**
	 * Retrieves set of indexed {@link ISpatialObject}s.
	 * Note: The collection is backed up by the indexer and should not be modified. 
	 * @return
	 */
	public Set <K> keySet() { return locations.keySet(); }
	
	/**
	 * Adds an {@link AABB} box, containing the object.
	 * @param aabb
	 * @param object
	 */
	protected abstract void addObject(AABB aabb, K object);
	
	/**
	 * Removes an {@link AABB} box, containing the object.
	 * @param aabb
	 * @param object
	 */
	protected abstract K removeObject(AABB aabb, K object);
	
	/**
	 * Updates an {@link AABB} box location.
	 * @param aabb
	 * @param object
	 */
	protected abstract void updateObject(AABB old, AABB aabb, K object);
	
	/**
	 * Iterates over specified rectangle, reporting fully or partially fitting objects in index. 
	 * {@link ISpatialSensor} must implement object observing logic. They can also modify 
	 * the sensed objects, proving that measurements are just a set of observations that 
	 * reduce uncertainty where the result is expressed as a quantity. One could also say that a 
	 * measurement is the collapse of the wavefunction.
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	public abstract ISpatialSensor <K> query(ISpatialSensor <K> sensor, double minx, double miny, double maxx, double maxy);
	
	/**
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	public abstract ISpatialSensor <K> query(ISpatialSensor <K> sensor, double x, double y, double radius);


}