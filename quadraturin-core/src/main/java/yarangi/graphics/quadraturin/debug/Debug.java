package yarangi.graphics.quadraturin.debug;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.spatial.SpatialHashMap;
import yarangi.spatial.SpatialIndexer;

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
		SpatialIndexer <IEntity> indexer = scene.getWorldLayer().getEntityIndex();
		Look spatialOverlay = null;
		if(indexer instanceof SpatialHashMap)
			spatialOverlay = new DebugSpatialHashMapLook();

		scene.addEntity( new EntityShell(scene.getWorldLayer().getEntityIndex(), null, spatialOverlay) );
//		scene.addOverlay(new SceneDebugOverlay(scene.getWorldLayer().getEntityIndex()));
	}
	
}
