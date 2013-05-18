package yar.quadraturin;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.config.EkranConfig;
import yar.quadraturin.debug.Debug;
import yar.quadraturin.objects.ILook;
import yar.quadraturin.objects.IVisible;
import yar.quadraturin.plugin.IGraphicsPlugin;
import yarangi.spatial.AABB;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.spinn3r.log5j.Logger;

/**
 * Wraps rendering properties, tests GL capabilities and aggregates graphical plug-ins.
 * Should also wrap GL object, so it could be more separated from entity definition.
 * Aims to be rendering tools class.
 * 
 * @author dveyarangi
 *
 */
public class DefaultRenderingContext implements IRenderingContext 
{
	private ViewPort viewPort;
	
	private final Map <String, IGraphicsPlugin> plugins;
	
	private final EkranConfig config;
	
	private final Logger log = Q.rendering;
	
	public static final float MIN_DEPTH_PRIORITY = 0;
	public static final float MAX_DEPTH_PRIORITY = 1;
	
	private GL2 gl;
	
	private float currFrameLength;
	
//	private final List <IVisible> entities = new LinkedList <IVisible> ();
	private final Multimap <ILook<?>, IVisible> entitiesLooks = LinkedListMultimap.<ILook<?>, IVisible>create();
	
	private final Multimap <ILook<?>, IVisible> overlayLooks = LinkedListMultimap.<ILook<?>, IVisible>create();
	
	private final Set <String> lookClasses = new HashSet <String> ();
	
	/**
	 * Queue of entities waiting to be initialized.
	 */
	private final Queue <ILook<?>> bornLooks = new LinkedList<ILook<?>> ();

	/**
	 * Queue of dead entities to be cleaned up.
	 */
	private final Queue <ILook<?>> deadLooks = new LinkedList <ILook<?>> ();
	
	/** Camera properties */
	private Camera2D viewPoint;
	
	public DefaultRenderingContext(EkranConfig config)
	{
		this.config = config;
		plugins = config.createPlugins();
		
		this.viewPoint = new Camera2D();
	}
	
	
////////////////////////////////////////////////////////////////////////////////////
// service methods
	
	protected void setViewPort(int refx, int refy, int width, int height) 
	{
		this.viewPort = new ViewPort(refx, refy, width, height);
	}
	
	protected void setViewPoint(Camera2D viewPoint)
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
			factory.init(gl, this);
		}
		
		// clearing off unsupported plugins:
		for(String pluginName : unavailablePlugins)
			plugins.remove(pluginName);
	}
	
	
	/**
	 * Displays the entirety of entities in this scene for one scene animation frame.
	 * Also handles the decomposition of newly created and oldly dead entities.
	 * @param gl
	 * @param time scene frame time
	 */
	protected void renderEntities(GL gl)
	{
		// injecting new entities
		while(!bornLooks.isEmpty())
		{
			ILook<?> born = bornLooks.poll();
			born.init( this );
		}
		
		while(!deadLooks.isEmpty())
		{
			ILook<?> dead = deadLooks.poll();
			dead.destroy( this );
		}
		
	    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	
		IVeil veil;
//		System.out.println(entities.size() + " : " + indexer.size());
//			ISpatialSensor <SceneEntity> clippingSensor = new ClippingSensor(gl, time, context);
//			getEntityIndex().query(clippingSensor, new AABB(0, 0, Math.max(viewPoint.getPortWidth(), viewPoint.getPortHeight()), 0));
//			System.out.println(Math.max(viewPoint.getPortWidth(), viewPoint.getPortHeight()));
			// TODO: do the viewport clipping already, you lazy me!
//			System.out.println("BEGIN ======================================================");
			for(ILook<?> look : entitiesLooks.keySet())
			{
				// TODO: sort by veil, then weave and tear once
				veil = look.getVeil();
//				System.out.println(entity + " : " + entity.getLook() + " : " + entity.getLook().getVeil());
				
				if(veil != null)
					veil.weave( gl, this );
				for(IVisible entity : entitiesLooks.get( look )) {
//					System.out.println(entity);
					entity.render( this );
				}
				if(veil != null)
					veil.tear( gl );

				if(Debug.ON)
				for(IVisible entity : entitiesLooks.get( look ))
					assert Debug.renderEntityOverlay(gl, entity, this);

			}
//			System.out.println("Total " + entities.size() + " entities rendered.");
//			root.display(gl, time, context);
/*		else
		{
			veilEffect.render(gl, time, entities, context);
		}*/
	}

	public void renderOverlays(GL gl)
	{
		for(ILook look : overlayLooks.keySet()) {
			for(IVisible entity : overlayLooks.get( look )) {
//				System.out.println(entity);
				entity.render( this );
			}
		}
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
			Q.rendering.debug("Resizing plugin [" + pluginName + "]");
			IGraphicsPlugin plugin = getPlugin(pluginName);
			plugin.resize(gl, this);
		}
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
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addVisible(IVisible entity)
	{
		addVisible( entity, entity.getLook(), entitiesLooks );
	}
	
	@Override
	public void addOverlay(IVisible overlay) {
		addVisible( overlay, overlay.getLook(), overlayLooks );
	}
	
	private void addVisible(IVisible entity, ILook look, Multimap queue) {
//		ILook look = entity.getLook();
		lookClasses.add(look.getClass().toString());
		Collection <IVisible> entities = queue.get( look );
//		System.out.println(entity.getLook());
		if(entities.size() == 0)
			bornLooks.add(look);

		queue.put( look, entity );
		entities.add( entity );
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeVisible(IVisible entity)
	{
		Collection <IVisible> entities = entitiesLooks.get( entity.getLook() );
		if(entities.size() == 1) {
			entitiesLooks.removeAll( entity.getLook() );
			deadLooks.add( entity.getLook() );
		}
		else
			entitiesLooks.remove( entity.getLook(), entity );
		
		entities.remove( entity );
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Camera2D getCamera() { return viewPoint; }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <K> K getAssociatedEntity(ILook <K> look)
	{
		Collection <IVisible> associatedEntities = entitiesLooks.get( look );
		if(associatedEntities == null || associatedEntities.isEmpty()) 
		{
			associatedEntities = overlayLooks.get( look );
			if(associatedEntities == null || associatedEntities.isEmpty()) 
				return null;
		}
		
		return (K)associatedEntities.iterator().next();
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


}