package yarangi.graphics.quadraturin.events;

import java.awt.Point;

import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.math.Vector2D;

/**
 * Cursor (mouse, currently) movement-related event. 
 * Allows binding of selected/hovered entity. 
 */
public class CursorEvent implements ICursorEvent
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
	 * Set if the cursor if hovering over an entity.
	 */
	private ILayerObject entity;
	
	public CursorEvent(Vector2D worldLocation, Point canvasLocation)
	{
		this.worldLocation = worldLocation;
		this.canvasLocation = canvasLocation;
	}
	
	/**
	 * @param mask
	 */
	public void setMouseLocation(Point canvasLocation)
	{
		this.canvasLocation = canvasLocation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2D getWorldLocation() {
		return worldLocation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point getCanvasLocation() {
		return canvasLocation;
	}
	
	public void setSceneEntity(ILayerObject entity) 
	{
		this.entity = entity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILayerObject getEntity() { return entity; }

	public void setWorldCoordinate(Vector2D worldCoordinates) {
		this.worldLocation = worldCoordinates;
	}

}
