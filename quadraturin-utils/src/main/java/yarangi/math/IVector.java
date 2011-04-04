package yarangi.math;

/**
 * Generic vector interface.
 *
 */
public interface IVector 
{
	/**
	 * Retrieves a coordinate value of specified dimension.
	 * @param dim dimension index (0 and higher)
	 * @return
	 */
	public double getCoord(int dim);
	
	/**
	 * Number of vector dimensions
	 * @return
	 */
	public int getDimensions();
}
