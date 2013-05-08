package yar.quadraturin.debug;

import java.util.Map;

import javax.media.opengl.GL;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.Q;
import yar.quadraturin.Scene;
import yar.quadraturin.SceneLayer;
import yar.quadraturin.UserLayer;
import yar.quadraturin.WorldLayer;
import yar.quadraturin.actions.IAction;
import yar.quadraturin.debug.looks.CoordinateGridLook;
import yar.quadraturin.debug.looks.EntityAreaLook;
import yar.quadraturin.debug.looks.EntityBodyLook;
import yar.quadraturin.debug.looks.EntitySensorLook;
import yar.quadraturin.events.UserActionEvent;
import yar.quadraturin.graphics.colors.Color;
import yar.quadraturin.objects.IEntity;
import yar.quadraturin.objects.ILook;
import yar.quadraturin.objects.IVisible;
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
	static boolean SHOW_CONSOLE = false;
	
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
		actionsMap.put("show-console", new IAction() {
			@Override public void act(UserActionEvent event) { SHOW_CONSOLE = !SHOW_CONSOLE; }});
	
		return true;
	}

}
