/// /////////////////// ///////////////////////// ///
//
//
//
//
//
/// /////////////////// ///////////////////////// ///

package yarangi.graphics.quadraturin;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import yarangi.graphics.quadraturin.config.ActionBinding;
import yarangi.graphics.quadraturin.config.InputConfig;
import yarangi.graphics.quadraturin.events.CursorEvent;
import yarangi.graphics.quadraturin.events.CursorListener;
import yarangi.graphics.quadraturin.events.InputHook;
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
	EventQueue queue;
	/**
	 * Holds last cursor motion event.
	 */
	private CursorEvent cursorEvent = new CursorEvent(null, null);
	
	/**
	 * List of listeners for the CursorMotionEvent-s
	 */
	private List <CursorListener> cursorListeners = new LinkedList <CursorListener> ();
	
	/**
	 * Maps input code to action id. 
	 */
	private Map <InputHook, String> binding = new HashMap <InputHook, String> ();
	
	/**
	 * User action listeners. 
	 */
	private HashMap <String, UserActionListener> userListeners = new HashMap <String, UserActionListener> ();
	
	
	/**
	 * User actions (key or mouse key hits)
	 */
	private LinkedBlockingQueue <UserActionEvent> userEvents = new LinkedBlockingQueue <UserActionEvent> ();
	
	
	
//	private IViewPoint viewPoint;
	
	private Scene scene;
	/**
	 * Mouse location
	 */
	private Point mouseLocation;
	
	public QuadVoices(/*IViewPoint viewPoint, */InputConfig config) 
	{
//		this.viewPoint = viewPoint;
		
		for(ActionBinding bind : config.getBinding())
			binding.put(new InputHook(bind.getModeId(), bind.getButtonId()), bind.getActionId());
	}
	
//	public IViewPoint getViewPoint() { return viewPoint; }
	
	public void runPreUnLock() { /* voice of the void? */ }

	public void runBody() 
	{

		// TODO: move to rendering cycle?
		SceneEntity pickedEntity = scene.pick(cursorEvent.getWorldLocation(), cursorEvent.getCanvasLocation());
		
		cursorEvent.setSceneEntity(pickedEntity);
		
		// firing the cursor motion event:
		for(CursorListener l : cursorListeners)
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
	
	public void hookAction(InputHook input, int actionId)
	{
		
	}

	
	public void runPostLock() { /* freedom of voice? */ }
	
	/** {@inheritDoc} */
	public void keyPressed(KeyEvent ke) 
	{
		InputHook hook = new InputHook(InputHook.PRESSED, ke.getKeyCode());
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook));
	}
	
	/** {@inheritDoc} */
	public void keyReleased(KeyEvent ke) 
	{ 
		InputHook hook = new InputHook(InputHook.RELEASED, ke.getKeyCode());
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook));
	}
	
	/** {@inheritDoc} */
	public void keyTyped(KeyEvent ke) 
	{ 
		InputHook hook = new InputHook(InputHook.TAPPED, ke.getKeyCode());
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook));
	}
	
	/** {@inheritDoc} */
	public void mousePressed(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
		InputHook hook = new InputHook(InputHook.PRESSED, InputHook.getMouseButton(e.getModifiersEx()));
//		System.out.println(hook);
		cursorEvent.setInput(hook);
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook));
	}
	
	/** {@inheritDoc} */
	public void mouseReleased(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
		
		InputHook hook = new InputHook(InputHook.RELEASED, InputHook.getMouseButton(e.getModifiersEx()));
		cursorEvent.setInput(hook);
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook));
	}
	
	/** {@inheritDoc} */
	public void mouseClicked(MouseEvent e) 
	{
		mouseLocation = e.getPoint();
		
		InputHook hook = new InputHook(InputHook.TAPPED, InputHook.getMouseButton(e.getModifiersEx()));
		cursorEvent.setInput(hook);
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook));
	}

	/** {@inheritDoc} */
	public void mouseDragged(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
		
		InputHook hook = new InputHook(InputHook.DRAGGED, InputHook.getMouseButton(e.getModifiersEx()));
		cursorEvent.setInput(hook);
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook));
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
	public void mouseMoved(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
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
	public void addUserActionListener(String actionId, UserActionListener listener)
	{
		if(userListeners.get(actionId) != null)
			throw new IllegalStateException("Action " + actionId + " already has a registered listener.");
		
		userListeners.put(actionId, listener);
	}
	public void removeUserActionListener(String actionId)
	{
		userListeners.remove(actionId);
	}
	
	/**
	 * Removes listener for entity pick events. 
	 * @param entityId
	 */
	public void addCursorListener(CursorListener listener)
	{
		cursorListeners.add(listener);
	}
	public void removeCursorListener(CursorListener listener)
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
	public void declare(CursorEvent event)
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
	}

}
