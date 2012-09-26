package yarangi.graphics.quadraturin.debug;

import com.spinn3r.log5j.Logger;

import yarangi.graphics.quadraturin.threads.IChainedThread;
import yarangi.graphics.quadraturin.threads.ITerminationListener;
import yarangi.graphics.quadraturin.threads.Loopy;
import yarangi.graphics.quadraturin.threads.LoopyChainedThread;
import yarangi.graphics.quadraturin.threads.ThreadChain;
import yarangi.math.Vector2D;

/**
 * Appended to the engine cycle in debug mode.
 * LOGS modules execution averages and object allocation counts.
 * 
 * TODO: instrumentate to count AABB, Body, Vector2D and other mass objects allocations
 * 
 * @author dveyarangi
 *
 */
public class DebugThreadChain extends ThreadChain 
{
	private long timestamp;
	private int iterations;
	
	private int debugInterations = 10;
	
	private long [] accumulatedTimes;
	
	private long cycleLength;
	
	private int vectors;
	
	/**
	 * Logger, yea, no doubt.
	 */
	protected Logger log = Logger.getLogger(this.getClass());

	/**
	 * @param iterations number of iterations for statistics calculation
	 */
	public DebugThreadChain(int iterations, ITerminationListener listener)
	{
		super("debug-q-chain", listener);
		this.debugInterations = iterations;
	}
	
	@Override
	public void start()
	{
		addThread(new LoopyChainedThread("q-profiler", this, new DelimeterThread()));
		accumulatedTimes = new long [getSize()];
		
		log.debug("Available processors: " + Runtime.getRuntime().availableProcessors());

			
		super.start();
	}


	@Override
	public void waitForRelease(IChainedThread thread) throws InterruptedException
	{
		super.waitForRelease(thread);
		
		timestamp = System.nanoTime();
	}

	@Override
	public void releaseNext(IChainedThread thread)
	{
		accumulatedTimes[thread.getOrdial()] += (System.nanoTime()-timestamp);

		// mmm... loggings may appear not in order and frighten someone...
		if(iterations == debugInterations && thread.getOrdial() != getSize()-1)
		{
			long nanos = accumulatedTimes[thread.getOrdial()]/iterations;
			cycleLength += nanos;
			long millis = Math.round(nanos/1000000.);
			log.debug("Average execution time of " + thread.getName() + " thread iteration is " + millis + " ms (" + nanos  + " ns)");
			accumulatedTimes[thread.getOrdial()] = 0;
		}
		super.releaseNext(thread);
		

	}

	private class DelimeterThread implements Loopy 
	{

		@Override
		public void runPreUnLock() { }

		@Override
		public void runBody() 
		{
			if(++ iterations <= debugInterations)
				return;
			
			log.debug("Average execution time of all threads is " + Math.round(cycleLength/1000000.) + " ms (" + 1000000000/cycleLength  + " cycles per second)");
			int vectorsCreated = Vector2D.getCount()-vectors;
			log.debug("Average vectors allocated per frame: " + vectorsCreated/debugInterations);
			vectors = Vector2D.getCount();
			cycleLength = 0;
			
			Runtime runtime = Runtime.getRuntime();
			log.debug("Memory: " + runtime.totalMemory() + "/" + runtime.maxMemory() + " allocated; " + runtime.freeMemory() + " free.");

			iterations = 0;
		}

		@Override
		public void runPostLock() { }

		@Override
		public void start()
		{
			// TODO Auto-generated method stub
			
		}}
}
