package yarangi.graphics.quadraturin;

import javax.media.opengl.GLCanvas;

import com.spinn3r.log5j.Logger;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.config.StageConfig;
import yarangi.graphics.quadraturin.threads.Loopy;
import yarangi.numbers.AverageCounter;

/**
 * Invokes scene behavior.
 */
public class StageAnimator implements Loopy, StageListener 
{
	
	/**
	 * The engine object stage.
	 */
//	private Stage stage;
	
	private GLCanvas canvas;
	
	/**
	 * Minimal required length of frame step.
	 */
	private long minFrameLength;
	
	private long frameStart;
	private long frameTimeLeft;

	private Scene currScene;
	
	/**
	 * Animator's logger.
	 */
	private Logger log = Logger.getLogger(NAME);

	public static final String NAME = "q-animus";
	
	private static final double FROM_NANO = 1./1000000000.;
	
	private boolean constantTime = false;
	/**
	 * Counter for frame length average.
	 */
	private AverageCounter counter;
	
	private static final int COUNTER_ITERATIONS = 10;
	
	private double frameLength;
	
	/**
	 * Average frame length calculated each {@link #COUNTER_ITERATIONS} cycles.
	 */
	private double approxFrameLength;
	
	private double frameTime;
	
	public StageAnimator(GLCanvas canvas, StageConfig stageConfig, EkranConfig ekranConfig)
	{
		if (canvas == null)
			throw new IllegalArgumentException("Canvas cannot be null.");
		this.canvas = canvas;
		
		int maxFPS = ekranConfig.getMaxFps();
		
		if ( maxFPS == 0)
			minFrameLength = 0;
		else
			minFrameLength = 1000000000/maxFPS;
		
		log.debug("Max frame rate set to " + (maxFPS == 0 ? "'unbound'" : maxFPS) + " fps" +
				" => frame length: " + minFrameLength + " ns.");
		
		
		counter = new AverageCounter();
		approxFrameLength = 1./maxFPS; // rough guess
		
		
		frameStart = System.nanoTime();

	}


	public void runPreUnLock() 
	{
	}

	public void runBody() 
	{
		if(currScene == null)
			return;
		//////////////////////////////////////////////////////////
		// Running scene behaviors:
		// TODO: maxFPS not working
		
		frameTime = constantTime ? frameLength : approxFrameLength * frameLength;
		currScene.animate(frameTime);
		//////////////////////////////////////////////////////////
		currScene.postAnimate(frameTime);
		
		long frameDuration = System.nanoTime() - frameStart;
		

		counter.addValue( frameDuration ); // adjusting frame average length
		

		if(counter.getCounter() > COUNTER_ITERATIONS)
		{
			// recalculating frame key time:
			approxFrameLength = counter.getAverage() * FROM_NANO; 
//			log.debug( "Estimated frame processing length: " +  approxFrameLength);
			counter.reset();
		}

		frameTimeLeft = minFrameLength - (frameDuration);
		
		if(frameTimeLeft > 0)
			try { // spending remaining frame time to match the maxFPS setting:
				log.trace("Going to sleep for %d ns.", frameTimeLeft);
				Thread.sleep((long)(frameTimeLeft * FROM_NANO * 1000));
			} 
			catch (InterruptedException e) { log.warn("Animator thread sleep was interrupted."); }
		
		frameStart = System.nanoTime();
		
		// requesting repaint (TODO: actual repaint time is still a mystery)
		canvas.repaint();
	}

	
	public void runPostLock() 
	{
		// 
//		try {
//		}
//		catch(java.lang.InterruptedException e) {	log.info("Animator thread display procedure was interrupted.");	}
	}


	public void sceneChanged(Scene currScene) 
	{
		this.currScene = currScene;
		frameLength = currScene.getFrameLength();
		if(frameLength < 0)
		{
			frameLength = -frameLength;
			constantTime = true;
		}
		else
			constantTime = false;
	}
}
