package yarangi.spatial;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import yarangi.math.FastArrays;
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
		Vector2D newPoint = new Vector2D(point.x(), point.y());
		this.points.add(idx, newPoint);
		updateAABB(newPoint);
	}
	
	
	public void add(Vector2D point) 
	{ 
		Vector2D newPoint = new Vector2D(point.x(), point.y());
		this.points.add(newPoint); 
		updateAABB(newPoint);
	}
	
	final private void updateAABB(Vector2D point)
	{
		if(minx == null || minx.x() > point.x())
			minx = point;
		if(maxx == null || maxx.x() < point.x())
			maxx = point;
		if(miny == null || miny.y() > point.y())
			miny = point;
		if(maxy == null || maxy.y() < point.y())
			maxy = point;
	}

	
	public void add(int idx, double x, double y) { add(idx, new Vector2D(x, y)); }
	
	public void add(double x, double y) 
	{
		add(new Vector2D(x, y)); 
	}
	
	// TODO: test idx % points.size() against idx < points.size() ? idx : 0;
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

	// TODO!
	@Override
	public double getMaxRadius() { return 1; }
	
	@Override
	public void translate(double dx, double dy) {
		ref.add(dx, dy);
	}
	

	@Override
	public IGridIterator<IAreaChunk> iterator(double cellsize) {
		return new PolyIterator(cellsize);
	}
	

	final public List <Vector2D> getPoints() { return points; }
	

	final public int size() { return points.size(); }
	
	/**
	 * Implements Bresenham's scanning for each of the polygon edges.
	 * TODO: Also consider DDA, ray casting, winding number, scan converting, 
	 */
	
	public class PolyIterator implements IGridIterator <IAreaChunk>
	{
		/** vertex of current polygon line */
		Vector2D currPoint, nextPoint;
		/** location of the next point in grid.  */
		private double currx, curry;
		
		
		private int currGridx, currGridy;
		/** location of the next point in grid.  */
		private int nextGridx, nextGridy;
		
		/** step to next point in grid */
		private double dx, dy;
		
		private int startGridx, startGridy;
		
		/** size of single grid cell */
		private double cellsize;
		
		int currStartIdx, currEndIdx;
		
		private boolean start, scan;

		public PolyIterator(double cellsize)
		{
			
			// TODO: move to {@link #next()} arguments?
			this.cellsize = cellsize;
			
			Polygon poly = Polygon.this;
			Vector2D tempPoint = poly.get(0);
			
			currx = tempPoint.x();
			curry = tempPoint.y();
			
			int tempGridx = currGridx = nextGridx = startGridx = (int)FastMath.toGrid(currx, cellsize); 
			int tempGridy = currGridy = nextGridy = startGridy = (int)FastMath.toGrid(curry, cellsize); 
			
			int size = poly.size();
			currStartIdx = 0;
			

//			System.out.println("== ITERATOR ===========================================");
			do {
				currPoint = tempPoint;
				
				currStartIdx = FastArrays.dec(currStartIdx, size);
				
				tempPoint = poly.points.get(currStartIdx);
				tempGridx = (int)FastMath.toGrid(tempPoint.x(), cellsize);
				tempGridy = (int)FastMath.toGrid(tempPoint.y(), cellsize);
//				System.out.println(" * testing at (" + tempGridx + "," + tempGridy + ")");
			} 
			while(tempGridx == startGridx
			   && tempGridy == startGridy);
//			System.out.println(" * starting loc (" + startGridx + "," + startGridy + ")");
			
			currGridx = (int)FastMath.toGrid(currx, cellsize);
			currGridy = (int)FastMath.toGrid(curry, cellsize);
				
			start = scan = true;
			nextPoint = currPoint;
			
			currEndIdx = currStartIdx+1;
			
			
//			nextIndex(false);
			
		}
		
		
		/**
		 * Advances indexes. Calculates currPoint and nextPoint for scanning.
		 * nextGridx, nextGridy
		 * currStartIdx, currEndIdx
		 * NextPoint
		 */
		private void rollIndexes()
		{
			int size = Polygon.this.size();
			currStartIdx = FastArrays.dec(currEndIdx, size);
			currEndIdx = currStartIdx;
		
			int tempGridx = nextGridx, tempGridy = nextGridy; 
			do {
				currPoint = nextPoint;
				currEndIdx = FastArrays.inc(currEndIdx, size);
				nextPoint = Polygon.this.get(currEndIdx);
				
				nextGridx = (int)FastMath.toGrid(nextPoint.x(), cellsize);
				nextGridy = (int)FastMath.toGrid(nextPoint.y(), cellsize);
			}
			while(nextGridx == tempGridx
			   && nextGridy == tempGridy);
			
			
			
		}
		
		/**
		 * Calculates deltas for Bresenham's scanning for specified polygon edge.
		 * dx and dy are changed
		 */
		private void calcSegment(Vector2D currPoint, Vector2D nextPoint)
		{
			
			// calculating segment slope:
			double sx = nextPoint.x() - currPoint.x();
			double sy = nextPoint.y() - currPoint.y();
			double m = sy / sx;
			
			if(m <= 1 && m >= -1) // x changes faster than y:
			{   
				dx = sx > 0 ? cellsize : -cellsize;
				dy = m * dx;
			}
			else 
			{
				dy = sy > 0 ? cellsize : -cellsize;
				dx = Double.isInfinite(m) ? 0 : dy / m;
			}
		}
		
		@Override
		public boolean hasNext() {
			return (currGridx != startGridx	|| currGridy != startGridy) || start;
		}

		@Override
		public IAreaChunk next() 
		{
			// going to the next grid cell center:
			if(!hasNext())
				throw new NoSuchElementException("No more elements.");
			
			// going to the next grid cell center:
			currGridx = (int)FastMath.toGrid(currx, cellsize);
			currGridy = (int)FastMath.toGrid(curry, cellsize);
			
			PolyChunk chunk;
			
			// in a dense poly, this condition will be met frequently:
			if(currGridx == nextGridx && currGridy == nextGridy) // we reached poly edge end 
			{
				// calculating its indexes range inside current cell:
				rollIndexes();
				chunk = new PolyChunk(currGridx, currGridy, currStartIdx, currEndIdx);
				 // set flag to calculate scanning parameters next segment line:
				scan = true; 
			}
			else // traversing along the current poly edge:  
			{
				if(scan)
				{
					// adjusting starting segment index:
					currStartIdx = FastArrays.dec(currEndIdx, Polygon.this.size());
					
					// calculating next line segment deltas:
					calcSegment(currPoint, nextPoint);
					
					
					currx = currPoint.x() + dx;
					curry = currPoint.y() + dy;
					// going to the next grid cell center:
					currGridx = (int)FastMath.toGrid(currx, cellsize);
					currGridy = (int)FastMath.toGrid(curry, cellsize);
					
					scan = false;
					start = false;
				}
				
				chunk = new PolyChunk(currGridx, currGridy, currStartIdx, currEndIdx);
				
				if(currGridx != nextGridx || currGridy != nextGridy)
				{ // we could get to the next polypoint point inside the if(scan)
					currx += dx;
					curry += dy;
				}
			}
			
//			System.out.println(chunk);
			return chunk;
		}
	}
	
	/**
	 * A piece of polygon that fits single grid cell.
	 * 
	 * TODO: add some meat 
	 */
	public class PolyChunk implements IAreaChunk
	{
		private int x, y;
		
		private List <Vector2D> pieces;
		
		private int minIdx;
		private int maxIdx;
		public PolyChunk(int x, int y, int minIdx, int maxIdx) 
		{
			
			this.x = x + FastMath.round(Polygon.this.getRefPoint().x());
			this.y = y + FastMath.round(Polygon.this.getRefPoint().y());
			
			this.minIdx = minIdx; 
			this.maxIdx = maxIdx;
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
		
		public boolean equals(Object object)
		{
			if(!(object instanceof PolyChunk))
				return false;
			
			PolyChunk chunk = (PolyChunk) object;
			return chunk.x == x && chunk.y == y;
		}
		
		public String toString()
		{
			return "Polychunk at [" + x + "," + y + "], idx[" + minIdx + ":" + maxIdx + "].";
		}
	}

}
