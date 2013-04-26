package yarangi.graphics.quadraturin;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.objects.ILook;
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
public class Q2DController extends ChainedThreadSkeleton implements GLEventListener, StageListener
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
	private final IEventManager voices;
	
	/**
	 * Set of rendering environment properties for {@link ILook} to consider. 
	 */
	static DefaultRenderingContext context;
	
	int viewport[] = new int[4];
	double mvmatrix[] = new double[16];
	double projmatrix[] = new double[16]; 
	
	public Q2DController(String moduleName, EkranConfig ekranConfig, IEventManager voices, ThreadChain chain) {

		super(moduleName, chain);
		
		this.voices = voices;
		
		this.context = new DefaultRenderingContext(ekranConfig);
	}

	@Override
	public void start() { /* nothing here */ }

	/**
	 * Called by the drawable immediately after the OpenGL context is
	 * initialized for the first time. Can be used to perform one-time OpenGL
	 * initialization such as setup of lights and display lists.
	 * 
	 * @param gLDrawable
	 *            The GLDrawable object.
	 */
	@Override
	public void init(GLAutoDrawable drawable) 
	{
		log.debug("// JOGL INIT ////////////////////////////////////////////////");

		if (Debug.ON) {
			log.info("GL core is in debug mode. TODO: not really");
//			drawable.setGL(new DebugGL(drawable.getGL()));
		}
		
		GL2 gl = drawable.getGL().getGL2();
		
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
	@Override
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		final GL gl = glDrawable.getGL();

		// adjusting viewport (implicit)
//		gl.glViewport(0, 0, width, height);
		
		// resetting context:
		context.reinit( width, height, gl);

	}

	/**
	 * Called by the drawable to initiate OpenGL rendering by the client. After
	 * all GLEventListeners have been notified of a display event, the drawable
	 * will swap its buffers if necessary.
	 * 
	 * @param gLDrawable
	 *            The GLDrawable object.
	 */
	@Override
	public void display(GLAutoDrawable glDrawable) 
	{
	
		///////////////////////////////////////////////////////////
		// WAITING FOR OTHER THREADS TO FINISH:
		///////////////////////////////////////////////////////////
		try { // waiting for our turn:
			waitForRelease();
		} catch (InterruptedException e1) {
			// TODO: should this interrupt the AWT event thread?
			// TODO: check for GLContext overriding
			log.warn("Renderer thread was interrupted.");
			releaseNext();
			return;
		}
		
		GL2 gl = glDrawable.getGL().getGL2();
		
		if(!this.isAlive())
		{   // terminating GL listener:
			if(currScene != null)
				currScene.destroy(gl, context);
			
			releaseNext();
			return;
		}
		
		///////////////////////////////////////////////////////////
		// LOADING SCENE IF CHANGED:
		///////////////////////////////////////////////////////////
		if(isScenePending.getAndSet(false))
		{	// swapping scene:
			if(prevScene != null)
				prevScene.destroy(gl, context);
			
			// initializing new scene:
			log.debug("Entering '" + currScene.getName() + "' scene...");
			// TODO: should not be locked in here, render a placeholder/progress bar instead:
			currScene.init(gl, context);
			
			context.setViewPoint( (Camera2D)currScene.getCamera() );
		}
		
		if(currScene == null)
		{ 	// nothing to display:
			releaseNext();
			return;
		}

		////////////////////////////////////////////////////////////////////
		// RENDERING CURRENT FRAME:
		////////////////////////////////////////////////////////////////////
		
		context.setFrameLength( (float)QAnimator.getLastFrameLength() );
		
		// ////////////////////////////////////////////////////
		// scene preprocessing, in untranslated coordinates:
		gl.glMatrixMode(GL2.GL_MODELVIEW); gl.glLoadIdentity(); 
		gl.glMatrixMode(GL2.GL_PROJECTION);  gl.glLoadIdentity(); 
		for(IGraphicsPlugin plugin : context.getPlugins()) {
			gl.glPushAttrib( GL2.GL_ENABLE_BIT );
				plugin.preRender(gl, context);
			gl.glPopAttrib();
		}
		
		Camera2D viewPoint = (Camera2D) currScene.getCamera();
		context.setViewPoint( viewPoint );
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		
		// applying top-down orthogonal projection with zoom scaling
		gl.glMatrixMode(GL2.GL_PROJECTION); gl.glLoadIdentity();
		gl.glOrtho(-viewport[2]/2*viewPoint.getScale(), viewport[2]/2*viewPoint.getScale(), -viewport[3]/2*viewPoint.getScale(), viewport[3]/2*viewPoint.getScale(), -1, 1);
		
		// applying view point translation:
		gl.glMatrixMode(GL2.GL_MODELVIEW);  gl.glLoadIdentity();
		gl.glTranslatef((float) viewPoint.getCenter().x(),
				(float) viewPoint.getCenter().y(), 0/* -(float) viewPoint.getHeight()*/);
		
		gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
		gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);

		
		// updating view point transformation parameters:
		viewPoint.updatePointModel(viewport, mvmatrix, projmatrix);
		
		
		// send world view point transformation event to event manager:
		voices.updateViewPoint(viewPoint);
		
		
		// ////////////////////////////////////////////////////
		// scene rendering:
		context.renderEntities(gl);
		assert Debug.drawWorldLayerOverlay( gl, currScene.getWorldLayer(), context );

		// ////////////////////////////////////////////////////
		// scene postprocessing:
//		gl.glMatrixMode(GL.GL_MODELVIEW); gl.glLoadIdentity();
//		gl.glMatrixMode(GL.GL_PROJECTION); gl.glLoadIdentity();
//		gl.glOrtho(0, viewport[2], viewport[3], 0, -1, 1);

		
		for(IGraphicsPlugin plugin : context.getPlugins()) {
			gl.glPushAttrib( GL2.GL_ENABLE_BIT );
			plugin.postRender(gl, context);
			gl.glPopAttrib();
		}
		
		gl.glMatrixMode(GL2.GL_MODELVIEW); gl.glLoadIdentity(); 
		gl.glMatrixMode(GL2.GL_PROJECTION);  gl.glLoadIdentity(); 
		// ////////////////////////////////////////////////////
		// ui rendering:
		context.renderOverlays(gl);

		// proceeding to next thread:
		releaseNext();

		// this part is not synchronized:
		// TODO: ensure that rendering cycle does not start again before this finishes:
		gl.glFlush();
		glDrawable.swapBuffers();
	}

	
	@Override
	public void sceneChanged(Scene currScene) 
	{
		this.prevScene = this.currScene;
		this.currScene = currScene;
		
//		if(isScenePending.getAndSet(true)) // sanity TODO: estimate harm
//			throw new IllegalStateException("Previous scene was not yet processed.");
//			log.warn("TODO: Scene is already queued, and  not initialized yet, not gracefull at all.");
	}

	public IRenderingContext getRenderingContext()
	{
		return context;
	}

	@Override
	public void dispose(GLAutoDrawable arg0)
	{
		// TODO Auto-generated method stub
		
	}

}
