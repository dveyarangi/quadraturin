package yarangi.graphics.quadraturin;

import java.awt.Point;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.events.CursorEvent;
import yarangi.graphics.quadraturin.threads.ChainedThreadSkeleton;
import yarangi.graphics.quadraturin.threads.ThreadChain;
import yarangi.graphics.utils.shaders.ShaderFactory;
import yarangi.math.Vector2D;

/**
 * TODO: nvidia extensions that allow faster rendering 
 * WGL_ARB_extensions_string
 * WGL_ARB_render_texture
 * WGL_ARG_pbuffer
 * WGL_ARB_pixel_format
 */
public class Quad2DController extends ChainedThreadSkeleton implements GLEventListener, StageListener
{
	
	private static final long serialVersionUID = -7140406537457631569L;

	/**
	 * Display configuration properties.
	 */
	private EkranConfig ekranConfig = QuadConfigFactory.getEkranConfig();

	/**
	 * OpenGL utilities.
	 */
	private GLU glu = new GLU();
	
	/**
	 * Stage controls entities' look and behaviors
	 */
	private Scene currScene, prevScene;
	
	private boolean isScenePending = false;
	
	private EventManager voices;

	/**
	 * Current canvas width/height ratio.
	 */
//	private float windowRatio;
	
	private DefaultRenderingContext context;
	
	public Quad2DController(String name, EventManager voices, ThreadChain chain) {

		super(name, chain);
		
		this.voices = voices;
//		System.out.println(voices);
//		this.windowRatio = (float) ekranConfig.getXres() / (float) ekranConfig.getYres();
		
		this.context = new DefaultRenderingContext();
//		frameLength = QuadConfigFactory.getStageConfig().getFrameLength();

	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

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
		gl.glEnable(GL.GL_DEPTH_TEST);
	    gl.glDepthFunc(GL.GL_LEQUAL); // new pixels must be same or shallower than drawn
	    gl.glClearDepth(1.0f);
	    //	    gl.glDepthFunc(GL.GL_ALWAYS);
		
	    /////
	    // color blending function:
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		// disable texture auto-mapping:
		gl.glDisable(GL.GL_TEXTURE_GEN_S);
		gl.glDisable(GL.GL_TEXTURE_GEN_T);
		
		// disable lighting (TODO: remove)
		gl.glDisable(GL.GL_LIGHTING);

		// enable 2D texture mapping
		gl.glEnable(GL.GL_TEXTURE_2D);					

		// antialiasing:
		if (ekranConfig.isAntialiasing()) {
			gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
			gl.glEnable(GL.GL_LINE_SMOOTH);
		} 
		else
		{
			gl.glDisable(GL.GL_LINE_SMOOTH);
		}
		
		log.trace("GL properties set.");

		// shader factory initialization:
		log.debug("Initializing shader factory...");
		ShaderFactory.init(gl);
		log.trace("Shader factory initialized.");
		
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
				gl.glOrtho(-width/2, width/2, -height/2, height/2, -1, 1);
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
		}
		
		GL gl = glDrawable.getGL();
		
		if(isScenePending)
		{
			if(prevScene != null)
				prevScene.destroy(gl);
			// initializing stage components:
			log.debug("Entering '" + currScene.getName() + "' scene...");

			currScene.init(gl);
			
			isScenePending = false;
		}

		// ////////////////////////////////////////////////////
		// TODO: viewpoint transformations
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		ViewPoint2D viewPoint = (ViewPoint2D) currScene.getViewPoint();
		
		gl.glTranslatef((float) viewPoint.getCenter().x,
				(float) viewPoint.getCenter().y, 0/* -(float) viewPoint.getHeight()*/);
		
		// TODO: extract matrices to viewPoint for world coordinates calculation:
		//updateViewPoint(gl, viewPoint);
		

		Point pickPoint = voices.getMouseLocation();
		Vector2D worldPickPoint = null;
		// TODO: OPTIMIZE: buffer the pick selection and do nothing if not changed
		if (pickPoint != null) 
			worldPickPoint = toWorldCoordinates(gl, pickPoint, viewPoint);

		voices.declare(new CursorEvent(worldPickPoint, pickPoint));

		
		context.setViewPoint(viewPoint);
		// ////////////////////////////////////////////////////
		// scene preprocessing:
		gl.glPushMatrix();
		gl.glLoadIdentity();
		currScene.preDisplay(gl, currScene.getFrameLength(), false);
		// ////////////////////////////////////////////////////
		// scene rendering:
		gl.glPopMatrix();
		currScene.display(gl, currScene.getFrameLength(), context);

		// ////////////////////////////////////////////////////
		// scene postprocessing:
		gl.glLoadIdentity();

		currScene.postDisplay(gl, currScene.getFrameLength(), context);

		gl.glFlush();
		glDrawable.swapBuffers();
		releaseNext();
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


	private Vector2D toWorldCoordinates(GL gl, Point pickPoint, ViewPoint2D viewPoint) 
	{
		int viewport[] = new int[4];
		double mvmatrix[] = new double[16];
		double projmatrix[] = new double[16];
		int realy = 0;// GL y coord pos
		double wcoord[] = new double[4];// wx, wy, wz;// returned xyz coords

		double x = pickPoint.getX();
		double y = pickPoint.getY();

		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, mvmatrix, 0);
		gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projmatrix, 0);
		
		/* note viewport[3] is height of window in pixels */
		realy = viewport[3] - (int) y;
		glu.gluUnProject((double) x, (double) realy, 0.0, mvmatrix, 0, projmatrix, 0, viewport, 0, wcoord, 0);
		return new Vector2D(wcoord[0], wcoord[1]);
//		return new Vector2D((wcoord[0]+viewPoint.getCenter().x)*viewPoint.getHeight(), (wcoord[1]+viewPoint.getCenter().y)*viewPoint.getHeight());
	}

	
	public void sceneChanged(Scene prevScene, Scene currScene) 
	{
		this.prevScene = prevScene;
		this.currScene = currScene;
		
		this.isScenePending = true;
	}
	
	
	protected class DefaultRenderingContext implements RenderingContext 
	{
		IViewPoint vp;
		public boolean doPushNames() { return false; }
		public boolean isForEffect() { return false; }
		public IViewPoint getViewPoint() { return vp; }
		
		protected void setViewPoint(IViewPoint vp) { this.vp = vp; }
	};
	
}
