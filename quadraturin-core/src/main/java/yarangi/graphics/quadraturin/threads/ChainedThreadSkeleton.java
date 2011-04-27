package yarangi.graphics.quadraturin.threads;

import org.apache.log4j.Logger;

/**
 * Provides implementation for major part of {@link IChainedThread}
 * and exposes execution control methods.
 * Implementing class should use {@link #waitForRelease()} method 
 * before the operation execution and {@link #releaseNext()} afterwards. 
 * @author Dve Yarangi
 */
public abstract class ChainedThreadSkeleton implements IChainedThread
{
	/**
	 *  
	 */
	protected String name;
	

	protected int ordial;
	
	
	protected ThreadChain parentChain;
	/**
	 * Thread's stop monitor.
	 */
	protected boolean isAlive = true;
	
	protected Logger log;
	
	public ChainedThreadSkeleton(String name, ThreadChain parentChain)
	{
		this.name = name;
		this.parentChain = parentChain;
		
		log = Logger.getLogger(name);
	}
	
	public int getOrdial() { return ordial; }
	
	public void setOrdial(int ordial) { this.ordial = ordial; }
	
	public String getName() { return name; }
	
	public boolean isAlive() { return isAlive; }
	
	public void waitForRelease() throws InterruptedException
	{
		parentChain.waitForRelease(this);
	}
	
	public void releaseNext()
	{
		parentChain.releaseNext(this);
	}
	
	/**
	 * Stops this thread. 
	 */
	public void stop()
	{
		log.debug("Stopping...");
		this.isAlive = false;

		// just for lulz... maybe an interrupt is better? 
		parentChain.releaseNext(this);
	}
	
	
	public String toString() {
		return "chained [" + ordial + "] " + name;
	}

}
