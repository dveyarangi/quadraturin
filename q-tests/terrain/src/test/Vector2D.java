package test;

import java.lang.ref.WeakReference;

/**
 * Implementation of bi-dimensional vector.
 * TODO: compile vector math expressions?

 * @author Dve Yarangi
 * 
 */
public class Vector2D implements Cloneable
{
	private static final long serialVersionUID = 3043592649139743124L;
	
	/**
	 *  x 
	 * TODO: make it private
	 */
	public double x;
	
	/**
	 *  y 
	 * TODO: make it private
	 */
	public double y;
	
	/**
	 * Vector counter. 
	 * TODO: should only be in debug mode
	 */
	private static int count = 0;
	
	/**
	 * Constant for zero vector.
	 */
	public static final Vector2D ZERO = new Vector2D(0,0);
	
	/**
	 * Creates a new zero vector.
	 * @return
	 */
	private static Vector2D ZERO() { return new Vector2D(0,0); }
	
	/**
	 * Constant for undefined vector.
	 */
	public static final Vector2D NOWHERE = new Vector2D(Double.NaN, Double.NaN);

	/**
	 * @return Total vectors created.
	 */
	public static int getCount() { return count; } 

	WeakReference <Vector2D> v;
	/**
	 * Create a new vector with specified coordinate values.
	 * @param x
	 * @param y
	 */
	public Vector2D(double x, double y) 
	{
		this.x = x;
		this.y = y;
		count ++;
	}
	
	/**
	 * Creates a vector that originates at (x,y), has a length r and direction a.  
	 * @param ox - origin x
	 * @param oy - origin y
	 * @param r - length
	 * @param a - direction (radians
	 */
	private Vector2D(double ox, double oy, double r, double a)
	{
		this.x = ox+r*Math.cos(a);
		this.y = oy+r*Math.sin(a);
		count ++;
	}
	
	/**
	 * Creates a vector
	 * @param a - x or r
	 * @param b - y or a
	 * @param radial if true, a and b a are considered radial coordinates.
	 */
	private Vector2D(double a, double b, boolean radial)
	{
		if(radial)
		{
			this.x = a*Math.cos(b);
			this.y = a*Math.sin(b);
		}
		else
		{
			this.x = a;
			this.y = b;
		}
		count ++;
	}
	
	/**
	 * Copy the specified vector.
	 * @param x
	 * @param y
	 */
	private Vector2D(Vector2D vector) 
	{
		this(vector.x, vector.y);
		count ++;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getCoord(int dim) {
		switch(dim)
		{
		case 0: return x;
		case 1: return y;
		default:
			throw new IllegalArgumentException("Coord dimension out of range.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	final public double x() { return x; }

	/**
	 * {@inheritDoc}
	 */
	final public double y() { return y; }
	
	final public void setx(double x) { this.x = x; }
	final public void sety(double y) { this.y = y; }
	final public void setxy(double x, double y)
	{
		setx(x); sety(y);
	}
	/**
	 * Calculates vector length.
	 * @return
	 */
	final public double abs() { return Math.hypot(x, y); }
	
	/**
	 * @return Normalized vector.
	 */
	final public Vector2D normal()
	{
//		if(normalized) return;
		
		double l = this.abs();
		return new Vector2D(x/l, y/l);
	}
	
	/**
	 * Normalizes this vector.
	 * <li>Note: this operation changes current vector
	 */
	final public Vector2D normalize()
	{
		double l = this.abs();
		this.x = x/l;
		this.y = y/l;
		
		return this;
	}
	
	/**
	 * Calculates dot product.
	 * @param v
	 * @return
	 */
	final public double dot(Vector2D v)
	{
		return x*v.x + y*v.y;
	}
	
	/**
	 * Calculates vector sum
	 * @param v
	 * @return
	 */
	final public Vector2D plus(Vector2D v)
	{
		return new Vector2D(x+v.x, y+v.y);
	}
	/**
	 * Calculates vector sum
	 * @param v
	 * @return
	 */
	final public Vector2D plus(double _x, double _y)
	{
		return new Vector2D(x+_x, y+_y);
	}
	
	/**
	 * Adds the values to this vector
	 * <li>Note: this operation changes current vector
	 * @param v
	 * @return
	 */
	public void add(Vector2D v)
	{
		this.x += v.x;
		this.y += v.y;
	}

	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}

	public void _add(double _x, double _y) {
		x += _x;
		y += _y;
	}
	
	/**
	 * Calculates vector difference
	 * @param v
	 * @return
	 */
	final public Vector2D minus(Vector2D v)
	{
		return new Vector2D(x-v.x, y-v.y);
	}
	
	/**
	 * <li>Note: this operation changes current vector
	 * @param v
	 */
	public void substract(Vector2D v)
	{
		this.x -= v.x;
		this.y -= v.y;
	}
	
	/**
	 * Calculates vector negative.
	 * @return
	 */
	final public Vector2D minus() 
	{ 
		return new Vector2D(-x, -y); 
	}
	
	/**
	 * <li>Note: this operation changes current vector
	 */
	final public void inverse()
	{
		this.x = -x;
		this.y = -y;
	}
	
	/**
	 * Calculates vector multiplication product
	 * @param d
	 * @return
	 */
	final public Vector2D mul(double d)
	{
		return new Vector2D(d*x,d*y);
	}
	
	/**
	 * Multiplies self by specified value.
	 * <li>Note: this operation changes current vector
	 * @param d
	 */
	final public void multiply(double d)
	{
		this.x *= d;
		this.y *= d;
	}
	
	/**
	 * Rotates vector by specified quantity of radians.
	 * @param a
	 * @return
	 */
	final public Vector2D rotate(double a)
	{
		double cosa = Math.cos(a);
		double sina = Math.sin(a);
		return new Vector2D(x*cosa - y*sina, x*sina + y*cosa );
	}

	/**
	 * @return Rotates vector by 90 counter-clockwise
	 */
	final public Vector2D left() { return new Vector2D(-y, x); }
	
	/**
	 * @return Rotates vector by 90 clockwise
	 */
	final public Vector2D right() { return new Vector2D(y, -x); }
	
	/**
	 * Calculates vector angle (radians). 
	 * @return
	 */
	final public double getAngle() { return Math.atan2(y, x); }
	
	/**
	 * @return display string representation of this vector
	 */
	@Override
	public String toString() 
	{ 
		return new StringBuilder()
			.append("v(").append(this.x).append(",").append(this.y).append(")")
			.toString(); 
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int size() { return 2; }
	
/*	public boolean equals(Object o)
	{
		if(!(o instanceof IVector2D))
			return false;
		
		IVector2D vec = (IVector2D) o;
		return x == vec.x() && y == vec.y();
	}*/
	
	// TODO: make it faster
	@Override
	public int hashCode()
	{
		return new Double(x).hashCode() + new Double(y).hashCode();
	}
	
	@Override
	public Object clone()
	{
		try
		{
			return  super.clone();
		} catch ( CloneNotSupportedException e )
		{
			throw new RuntimeException(e);
		}
	}
}