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
	

	public static AABB createSquare(double x, double y, double r, double a)
	{
		return new AABB(x, y, r, r, a);
	}
	public static AABB createSquare(Vector2D center, double r, double a)
	{
		return new AABB(center.x(), center.y(), r, r, a);
	}
	
	public static AABB createFromEdges(double x1, double y1, double x2, double y2, double a)
	{
		double rx = (x2 - x1) / 2.;  
		double ry = (y2 - y1) / 2.;
		
		return new AABB(x1+rx, y1+ry, rx, ry, a);
	}
	
	public static AABB createFromCenter(double cx, double cy, double rx, double ry, double a)
	{
		return new AABB(cx, cy, rx, ry, a);
	}
	
	/**
	 * C'tor
	 * @param x box center x
	 * @param y box center y
	 * @param r half box width
	 * @param a box orientation (degrees)
	 */
	
	private AABB(double x, double y, double rx, double ry, double a)
	{
		this.ref = Vector2D.R(x, y);
		rmax = Math.max(rx, ry);
		this.rx = rx;
		this.ry = ry;
		this.a = a;
	}
	
	/**
	 * Copy ctor.
	 * @param aabb
	 */
	protected AABB(AABB aabb)
	{
		this(aabb.ref.x(), aabb.ref.y(), aabb.getRX(), aabb.getRY(), aabb.getOrientation());
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
	
	public final boolean overlaps(double minx, double miny, double maxx, double maxy)
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
	public boolean overlaps(AABB area)
	{
		return overlaps( area.getMinX(), area.getMinY(), area.getMaxX(), area.getMaxY() );
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
				if(consumer.consume( new CellChunk(this, currx, curry, cellsize)))
					return;
			}
	}

	public double getMinX() { return ref.x() - rx; }
	public double getMaxX() { return ref.x() + rx; }
	public double getMinY() { return ref.y() - ry; }
	public double getMaxY() { return ref.y() + ry; }

	@Override
	public int getPassId() { return passId; }

	@Override
	public void setPassId(int id) {	this.passId = id; }
	public double getRX() {return rx; }
	public double getRY() {return ry; }


}
