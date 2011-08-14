package yarangi.spatial;

/**
 * Interface for object with shape.
 */
public interface ISpatialObject 
{

	/**
	 * Object volume.
	 * @return
	 */
	public Area getArea();
	
	/**
	 * Service method for spatial query: query id
	 * @return
	 */
	public int getPassId();
	
	/**
	 * Sets spatial query id (used by {@link SpatialHashMap}
	 */
	public void setPassId(int id);

}
