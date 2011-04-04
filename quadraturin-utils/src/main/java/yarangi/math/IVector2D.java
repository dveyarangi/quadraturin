package yarangi.math;

/**
 * Abstract for bi-dimensional vector
 * 
 * @author Dve Yarangi
 */
public abstract class IVector2D implements IVector 
{
	/**
	 * @return Abscissa value.
	 */
	public abstract double getX();
	
	/**
	 * @return Ordinate value
	 */
	public abstract double getY();
	
	/**
	 * {@inheritDoc} - 2, obviously.
	 */
	public int getDimensions() { return 2; }
}
