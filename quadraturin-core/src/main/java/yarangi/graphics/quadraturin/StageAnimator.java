package yarangi.graphics.quadraturin;

import javax.media.opengl.GLCanvas;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
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
	
	private IPhysicsEngine engine;
	
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
	
	private double defaultFrameLength;

	public static final String NAME = "q-animus";
	
	public StageAnimator(GLCanvas canvas, IPhysicsEngine engine)
	{
		if ( engine == null)
			throw new IllegalArgumentException("Engine cannot be null.");
		this.engine = engine;
		
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
		
		
		defaultFrameLength = QuadConfigFactory.getStageConfig().getFrameLength();
		
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
		currScene.preAnimate();
		
		//////////////////////////////////////////////////////////
		// Running scene behaviors:
		// TODO: fix animation step for shorter frames
		currScene.animate(defaultFrameLength);
		
		engine.calculate(defaultFrameLength);
		
		//////////////////////////////////////////////////////////
		// Clean up dead entities:
		currScene.postAnimate();
		
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
			canvas.repaint();
//		}
//		catch(java.lang.InterruptedException e) {	log.info("Animator thread display procedure was interrupted.");	}
	}


	public void sceneChanged(Scene prevScene, Scene currScene) 
	{
		this.currScene = currScene;
		
		engine.setCollisionManager(currScene.getWorldVeil().createCollisionManager());
	}
}
