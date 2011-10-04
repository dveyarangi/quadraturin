package yarangi.graphics.quadraturin.events;

import java.awt.Point;

import yarangi.graphics.quadraturin.objects.IEntity;
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
	 * Set if the cursor if hovering over and entity.
	 */
	private IEntity entity;
	
	/**
	 * 
	 */
	private InputHook hook;
	
	public CursorEvent(Vector2D worldLocation, Point canvasLocation)
	{
		this.worldLocation = worldLocation;
		this.canvasLocation = canvasLocation;
	}
	
	/**
	 * @param mask
	 */
	public void setInput(InputHook hook, Point canvasLocation)
	{
		this.hook = hook;
		this.canvasLocation = canvasLocation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputHook getInput() { return hook; }

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
	
	public void setSceneEntity(IEntity entity) 
	{
		this.entity = entity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IEntity getEntity() { return entity; }

	public void setWorldCoordinate(Vector2D worldCoordinates) {
		this.worldLocation = worldCoordinates;
	}

}
