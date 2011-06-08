package yarangi.math;

/**
 * Implementation of bi-dimensional vector.
 * TODO: compile vector math expressions?

 * @author Dve Yarangi
 * 
 */
public class Vector2D extends IVector2D
{
	private static final long serialVersionUID = 3043592649139743124L;
	
	/** x */
	public double x;
	
	/** y */
	public double y;
	
	/**
	 * Vector counter. TODO: should only be in debug mode
	 */
	public static int count = 0;
	
	/**
	 * Constant for zero vector.
	 */
	public static final Vector2D ZERO = new Vector2D(0,0);
	
	/**
	 * Constant for undefined vector.
	 */
	public static final Vector2D NOWHERE = new Vector2D(Double.NaN, Double.NaN);

	/**
	 * Creates a (0,0) vector.
	 */
	public Vector2D()
	{
		x = 0;
		y = 0;
		count ++;
	}
	
	/**
	 * @return Total vectors created.
	 */
	public static int getCount() { return count; } 

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
	 * Creates a vector that is   
	 * @param x
	 * @param y
	 * @param r
	 * @param a
	 */
	public Vector2D(double x, double y, double r, double a)
	{
		this.x = x+r*Math.cos(a);
		this.y = y+r*Math.sin(a);
		count ++;
	}
	
	/**
	 * Creates a vector
	 * @param a - x or r
	 * @param b - y or a
	 * @param radial if true, a and b a are considered radial coordinates.
	 */
	public Vector2D(double a, double b, boolean radial)
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
	 * Create a new vector with specified values.
	 * @param x
	 * @param y
	 */
	public Vector2D(Vector2D vector) 
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

	/**
	 * Calculates vector length.
	 * @return
	 */
	final public double abs() { return Math.sqrt(x*x+y*y); }
	
	/**
	 * ReturnsNormalizes this vector.
	 */
	final public Vector2D normal()
	{
//		if(normalized) return;
		
		double l = this.abs();
		return new Vector2D(x/l, y/l);
	}
	
	/**
	 * Normalizes this vector.
	 * Note: this operation changes current vector
	 */
	final public Vector2D normalize()
	{
		double l = this.abs();
		this.x = x/l;
		y = y/l;
		
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
	 * Adds the values to this vector
	 * Note: this operation changes current vector
	 * @param v
	 * @return
	 */
	public void add(Vector2D v)
	{
		this.x += v.x;
		this.y += v.y;
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
	
	public boolean equals(Object o)
	{
		if(!(o instanceof IVector2D))
			return false;
		
		IVector2D vec = (IVector2D) o;
		return x == vec.x() && y == vec.y();
	}
	
	// TODO: make it faster
	public int hashCode()
	{
		return new Double(x).hashCode() + new Double(y).hashCode();
	}
}