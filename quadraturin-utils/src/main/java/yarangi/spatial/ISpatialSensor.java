package yarangi.spatial;

public interface ISpatialSensor <K extends ISpatialObject> 
{
	public void objectFound(K object, double distanceSquare);
}
