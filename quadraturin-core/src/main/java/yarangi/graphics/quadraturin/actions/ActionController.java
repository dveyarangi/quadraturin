package yarangi.graphics.quadraturin.actions;

import java.awt.Point;
import java.util.Map;

import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.UserLayer;
import yarangi.graphics.quadraturin.WorldLayer;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.math.IVector2D;
import yarangi.spatial.ISpatialFilter;
import yarangi.spatial.PickingSensor;

/**
 * Input definitions interface; may be overriden by scene implementations ({@link Scene#getActionController()}).
 * 
 * @author dveyarangi
 */
public abstract class ActionController 
{
	
	/**
	 * Picking area span (scene section coordinates)
	 */
	public static final double CURSOR_PICK_SPAN = 5;
	
	private final WorldLayer worldLayer;
	
	private final UserLayer uiLayer;
	
	private final Scene scene;
	
	public ActionController(Scene scene)
	{
		this.scene = scene;
		
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
	 * Allows to set up picking mode
	 * @see {@link PickingSensor.Mode}
	 * @return
	 */
	public PickingSensor.Mode getPickingMode() { return PickingSensor.Mode.ANY; } 
	
	/** 
	 * Selects an entity at {@link CURSOR_PICK_SPAN} radius around cursor location

	 * @param worldLocation
	 * @param canvasLocation
	 * @return
	 */
	public ILayerObject pick(IVector2D worldLocation, Point canvasLocation)
	{
		
		// ui picks
		// TODO: introduce filter from
		ILayerObject picked = null;
		if(canvasLocation != null)
			picked = uiLayer.processPick(canvasLocation);
		
		if(picked != null)
			return picked;

		if(worldLocation != null)
			picked = worldLayer.processPick(worldLocation, getPickingMode(), getPickingFilter());
		
		// TODO: terrain picks
		// TODO: flexible pick priorities
		
		return picked;
	}

	/**
	 * Defines camera moving method
	 * @return
	 */
	public abstract ICameraMan getCameraManager();
	
	public Scene getScene() { return scene; }

	/**
	 * Called on mouse hovering over selectable {@link ILayerObject}
	 * @param prevEntity
	 * @param pickedEntity
	 */
	public void hover(ILayerObject prevEntity, ILayerObject pickedEntity)
	{
	}

}
