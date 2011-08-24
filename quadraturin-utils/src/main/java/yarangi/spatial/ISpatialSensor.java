package yarangi.spatial;

public interface ISpatialSensor <K extends ISpatialObject> 
{
	/**
	 * Called on object detection.
	 * @param chunk
	 * @param object
	 * @return true, if the query should stop after this object.
	 */
	public boolean objectFound(IAreaChunk chunk, K object);
	
	public void clear();
}
