package yarangi.graphics.quadraturin.debug;

import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.Q;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.SceneLayer;
import yarangi.graphics.quadraturin.UserLayer;
import yarangi.graphics.quadraturin.WorldLayer;
import yarangi.graphics.quadraturin.actions.IAction;
import yarangi.graphics.quadraturin.debug.looks.CoordinateGridLook;
import yarangi.graphics.quadraturin.debug.looks.EntityAreaLook;
import yarangi.graphics.quadraturin.debug.looks.EntityBodyLook;
import yarangi.graphics.quadraturin.debug.looks.EntitySensorLook;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.graphics.quadraturin.objects.IVisible;
import yarangi.spatial.SpatialHashMap;
import yarangi.spatial.SpatialIndexer;

import com.spinn3r.log5j.Logger;

public class Debug 
{
	public static Logger LOG = Q.debug;

	public static boolean ON = Boolean.valueOf(System.getProperty(Q.DEBUG_MODE));
	static ILook userLayerSpatialOverlay = null;
	
	static boolean SHOW_COORDINATE_GRID = false;
	static boolean SHOW_SENSORS = false;
	static boolean SHOW_AREAS = false;
	static boolean SHOW_BODIES = false;
	
	static {
		if(!ON) 
			LOG.info("To start Quadraturin profiling features, set true '" + Q.DEBUG_MODE + "' JVM variable.");
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
	
	private static ILook<SceneLayer> coordinateGridLook = new CoordinateGridLook( new Color(0.0f, 0.5f, 1f, 1f) );
	
	public static boolean drawWorldLayerOverlay(GL gl, WorldLayer layer, IRenderingContext context)
	{
		if(!Debug.ON)
			return true;
		
		if(SHOW_COORDINATE_GRID)
			coordinateGridLook.render( layer, context );
		
		return true;
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
	
	private static ILook <IEntity> sensorLook = new EntitySensorLook();
	private static ILook <IEntity> areaLook = new EntityAreaLook();
	private static ILook <IEntity> bodyLook = new EntityBodyLook();

	public static boolean renderEntityOverlay(GL gl, IVisible e, IRenderingContext context)
	{
		if(!Debug.ON)
			return true;
		
		if(!(e instanceof IEntity))
			return true;
		IEntity entity = (IEntity) e;
		
		if(SHOW_SENSORS) {
			if(entity.getEntitySensor() != null)
				sensorLook.render( entity, context );
		}
		if(SHOW_AREAS) {
			if(entity.getArea() != null)
				areaLook.render( entity, context );
		}
		if(SHOW_BODIES) {
			if(entity.getBody() != null)
				bodyLook.render( entity, context );
		}
		return true;
	}
	
	
	public static boolean appendDebugActions(Map <String, IAction> actionsMap)
	{
		if(!Debug.ON)
			return true;
	
		actionsMap.put("show-sensors", new IAction() {
			@Override public void act(UserActionEvent event) { SHOW_SENSORS = !SHOW_SENSORS; }});
		actionsMap.put("show-areas", new IAction() {
			@Override public void act(UserActionEvent event) { SHOW_AREAS = !SHOW_AREAS; }});
		actionsMap.put("show-bodies", new IAction() {
			@Override public void act(UserActionEvent event) { SHOW_BODIES = !SHOW_BODIES; }});
		actionsMap.put("show-coordinates", new IAction() {
			@Override public void act(UserActionEvent event) { SHOW_COORDINATE_GRID = !SHOW_COORDINATE_GRID; }});
		
		return true;
	}

}
