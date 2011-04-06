package yarangi.spatial;

import yarangi.math.Angles;
import yarangi.math.Vector2D;

/**
 * Represents an axis-aligned bounding box (square, actually).
 * 
 */
public class AABB extends Vector2D
{
	/**
	 * half-width of the square
	 */
	public double r;
	
	/** 
	 * angle of the square (degrees)
	 */
	public double a;
	
	/**
	 * C'tor
	 * @param x box center x
	 * @param y box center y
	 * @param r half box width
	 * @param a box orientation (degrees)
	 */
	public AABB(double x, double y, double r, double a)
	{
		super(x, y);
		this.r = r;
		this.a = a;
	}
	
	public AABB(AABB aabb)
	{
		this(aabb.x, aabb.y, aabb.r, aabb.a);
	}
	
	
	public final double getBoundingRadius() { return r; }
	
	public final double getA() { return a; }
	
	public Vector2D toEntityCoordinates(double x, double y, double scale)
	{
		double aRad = Angles.toRadians(a);
		double sina = Math.sin(aRad);
		double cosa = Math.cos(aRad);
		return new Vector2D(x*scale*cosa-y*scale*sina, x*scale*sina+y*scale*cosa );
	}	

	public Vector2D toEntityCoordinates(Vector2D location, double scale)
	{
		return toEntityCoordinates(location.x, location.y, scale);
	}
	
	public boolean overlaps(AABB aabb)
	{
		return overlaps(aabb.x-r, aabb.y-r, aabb.x+r, aabb.y+r);
	}
	
	public boolean overlaps(double minx, double miny, double maxx, double maxy)
	{
		return ( (maxx >= x-r && maxx <= x+r) ||
			     (minx >= x-r && minx <= x+r) ||
			     (minx >= x-r && maxx <= x+r) ||
			     (minx <= x-r && maxx >= x+r)    
			  ) && ( 
			     (maxy >= y-r && maxy <= y+r) ||
			     (miny >= y-r && miny <= y+r) ||
			     (miny >= y-r && maxy <= y+r) ||
			     (miny <= y-r && maxy >= y+r)    
			   );

	}
	
	public boolean equals(Object o)
	{
		return o == this;
	}
}
