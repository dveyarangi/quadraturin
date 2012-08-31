package yarangi.graphics.quadraturin.events;

import java.awt.Point;

import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.math.IVector2D;
import yarangi.math.Vector2D;

public interface ICursorEvent {

	/**
	 * @return Cursor location in scene coordinates.
	 */
	public IVector2D getWorldLocation();

	/**
	 * @return Cursor location in canvas coordinates.
	 */
	public Point getCanvasLocation();
	
	/**
	 * @return Entity at the cursor location; null if none.
	 */
	public ILayerObject getEntity();

}