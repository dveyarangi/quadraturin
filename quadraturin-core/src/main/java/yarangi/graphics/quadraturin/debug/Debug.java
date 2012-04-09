package yarangi.graphics.quadraturin.debug;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.UserLayer;
import yarangi.graphics.quadraturin.objects.Dummy;
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
	public static final String DEBUG_MODE = "q.debug";

	public static boolean ON = Boolean.valueOf(System.getProperty(DEBUG_MODE));
	static Look userLayerSpatialOverlay = null;
	
	static {
		if(!ON) 
			LOG.info("To start Quadraturin profiling features, set true '" + DEBUG_MODE + "' JVM variable.");
		else
			LOG.info("Quadraturin is started in profiling mode.");
	}

	public static void instrumentate(Scene scene)
	{
		SpatialIndexer <IEntity> indexer = scene.getWorldLayer().getEntityIndex();
		if(indexer instanceof SpatialHashMap)
			userLayerSpatialOverlay = new DebugSpatialHashMapLook();

//		scene.addEntity( new EntityShell(scene.getUILayer().getEntityIndex(), Dummy.BEHAVIOR, spatialOverlay) );
//		scene.addOverlay(new SceneDebugOverlay(scene.getWorldLayer().getEntityIndex()));
	}

	public static void drawUserLayerOverlay(GL gl, UserLayer layer, IRenderingContext context)
	{
//		userLayerSpatialOverlay.render( gl, 0, layer.getEntityIndex(), context );
	}

	public static void init(GL gl, Scene scene, IRenderingContext context) {
//		userLayerSpatialOverlay.init(gl, scene.getUILayer().getEntityIndex(), context);
	}
	public static void destroy(GL gl, Scene scene, IRenderingContext context) {
//		userLayerSpatialOverlay.destroy(gl, scene.getUILayer().getEntityIndex(), context);
	}
	
}
