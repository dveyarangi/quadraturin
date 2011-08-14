package yarangi.spatial;

import java.util.NoSuchElementException;

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
	public IGridIterator <? extends IAreaChunk> iterator(int cellsize);
	
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
		
		private Vector2D ref = new Vector2D(0,0);

		@Override
		public IGridIterator<IAreaChunk> iterator(int cellsize) {
			return new EmptyIterator();
		}

		@Override
		public double getOrientation() {
			return 0;
		}

		@Override
		public void setOrientation(double a) { }

		@Override
		public Vector2D getRefPoint() {return ref; }

		public double getMaxRadius() { return 0; }

		@Override
		public void translate(double dx, double dy) { }
		@Override
		public void fitTo(double radius) { }
	
		class EmptyIterator implements IGridIterator <IAreaChunk>
		{
			@Override public boolean hasNext() { return false; }

			@Override public IAreaChunk next() { throw new NoSuchElementException(); }
			
		}
		public Area clone() { return this; }

	
	};
	
	public static class PointArea implements Area 
	{
		private Vector2D ref;
		public PointArea(double x, double y)
		{
			ref = new Vector2D(x, y);
		}
		public PointArea(Vector2D point)
		{
			this( point.x(), point.y() );
		}
		@Override
		public IGridIterator<? extends IAreaChunk> iterator(int cellsize) {
			return new PointIterator();
		}

		@Override
		public double getOrientation() 
		{
			return 0;
		}

		public double getMaxRadius() { return 0; }

		@Override
		public void setOrientation(double a) 
		{
			throw new IllegalStateException("This method is not supported for class " + this.getClass());
		}


		@Override
		public Vector2D getRefPoint() {
			return ref;
		}

		@Override
		public void translate(double dx, double dy) {
			ref.add(dx, dy);
		}
		@Override
		public void fitTo(double radius)
		{
			throw new IllegalStateException("This method is not supported for class " + this.getClass());
		}
		
		public Area clone()
		{
			return new PointArea(ref.x(), ref.y());
		}
		
		class PointIterator implements IGridIterator <IAreaChunk>
		{
			boolean read = false;
			@Override public boolean hasNext() { return false; }

			@Override public IAreaChunk next() 
			{ 
				if(read)
					throw new NoSuchElementException();
				return new PointChunk(PointArea.this.ref);
			}
			
		}
		
		class PointChunk extends Vector2D implements IAreaChunk
		{
	
			public PointChunk(Vector2D ref)
			{
				super(ref);
			}

			@Override
			public Area getArea() { return PointArea.this; }

			@Override
			public double getX() { return x(); }

			@Override
			public double getY() { return y(); }

			@Override
			public boolean overlaps(double xmin, double ymin, double xmax, double ymax) {
				return x() >= xmin && x() <= xmax && y() >= ymin && y() <= ymax; 
			}

			@Override
			public double getMinX() { return x(); }

			@Override
			public double getMinY() { return y(); }

			@Override
			public double getMaxX() { return x(); }

			@Override
			public double getMaxY() { return y(); }
			
		}

		
	}

}