package yarangi.spatial;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import yarangi.math.FastMath;
import yarangi.math.Vector2D;

public class Polygon implements Area 
{
	private List <Vector2D> points = new ArrayList <Vector2D> ();
	
	
	private Vector2D ref;
	
	private Vector2D maxx, maxy, minx, miny;
	
	public Polygon(double x, double y) 
	{ 
		this.ref = new Vector2D(x, y);
	}
	
	private Polygon(Polygon polygon)
	{

		for(Vector2D point : polygon.points)
			points.add(point);
		
		maxx = polygon.maxx;
		minx = polygon.minx;
		maxy = polygon.maxy;
		miny = polygon.miny;
		
		ref = new Vector2D(polygon.ref);
	}
	
	public void add(int idx, Vector2D point) 
	{ 
		Vector2D newPoint = new Vector2D(point.x+ref.x, point.y+ref.y);
		this.points.add(idx, newPoint);
		updateAABB(newPoint);
	}
	
	
	public void add(Vector2D point) 
	{ 
		Vector2D newPoint = new Vector2D(point.x+ref.x, point.y+ref.y);
		this.points.add(newPoint); 
		updateAABB(newPoint);
	}
	
	final private void updateAABB(Vector2D point)
	{
		if(minx == null || minx.x > point.x)
			minx = point;
		if(maxx == null || maxx.x < point.x)
			maxx = point;
		if(miny == null || miny.y > point.y)
			miny = point;
		if(maxy == null || maxy.y < point.y)
			maxy = point;
	}

	
	public void add(int idx, double x, double y) { add(idx, new Vector2D(x, y)); }
	
	public void add(double x, double y) 
	{
		add(new Vector2D(x, y)); 
	}
	
	public void remove(int idx) { this.points.remove(idx); }
	
	public Vector2D get(int idx) { return this.points.get(idx); }
	
	public Area clone()
	{
		return new Polygon(this);
	}


	@Override public Vector2D getRefPoint() { return ref; }

	@Override
	public double getOrientation() { return 0; }

	@Override
	public void setOrientation(double a) { throw new IllegalStateException("This method is not yet implemented"); }

	@Override
	public void translate(double dx, double dy) {
		ref.x += dx;
		ref.y += dy;
	}
	

	@Override
	public IGridIterator<IAreaChunk> iterator(double cellsize) {
		return new PolyIterator(cellsize);
	}
	

	final public List <Vector2D> getPoints() { return points; }
	

	
	/**
	 * Implements Bresenham's scanning for each of the polygon edges.
	 * TODO: Also consider DDA, ray casting, winding number, scan converting, 
	 */
	
	public class PolyIterator implements IGridIterator <IAreaChunk>
	{
		/** vertex of current polygon line */
		Vector2D currPoint, nextPoint;
		/** index within the current line */
		private int step, steps;
		
		/** location of the next point in grid.  */
		private double currx, curry;
		
		/** step to next point in grid */
		private double dx, dy;
		
		/** size of single grid cell */
		private double cellsize;
		
		/** index of current polygon point */
		private int idx = 0, size;
		

		public PolyIterator(double cellsize)
		{
			
			this.size = Polygon.this.points.size();
			
			// TODO: move to {@link #next()} arguments?
			this.cellsize = cellsize;
			
			idx = 0;
			
			nextPoint = Polygon.this.points.get(0);
			nextSegment();
			
		}
		
		@Override
		public boolean hasNext() {
			return idx <= size;
		}

		@Override
		public IAreaChunk next() 
		{
			IAreaChunk chunk = new PolyChunk(FastMath.round(currx), FastMath.round(curry));
			
			// going to the next grid cell center:
			currx += dx;
			curry += dy;

			if(step++ >= steps) // advancing the counter and testing if the segment ended:
				nextSegment();
			return chunk;
		}

		/**
		 * Calculates the iterator state for next line segment.
		 */
		private void nextSegment()
		{
			if(idx ++ > size)
				throw new NoSuchElementException("No more area chunks available.");
			
			// getting the next scanned segment points:
			currPoint = nextPoint;
			nextPoint = Polygon.this.points.get(idx < size ? idx : 0);
			
			// calculating segment slope:
			double sx = nextPoint.x - currPoint.x;
			double sy = nextPoint.y - currPoint.y;
			double m = sy / sx;
			
			if(m <= 1 && m >= -1) // x changes faster than y:
			{   
				dx = sx > 0 ? cellsize : -cellsize;
				dy = m * dx;
				// number of x steps:
				steps = (int)(Math.abs(sx) / cellsize) + 1;
			}
			else 
			{
				dy = sy > 0 ? cellsize : -cellsize;
				dx = Double.isInfinite(m) ? 0 : dy / m;
				// number of y steps:
				steps = (int)(Math.abs(sy) / cellsize) + 1;
			}

			// aligning the starting line vertex to the grid:
			currx = currPoint.x;//FastMath.toGrid(currPoint.x, cellsize);
			curry = currPoint.y;//FastMath.toGrid(currPoint.y, cellsize);
			
			
			step = 0;
//			System.out.println("dxy: " + dx + "," + dy + " tan:" + m + " steps:" + steps + " cell:" + cellsize);
//			System.out.println("x: " + currx + "," + point1.x + ", " + point2.x);
//			System.out.println("y: " + curry + "," + point1.y + ", " + point2.y);

		}
	}
	
	/**
	 * A piece of polygon that fits single grid cell.
	 * 
	 * TODO: add some meat 
	 */
	public class PolyChunk implements IAreaChunk
	{
		private double x, y;
		
		private List <Vector2D> pieces;
		public PolyChunk(double x, double y) 
		{
			this.x = x;
			this.y = y;
			this.pieces = new ArrayList <Vector2D> (2);
		}
		
		public void add(Vector2D piece) { pieces.add(piece); }

		@Override public Area getArea() { return Polygon.this; }

		@Override public double getX() { return x; }
		@Override public double getY() { return y; }
		
		@Override public double getMinX() { return x; }
		@Override public double getMinY() { return y; }
		@Override public double getMaxX() { return x; }
		@Override public double getMaxY() { return y; }

		@Override
		public boolean overlaps(double xmin, double ymin, double xmax,
				double ymax) {
			// TODO Auto-generated method stub
			return true;
		}

		
	}
}
