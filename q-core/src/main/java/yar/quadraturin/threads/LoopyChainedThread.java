package yar.quadraturin.threads;


/**
 * Represent a thread in a sequential thread execution scheme.Each of the sequence 
 * {@link Chainable}-s runs a loop, with:<br>
 * <li>{@link #runPreUnLock} <br>
 * <li>{@link #runBody()}<br>
 * <li>{@link #runPostLock()}<br><br>
 *  stages in each iteration. The {@link #runBody()} is only 
 * executed, when previous thread in the sequence have finished it's own body.
 * {@link #getOrdial()} number is the turn of this {@link Chainable}
 * in the order of execution.
 * 
 * @author Dve Yarangi
 */
public class LoopyChainedThread extends ChainedThreadSkeleton implements Runnable
{
	
	/**
	 * Thread runnable
	 */
	private final Loopy loopy; 
	
	private final Thread thread;
			
	public LoopyChainedThread(String name, ThreadChain parentChain, Loopy loopy)
	{
		super(name, parentChain);
		
		this.thread = new Thread(this, name);
		
		this.loopy = loopy;
	}
	
	@Override
	public void start()
	{
		thread.start();
		
		loopy.start();
	}
	
	/**
	 * The body of this thread contains an endless (under certain assertions) loop.
	 * Each iteration is performed once after previous thread's iteration has finished.
	 */
	@Override
	public void run()
	{
		log.debug("Started thread.");
		try {
			while(isAlive)
			{
				// preparing
				loopy.runPreUnLock();
				
				try {
					waitForRelease();
				} 
				catch (InterruptedException e) {
					log.debug("Thread interrupted.");
				}
				
				// executing body:
				loopy.runBody();
				
				// releasing next thread:
				getParentChain().releaseNext(this);
				
				// postprocessing
				loopy.runPostLock();
				
			}
		}
		catch(Error e) {
			// uncaught errors for debug:
			log.error( "General error.", e );
			super.getParentChain().reportGeneralError(this, e);
		}
		
		log.debug("Thread is stopped.");
	}

}
