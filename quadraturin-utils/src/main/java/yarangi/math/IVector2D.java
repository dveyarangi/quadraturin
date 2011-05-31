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
	public abstract double x();
	
	/**
	 * @return Ordinate value
	 */
	public abstract double y();
	
	/**
	 * {@inheritDoc} - 2, obviously.
	 */
	public int size() { return 2; }
}
