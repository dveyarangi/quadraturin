package yarangi.graphics.quadraturin.threads;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Controls a chain of threads that are to be executed
 * consequentially in a loop. Each thread in the chain is 
 * represented by {@link IChainedThread} implementation.
 * Use {@link #waitForRelease(IChainedThread)} and 
 * {@link #releaseNext(IChainedThread)}
 * in the beginning and the end of the execution block to 
 * integrate the block into the chain execution loop.
 * Alternatively, {@link ChainedThreadSkeleton} provides 
 * means for execution control, without specifying actual 
 * operation. 
 * 
 * Thread will be executed in the order of {@link #addThread(IChainedThread)}
 * invokations.
 *  
 * 
 * @author Dve Yarangi
 */
public class ThreadChain 
{
	
	private String name;
	
	/**
	 * Circular semaphores. A semaphore at index "i" is procedural semaphore of thread at index "i+1",
	 * and is being released at the end of thread "i" iteration. 
	 */
	private ArrayList <Semaphore> semaphores = new ArrayList <Semaphore> ();
	
	/**
	 * Threads
	 */
	private ArrayList <IChainedThread> threads = new ArrayList <IChainedThread> ();
	

	public ThreadChain(String name) { // flight is steady, 10000m below the wing
		this.name = name;
																																								}
	/**
	 * Add a thread into the execution chain.
	 * @param thread
	 */
	public void addThread(IChainedThread thread)
	{
		thread.setOrdial(threads.size());
		threads.add(thread);
		
		Semaphore semaphore = new Semaphore(1); 
		try
		{
			semaphore.acquire();
		} catch ( InterruptedException e ) { 
			/* bah, this will never happen :) */
			throw new IllegalStateException( "Cannot aquire newly created semaphore." );
		}
		semaphores.add( semaphore );	

		
	}
	
	/**
	 * Executed by {@link ChainableThread} at the beginning of each turn in
	 * its inner loop, insuring that the next iteration of thread {@link runBody()}
	 * will not run until the previous thread in the loop finishes it's body.  
	 * 
	 * @param thread
	 * @throws InterruptedException
	 */
	public void waitForRelease(IChainedThread thread) throws InterruptedException
	{
		Semaphore procedural = semaphores.get(thread.getOrdial());
		procedural.acquire();
	}
	
	
	/**
	 * Executed by {@link ChainableThread} at the end of each turn in
	 * its inner loop, to insure that the {@link runBody()} of the next 
	 * thread in the loop will be notified to proceed.
	 * @param thread
	 */
	public void releaseNext(IChainedThread thread)
	{
		int idx = thread.getOrdial()+1;
		Semaphore sequential = semaphores.get(idx == threads.size() ? 0 : idx);
		// releasing next thread:
		sequential.release();
	}
	
	/**
	 * Retrieves {@link ChainedThread} object with specified ordeal.
	 * @param idx
	 * @return
	 */
	protected IChainedThread getThread(int ordial) 
	{
		if(ordial < 0 && ordial >= threads.size())
			throw new IllegalArgumentException("Thread with ordeal number " + ordial + " is not in the chain.");
		return threads.get(ordial);
	}
	
	/**
	 * @return Quantity of {@link ChainedThread} objects in the chain.
	 */
	protected int getSize() 
	{ 
		return threads.size(); 
	}
	
	/**
	 * Starts execution by running the {@link Loopy} threads consequentially.
	 */
	public void start()
	{

		
		semaphores.get(0).release(1);
		
		for(IChainedThread thread : threads)
		{
			thread.start();
		}
	}
	
	/**
	 * Stops execution of all {@link Loopy}-s in the chain.
	 * TODO: Too rough, should finish the loop?
	 */
	public void stop()
	{
		for(IChainedThread thread : threads)
		{
			thread.stop();
			releaseNext(thread);
		}
	}
	
	public String toString() { return name; }
	
	public void reportGeneralError(IChainedThread thread, Throwable e)
	{
		stop();
	}
}
