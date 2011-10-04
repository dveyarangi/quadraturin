package yarangi.graphics.quadraturin.events;

import java.awt.Point;

import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.math.Vector2D;

public interface ICursorEvent {

	/**
	 * @return Cursor location in scene coordinates.
	 */
	public Vector2D getWorldLocation();

	/**
	 * @return Cursor location in canvas coordinates.
	 */
	public Point getCanvasLocation();

	public InputHook getInput();
	
	/**
	 * @return Entity at the cursor location; null if none
	 */
	public IEntity getEntity();

}