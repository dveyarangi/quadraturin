package yarangi.graphics.quadraturin;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import yarangi.graphics.quadraturin.events.CursorMotionEvent;
import yarangi.graphics.quadraturin.events.CursorMotionListener;
import yarangi.graphics.quadraturin.events.UserActionListener;

/**
 * User IO events manager interface.
 * 
 */
public abstract class EventManager implements KeyListener, MouseListener, MouseMotionListener, StageListener
{

	/**
	 * Exposes {@link IViewPoint} for camera control. 
	 * @return
	 */
	protected abstract IViewPoint getViewPoint();

	/**
	 * Exposes mouse location within the canvas.
	 * @return
	 */
	protected abstract Point getMouseLocation();
	
	/**
	 * TODO: unworthy creation of event object.
	 * @param event
	 */
	protected abstract void declare(CursorMotionEvent event);

	/**
	 * Register a user action listener.
	 * @param action
	 * @param listener
	 */
	public abstract void addUserActionListener(int action, UserActionListener listener);
	public abstract void removeUserActionListener(int action);
	
	/**
	 * Register a cursor io listener.
	 * @param listener
	 */
	public abstract void addCursorListener(CursorMotionListener listener);
	public abstract void removeCursorListener(CursorMotionListener listener);
}
