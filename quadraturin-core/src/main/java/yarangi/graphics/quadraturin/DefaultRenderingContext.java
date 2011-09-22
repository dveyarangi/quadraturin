package yarangi.graphics.quadraturin;

import java.util.Map;
import java.util.Set;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;

public class DefaultRenderingContext implements RenderingContext 
{
	private IViewPoint vp;
	
	private Map <String, IGraphicsPlugin> plugins;
	
	public DefaultRenderingContext(EkranConfig config)
	{
		//plugins = config.getPlugins();
	}
	
	public boolean doPushNames() { return false; }
	public boolean isForEffect() { return false; }
	public IViewPoint getViewPoint() { return vp; }
	
	void setViewPoint(IViewPoint vp) { this.vp = vp; }
	
	public IGraphicsPlugin getPlugin(String name) {
		return plugins.get(name);
	}
	
	public Set <String> getPluginsNames()
	{
		return  plugins.keySet();
	}
}