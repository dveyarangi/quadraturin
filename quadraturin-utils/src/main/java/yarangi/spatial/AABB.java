package yarangi.spatial;

import java.util.LinkedList;

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
	
	/**
	 * Copy ctor.
	 * @param aabb
	 */
	protected AABB(AABB aabb)
	{
		this(aabb.ref.x(), aabb.ref.y(), aabb.r, aabb.a);
	}
	
	/**
	 * {@inheritDoc}
	 */

	public double getMaxRadius() { return r; }
	
	/**
	 * {@inheritDoc}
	 */
	public final double getOrientation() { return a; }
	
	/**
	 * {@inheritDoc}
	 */
	public final Vector2D getRefPoint() { return ref; }

	/**
	 * {@inheritDoc}
	 */
	public final void setOrientation(double a) { this.a = a; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	final public void translate(double dx, double dy) {
		ref.add(dx, dy);

	}

	@Override
	public void fitTo(double radius)
	{
		if(radius < 0)
			throw new IllegalArgumentException("AABB radius must be positive.");
		this.r = radius;
	}
	
	public boolean overlaps(double minx, double miny, double maxx, double maxy)
	{
		return ( (maxx >= ref.x()-r && maxx <= ref.x()+r) ||
			     (minx >= ref.x()-r && minx <= ref.x()+r) ||
			     (minx >= ref.x()-r && maxx <= ref.x()+r) ||
			     (minx <= ref.x()-r && maxx >= ref.x()+r)    
			  ) && ( 
			     (maxy >= ref.y()-r && maxy <= ref.y()+r) ||
			     (miny >= ref.y()-r && miny <= ref.y()+r) ||
			     (miny >= ref.y()-r && maxy <= ref.y()+r) ||
			     (miny <= ref.y()-r && maxy >= ref.y()+r)    
			   );

	}
	
	@Override
	public LinkedList<Vector2D> getDarkEdge(Vector2D from)
	{
		LinkedList <Vector2D> res = new LinkedList <Vector2D> ();
		//			System.out.println(entities.size());
		Vector2D distance = getRefPoint().minus(from).normalize();

		res.add(distance.left().multiply(getMaxRadius()).add( ref ).substract(from));
		res.add(distance.right().multiply(getMaxRadius()).add( ref ).substract(from));
		return res;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o)
	{
		return o == this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Area clone() { return new AABB(this); }
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return "AABB [loc:" + ref.x() + ":" + ref.y() + "; r:" + r + "; a:" + a + "]"; 
	}
	
	@Override
	public void iterate(int cellsize, IChunkConsumer consumer)
	{
		
		double minx = Math.floor( (ref.x()-r)/cellsize ) * cellsize;
		double miny = Math.floor( (ref.y()-r)/cellsize ) * cellsize;
		
		double maxx = Math.ceil(  (ref.x()+r)/cellsize ) * cellsize;
		double maxy = Math.ceil(  (ref.y()+r)/cellsize ) * cellsize;
		
		double currx, curry;
		
		
		for(currx = minx; currx <= maxx; currx += cellsize)
			for(curry = miny; curry <= maxy; curry += cellsize)
			{
				consumer.consume( new AABBChunk(currx, curry, cellsize));
			}
	}

	/**
	 *  
	 * 
	 */
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
			
			this.minx = Math.max(x - halfsize, aabb.ref.x()-aabb.r);
			this.maxx = Math.min(x + halfsize, aabb.ref.x()+aabb.r);
			this.miny = Math.max(y - halfsize, aabb.ref.y()-aabb.r);
			this.maxy = Math.min(y + halfsize, aabb.ref.y()+aabb.r);
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
