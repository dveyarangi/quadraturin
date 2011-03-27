/// /////////////////// ///////////////////////// ///
//
//
//
//
//
/// /////////////////// ///////////////////////// ///

package yarangi.graphics.quadraturin;

import static yarangi.graphics.quadraturin.events.UserActionEvent.MOUSE_LEFT_BUTTON;
import static yarangi.graphics.quadraturin.events.UserActionEvent.PRESSED;
import static yarangi.graphics.quadraturin.events.UserActionEvent.RELEASED;
import static yarangi.graphics.quadraturin.events.UserActionEvent.TAPPED;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import yarangi.graphics.quadraturin.events.CursorMotionEvent;
import yarangi.graphics.quadraturin.events.CursorMotionListener;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.events.UserActionListener;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.graphics.quadraturin.thread.Loopy;

/**
 * Used to aggregate the regular AWT keyboard and mouse events, transformed mouse events, cursor hover and picking
 * events and whatever else that will become a requirement in the future. 
 * Runs as a separate thread. All accumulated event are dispatched at once.
 * 
 * TODO: add "TransfomationEvent" to adjust mouse-world conversion?
 * 
 * TODO: append CursorMotionEvent listener that fires GUIEvents?
 * 
 * @author Dve Yarangi
 */
public class QuadVoices extends EventManager implements Loopy
{
	
	/**
	 * Holds last cursor motion event.
	 */
	private CursorMotionEvent cursorEvent = new CursorMotionEvent(null, null);
	
	/**
	 * List of listeners for the CursorMotionEvent-s
	 */
	private List <CursorMotionListener> cursorListeners = new LinkedList <CursorMotionListener> ();
	
	/**
	 * User actions (key or mouse key hits)
	 */
	private LinkedBlockingQueue <UserActionEvent> userEvents = new LinkedBlockingQueue <UserActionEvent> ();
	
	/**
	 * User action listeners. 
	 */
	private HashMap <Integer, UserActionListener> userListeners = new HashMap <Integer, UserActionListener> ();
	
	private IViewPoint viewPoint;
	
	/**
	 * Maps input code to action id. 
	 */
	private Map <Integer, Integer> binding;
	
	private Scene scene;
	/**
	 * Mouse location
	 */
	private Point mouseLocation;
	
	public QuadVoices(IViewPoint viewPoint) 
	{
		this.viewPoint = viewPoint;
	}
	
	public IViewPoint getViewPoint() { return viewPoint; }
	
	public void runPreUnLock() { /* voice of the void? */ }

	public void runBody() 
	{

		// TODO: move to rendering cycle?
		SceneEntity pickedEntity = scene.pick(cursorEvent.getWorldLocation(), cursorEvent.getCanvasLocation());
		
		cursorEvent.setSceneEntity(pickedEntity);
		
		// firing the cursor motion event:
		for(CursorMotionListener l : cursorListeners)
			l.onCursorMotion(cursorEvent);
		
		// firing user actions info:
		while(userEvents.size() > 0)
		{
			UserActionEvent event = userEvents.poll();
			
			// TODO: either prioritize query, so the picks order will be predictable,
			// or revise to work with picks list: 
			event.setSceneEntity(pickedEntity);
			
			UserActionListener listener = userListeners.get(event.getActionId());
			if(listener == null)
				continue;
			
			listener.onUserAction(event);
		}
		
/*		if(sceneTime != -1)
		{
			for(SceneListener l : sceneListeners)
				l.timeAdvanced(sceneTime);
			sceneTime = -1;
		}*/
	}

	
	public void runPostLock() { /* freedom of voice? */ }
	
	/** {@inheritDoc} */
	public void keyPressed(KeyEvent ke) 
	{
		if(binding.containsKey(ke.getKeyCode()))
			userEvents.add(new UserActionEvent(binding.get(ke.getKeyCode()), ke.getKeyCode(), PRESSED));
	}
	
