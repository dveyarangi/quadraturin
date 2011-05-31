package yarangi.spatial;

import java.util.Iterator;

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
	public Iterator <IAreaChunk> iterator(double cellsize);

	/**
	 * Retrieves area's center reference point.
	 * @return
	 */
	public Vector2D getRefPoint();
	
	/**
	 * Retrieves area orientation.
	 * @return
	 */
	public double getOrientation();
	public void setOrientation(double a);

	public void translate(double dx, double dy);

	public Area clone();
	
//	public Area clone();
}