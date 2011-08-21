package yarangi.spatial;

import java.util.LinkedList;
import java.util.List;

import yarangi.ZenUtils;
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
//	public IGridIterator <? extends IAreaChunk> iterator(int cellsize);
	
	public void iterate(int cellsize, IChunkConsumer consumer);
	
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
	 * Scales the area to fit specified maximum radius.
	 * @param radius
	 */
	public void fitTo(double radius);

	/**
	 * Retrieves area's max radius.
	 * @return
	 */
	public double getMaxRadius();
	
	public LinkedList <Vector2D> getDarkEdge(Vector2D from);

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
	
//	public void traverse(IChunkObserver observer);
//	public void traverse(IChunkObserver observer);
	//////////////////////////////////////////////////
	// TODO: all following statics bother me
	
	public final static class EmptyChunk implements IAreaChunk
	{
		
		@Override public Area getArea() { return EMPTY; }

		@Override public double getX() { throw new IllegalStateException("This method is not defined."); }
		@Override public double getY() { throw new IllegalStateException("This method is not defined."); }

		@Override public boolean overlaps(double xmin, double ymin, double xmax, double ymax) { return false;	}

		@Override public double getMinX() { throw new IllegalStateException("This method is not defined."); }
		@Override public double getMinY() { throw new IllegalStateException("This method is not defined."); }
		@Override public double getMaxX() { throw new IllegalStateException("This method is not defined."); }
		@Override public double getMaxY() { throw new IllegalStateException("This method is not defined.");}
		
	}
	
	
	public static final IAreaChunk EMPTY_CHUNK = new EmptyChunk();
	
	public static final Area EMPTY = new Area() 
	{

		@Override
		public void iterate(int cellsize, IChunkConsumer consumer) { }
		
		@Override
		public double getOrientation() { ZenUtils.methodNotSupported(this.getClass()); return Double.NaN;  }

		@Override
		public void setOrientation(double a) { ZenUtils.methodNotSupported(this.getClass()); }

		@Override
		public Vector2D getRefPoint() {  ZenUtils.methodNotSupported(this.getClass()); return null; }

		public double getMaxRadius() { ZenUtils.methodNotSupported(this.getClass()); return Double.NaN; }

		@Override
		public void translate(double dx, double dy) { ZenUtils.methodNotSupported(this.getClass()); }
		@Override
		public void fitTo(double radius) { ZenUtils.methodNotSupported(this.getClass()); }

		public Area clone() { return this; }

		@Override
		public LinkedList <Vector2D> getDarkEdge(Vector2D from)
		{
			ZenUtils.methodNotSupported(this.getClass());
			return new LinkedList <Vector2D> ();
		}
	
	};
	

}