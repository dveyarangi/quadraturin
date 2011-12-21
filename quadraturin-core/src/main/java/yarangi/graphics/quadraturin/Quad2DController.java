package yarangi.graphics.quadraturin;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;
import yarangi.graphics.quadraturin.threads.ChainedThreadSkeleton;
import yarangi.graphics.quadraturin.threads.ThreadChain;

/**
 * TODO: nvidia extensions that allow faster rendering 
 * WGL_ARB_extensions_string
 * WGL_ARB_render_texture
 * WGL_ARG_pbuffer
 * WGL_ARB_pixel_format
 */
public class Quad2DController extends ChainedThreadSkeleton implements GLEventListener, StageListener
{

	/**
	 * OpenGL utilities.
	 */
//	private GLU glu = new GLU();
	
	/**
	 * Stage controls entities' look and behaviors
	 */
	private Scene currScene, prevScene;
	
	/**
	 * marks scene transition state.
	 */
	private volatile AtomicBoolean isScenePending = new AtomicBoolean(false);
	
	/**
	 * mouse location goes here
	 */
	private IEventManager voices;
	
	/**
	 * Set of rendering environment properties for {@link Look} to consider. 
	 */
	private DefaultRenderingContext context;
	
	public Quad2DController(String moduleName, EkranConfig ekranConfig, IEventManager voices, ThreadChain chain) {

		super(moduleName, chain);
		
		this.voices = voices;
		
		this.context = new DefaultRenderingContext(ekranConfig);
	}

	public void start() { /* nothing here */ }

	/**
	 * Called by the drawable immediately after the OpenGL context is
	 * initialized for the first time. Can be used to perform one-time OpenGL
	 * initialization such as setup of lights and display lists.
	 * 
	 * @param gLDrawable
	 *            The GLDrawable object.
	 */
	public void init(GLAutoDrawable drawable) 
	{
		log.debug("// JOGL INIT ////////////////////////////////////////////////");

		if (Debug.ON) {
			log.info("GL core is in debug mode.");
			drawable.setGL(new DebugGL(drawable.getGL()));
		}
		
		GL gl = drawable.getGL();
		
		//////////////////////////////////////////////////////////////////
		// Global settings:
		log.debug("Setting global GL properties...");
		context.init(gl);
		
		// control buffer swapping:
		drawable.setAutoSwapBufferMode(false);
		
		log.debug("/////////////////////////////////////////////////////////////");
	}

	/**
	 * Called by the drawable during the first repaint after the component has
	 * been resized. The client can update the viewport and view volume of the
	 * window appropriately, for example by a call to
	 * {@link GL#glViewport(int, int, int, int)} note that for convenience the
	 * component has already called GL.glViewport(int, int, int, int)(x, y,
	 * width, height) when this method is called, so the client may not have to
	 * do anything in this method.
	 * 
	 * @param gLDrawable
	 *            The GLDrawable object.
	 * @param x
	 *            The X Coordinate of the viewport rectangle.
	 * @param y
	 *            The Y coordinate of the viewport rectanble.
	 * @param height
	 *            The new height of the window.
	 */
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		Q.rendering.debug( "Resizing GL canvas to [" + width + "x" + height + "]");
		final GL gl = glDrawable.getGL();

		// adjusting viewport (implicit)
//		gl.glViewport(0, 0, width, height);
		
		// resetting context:
		context.reinit( width, height, gl);
		
