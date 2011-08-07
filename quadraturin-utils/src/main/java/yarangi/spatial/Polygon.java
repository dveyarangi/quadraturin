package yarangi.spatial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import yarangi.math.FastArrays;
import yarangi.math.FastMath;
import yarangi.math.Vector2D;

public class Polygon implements Area 
{
	private List <PolyPoint> points = new ArrayList <PolyPoint> ();
	
	
	private Vector2D ref;
	
	private PolyPoint maxx, maxy, minx, miny;
	
	private double radius = 1;
	
	public Polygon(double x, double y) 
	{ 
		this.ref = new Vector2D(x, y);
	}
	
	private Polygon(Polygon polygon)
	{

		for(PolyPoint point : polygon.points)
			points.add(point);
		
		maxx = polygon.maxx;
		minx = polygon.minx;
		maxy = polygon.maxy;
		miny = polygon.miny;
		
		ref = new Vector2D(polygon.ref);
	}
	
	public void add(int idx, double x, double y) 
	{ 
		PolyPoint newPoint = new PolyPoint(x, y);
		this.points.add(idx, newPoint);
		updateAABB(newPoint);
	}
	
	
	public void add(double x, double y) 
	{ 
		PolyPoint newPoint = new PolyPoint(x, y);
		this.points.add(newPoint); 
		updateAABB(newPoint);
	}
	
	final private void updateAABB(PolyPoint point)
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

	
	public void add(int idx, Vector2D v) { add(idx, v.x(), v.y()); }
	
	public void add(Vector2D v) 
	{
		add(v.x(), v.y()); 
	}
	
	// TODO: test idx % points.size() against idx < points.size() ? idx : 0;
	public void remove(int idx) { this.points.remove(idx); }
	
	public PolyPoint get(int idx) { return this.points.get(idx); }
	
	public Polygon clone()
	{
		return new Polygon(this);
	}


	@Override public Vector2D getRefPoint() { return ref; }

	@Override
	public double getOrientation() { return 0; }

	@Override
	public void setOrientation(double a) { throw new IllegalStateException("This method is not yet implemented"); }
	@Override
	public void fitTo(double radius)
	{
		this.radius = radius;
	}

	// TODO!
	@Override
	public double getMaxRadius() { return radius; }
	
	@Override
	public void translate(double dx, double dy) {
		ref.add(dx, dy);
	}
	

	@Override
	public IGridIterator<PolyChunk> iterator(int cellsize) {
		return new PolyIterator(generateChunks(cellsize));
		
	}
	

	final public List <PolyPoint> getPoints() { return points; }
	

