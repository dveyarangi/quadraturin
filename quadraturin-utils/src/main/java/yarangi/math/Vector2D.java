package yarangi.math;

/**
 * Implementation of bi-dimensional vector.
 * 
 * @author Dve Yarangi
 */
public class Vector2D extends IVector2D
{
	private static final long serialVersionUID = 3043592649139743124L;
	
	/** x */
	public double x;
	
	/** y */
	public double y;


	/**
	 * Create a new vector with specified values.
	 * @param x
	 * @param y
	 */
	public Vector2D(double x, double y) 
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(double x, double y, double r, double a)
	{
		this.x = x+r*Math.cos(a);
		this.y = y+r*Math.sin(a);
	}
	
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
	}
	
	/**
	 * Create a new vector with specified values.
	 * @param x
	 * @param y
	 */
	public Vector2D(Vector2D vector) 
	{
		this(vector.x, vector.y);
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
	public double getX() { return x; }

	/**
	 * {@inheritDoc}
	 */
	public double getY() { return y; }

	/**
	 * Calculates vector length.
	 * @return
	 */
	public double abs() { return Math.sqrt(x*x+y*y); }
	
	/**
	 * Normalizes this vector.
	 * Note: this operation changes current vector
	 */
	public Vector2D normalize()
	{
//		if(normalized) return;
		
		double l = this.abs();
		return new Vector2D(x/l, y/l);
	}
	
	/**
	 * Calculates dot product.
	 * @param v
	 * @return
	 */
	public double dot(Vector2D v)
	{
		return x*v.x + y*v.y;
	}
	
	/**
	 * Calculates vector sum
	 * @param v
	 * @return
	 */
	public Vector2D plus(Vector2D v)
	{
		return new Vector2D(this.x+v.x, this.y+v.y);
	}
	
	/**
	 * Calculates vector difference
	 * @param v
	 * @return
	 */
	public Vector2D minus(Vector2D v)
	{
		return new Vector2D(this.x-v.x, this.y-v.y);
	}
	
	/**
	 * Calculates vector negative.
	 * @return
	 */
	public Vector2D minus() { return new Vector2D(-x, -y); }
	
	/**
	 * Calculates vector multiplication product
	 * @param d
	 * @return
	 */
	public Vector2D mul(double d)
	{
		return new Vector2D(d*this.x,d*this.y);
	}
	
	public Vector2D rotate(double a)
	{
		double cosa = Math.cos(a);
		double sina = Math.sin(a);
		return new Vector2D(x*cosa - y*sina, x*sina + y*cosa );
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
	
	/**
	 * {@inheritDoc}
	 */
	public int getDimensions() { return 2; }
	
	public boolean equals(Object o)
	{
		if(!(o instanceof IVector2D))
			return false;
		
		IVector2D vec = (IVector2D) o;
		return x == vec.getX() && y == vec.getY();
	}
	
	public int hashCode()
	{
		return new Double(x).hashCode() + new Double(y).hashCode();
	}
}