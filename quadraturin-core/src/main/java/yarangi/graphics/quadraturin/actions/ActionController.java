package yarangi.graphics.quadraturin.actions;

import java.awt.Point;
import java.util.Map;

import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.WorldLayer;
import yarangi.graphics.quadraturin.events.CursorListener;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.math.Vector2D;
import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialFilter;
import yarangi.spatial.PickingSensor;

/**
 * Input definitions interface
 * 
 * @author dveyarangi
 */
public abstract class ActionController implements CursorListener
{
	
	/**
	 * Picking area span (scene section coordinates)
	 */
	public static final double CURSOR_PICK_SPAN = 5;
	
	private WorldLayer worldLayer;
	
	public ActionController(Scene scene)
	{
		worldLayer = scene.getWorldLayer();
	}
	
	/**
	 * A map from user input event name to corresponding {@link IAction} 
	 * @return
	 */
	public abstract Map <String, IAction> getActions();
	
	/**
	 * Defines selection of picked entities at mouse cursor.
	 * @return
	 */
	public abstract ISpatialFilter <IEntity> getPickingFilter();
	
	/** 
	 * Selects an entity at {@link CURSOR_PICK_SPAN} radius around cursor location

	 * @param worldLocation
	 * @param canvasLocation
	 * @return
	 */
	public IEntity pick(Vector2D worldLocation, Point canvasLocation)
	{
		// collecting picked entities:
		PickingSensor <IEntity> sensor = new PickingSensor <IEntity> (getPickingFilter());
		
		// TODO: ui picks
//		if(canvasLocation != null)
//			uiVeil.getEntityIndex().query(sensor, new AABB(canvasLocation.x, canvasLocation.y, CURSOR_PICK_SPAN, 0));
		
		// testing world section:
		if(sensor.getObject() == null && worldLocation != null)
			worldLayer.getEntityIndex().query(sensor, new AABB(worldLocation.x(), worldLocation.y(), CURSOR_PICK_SPAN, 0));
		
		// TODO: terrain picks
		// TODO: flexible pick priorities
		
		if(sensor.getObject() != null)
			return sensor.getObject();
		
		return null;
	}

	public abstract ICameraMan getCameraManager();

}