	final public int size() { return points.size(); }
	
	
	private List <PolyChunk> generateChunks(int cellsize)
	{
		List <PolyChunk> list = new LinkedList <PolyChunk> ();
		
		/** vertex of current polygon line */
		PolyPoint currPoint, nextPoint;
		
		/** location of the next point in grid.  */
//		double currx = tempPoint.x();
//		double curry = tempPoint.y();
		
		
		int currGridx, currGridy;
		/** location of the next point in grid.  */
		int nextGridx, nextGridy;
		
		/** step to next point in grid */
		double dx, dy;
		
		int startGridx, startGridy;
		
		int currStartIdx, currEndIdx;
		
		int pointsNum = this.size();
		
		int tempGridx, tempGridy;
		
		currStartIdx = 0;
		
		PolyPoint tempPoint = this.get(0);
		
		tempGridx = currGridx = nextGridx = startGridx = FastMath.toGrid(tempPoint.x(), cellsize); 
		tempGridy = currGridy = nextGridy = startGridy = FastMath.toGrid(tempPoint.y(), cellsize); 
		// searching for first polygon point in this grid cell:
		do {
			currPoint = tempPoint;
			
			currStartIdx = FastArrays.dec(currStartIdx, pointsNum);
			
			tempPoint = points.get(currStartIdx);
			tempGridx = FastMath.toGrid(tempPoint.x, cellsize);
			tempGridy = FastMath.toGrid(tempPoint.y, cellsize);
//			System.out.println(" * testing at (" + tempGridx + "," + tempGridy + ")");
		} 
		while(tempGridx == startGridx
		   && tempGridy == startGridy);

		nextPoint = currPoint;
		
		currEndIdx = currStartIdx+1;
		
		// line scan parameters (from "A Fast Voxel Traversal Algorithm for Ray Tracing" article): 
		double tMaxX=1, tMaxY=1;
		double tDeltaX=0, tDeltaY=0;
		double stepX=0 , stepY=0;
		
		do
		{
			
			// in a dense poly, this condition will be met frequently:
			if(tMaxX >= 1 && tMaxY >= 1) // we reached poly line end 
			{
				// calculating its indexes range inside current cell:
				currStartIdx = FastArrays.dec(currEndIdx, size());
				currEndIdx = currStartIdx;
			
				do {
					currPoint = nextPoint;
					currEndIdx = FastArrays.inc(currEndIdx, size());
					nextPoint = Polygon.this.get(currEndIdx);
					
					nextGridx = FastMath.toGrid(nextPoint.x, cellsize);
					nextGridy = FastMath.toGrid(nextPoint.y, cellsize);
				}
				while(nextGridx == currGridx
				   && nextGridy == currGridy);
				
				// 
				list.add(new PolyChunk(currGridx, currGridy, currStartIdx, currEndIdx));
				
				
				// calculating parameters for line scan :
				
				dx = nextPoint.x() - currPoint.x();
				dy = nextPoint.y() - currPoint.y();
				
				if(dx > 0)
				{
					tMaxX = ((currGridx + cellsize/2.) - currPoint.x()) / dx;
					tDeltaX = cellsize / dx;
					stepX = cellsize;
				}					
				else
				if(dx < 0)
				{
					tMaxX = ((currGridx - cellsize/2.) - currPoint.x()) / dx;
					tDeltaX = -cellsize / dx;
					stepX = -cellsize;
				}
				else { tMaxX = Double.MAX_VALUE;}
				
				if(dy > 0)
				{
					tMaxY = ((currGridy + cellsize/2.) - currPoint.y()) / dy;
					tDeltaY = cellsize / dy;
					stepY = cellsize;
				}
				else
				if(dy < 0)
				{
					tMaxY = ((currGridy - cellsize/2.) - currPoint.y()) / dy;
					tDeltaY = -cellsize / dy;
					stepY = -cellsize;
				}
				else { tMaxY = Double.MAX_VALUE;}
				

			}
			
			if(tMaxX < tMaxY)
			{
				tMaxX += tDeltaX;
				currGridx += stepX;
			}
			else
			{
				tMaxY += tDeltaY;
				currGridy += stepY;
			}
			
			list.add(new PolyChunk(currGridx, currGridy, currStartIdx, currEndIdx));
			
		}
		while((currGridx != startGridx || currGridy != startGridy));
		
		return list;
	}
	
	public class PolyIterator implements IGridIterator <PolyChunk>
	{
		private Iterator <PolyChunk> parent;
		
		public PolyIterator(List <PolyChunk> chunks)
		{
			parent = chunks.iterator();
		}
		
		@Override
		public boolean hasNext()
		{
			return parent.hasNext();
		}

		@Override
		public PolyChunk next()
		{
			return parent.next();
		}
		
	}

	public class PolyPoint
	{
		private double x, y;
		
		private int nextConvexIdx, prevConvexIdx;
		
		public PolyPoint(double x, double y)
		{
			this.x = x;
			this.y = y;
		}
		
		public final double x() { return x; }
		public final double y() { return y; }
		
		public void setPrevConvexIdx(int idx) { this.prevConvexIdx = idx; } 
		public void setNextConvexIdx(int idx) { this.nextConvexIdx = idx; }
		
		public double getNextConvexIdx() { return nextConvexIdx; }
		public double getPrevConvexIdx() { return prevConvexIdx; }
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
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}


		@Override
		public boolean equals(Object obj)
		{
			if ( this == obj )
				return true;
			if ( obj == null )
				return false;
			if ( getClass() != obj.getClass() )
				return false;
			PolyChunk other = (PolyChunk) obj;
			if ( !getOuterType().equals( other.getOuterType() ) )
				return false;
			if ( x != other.x )
				return false;
			if ( y != other.y )
				return false;
			return true;
		}
		
		public String toString()
		{
			return "Polychunk at [" + x + "," + y + "], idx[" + minIdx + ":" + maxIdx + "].";
		}


		private Polygon getOuterType()
		{
			return Polygon.this;
		}
	}


}
