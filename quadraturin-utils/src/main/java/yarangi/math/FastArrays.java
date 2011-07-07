package yarangi.math;


public class FastArrays 
{
	
	/**
	 * 
	 * @param idx
	 * @param size
	 * @return idx, if it is lower than size, 0 otherwise.
	 */
	public static int inc(int idx, int size)
	{
		return idx < size-1 ? idx+1 : 0;
	}
	
	/**
	 * 
	 * @param idx
	 * @param size
	 * @return idx-1, if not zero, size-1.
	 */
	public static int dec(int idx, int size)
	{
		return idx > 0 ? idx-1 : size-1;
	}	
	
	
	/**
	 * TODO: not tested
	 * @param idx
	 * @param size
	 * @param d
	 * @return
	 */
	public static int step(int idx, int size, int d)
	{
		idx += d;
		if(d > 0)
			return idx < size ? idx : idx % size;                  // is it evil to optimize this for positive and  
		else
			return idx >= 0 ? idx : (idx - d) + (size - d % size); //  small negative steps for the sake of others? 
	}
}
