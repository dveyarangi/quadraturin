package yarangi.graphics.quadraturin;

import javax.media.opengl.GLCanvas;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.thread.Loopy;

/**
 * Invokes scene behavior.
 */
public class StageAnimator implements Loopy 
{
	
	/**
	 * The engine object stage.
	 */
	private Stage stage;
	
	private GLCanvas canvas;
	
	/**
	 * Minimal required length of frame step.
	 */
	private long minFrameLength;
	
	private long frameStart;
	private long frameTimeLeft;

	
	/**
	 * Animator's logger.
	 */
	private Logger log = Logger.getLogger(this.getClass());

	
	public StageAnimator(Stage stage, GLCanvas canvas)
	{
		
		if ( stage == null)
			throw new IllegalArgumentException("Stage cannot be null.");
		this.stage = stage;
		
		if (canvas == null)
			throw new IllegalArgumentException("Canvas cannot be null.");
		this.canvas = canvas;
		
		int maxFPS = QuadConfigFactory.getStageConfig().getMaxFps();
		
		if ( maxFPS == 0)
			minFrameLength = 0;
		else
			minFrameLength = 1000000000/maxFPS;
		
		log.debug("Max frame rate set to " + (maxFPS == 0 ? "'unbound'" : maxFPS) + " fps" +
				" => frame length: " + minFrameLength + " ns.");
		
		frameStart = System.nanoTime();
		
//		frameSum = FRAMES_NUM * minFrameLength;
//		for(int idx = 0; idx < FRAMES_NUM; idx ++)
//			lastFrames[idx] = minFrameLength;
	}


	public void runPreUnLock() 
	{
	}

	public void runBody() 
	{
		//////////////////////////////////////////////////////////
		// New entities are born here:
		stage.preAnimate();
		
		//////////////////////////////////////////////////////////
		// Running scene behaviors:
		// TODO: fix animation step for shorter frames
		stage.animate(stage.getFrameLength());
		
		//////////////////////////////////////////////////////////
		// Clean up dead entities:
		stage.postAnimate();
		
		long newStart = System.nanoTime();
//		int fps = (int)(1000000000l/(newStart-frameStart));
//		System.out.println("FPS: " + fps);
		frameTimeLeft = minFrameLength - (newStart - frameStart);
		if(frameTimeLeft > 0)
		try {
			if( log.isTraceEnabled() )
				log.trace("Going to sleep for " + frameTimeLeft + " ns.");
			Thread.sleep(frameTimeLeft / 1000000);
		} 
		catch (InterruptedException e) { log.warn("Animator thread sleep was interrupted."); }
		
		frameStart = System.nanoTime();
	}

	
	public void runPostLock() 
	{
		// TODO: this invokes job in AWT event thread - understand what to do and do it.
		// 
//		try {
			canvas.display();
//		}
//		catch(java.lang.InterruptedException e) {	log.info("Animator thread display procedure was interrupted.");	}
	}
}
