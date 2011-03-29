package yarangi.graphics.quadraturin;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import yarangi.graphics.quadraturin.events.CursorEvent;
import yarangi.graphics.quadraturin.events.CursorListener;
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
//	public abstract IViewPoint getViewPoint();

	/**
	 * Exposes mouse location within the canvas.
	 * @return
	 */
	protected abstract Point getMouseLocation();
	
	/**
	 * TODO: unworthy creation of event object.
	 * @param event
	 */
	protected abstract void declare(CursorEvent event);

	/**
	 * Register a user action listener.
	 * @param action
	 * @param listener
	 */
	public abstract void addUserActionListener(String action, UserActionListener listener);
	
	public abstract void removeUserActionListener(String action);
	
	/**
	 * Register a cursor io listener.
	 * @param listener
	 */
	public abstract void addCursorListener(CursorListener listener);
	public abstract void removeCursorListener(CursorListener listener);
}
