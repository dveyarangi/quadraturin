package yarangi.spatial;

import java.util.Iterator;
import java.util.NoSuchElementException;

import yarangi.math.Vector2D;

/**
 * Represents an axis-aligned bounding box (square, actually).
 * 
 */
public class AABB implements Area
{
	/**
	 * center point
	 */
	private Vector2D ref;
	
	/**
	 * half-width of the square
	 */
	private double r;
	
	/** 
	 * angle of the square (degrees)
	 */
	private double a;
	
	/**
	 * C'tor
	 * @param x box center x
	 * @param y box center y
	 * @param r half box width
	 * @param a box orientation (degrees)
	 */
	public AABB(double x, double y, double r, double a)
	{
		this.ref = new Vector2D(x, y);
		this.r = r;
		this.a = a;
	}
	
	protected AABB(AABB aabb)
	{
		this(aabb.ref.x, aabb.ref.y, aabb.r, aabb.a);
	}
	
	
	public final double getBoundingRadius() { return r; }
	
	public final double getOrientation() { return a; }
	
	public final Vector2D getRefPoint() { return ref; }

	
	public final void setOrientation(double a) { this.a = a; }
	
	
	
//	public boolean overlaps(AABB aabb)
//	{
//		return overlaps(aabb.x-r, aabb.y-r, aabb.x+r, aabb.y+r);
//	}
	

	@Override
	final public void translate(double dx, double dy) {
		ref.x += dx; 
		ref.y += dy;
	}

	
	public boolean overlaps(double minx, double miny, double maxx, double maxy)
	{
		return ( (maxx >= ref.x-r && maxx <= ref.x+r) ||
			     (minx >= ref.x-r && minx <= ref.x+r) ||
			     (minx >= ref.x-r && maxx <= ref.x+r) ||
			     (minx <= ref.x-r && maxx >= ref.x+r)    
			  ) && ( 
			     (maxy >= ref.y-r && maxy <= ref.y+r) ||
			     (miny >= ref.y-r && miny <= ref.y+r) ||
			     (miny >= ref.y-r && maxy <= ref.y+r) ||
			     (miny <= ref.y-r && maxy >= ref.y+r)    
			   );

	}
	
	public boolean equals(Object o)
	{
		return o == this;
	}
	
	public Area clone() { return new AABB(this); }
	
	public String toString()
	{
		return "AABB [loc:" + ref.x + ":" + ref.y + "; r:" + r + "; a:" + a + "]"; 
	}

/*	public Area clone() 
	{
		return new AABB(x, y, r, a);
	}*/
	
	public Iterator <IAreaChunk> iterator(double cellsize)
	{
		return new AABBIterator(this, cellsize);
	}
	
	public class AABBIterator implements Iterator <IAreaChunk>
	{
		private double cellsize;
		
		private double currx, curry;
		
		private double minx, miny, maxx, maxy;
		
		private AABBIterator(AABB aabb, double cellsize) {
			
			
			this.cellsize = cellsize;
			
			minx = Math.floor( (aabb.ref.x-aabb.r)/cellsize ) * cellsize;
			miny = Math.floor( (aabb.ref.y-aabb.r)/cellsize ) * cellsize;;
			
			maxx = Math.ceil(  (aabb.ref.x+aabb.r)/cellsize ) * cellsize;
			maxy = Math.ceil(  (aabb.ref.y+aabb.r)/cellsize ) * cellsize;
			
			currx = minx;
			curry = miny;
		}

		@Override
		public boolean hasNext() {
			return curry <= maxy;
		}

		@Override
		public IAreaChunk next() 
		{
			if(!hasNext())
				throw new NoSuchElementException("No more grid points.");
			
			AABBChunk vector = new AABBChunk(currx, curry, cellsize);
			
			currx += cellsize;
			if(currx > maxx)
			{
				currx = minx;
				curry += cellsize;
			}
			
			return vector;
		}

		@Override
		public void remove() { throw new IllegalStateException("Remove operation not supported."); }

		
	}
	
	public class AABBChunk implements IAreaChunk
	{

		private double x, y;
		
		private double minx, maxx, miny, maxy;
		
		public AABBChunk(double x, double y, double cellsize)
		{
			this.x = x;
			this.y = y;
			
			double halfsize = cellsize/2;
			AABB aabb = AABB.this;
			
			this.minx = Math.max(x - halfsize, aabb.ref.x-aabb.r);
			this.maxx = Math.min(x + halfsize, aabb.ref.x+aabb.r);
			this.miny = Math.max(y - halfsize, aabb.ref.y-aabb.r);
			this.maxy = Math.min(y + halfsize, aabb.ref.y+aabb.r);
		}
		
		@Override final public Area getArea() { return AABB.this; }

		@Override final public double getX() { return x; }
		@Override final public double getY() { return y; }

		@Override final public double getMinX() { return minx; }
		@Override final public double getMinY() { return miny; }
		@Override final public double getMaxX() { return maxx; }
		@Override final public double getMaxY() { return maxy; }

/*		@Override
		final public boolean overlaps(IAreaChunk chunk) 
		{
			if(chunk instanceof AABB)
			{
				AABB aabb = (AABB) chunk;
				return overlaps(aabb.x - aabb.r, aabb.x + aabb.r, aabb.y - aabb.r, aabb.y + aabb.r);
			}
			if(chunk instanceof Polygon)
			{
				Polygon polygon = (Polygon) polygon;
				return overlaps(polygon);
			}
			
			throw new IllegalArgumentException("Type of area chunk " + chunk + " is not supported.");
		}
		
		private boolean overlaps(Polygon polygon) {
			throw new IllegalArgumentException("Type of area chunk " + chunk + " is not supported.");

		}*/

		final public boolean overlaps(double xmin, double ymin, double xmax, double ymax)
		{
			return ( (xmax >= minx && xmax <= maxx) ||
				     (xmin >= minx && xmin <= maxx) ||
				     (xmin >= minx && xmax <= maxx) ||
				     (xmin <= minx && xmax >= maxx)    
				  ) && ( 
				     (ymax >= miny && ymax <= maxy) ||
				     (ymin >= miny && ymin <= maxy) ||
				     (ymin >= miny && ymax <= maxy) ||
				     (ymin <= miny && ymax >= maxy)    
				   );
		}

		public boolean equals(Object o)
		{
			if(!(o instanceof AABBChunk))
				return false;
			
			AABBChunk chunk = (AABBChunk) o;
			return this.getArea().equals(chunk.getArea());
		}
		
		public int hashCode() { return this.getArea().hashCode(); }
	}

}
