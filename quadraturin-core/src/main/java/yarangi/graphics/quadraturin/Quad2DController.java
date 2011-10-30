package yarangi.graphics.quadraturin;

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
	 * Display configuration properties.
	 */
	private EkranConfig ekranConfig;

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
	private volatile boolean isScenePending = false;
	
	/**
	 * mouse location goes here
	 */
	private IEventManager voices;
	
	
	public static final float MIN_DEPTH_PRIORITY = 0;
	public static final float MAX_DEPTH_PRIORITY = 1;
	
	/**
	 * Set of rendering environment properties for {@link Look} to consider. 
	 */
	private DefaultRenderingContext context;
	
	
	
	public Quad2DController(String moduleName, EkranConfig ekranConfig, IEventManager voices, ThreadChain chain) {

		super(moduleName, chain);
		
		this.voices = voices;
		
		this.context = new DefaultRenderingContext(ekranConfig);
		
		this.ekranConfig = ekranConfig;
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
		drawable.setAutoSwapBufferMode(false);
		log.debug("// JOGL INIT ////////////////////////////////////////////////");
		GL gl = drawable.getGL();

		if (Debug.ON) {
			log.info("GL core is in debug mode.");
			drawable.setGL(new DebugGL(gl));
		}
		
		//////////////////////////////////////////////////////////////////
		// Global settings:
		// TODO: extract to EkranConfig / Scene initialization
		
		log.debug("Setting global GL properties...");
		
//		gl.glDisable(GL.GL_CULL_FACE);
//		gl.glCullFace(GL.GL_BACK);
		
		/////
		// specifies how the pixels are overriden by overlapping objects:
//		gl.glDisable(GL.GL_DEPTH_TEST);
		// TODO: fix entity prioritizing:
	    gl.glDepthFunc(GL.GL_LEQUAL); // new pixels must be same or shallower than drawn
	    gl.glClearDepth(MAX_DEPTH_PRIORITY);
	    //	    gl.glDepthFunc(GL.GL_ALWAYS);
		
	    /////
	    // color blending function:
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		// disable lighting (TODO: remove)
		gl.glDisable(GL.GL_LIGHTING);
		
		// disable texture auto-mapping:
		gl.glDisable(GL.GL_TEXTURE_GEN_S);
		gl.glDisable(GL.GL_TEXTURE_GEN_T);

		// enable 2D texture mapping
		gl.glEnable(GL.GL_TEXTURE_2D);					

		// antialiasing:
		if (ekranConfig.isAntialiasing()) {
			gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
			gl.glEnable(GL.GL_LINE_SMOOTH);
		} 
		else
			gl.glDisable(GL.GL_LINE_SMOOTH);

		
		// plugins initialization:
		
		log.trace("GL extensions: " + gl.glGetString(GL.GL_EXTENSIONS));
		
		context.setScreenResolution( ekranConfig.getXres(), ekranConfig.getYres() );
		context.init(gl);

		// TODO: this must be also invoked on screen resizing or resolution change to make FBO plugins work properly
		
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
		final GL gl = glDrawable.getGL();

		if (height <= 0) // avoid a divide by zero error!
			height = 1;

//		windowRatio = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		if(currScene != null)
		{
			ViewPoint2D viewPoint = (ViewPoint2D) currScene.getViewPoint();
			if(viewPoint != null)
			{
				gl.glOrtho(-width*viewPoint.getScale(), width*viewPoint.getScale(), -height*viewPoint.getScale(), height*viewPoint.getScale(), -1, 1);
				gl.glViewport(0, 0, width, height);
			}
		}
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		context.setScreenResolution( width, height );
		context.reinit(gl);

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
			log.debug("Renderer thread was interrupted.");
			return;
		}
		
		GL gl = glDrawable.getGL();
		
		if(!this.isAlive())
		{   // terminating GL listener
			currScene.destroy(gl, context);
			return;
		}
		
		if(isScenePending) // on scene change
		{
			if(prevScene != null)
				prevScene.destroy(gl, context);
			// initializing stage components:
			log.debug("Entering '" + currScene.getName() + "' scene...");

			currScene.init(gl, context);
			
			isScenePending = false;
		}
		
		if(currScene == null) // nothing to display
			return;

		ViewPoint2D viewPoint = (ViewPoint2D) currScene.getViewPoint();
		int viewport[] = new int[4]; gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		
		// applying top-down orthogonal projection with zoom scaling
		gl.glMatrixMode(GL.GL_PROJECTION); gl.glLoadIdentity();
		gl.glOrtho(-viewport[2]*viewPoint.getScale(), viewport[2]*viewPoint.getScale(), -viewport[3]*viewPoint.getScale(), viewport[3]*viewPoint.getScale(), -1, 1);
		
		// applying view point translation:
		gl.glMatrixMode(GL.GL_MODELVIEW);
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
		gl.glLoadIdentity();

		currScene.postDisplay(gl, currScene.getFrameLength(), context);
		
		for(IGraphicsPlugin plugin : context.getPlugins())
			plugin.postRender(gl, context);
	
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
		
		this.isScenePending = true;
	}
	
	


}
