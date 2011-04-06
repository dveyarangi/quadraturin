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
	 * Create an iterator that goes over object inside specified box. 
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	public abstract SpatialProcessor <K> query(SpatialProcessor <K> processor, double minx, double miny, double maxx, double maxy);

}