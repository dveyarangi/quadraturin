package yarangi.spatial;

import java.util.List;

import yarangi.Zen;
import yarangi.math.Vector2D;

/**
 * Interface for 2D area that is to be managed by {@link SpatialIndexer}. 
 * 
 * @author dveyarangi
 */
public interface Area
{
	
	/**
	 * Retrieves area orientation.
	 * @return
	 */
	public double getOrientation();
	
	/**
	 * Sets area orientation (degrees)
	 * @param a
	 */
	public void setOrientation(double a);

	/**
	 * Retrieves area's center reference point.
	 * Note: returns a living object, changing it may corrupt area object.
	 * TODO: quadraturin should not frequently use this vector, so get rid of its references
	 * and replace with cloned vector.
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
	 * Scales the area to fit specified maximum radius.
	 * @param radius
	 */
	public void fitTo(double radius);

	/**
	 * Retrieves area's span radius.
	 * @return
	 */
	public double getMaxRadius();
	
	/**
	 * Iterates over grid cells of specified size (aligned to zero)
	 * that overlaps with this area. The cell info is fed to the 
	 * specified consumer. 
	 * This method provides appropriate {@link IAreaChunk} objects.
	 *  
	 * @param cellsize size of the grid cell
	 * @return
	 */
	public void iterate(int cellsize, IChunkConsumer consumer);
	
	/**
	 * Extracts a area perimeter part that is hidden from specified direction.
	 * TODO: this method is too specific :) maybe its better to retrieve the whole perimeter
	 * @param from
	 * @return
	 */
	public List <Vector2D> getDarkEdge(Vector2D from);

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
	
	
	/**
	 * Service method for spatial query: query id
	 * @return
	 */
	public int getPassId();
	
	/**
	 * Sets spatial query id (used by {@link SpatialHashMap}
	 */
	public void setPassId(int id);
	
	
//	public void traverse(IChunkObserver observer);
//	public void traverse(IChunkObserver observer);
	//////////////////////////////////////////////////
	// TODO: all following statics bother me
	
	public final static class EmptyChunk implements IAreaChunk
	{
		
		@Override public Area getArea() { return EMPTY; }

		@Override public double getX() { return Zen.notSupported(); }
		@Override public double getY() { return Zen.notSupported(); }

		@Override public boolean overlaps(double xmin, double ymin, double xmax, double ymax) { return false;	}

		@Override public double getMinX() { return Zen.notSupported(); }
		@Override public double getMinY() { return Zen.notSupported(); }
		@Override public double getMaxX() { return Zen.notSupported(); }
		@Override public double getMaxY() { return Zen.notSupported(); }
		
	}
	
	
	public static final IAreaChunk EMPTY_CHUNK = new EmptyChunk();
	
	public static final Area EMPTY = new Area() 
	{

		@Override
		public void iterate(int cellsize, IChunkConsumer consumer) { }
		
		@Override
		public double getOrientation() { return Zen.notSupported(); }

		@Override
		public void setOrientation(double a) { Zen.notSupported(); }

		@Override
		public Vector2D getRefPoint() { return Zen.notSupported(); }

		public double getMaxRadius() { return Zen.notSupported();  }

		@Override
		public void translate(double dx, double dy) { Zen.notSupported(); }
		@Override
		public void fitTo(double radius) { Zen.notSupported(); }

		public Area clone() { return this; }

		@Override
		public List <Vector2D> getDarkEdge(Vector2D from)
		{
			return Zen.notSupported();
		}

		@Override
		public int getPassId() { return 0; }

		@Override
		public void setPassId(int id) { }

		@Override
		public boolean overlaps(AABB area) { return false; }
	
	};

	public boolean overlaps(AABB area);
	

}