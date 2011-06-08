package yarangi.spatial;

import yarangi.math.Vector2D;

/**
 * Interface for 2D area that is to be managed by {@link SpatialIndexer}. 
 * 
 * @author dveyarangi
 */
public interface Area
{
	
	/**
	 * Iterates over grid cells of specified size that overlaps with this area.
	 * @param cellsize size of the grid cell
	 * @return
	 */
	public IGridIterator <IAreaChunk> iterator(double cellsize);
	
	/**
	 * Retrieves area orientation.
	 * @return
	 */
	public double getOrientation();
	
	/**
	 * Sets area orientation
	 * @param a
	 */
	public void setOrientation(double a);

	/**
	 * Retrieves area's center reference point.
	 * @return
	 */
	public Vector2D getRefPoint();
	
	/**
	 * Translates area reference point. 
	 * @param dx
	 * @param dy
	 */
	public void translate(double dx, double dy);
	
	/**
	 * Area profile width from specified angle.
	 * @param direction
	 * @return
	 */
//	public double calcWidth(double angle);

	/**
	 * Copies the area.
	 * @return
	 */
	public Area clone();
}