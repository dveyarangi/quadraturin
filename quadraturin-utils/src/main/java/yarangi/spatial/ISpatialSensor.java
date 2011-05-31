package yarangi.spatial;

public interface ISpatialSensor <K extends ISpatialObject> 
{
	public void objectFound(IAreaChunk chunk, K object);
}
