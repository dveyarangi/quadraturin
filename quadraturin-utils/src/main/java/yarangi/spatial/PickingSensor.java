package yarangi.spatial;

/**
 * Sensor that picks a single object from the indexer.
 * @param <K> indexer ojbects type
 */
public class PickingSensor <K extends ISpatialObject> implements ISpatialSensor <IAreaChunk, K> 
{
	
	/**
	 * Object filter
	 */
	private ISpatialFilter <K> filter;
	
	/**
	 * Object picked during last indexer query.
	 */
	private K object;
	
	public PickingSensor()
	{
		this(null);
	}
	
	public PickingSensor(ISpatialFilter <K> filter)
	{
		this.filter = filter;
	}

	@Override
	public boolean objectFound(IAreaChunk chunk, K object) 
	{
		if(filter == null || filter.accept( object ))
		{
			this.object = object;
			return true; // terminating query
		}
		
		return false;
	}
	
	/**
	 * @return Object picked in last indexer query with this sensor. 
	 */
	public K getObject() { return object; }

	@Override
	public void clear()
	{
		object = null;
	}

}
