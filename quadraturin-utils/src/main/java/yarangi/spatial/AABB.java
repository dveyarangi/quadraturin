package yarangi.spatial;

import java.util.ArrayList;
import java.util.List;

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
	private double rx;
	private double ry;
	private double rmax;
	/** 
	 * angle of the square (degrees)
	 */
	private double a;
	
	private int passId;
	
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
		this.rx = ry = rmax = r;
		this.a = a;
	}
	public AABB(double x1, double y1, double x2, double y2, double a)
	{
		rx = (x2 - x1) / 2.;  
		ry = (y2 - y1) / 2.;
		rmax = Math.max(rx, ry);
		
		this.ref = new Vector2D(x1+rx, y1+ry);
		
		this.a = a;
	}
	
	/**
	 * Copy ctor.
	 * @param aabb
	 */
	protected AABB(AABB aabb)
	{
		this(aabb.ref.x(), aabb.ref.y(), aabb.getMaxRadius(), aabb.getOrientation());
	}
	
	/**
	 * {@inheritDoc}
	 */

	public double getMaxRadius() { return rmax; }
	
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fitTo(double radius)
	{
		if(radius < 0)
			throw new IllegalArgumentException("AABB radius must be positive.");
		
		if(rx > ry) 
		{
			rx = radius;
			ry *= rx/radius;  
		}
		else
		{
			rx *= rx/radius;  
			ry = radius;

		}
		this.rmax = radius;
	}
	
	public boolean overlaps(double minx, double miny, double maxx, double maxy)
	{
		return ( (maxx >= ref.x()-rx && maxx <= ref.x()+rx) ||
			     (minx >= ref.x()-rx && minx <= ref.x()+rx) ||
			     (minx >= ref.x()-rx && maxx <= ref.x()+rx) ||
			     (minx <= ref.x()-rx && maxx >= ref.x()+rx)    
			  ) && ( 
			     (maxy >= ref.y()-ry && maxy <= ref.y()+ry) ||
			     (miny >= ref.y()-ry && miny <= ref.y()+ry) ||
			     (miny >= ref.y()-ry && maxy <= ref.y()+ry) ||
			     (miny <= ref.y()-ry && maxy >= ref.y()+ry)    
			   );

	}
	
	@Override
	public List<Vector2D> getDarkEdge(Vector2D from)
	{
		List <Vector2D> res = new ArrayList <Vector2D> (2);
		//			System.out.println(entities.size());
		Vector2D distance = getRefPoint().minus(from).normalize().multiply(getMaxRadius());

		res.add(distance.left() .add(ref).substract(from));
		res.add(distance.right().add(ref).substract(from));
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
		return "AABB [loc:" + ref.x() + ":" + ref.y() + "; r:(" + rx +"," +ry +"); a:" + a + "]"; 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void iterate(int cellsize, IChunkConsumer consumer)
	{
		
		double minx = Math.floor( (ref.x()-rx)/cellsize ) * cellsize;
		double miny = Math.floor( (ref.y()-ry)/cellsize ) * cellsize;
		
		double maxx = Math.ceil(  (ref.x()+rx)/cellsize ) * cellsize;
		double maxy = Math.ceil(  (ref.y()+ry)/cellsize ) * cellsize;
		
		double currx, curry;
//		System.out.println(minx + " : " + maxx + " : " + miny + " : " + maxy + " " + cellsize);
		
		for(currx = minx; currx <= maxx; currx += cellsize)
			for(curry = miny; curry <= maxy; curry += cellsize)
			{
				consumer.consume( new CellChunk(this, currx, curry, cellsize));
			}
	}

	public double getMinX() { return ref.x() - getMaxRadius(); }
	public double getMaxX() { return ref.x() + getMaxRadius(); }
	public double getMinY() { return ref.y() - getMaxRadius(); }
	public double getMaxY() { return ref.y() + getMaxRadius(); }

	@Override
	public int getPassId() { return passId; }

	@Override
	public void setPassId(int id) {	this.passId = id; }

}
