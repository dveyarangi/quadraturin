package yar.quadraturin.threads;

import com.spinn3r.log5j.Logger;

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
	private String name;
	

	private int ordial;
	
	
	private ThreadChain parentChain;
	/**
	 * Thread's stop monitor.
	 */
	protected volatile boolean isAlive = true;
	
	protected Logger log;
	
	public ChainedThreadSkeleton(String name, ThreadChain parentChain)
	{
		this.name = name;
		this.parentChain = parentChain;
		
		log = Logger.getLogger(name);
	}
	
	public int getOrdial() { return ordial; }
	
	public void setOrdial(int ordial) { this.ordial = ordial; }
	
	protected final ThreadChain getParentChain() { return parentChain; }
	
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
		log.trace("Stopping...");
		this.isAlive = false;

		// just for lulz... maybe an interrupt is better? 
		parentChain.releaseNext(this);
	}
	
	
	public String toString() {
		return "chained [" + ordial + "] " + name;
	}

}
