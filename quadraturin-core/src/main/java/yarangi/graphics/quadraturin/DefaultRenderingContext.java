package yarangi.graphics.quadraturin;

import java.util.Map;
import java.util.Set;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;

public class DefaultRenderingContext implements IRenderingContext 
{
	private IViewPoint vp;
	
	private Map <String, IGraphicsPlugin> plugins;
	
	public DefaultRenderingContext(EkranConfig config)
	{
		plugins = config.getPlugins();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isForEffect() { return false; }
	/**
	 * {@inheritDoc}
	 */
	public IViewPoint getViewPoint() { return vp; }
	
	void setViewPoint(IViewPoint vp) { this.vp = vp; }
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends IGraphicsPlugin> T getPlugin(String name) {
		return (T) plugins.get(name);
	}
	
	public Set <String> getPluginsNames()
	{
		return plugins.keySet();
	}
}