package yarangi.spatial;


public abstract class SpatialObjectSkeleton implements ISpatialObject
{
	
	private static final long serialVersionUID = 7334675818507558850L;
	
	/**
	 * Id auto-incremental counter
	 */
	private static int lastEntityId = 1;
	
	/** 
	 * Auto-generated entity scene id.
	 */
	private int id;
	
	/** 
	 * Scene entity geometry container 
	 */
	protected Area area;
	
	/**
	 * Creates a new located and oriented entity.
	 * @param x
	 * @param y
	 * @param a
	 */
	protected SpatialObjectSkeleton(Area area)
	{
//		if(aabb == null)
//			throw new RuntimeException("Bounding box must not be null.");
		this.area = area;
		id = (lastEntityId++);
	}
	
	/**
	 * Unique entity id.
	 * @return
	 */
	public final int getId() { return id; }

	/**
	 * Object volume
	 * @return
	 */
	public final Area getArea() { return area; }


	public int hashCode() { return id; }
	
	public boolean equals(Object o)
	{
		if ( ! (o instanceof SpatialObjectSkeleton))
			return false;
		
		return o.hashCode() == hashCode();
	}

}
