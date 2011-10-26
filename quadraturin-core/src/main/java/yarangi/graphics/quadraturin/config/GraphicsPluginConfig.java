package yarangi.graphics.quadraturin.config;

import java.util.Map;

public class GraphicsPluginConfig {
	private String factoryClass;
	private Map <String, String> properties;
	
	public String getFactoryClass() { return factoryClass; }
	
	public Map <String, String> getProperties()
	{
		return properties; 
	}
}
