package yarangi.graphics.quadraturin;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

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
	private GLU glu = new GLU();
	
	/**
	 * Stage controls entities' look and behaviors
	 */
	private Scene currScene, prevScene;
	
	/**
	 * marks scene transition state.
	 */
	private boolean isScenePending = false;
	
	/**
	 * mouse location goes here
	 */
	private IEventManager voices;
	
	
	public static final float MAX_DEPTH_PRIORITY = 1;

	/**
	 * Current canvas width/height ratio.
	 */
//	private float windowRatio;
	
	/**
	 * Set of rendering environment properties for {@link Look} to consider. 
	 */
	private DefaultRenderingContext context;
	
	
	
	public Quad2DController(String name, EkranConfig ekranConfig, IEventManager voices, ThreadChain chain) {

		super(name, chain);
		
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
		
		log.trace("GL extensions: " + gl.glGetString(GL.GL_EXTENSIONS));
		
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
		for(String name : context.getPluginsNames())
		{
			IGraphicsPlugin factory = context.getPlugin(name);
			log.debug("Initializing plugin [" + name + "]...");
			factory.init(gl);
		}
		
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

//		if (!stage.changePending())
//			return; // nothing changed, nothing to redraw
		
	
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
		{
			currScene.destroy(gl, context);
			return;
		}
		if(isScenePending)
		{
			if(prevScene != null)
				prevScene.destroy(gl, context);
			// initializing stage components:
			log.debug("Entering '" + currScene.getName() + "' scene...");

			currScene.init(gl, context);
			
			isScenePending = false;
		}
		
		if(currScene == null)
			return;

		// ////////////////////////////////////////////////////
		// TODO: viewpoint transformations
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		ViewPoint2D viewPoint = (ViewPoint2D) currScene.getViewPoint();
		
		int viewport[] = new int[4];
		double mvmatrix[] = new double[16];
		double projmatrix[] = new double[16];

		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, mvmatrix, 0);
		gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projmatrix, 0);
		viewPoint.setPointModel(viewport, mvmatrix, projmatrix);
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		
		gl.glLoadIdentity();
		gl.glOrtho(-viewport[2]*viewPoint.getScale(), viewport[2]*viewPoint.getScale(), -viewport[3]*viewPoint.getScale(), viewport[3]*viewPoint.getScale(), -1, 1);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glTranslatef((float) viewPoint.getCenter().x(),
				(float) viewPoint.getCenter().y(), 0/* -(float) viewPoint.getHeight()*/);
		// TODO: extract matrices to viewPoint for world coordinates calculation:
		//updateViewPoint(gl, viewPoint);
		
		// send world transformation event to voices:
		voices.updateViewPoint(viewPoint);
		
		context.setViewPoint(viewPoint);
		// ////////////////////////////////////////////////////
		// scene preprocessing:
		gl.glPushMatrix();
		gl.glLoadIdentity();
		// TODO: fix display times:
		currScene.preDisplay(gl, currScene.getFrameLength(), false);
		// ////////////////////////////////////////////////////
		// scene rendering:
		gl.glPopMatrix();
		currScene.display(gl, currScene.getFrameLength(), context);

		// ////////////////////////////////////////////////////
		// scene postprocessing:
		gl.glLoadIdentity();

		currScene.postDisplay(gl, currScene.getFrameLength(), context);
		
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
