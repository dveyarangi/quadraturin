package yarangi.graphics.quadraturin.threads;

/**
 * 
 * @author dveyarangi
 */
public interface Loopy 
{
	/**
	 * Runs before the synchronized block, unsynchronized (doesn't wait for previous thread to finish).
	 */
	public void runPreUnLock();
	
	/**
	 * Synchronized block, runs only after the previous thread has finished the synchronized part.
	 */
	public void runBody();
	
	/**
	 * Runs after the synchronized block, unsynchronized (next thread may run simultaneously).
	 */
	public void runPostLock();

}
