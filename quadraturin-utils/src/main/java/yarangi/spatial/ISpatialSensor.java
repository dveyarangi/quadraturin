package yarangi.spatial;

public interface ISpatialSensor <T, O> 
{
	/**
	 * Called on object detection.
	 * @param chunk
	 * @param object
	 * @return true, if the query should stop after this object.
	 */
	public boolean objectFound(T tile, O object);
	
	/**
	 * Resets sensor collections; called by {@link ISpatialIndex} at start of each query.
	 */
	public void clear();
}
