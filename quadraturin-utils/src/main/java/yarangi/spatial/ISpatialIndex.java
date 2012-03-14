package yarangi.spatial;


public interface ISpatialIndex <T, O>
{
	/**
	 * Iterates over specified area, reporting fully or partially fitting objects in index. 
	 * {@link ISpatialSensor} must implement object observing logic. They can also modify 
	 * the sensed objects, proving that measurements are just a set of observations that 
	 * reduce uncertainty where the result is expressed as a quantity. One could also say that a 
	 * measurement is the collapse of the wavefunction.
	 * 
	 * Nevertheless, query terminates if {@link ISpatialSensor#objectFound(IAreaChunk, ISpatialObject)} returns true.
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	public ISpatialSensor <T, O> query(ISpatialSensor <T, O> sensor, Area area);
//	public ISpatialSensor <K> query(ISpatialSensor <K> sensor, AABB area);
	
	/**
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	public ISpatialSensor <T, O> query(ISpatialSensor <T, O> sensor, double x, double y, double radiusSquare);
	
	/**
	 * Iterates over a line, originating at (ox, oy) width (dx, dy) length
	 * 
	 * @param sensor
	 * @param ox
	 * @param oy
	 * @param dx
	 * @param dy
	 * @return
	 */
	public ISpatialSensor <T, O> query(ISpatialSensor <T, O> sensor, double ox, double oy, double dx, double dy);

}
