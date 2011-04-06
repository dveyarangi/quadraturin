package yarangi.spatial;

public interface SpatialProcessor <K extends ISpatialObject> 
{
	public void objectFound(K object);
}
