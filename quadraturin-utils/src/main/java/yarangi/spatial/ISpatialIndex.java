package yarangi.spatial;

public interface ISpatialIndex <K extends ISpatialObject>
{
	/**
	 * Iterates over specified rectangle, reporting fully or partially fitting objects in index. 
	 * {@link ISpatialSensor} must implement object observing logic. They can also modify 
	 * the sensed objects, proving that measurements are just a set of observations that 
	 * reduce uncertainty where the result is expressed as a quantity. One could also say that a 
	 * measurement is the collapse of the wavefunction.
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	public ISpatialSensor <K> query(ISpatialSensor <K> sensor, Area area);
	
	/**
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	public ISpatialSensor <K> query(ISpatialSensor <K> sensor, double x, double y, double radius);

}
