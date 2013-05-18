package yar.quadraturin;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.config.EkranConfig;
import yar.quadraturin.objects.ILook;
import yar.quadraturin.plugin.IGraphicsPlugin;
import yarangi.spatial.AABB;

import com.spinn3r.log5j.Logger;

/**
 * Wraps rendering properties, tests GL capabilities and aggregates graphical plug-ins.
 * Also wrap GL object, so it could be more separated from entity definition.
 * Aims to be rendering tools class.
 * 
 * @author dveyarangi
 *
 */
public class GL2RenderingContext implements IRendererAggregate 
{
	private ViewPort viewPort;
	
	private final Map <String, IGraphicsPlugin> plugins;
	
	private final EkranConfig config;
	
	private final Logger log = Q.rendering;
	
	public static final float MIN_DEPTH_PRIORITY = 0;
	public static final float MAX_DEPTH_PRIORITY = 1;
	
	private GL2 gl;
	
	private float currFrameLength;

	
	private LookManager worldLookManager;
	private LookManager uiLookManager;
	
	/** Camera properties */
	private Camera2D viewPoint;
	
	public GL2RenderingContext(EkranConfig config)
	{
		this.config = config;
		plugins = config.createPlugins();
		
		this.viewPoint = new Camera2D();
		
	}
	
	
	@Override
	public void setWorldLookManager(LookManager man)
	{
		this.worldLookManager = man;
	}
	
	@Override
	public void setUILookManager(LookManager man)
	{
		this.uiLookManager = man;
	}
	
	
////////////////////////////////////////////////////////////////////////////////////
// service methods
	
	protected void setViewPort(int refx, int refy, int width, int height) 
	{
		this.viewPort = new ViewPort(refx, refy, width, height);
	}
	
	@Override
	public void setViewPoint(Camera2D viewPoint)
	{
		this.viewPoint = viewPoint;
	}
	
	protected void setFrameLength(float length) 
	{
		this.currFrameLength = length;
	}
//
////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////
// 
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getPlugin(String name) {
		return (T) plugins.get(name);
	}

	protected Collection <IGraphicsPlugin> getPlugins()
	{
		return plugins.values();
	}
	
	public Set <String> getPluginsNames()
	{
		return plugins.keySet();
	}

	@Override
	public ViewPort getViewPort()
	{
		return viewPort;
	}
	
	@Override
	public final GL2 gl() {
		return gl;
	}

	/**
	 * Sets OpenGL base environment. 
	 * Initializes graphic plugins.
	 * @param gl
	 */
	protected void init(GL2 gl) {
		
		this.gl = gl;
		
		setViewPort( 0, 0, config.getXres(), config.getYres() );
		
//		gl.glDisable(GL.GL_CULL_FACE);
		
		/////
		// specifies how the pixels are overriden by overlapping objects:
		gl.glEnable(GL.GL_DEPTH_TEST);
		// TODO: fix entity prioritizing:
	    gl.glDepthFunc(GL.GL_LEQUAL); // new pixels must be same or shallower than drawn
	    gl.glClearDepth(MAX_DEPTH_PRIORITY);
//	    gl.glDepthFunc(GL.GL_ALWAYS);
		
	    /////
	    // color blending function:
		gl.glEnable(GL.GL_BLEND);
		setDefaultBlendMode( gl );
		
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		// disable lighting (TODO: remove)
		gl.glDisable(GL2.GL_LIGHTING);
		
		// enable 2D texture mapping
		gl.glEnable(GL2.GL_TEXTURE_2D);	
		
		// disable texture auto-mapping:
		gl.glDisable(GL2.GL_TEXTURE_GEN_S);
		gl.glDisable(GL2.GL_TEXTURE_GEN_T);


		// antialiasing:
		if (config.isAntialiasing()) {
			gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
			gl.glEnable(GL.GL_LINE_SMOOTH);
		} 
		else
			gl.glDisable(GL.GL_LINE_SMOOTH);

		
		
		log.trace("Following GL extensions available:\n" + gl.glGetString(GL.GL_EXTENSIONS));
		
		////////////////////////////////////////////////////////////////////////////
		// testing and initiating plugins:
		List <String> unavailablePlugins = new LinkedList <String> ();
		for(String pluginName : getPluginsNames())
		{
			IGraphicsPlugin factory = getPlugin(pluginName);
			
			log.debug("Validating plugin [" + pluginName + "]...");
			boolean isPluginAvailable = true;
			if(factory.getRequiredExtensions() != null) 
			{
				for(String extensionName : factory.getRequiredExtensions())
					if(! gl.isExtensionAvailable(extensionName)) 
					{
						log.error("GL extension [" + extensionName + "] required by plugin [" + pluginName + "] is not available.");
						isPluginAvailable = false;
					}
			}
			
			if(!isPluginAvailable) {
				log.debug("Plugin [" + pluginName + "] is not supported by hardware.");
				unavailablePlugins.add(pluginName);
				continue;
			}
			
			log.debug("Initializing plugin [" + pluginName + "]...");
			factory.init( this );
		}
		
		// clearing off unsupported plugins:
		for(String pluginName : unavailablePlugins)
			plugins.remove(pluginName);
	}
	
	
	
	/**
	 * Meant to handle graphics setup changes.
	 * TODO: Should not reinitiate all plug-ins every step of window resizing.
	 * @param width
	 * @param height
	 * @param gl
	 */
	protected void reinit(int width, int height, GL gl) {
		
		if(viewPort.getWidth() == width && viewPort.getHeight() == height)
			return; // no screen dimensions change
		
		log.debug( "Resizing GL canvas to [" + width + "x" + height + "]");
		
		setViewPort( 0, 0, width, height );
		
		// reinitializing graphical plugins:
		for(String pluginName : getPluginsNames())
		{
//			Q.rendering.debug("Resizing plugin [" + pluginName + "]");
			IGraphicsPlugin plugin = getPlugin( pluginName );
			plugin.resize( this );
		}
	}
	
	/**
	 * Displays the entirety of entities in this scene for one scene animation frame.
	 * @param gl
	 * @param time scene frame time
	 */
	protected void renderEntities()
	{
		if(worldLookManager != null)
			worldLookManager.renderEntities( this );
	}
	public void renderOverlays()
	{
		if(uiLookManager != null)
			uiLookManager.renderEntities( this );
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultBlendMode(GL gl) 
	{
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBlendEquation(GL.GL_FUNC_ADD);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final float getFrameLength()
	{
		return currFrameLength;
	}
	

	public static void useEntityCoordinates(GL gl, AABB area, ILook look) {
		GL2 gl2 = gl.getGL2();
		// storing transformation matrix:
		gl2.glMatrixMode( GL2.GL_MODELVIEW );
		gl2.glPushMatrix();
//		gl.glLoadIdentity(); 	
		
		// transforming into entity coordinates:
		if(area == null)
			gl2.glTranslatef(0, 0, 0); // just adjusting priority
		else
		{
			float priority = -look.getPriority();
			gl2.glTranslatef((float)area.getAnchor().x(), (float)area.getAnchor().y(), priority);
			gl2.glRotatef((float)area.getOrientation(), 0, 0, 1 );
		}

	}
	
	public static void useWorldCoordinates(GL gl) {
		GL2 gl2 = gl.getGL2();
		gl2.glMatrixMode( GL2.GL_MODELVIEW );
		gl2.glPopMatrix();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Camera2D getCamera() { return viewPoint; }


}