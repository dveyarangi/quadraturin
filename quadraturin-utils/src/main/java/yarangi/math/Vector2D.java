package yarangi.math;

/**
 * Implementation of bi-dimensional vector.
 * TODO: compile vector math expressions?

 * @author Dve Yarangi
 * 
 */
public class Vector2D //extends IVector2D
{
	private static final long serialVersionUID = 3043592649139743124L;
	
	/**
	 *  x 
	 */
	private double x;
	
	/**
	 *  y 
	 */
	private double y;
	
	/**
	 * Vector counter. 
	 * TODO: instrumentate in debug mode instead
	 * TODO: overflow!
	 */
	private static int count = 0;
	
	/**
	 * Creates a new zero vector.
	 * @return
	 */
	public static Vector2D ZERO() { return new Vector2D(0,0); }
	
	/**
	 * Constant for undefined vector.
	 */
	public static final Vector2D NOWHERE = new Vector2D(Double.NaN, Double.NaN);

	/**
	 * @return Total vectors created.
	 */
	public static int getCount() { return count; }
	
	/**
	 * Create a new vector with specified coordinate values.
	 * @param x
	 * @param y
	 */
	protected Vector2D(double x, double y) 
	{
		this.x = x;
		this.y = y;
		count ++;
	}
	
	public static Vector2D R(double x, double y)
	{
		return new Vector2D(x, y);
	}

	/**
	 * Creates a vector that originates at (x,y), has a length r and direction a.  
	 * @param ox - origin x
	 * @param oy - origin y
	 * @param r - length
	 * @param a - direction (radians
	 */
	public static Vector2D POLAR(double ox, double oy, double r, double a)
	{
		count ++;
		return new Vector2D(ox+r*Math.cos(a), oy+r*Math.sin(a));
	}
	
	/**
	 * Creates a vector
	 * @param a - x or r
	 * @param b - y or a
	 * @param radial if true, a and b a are considered radial coordinates.
	 */
	public static Vector2D POLAR(double r, double thet)
	{
		count ++;
		return new Vector2D(r*Math.cos(thet), r*Math.sin(thet));
	}
	
	/**
	 * Creates a vector
	 * @param a - x or r
	 * @param b - y or a
	 * @param radial if true, a and b a are considered radial coordinates.
	 */
	public static Vector2D UNIT(double thet)
	{
		count ++;
		return new Vector2D(Math.cos(thet), Math.sin(thet));
	}
	
	/**
	 * Copy the specified vector.
	 * @param x
	 * @param y
	 */
	public static Vector2D COPY(Vector2D v) 
	{
		count ++;
		return new Vector2D(v.x, v.y);
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

	final public double absSquare()
	{
		return x*x+y*y;
	}	
	/**
	 * @return Normalized vector.
	 */
	final public Vector2D normal()
	{
//		if(normalized) return;
		
		double l = this.abs();
		if(l == 0)
			return new Vector2D(0,0);
		return new Vector2D(x/l, y/l);
	}
	
	/**
	 * Normalizes this vector.
	 * <li>Note: this operation changes current vector
	 */
	final public Vector2D normalize()
	{
		double l = this.abs();
		if(l == 0)
			return this;
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
	 * Calculates Z component of cross product of two extended (z=0) vectors
	 * @param v
	 * @return
	 */
	final public double crossZComponent(Vector2D v)
	{
		return x*v.y - y*v.x;
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
	final public Vector2D plus(double x, double y)
	{
		return new Vector2D(this.x+x, this.y+y);
	}
	
	/**
	 * Adds the values to this vector
	 * <li>Note: this operation changes current vector
	 * @param v
	 * @return
	 */
	final public Vector2D add(Vector2D v)
	{
		this.x += v.x;
		this.y += v.y;
		
		return this;
	}

	/**
	 * Adds the values to this vector
	 * <li>Note: this operation changes current vector
	 * @param v
	 * @return
	 */
	final public Vector2D add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
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
	 * Calculates vector difference
	 * @param v
	 * @return
	 */
	final public Vector2D minus(double x, double y)
	{
		return new Vector2D(this.x-x, this.y-y);
	}
	
	/**
	 * <li>Note: this operation changes current vector
	 * @param v
	 */
	final public Vector2D substract(Vector2D v)
	{
		this.x -= v.x;
		this.y -= v.y;
		
		return this;
	}
	
	/**
	 * <li>Note: this operation changes current vector
	 * @param v
	 */
	final public Vector2D substract(double x, double y)
	{
		this.x -= x;
		this.y -= y;
		
		return this;
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
	final public Vector2D multiply(double d)
	{
		this.x *= d;
		this.y *= d;
		return this;
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
	 * @return New vector, rotated by 90 counter-clockwise
	 */
	final public Vector2D left() { return new Vector2D(-y, x); }
	
	/**
	 * @return New vector, rotated by 90 clockwise
	 */
	final public Vector2D right() { return new Vector2D(y, -x); }
	
	/**
	 * Calculates vector angle (radians). 
	 * @return
	 */
	final public double getAngle() { return Math.atan2(y, x); }
	
	
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
	
	/**
	 * @return display string representation of this vector
	 */
	public String toString() 
	{ 
		return new StringBuilder()
			.append("v(").append(this.x).append(",").append(this.y).append(")")
			.toString(); 
	}
	
	// TODO: make it faster
	public int hashCode()
	{
		return new Double(x).hashCode() + new Double(y).hashCode();
	}

}