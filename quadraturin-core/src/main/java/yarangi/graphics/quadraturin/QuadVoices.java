/// /////////////////// ///////////////////////// ///
//
//
//
//
//
/// /////////////////// ///////////////////////// ///

package yarangi.graphics.quadraturin;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import yarangi.ZenUtils;
import yarangi.graphics.quadraturin.actions.IAction;
import yarangi.graphics.quadraturin.actions.ActionController;
import yarangi.graphics.quadraturin.config.InputBinding;
import yarangi.graphics.quadraturin.config.InputConfig;
import yarangi.graphics.quadraturin.events.CursorEvent;
import yarangi.graphics.quadraturin.events.CursorListener;
import yarangi.graphics.quadraturin.events.InputHook;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.threads.Loopy;

/**
 * Used to aggregate the regular AWT keyboard and mouse events, transformed mouse events, 
 * cursor hover and picking  events and whatever else that will become a requirement in 
 * the future.  Runs in a separate thread. All accumulated event are dispatched at once.
 * 
 * TODO: append CursorMotionEvent listener that fires GUIEvents?
 * TODO: replacing the AWT event queue shall be more efficient.
 * 
 * @author Dve Yarangi
 */
public class QuadVoices implements IEventManager, Loopy
{
	
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
	 * User actions (key or mouse key hits)
	 */
	private LinkedBlockingQueue <UserActionEvent> userEvents = new LinkedBlockingQueue <UserActionEvent> ();
	
	/** 
	 * Input definitions provider
	 */
	private ActionController controller;

	/**
	 * Mouse location
	 */
	private Point mouseLocation;

	/**
	 * Logging name
	 */
	public static final String NAME="q-voice"; 
	
	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(NAME);
	
	
	public QuadVoices(InputConfig config) 
	{
		ZenUtils.summonLogic();
		
		// mapping input hooks to action ids:
		for(InputBinding bind : config.getBindings())
		{
			InputHook hook = new InputHook(bind.getModeId(), bind.getButtonId());
			binding.put(hook, bind.getActionId());
			log.debug("Attached input hook [" + hook + "] for action [" + bind.getActionId() + "].");

		}
	}
	
//	public IViewPoint getViewPoint() { return viewPoint; }
	
	public void runPreUnLock() { /* voice of the void? */ }

	public void runBody() 
	{
		if(controller == null)
			return;
		
		// picking object under cursor:
		IEntity pickedEntity = controller.pick(cursorEvent.getWorldLocation(), cursorEvent.getCanvasLocation());
		
		// firing the cursor motion event:
		cursorEvent.setSceneEntity(pickedEntity);
		for(CursorListener l : cursorListeners)
			l.onCursorMotion(cursorEvent);
		
		// firing user actions info:
//		log.debug("User action events in queue: " + userEvents.size());
		while(userEvents.size() > 0)
		{
			UserActionEvent event = userEvents.poll();
//			log.debug("Firing user action event: " + event.getActionId());
			
			// TODO: either prioritize query, so the picks order will be predictable,
			// or revise to work with picks list: 
			event.setSceneEntity(pickedEntity);
			
			IAction action = controller.getActions().get(event.getActionId());
			if(action != null)
				action.act( event );
		}
		
/*		if(sceneTime != -1)
		{
			for(SceneListener l : sceneListeners)
				l.timeAdvanced(sceneTime);
			sceneTime = -1;
		}*/
	}
	
	public void runPostLock() { /* freedom of voice? */ }
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// EVENTS TRANSFORMATION
	
	/** {@inheritDoc} */
	public void keyPressed(KeyEvent ke) 
	{
		InputHook hook = new InputHook(InputHook.PRESSED, ke.getKeyCode());
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook, cursorEvent));
	}
	
	/** {@inheritDoc} */
	public void keyReleased(KeyEvent ke) 
	{ 
		InputHook hook = new InputHook(InputHook.RELEASED, ke.getKeyCode());
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook, cursorEvent));
	}
	
	/** {@inheritDoc} */
	public void keyTyped(KeyEvent ke) 
	{ 
		InputHook hook = new InputHook(InputHook.TAPPED, ke.getKeyCode());
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook, cursorEvent));
	}
	
	/** {@inheritDoc} */
	public void mousePressed(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
		InputHook hook = new InputHook(InputHook.PRESSED, InputHook.getMouseButton(e.getModifiersEx()));
		cursorEvent.setInput(hook);
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook, cursorEvent));
	}
	
	/** {@inheritDoc} */
	public void mouseReleased(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
		
		// TODO: buggy, no button flag are lit on release, and thus input hook constructed incorrectly:
		InputHook hook = new InputHook(InputHook.RELEASED, InputHook.getMouseButton(e.getModifiersEx()));
		cursorEvent.setInput(hook);
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook, cursorEvent));
	}
	
	/** {@inheritDoc} */
	public void mouseClicked(MouseEvent e) 
	{
		mouseLocation = e.getPoint();
		
		InputHook hook = new InputHook(InputHook.TAPPED, InputHook.getMouseButton(e.getModifiersEx()));
		cursorEvent.setInput(hook);
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook, cursorEvent));
	}

	/** {@inheritDoc} */
	public void mouseDragged(MouseEvent e) 
	{ 
		mouseLocation = e.getPoint();
		
		InputHook hook = new InputHook(InputHook.DRAGGED, InputHook.getMouseButton(e.getModifiersEx()));
		cursorEvent.setInput(hook);
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook, cursorEvent));
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

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		mouseLocation = e.getPoint();
		int notches = e.getWheelRotation();
		if(notches == 0)
			return;
		
		InputHook hook = new InputHook(notches > 0 ? InputHook.BACKWARD : InputHook.FORWARD, InputHook.MOUSE_WHEEL);
		cursorEvent.setInput(hook);
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook, cursorEvent));
	}

	public Point getMouseLocation() 
	{
		return mouseLocation;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ENGINE EVENTS

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
	
	/**
	 * Adds an cursor event to the events queue.
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
	

	/**
	 * {@inheritDoc}
	 * Updates event listeners from new scene
	 */
	public void sceneChanged(Scene nextScene) 
	{
//		environmentListeners.clear();
//		selectionListeners.clear();
		// TODO: clear listeners and reset from scene.
	}

	/**
	 * Allows for dynamic binding of input definitions.
	 * @param controller
	 */
	public void setActionController(ActionController controller)
	{

		removeCursorListener( this.controller );
		
		this.controller = controller;
		addCursorListener( controller );
		
	}
}
