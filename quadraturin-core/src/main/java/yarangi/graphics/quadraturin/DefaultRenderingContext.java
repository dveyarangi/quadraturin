package yarangi.graphics.quadraturin;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;

public class DefaultRenderingContext implements IRenderingContext 
{
	private ViewPort viewPort;
	
	private Map <String, IGraphicsPlugin> plugins;
	
	private EkranConfig config;
	
	private Logger log = QServices.rendering;
	
	public static final float MIN_DEPTH_PRIORITY = 0;
	public static final float MAX_DEPTH_PRIORITY = 1;
	
	public DefaultRenderingContext(EkranConfig config)
	{
		this.config = config;
		plugins = config.getPlugins();
	}
	
	protected void setViewPort(int refx, int refy, int width, int height) 
	{
		this.viewPort = new ViewPort(refx, refy, width, height);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends IGraphicsPlugin> T getPlugin(String name) {
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

	protected void init(GL gl) {
		
		setViewPort( 0, 0, config.getXres(), config.getYres() );
		
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
		if (config.isAntialiasing()) {
			gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
			gl.glEnable(GL.GL_LINE_SMOOTH);
		} 
		else
			gl.glDisable(GL.GL_LINE_SMOOTH);

		
		// plugins initialization:
		
		log.trace("GL extensions: " + gl.glGetString(GL.GL_EXTENSIONS));

		
		List <String> unavailablePlugins = new LinkedList <String> ();
		for(String pluginName : getPluginsNames())
		{
			IGraphicsPlugin factory = getPlugin(pluginName);
			boolean isPluginAvailable = true;
			for(String extensionName : factory.getRequiredExtensions())
				if(! gl.isExtensionAvailable(extensionName)) 
				{
					log.error("GL extension [" + extensionName + "] required by plugin [" + pluginName + "] is not available.");
					isPluginAvailable = false;
				}
			
			if(!isPluginAvailable) {
				QServices.rendering.debug("Plugin [" + pluginName + "] is not supported by hardware.");
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
	
	protected void reinit(int width, int height, GL gl) {
		
		setViewPort( 0, 0, width, height );
		for(String pluginName : getPluginsNames())
		{
			QServices.rendering.debug("Resetting plugin [" + pluginName + "]");
			IGraphicsPlugin factory = getPlugin(pluginName);
			factory.reinit(gl, this);
		}
	}
}