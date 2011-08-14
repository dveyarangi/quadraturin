package yarangi.graphics.quadraturin.events;

import java.awt.Point;

import yarangi.graphics.quadraturin.objects.IWorldEntity;
import yarangi.math.Vector2D;

/**
 * Cursor (mouse, currently) movement-related event. 
 * Allows binding of selected/hovered entity. 
 */
public class CursorEvent
{

	
	/**
	 * User cursor location in scene coordinates
	 */
	private Vector2D worldLocation;
	
	/**
	 * User cursor location in drawing canvas coordinates
	 */
	private Point canvasLocation;

	/**
	 * Set if the cursor if hovering over and entity.
	 */
	private IWorldEntity entity;

	private InputHook hook;
	
	public CursorEvent(Vector2D worldLocation, Point canvasLocation)
	{
		this.worldLocation = worldLocation;
		this.canvasLocation = canvasLocation;
	}
	
	/**
	 * @param mask
	 */
	public void setInput(InputHook hook)
	{
		this.hook = hook;
	}
	
	public InputHook getInput() { return hook; }


	/**
	 * @return Cursor location in scene coordinates.
	 */
	public Vector2D getWorldLocation() {
		return worldLocation;
	}

	/**
	 * @return Cursor location in canvas coordinates.
	 */
	public Point getCanvasLocation() {
		return canvasLocation;
	}
	
	public void setSceneEntity(IWorldEntity entity) 
	{
		this.entity = entity;
	}
	
	public IWorldEntity getEntity() { return entity; }
	
}
