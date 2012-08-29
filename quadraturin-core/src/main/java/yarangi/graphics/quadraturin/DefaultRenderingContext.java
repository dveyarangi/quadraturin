package yarangi.graphics.quadraturin;

import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.graphics.quadraturin.objects.IVisible;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;

import com.spinn3r.log5j.Logger;

/**
 * Wraps rendering properties, tests GL capabilities and aggregates graphical plug-ins.
 * Should also wrap GL object, so it could be more separated from entity definition.
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
	
	private GL gl;
	
	private float currFrameLength;
	
	private final List <IVisible> entities = new LinkedList <IVisible> ();
	private final TObjectIntHashMap <ILook> looks = new TObjectIntHashMap <ILook> ();
	
	
	private final Set <String> lookClasses = new HashSet <String> ();
	
	/**
	 * Queue of entities waiting to be initialized.
	 */
	private final Queue <IVisible> bornEntities = new LinkedList<IVisible> ();

	/**
	 * Queue of dead entities to be cleaned up.
	 */
	private final Queue <IVisible> deadEntities = new LinkedList <IVisible> ();
	
	private ViewPoint2D viewPoint;
	
	public DefaultRenderingContext(EkranConfig config)
	{
		this.config = config;
		plugins = config.createPlugins();
		
		this.viewPoint = new ViewPoint2D();
	}
	
	protected void setViewPort(int refx, int refy, int width, int height) 
	{
		this.viewPort = new ViewPort(refx, refy, width, height);
	}
	
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
	public final GL gl() {
		return gl;
	}

	protected void init(GL gl) {
		
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
		
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		// disable lighting (TODO: remove)
		gl.glDisable(GL.GL_LIGHTING);
		
		// enable 2D texture mapping
		gl.glEnable(GL.GL_TEXTURE_2D);	
		
		// disable texture auto-mapping:
		gl.glDisable(GL.GL_TEXTURE_GEN_S);
		gl.glDisable(GL.GL_TEXTURE_GEN_T);


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
	protected void render(GL gl)
	{
		// injecting new entities
		while(!bornEntities.isEmpty())
		{
			IVisible born = bornEntities.poll();
			born.init( gl, this );
		}
		
		while(!deadEntities.isEmpty())
		{
			IVisible dead = deadEntities.poll();
			dead.destroy( gl, this );
		}
		
	    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	
		IVeil veil;
//		System.out.println(entities.size() + " : " + indexer.size());
//			ISpatialSensor <SceneEntity> clippingSensor = new ClippingSensor(gl, time, context);
//			getEntityIndex().query(clippingSensor, new AABB(0, 0, Math.max(viewPoint.getPortWidth(), viewPoint.getPortHeight()), 0));
//			System.out.println(Math.max(viewPoint.getPortWidth(), viewPoint.getPortHeight()));
			// TODO: do the viewport clipping already, you lazy me!
//			System.out.println("BEGIN ======================================================");
			for(IVisible entity : entities)
			{
				// TODO: sort by veil, then weave and tear once
				veil = entity.getLook().getVeil();
//				System.out.println(entity + " : " + entity.getLook() + " : " + entity.getLook().getVeil());
				
				if(veil != null)
					veil.weave( gl, entity, this );
				entity.render( gl,  this );
				if(veil != null)
					veil.tear( gl );
				
				assert Debug.renderEntityOverlay(gl, entity, this);
			}
//			System.out.println("Total " + entities.size() + " entities rendered.");
//			root.display(gl, time, context);
/*		else
		{
			veilEffect.render(gl, time, entities, context);
		}*/
	}
	
	protected void reinit(int width, int height, GL gl) {
		
		if(viewPort.getWidth() == width && viewPort.getHeight() == height)
			return;
		setViewPort( 0, 0, width, height );
		for(String pluginName : getPluginsNames())
		{
			Q.rendering.debug("Resizing plugin [" + pluginName + "]");
			IGraphicsPlugin factory = getPlugin(pluginName);
			factory.resize(gl, this);
		}
	}
	
	@Override
	public void setDefaultBlendMode(GL gl) 
	{
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBlendEquation(GL.GL_FUNC_ADD);
	}

	@Override
	public final float getFrameLength()
	{
		return currFrameLength;
	}
	
	protected void setFrameLength(float length) 
	{
		this.currFrameLength = length;
	}

	@Override
	public void addVisible(IVisible entity)
	{
		lookClasses.add(entity.getLook().getClass().toString());
		int count = looks.get( entity.getLook() );
		if(count == 0)
			bornEntities.add(entity);

		looks.put( entity.getLook(), count+1 );
		entities.add( entity );
	}
	
	@Override
	public void removeVisible(IVisible entity)
	{
		int count = looks.get( entity.getLook() );
		if(count == 1) {
			looks.remove( entity.getLook() );
			deadEntities.add( entity );
		}
		else
			looks.put( entity.getLook(), count - 1 );
		
		entities.remove( entity );
		
	}

	public void setViewPoint(ViewPoint2D viewPoint)
	{
		this.viewPoint = viewPoint;
	}
	
	@Override
	public ViewPoint2D getViewPoint() { return viewPoint; }

}