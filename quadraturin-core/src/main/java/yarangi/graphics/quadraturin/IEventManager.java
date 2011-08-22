package yarangi.graphics.quadraturin;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import yarangi.graphics.quadraturin.events.CursorEvent;
import yarangi.graphics.quadraturin.events.CursorListener;

/**
 * User IO events manager interface.
 * 
 */
public interface IEventManager extends KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, StageListener
{

	/**
	 * Exposes {@link IViewPoint} for camera control. 
	 * @return
	 */
//	public abstract IViewPoint getViewPoint();

	/**
	 * Exposes mouse location within the canvas.
	 * @return
	 */
	public Point getMouseLocation();
	
	/**
	 * TODO: unworthy creation of event object.
	 * @param event
	 */
	public void declare(CursorEvent event);
	
	/**
	 * Register a cursor io listener.
	 * @param listener
	 */
	public void addCursorListener(CursorListener listener);
	public void removeCursorListener(CursorListener listener);
}
