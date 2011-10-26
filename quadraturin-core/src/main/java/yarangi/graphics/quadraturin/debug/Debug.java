package yarangi.graphics.quadraturin.debug;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.Scene;

public class Debug 
{
	public static Logger LOG = Logger.getLogger("q-debug");
	/**
	 * System property to turn on GL debug mode.
	 */
	public static final String DEBUG_MODE = "yarangi.graphics.quadraturin.debug";

	public static boolean ON = Boolean.valueOf(System.getProperty(DEBUG_MODE));
	
	static {
		if(!ON) 
			LOG.info("To start Quadraturin profiling features, set true '" + DEBUG_MODE + "' JVM variable.");
		else
			LOG.info("Quadraturin is started in profiling mode.");
	}

	public static void instrumentate(Scene scene)
	{
		scene.addOverlay(new SceneDebugOverlay(scene.getWorldLayer().getEntityIndex()));
	}
	
}
