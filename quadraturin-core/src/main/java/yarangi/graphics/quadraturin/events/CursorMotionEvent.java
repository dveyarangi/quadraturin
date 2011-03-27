package yarangi.graphics.quadraturin.events;

import java.awt.Point;

import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.math.Vector2D;

/**
 * Cursor (mouse, currently) movement-related event. 
 * Allows binding of selected/hovered entity. 
 */
public class CursorMotionEvent
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
	private SceneEntity entity;
	
	public CursorMotionEvent(Vector2D worldLocation, Point canvasLocation)
	{
		this.worldLocation = worldLocation;
		this.canvasLocation = canvasLocation;
	}

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
	
	public void setSceneEntity(SceneEntity entity) 
	{
		this.entity = entity;
	}
	
	public SceneEntity getEntity() { return entity; }
	
}
