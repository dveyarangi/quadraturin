package yarangi.graphics.quadraturin;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;

public class DefaultRenderingContext implements IRenderingContext 
{
	private int width, height;
	
	private Map <String, IGraphicsPlugin> plugins;
	
	public DefaultRenderingContext(EkranConfig config)
	{
		plugins = config.getPlugins();
	}
	
	public void setScreenResolution(int width, int height) 
	{
		this.width = width;
		this.height = height;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isForEffect() { return false; }
	/**
	 * {@inheritDoc}
	 */
//	public ViewPoint2D getViewPoint() { return vp; }
	
//	void setViewPoint(ViewPoint2D vp) { this.vp = vp; }
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends IGraphicsPlugin> T getPlugin(String name) {
		return (T) plugins.get(name);
	}
	
	Collection <IGraphicsPlugin> getPlugins()
	{
		return plugins.values();
	}
	
	public Set <String> getPluginsNames()
	{
		return plugins.keySet();
	}

	@Override
	public int getScreenWidth()
	{
		return width;
	}

	@Override
	public int getScreenHeight()
	{
		return height;
	}

	public void init(GL gl) {
		List <String> unavailablePlugins = new LinkedList <String> ();
		for(String pluginName : getPluginsNames())
		{
			IGraphicsPlugin factory = getPlugin(pluginName);
			boolean isPluginAvailable = true;
			for(String extensionName : factory.getRequiredExtensions())
				if(! gl.isExtensionAvailable(extensionName)) 
				{
					QServices.rendering.error("GL extension [" + extensionName + "] required by plugin [" + pluginName + "] is not available.");
					isPluginAvailable = false;
				}
			
			if(!isPluginAvailable) {
				QServices.rendering.debug("Plugin [" + pluginName + "] is not supported by hardware.");
				unavailablePlugins.add(pluginName);
				continue;
			}
			
			QServices.rendering.debug("Initializing plugin [" + pluginName + "]...");
			factory.init(gl, this);
		}
		
		// clearing off unsupported plugins:
		for(String pluginName : unavailablePlugins)
			plugins.remove(pluginName);
	}
	
	public void reinit(GL gl) {
		for(String pluginName : getPluginsNames())
		{
			QServices.rendering.debug("Resetting plugin [" + pluginName + "]");
			IGraphicsPlugin factory = getPlugin(pluginName);
			factory.reinit(gl, this);
		}
	}
}