package yarangi.spatial;


public class PickingSensor <K extends ISpatialObject> implements ISpatialSensor <K> 
{

	private static final long serialVersionUID = 9025712177585233445L;
	
	private ISpatialFilter <K> filter;
	
	private K object;
	
	public PickingSensor(ISpatialFilter <K> filter)
	{
		this.filter = filter;
	}

	public boolean objectFound(IAreaChunk chunk, K object) 
	{
		if(filter == null || filter.accept( object ))
		{
			this.object = object;
			return true; // terminating query
		}
		return false;
	}
	
	public K getObject() { return object; }

	@Override
	public void clear()
	{
		object = null;
	}

}
