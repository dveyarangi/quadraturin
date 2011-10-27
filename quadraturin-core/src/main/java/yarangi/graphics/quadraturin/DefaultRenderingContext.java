package yarangi.graphics.quadraturin;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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
}