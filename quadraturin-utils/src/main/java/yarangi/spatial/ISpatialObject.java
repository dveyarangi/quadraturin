package yarangi.spatial;

/**
 * Interface for object with shape.
 */
public interface ISpatialObject 
{

	/**
	 * Object id.
	 * @return
	 */
	public int getId();
	/**
	 * Object volume.
	 * @return
	 */
	public Area getArea();

}
