package yarangi.graphics.quadraturin.actions;

import java.awt.Point;
import java.util.Map;

import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.UserLayer;
import yarangi.graphics.quadraturin.WorldLayer;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.math.Vector2D;
import yarangi.spatial.ISpatialFilter;

/**
 * Input definitions interface
 * 
 * @author dveyarangi
 */
public abstract class ActionController
{
	
	/**
	 * Picking area span (scene section coordinates)
	 */
	public static final double CURSOR_PICK_SPAN = 5;
	
	private WorldLayer worldLayer;
	
	private UserLayer uiLayer;
	
	public ActionController(Scene scene)
	{
		worldLayer = scene.getWorldLayer();
		
		uiLayer = scene.getUILayer();
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
	 * @return Graphical overlay of this controller 
	 */
	public abstract Look <ActionController> getLook();
	
	/** 
	 * Selects an entity at {@link CURSOR_PICK_SPAN} radius around cursor location

	 * @param worldLocation
	 * @param canvasLocation
	 * @return
	 */
	public ILayerObject pick(Vector2D worldLocation, Point canvasLocation)
	{
		
		// ui picks
		// TODO: introduce filter from
		ILayerObject picked = null;
		if(canvasLocation != null)
			picked = uiLayer.processPick(canvasLocation);
		
		if(picked != null)
			return picked;
		
		if(worldLocation != null)
			picked = worldLayer.processPick(worldLocation, getPickingFilter());
		
		// TODO: terrain picks
		// TODO: flexible pick priorities
		
		return picked;
	}

	/**
	 * Defines camera moving functions
	 * @return
	 */
	public abstract ICameraMan getCameraManager();

}
