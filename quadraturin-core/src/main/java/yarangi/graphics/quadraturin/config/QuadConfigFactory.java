package yarangi.graphics.quadraturin.config;

public class QuadConfigFactory 
{
	
	public static IQuadConfig instance = QuadJsonConfig.load();

	public static IQuadConfig getConfig() { return instance; }
}
