package yarangi.graphics.quadraturin;

import javax.media.opengl.GLCanvas;

import org.apache.log4j.Logger;

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
	
	/**
	 * Counter for frame length average.
	 */
	private AverageCounter counter;
	
	private static final int COUNTER_ITERATIONS = 10;
	
	/**
	 * Average frame length calculated each {@link #COUNTER_ITERATIONS} cycles.
	 */
	private double approxFrameLength;
	
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
		//////////////////////////////////////////////////////////
		// Running scene behaviors:
		// TODO: give currScene.getFrameLength()
		currScene.animate(approxFrameLength * currScene.getFrameLength());
		
		//////////////////////////////////////////////////////////
		currScene.postAnimate(approxFrameLength * currScene.getFrameLength());
		
		long frameDuration = System.nanoTime() - frameStart;
		

		counter.addValue( frameDuration ); // adjusting frame average length
		

		if(counter.getCounter() > COUNTER_ITERATIONS)
		{
			// recalculating frame key time:
			approxFrameLength = 1000000 * counter.get1DivAverage(); 
			counter.reset();
		}

		frameTimeLeft = minFrameLength - (frameDuration);
		
		if(frameTimeLeft > 0)
		try { // spending remaining frame time to match the maxFPS setting:
			if( log.isTraceEnabled() )
				log.trace("Going to sleep for " + frameTimeLeft + " ns.");
			Thread.sleep(frameTimeLeft / 1000000);
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
	}
}