	/** {@inheritDoc} */
	public void keyReleased(KeyEvent ke) 
	{ 
		if(binding.containsKey(ke.getKeyCode()))
			userEvents.add(new UserActionEvent(binding.get(ke.getKeyCode()), ke.getKeyCode(), RELEASED));
	}
	
	/** {@inheritDoc} */
	public void keyTyped(KeyEvent ke) 
	{ 
		if(binding.containsKey(ke.getKeyCode()))
			userEvents.add(new UserActionEvent(binding.get(ke.getKeyCode()), ke.getKeyCode(), TAPPED));
	}
	
	/** {@inheritDoc} */
	public void mouseClicked(MouseEvent e) 
	{
		mouseLocation = e.getPoint();
		// TODO: mouse button should be separated.
		if(binding.containsKey(MOUSE_LEFT_BUTTON))
			userEvents.add(new UserActionEvent(binding.get(MOUSE_LEFT_BUTTON), MOUSE_LEFT_BUTTON, TAPPED));
	}
	
	/** {@inheritDoc} */
	public void mouseEntered(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
	}
	
	/** {@inheritDoc} */
	public void mouseExited(MouseEvent e) 
	{ 
		mouseLocation = null;
	}
	
	/** {@inheritDoc} */
	public void mousePressed(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
		// TODO: mouse button should be separated.
		if(binding.containsKey(UserActionEvent.MOUSE_LEFT_BUTTON))
			userEvents.add(new UserActionEvent(binding.get(MOUSE_LEFT_BUTTON), MOUSE_LEFT_BUTTON, PRESSED));
	}
	
	/** {@inheritDoc} */
	public void mouseReleased(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
		if(binding.containsKey(UserActionEvent.MOUSE_LEFT_BUTTON))
			userEvents.add(new UserActionEvent(binding.get(MOUSE_LEFT_BUTTON), MOUSE_LEFT_BUTTON, RELEASED));
	}
	
	/** {@inheritDoc} */
	public void mouseMoved(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
	}

	/** {@inheritDoc} */
	public void mouseDragged(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
		// TODO: mouse button should be separated.
		if(binding.containsKey(UserActionEvent.MOUSE_LEFT_BUTTON))
			userEvents.add(new UserActionEvent(binding.get(MOUSE_LEFT_BUTTON), MOUSE_LEFT_BUTTON, PRESSED));
	}
	
	public Point getMouseLocation() 
	{
		return mouseLocation;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ENGINE EVENTS

	/**
	 * Adds listeners for user actions
	 * @param entityId
	 * @param listener
	 */
	public void addUserActionListener(int actionId, UserActionListener listener)
	{
		if(userListeners.get(actionId) != null)
			throw new IllegalStateException("Action " + actionId + " already has a registered listener.");
		
		userListeners.put(actionId, listener);
	}
	public void removeUserActionListener(int actionId)
	{
		userListeners.remove(actionId);
	}
	
	/**
	 * Removes listener for entity pick events. 
	 * @param entityId
	 */
	public void addCursorListener(CursorMotionListener listener)
	{
		cursorListeners.add(listener);
	}
	public void removeCursorListener(CursorMotionListener listener)
	{
		cursorListeners.remove(listener);
	}
	
/*	public void addSceneListener(SceneListener listener)
	{
		sceneListeners.add(listener);
	}
	public void removeSceneListener(SceneListener listener)
	{
		sceneListeners.remove(listener);
	}*/
	
	/**
	 * Adds an environment event to the events queue.
	 * @param event
	 */
	public void declare(CursorMotionEvent event)
	{
		cursorEvent = event;
	}

	/**
	 * Adds a selection event to the  events queue. 
	 * @param event
	 */
	public void declare(UserActionEvent event)
	{
		userEvents.add(event);
	}
	


	public void sceneSet(Scene scene) 
	{
		this.scene = scene;
//		environmentListeners.clear();
//		selectionListeners.clear();
		// TODO: clear listeners and reset from scene.
		binding = scene.bind(viewPoint, this);
	}

}
