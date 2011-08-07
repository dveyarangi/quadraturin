package yarangi.graphics.quadraturin;

import javax.media.opengl.GLCanvas;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.config.StageConfig;
import yarangi.graphics.quadraturin.threads.Loopy;

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
	
//	private double defaultFrameLength;

	public static final String NAME = "q-animus";
	
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
		// Running scene behaviors:
		// TODO: fix animation step for shorter frames
		currScene.animate(currScene.getFrameLength());
		
		//////////////////////////////////////////////////////////
		currScene.postAnimate(currScene.getFrameLength());
		
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
		
		// scheduling repaint:
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