		Q.rendering.trace( "GL canvas successfully resized.");

	}

	/**
	 * Called by the drawable to initiate OpenGL rendering by the client. After
	 * all GLEventListeners have been notified of a display event, the drawable
	 * will swap its buffers if necessary.
	 * 
	 * @param gLDrawable
	 *            The GLDrawable object.
	 */
	public void display(GLAutoDrawable glDrawable) 
	{
	
		try {
			waitForRelease();
		} catch (InterruptedException e1) {
			// TODO: should this interrupt the AWT event thread?
			// TODO: check for GLContext overriding
			log.warn("Renderer thread was interrupted.");
			releaseNext();
			return;
		}
		
		GL gl = glDrawable.getGL();
		
		if(!this.isAlive())
		{   // terminating GL listener:
			if(currScene != null)
				currScene.destroy(gl, context);
			
			releaseNext();
			return;
		}
		
		if(isScenePending.getAndSet(false))
		{	// swapping scene:
			if(prevScene != null)
				prevScene.destroy(gl, context);
			
			// initializing new scene:
			log.debug("Entering '" + currScene.getName() + "' scene...");
			// TODO: should not be locked in here, render a placeholder/progress bar instead:
			currScene.init(gl, context);
		}
		
		if(currScene == null)
		{ 	// nothing to display:
			releaseNext();
			return;
		}

		////////////////////////////////////////////////////////////////////
		// rendering current frame:
		
		ViewPoint2D viewPoint = (ViewPoint2D) currScene.getViewPoint();
		int viewport[] = new int[4]; gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		
		// applying top-down orthogonal projection with zoom scaling
		gl.glMatrixMode(GL.GL_PROJECTION); gl.glLoadIdentity();
		gl.glOrtho(-viewport[2]/2*viewPoint.getScale(), viewport[2]/2*viewPoint.getScale(), -viewport[3]/2*viewPoint.getScale(), viewport[3]/2*viewPoint.getScale(), -1, 1);
		
		// applying view point translation:
		gl.glMatrixMode(GL.GL_MODELVIEW);  gl.glLoadIdentity();
		gl.glTranslatef((float) viewPoint.getCenter().x(),
				(float) viewPoint.getCenter().y(), 0/* -(float) viewPoint.getHeight()*/);
		
		double mvmatrix[] = new double[16]; gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, mvmatrix, 0);
		double projmatrix[] = new double[16]; gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projmatrix, 0);
		
		// updating view point transformation parameters:
		viewPoint.updatePointModel(viewport, mvmatrix, projmatrix);
		
		// send world view point transformation event to event manager:
		voices.updateViewPoint(viewPoint);
		
		// ////////////////////////////////////////////////////
		// scene preprocessing, in untranslated coordinates:
		gl.glPushMatrix(); gl.glLoadIdentity(); 
		
		for(IGraphicsPlugin plugin : context.getPlugins())
				plugin.preRender(gl, context);
	
		currScene.preDisplay(gl, currScene.getFrameLength(), false);
		gl.glPopMatrix();
		
		// ////////////////////////////////////////////////////
		// scene rendering:
		currScene.display(gl, currScene.getFrameLength(), context);

		// ////////////////////////////////////////////////////
		// scene postprocessing:
		gl.glMatrixMode(GL.GL_MODELVIEW); gl.glLoadIdentity();
		gl.glMatrixMode(GL.GL_PROJECTION); gl.glLoadIdentity();
		gl.glOrtho(0, viewport[2], viewport[3], 0, -1, 1);

		
		for(IGraphicsPlugin plugin : context.getPlugins())
			plugin.postRender(gl, context);
		
		currScene.postDisplay(gl, currScene.getFrameLength(), context);
	
		// proceeding to next thread:
		releaseNext();

		// TODO: ensure that rendering cycle does not start again before this finishes:
		gl.glFlush();
		glDrawable.swapBuffers();
	}

	/**
	 * Called when the display mode has been changed. <B>!! CURRENTLY
	 * UNIMPLEMENTED IN JOGL !!</B>
	 * 
	 * @param gLDrawable
	 *            The GLDrawable object.
	 * @param modeChanged
	 *            Indicates if the video mode has changed.
	 * @param deviceChanged
	 *            Indicates if the video device has changed.
	 */
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) { }




	
	public void sceneChanged(Scene currScene) 
	{
		this.prevScene = this.currScene;
		this.currScene = currScene;
		
		if(isScenePending.getAndSet(true)) // sanity
			throw new IllegalStateException("Previous scene was not yet processed.");
	}
	
	


}
