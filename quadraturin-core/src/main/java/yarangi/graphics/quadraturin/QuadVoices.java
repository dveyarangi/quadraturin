/// /////////////////// ///////////////////////// ///
//
//
//
//
//
/// /////////////////// ///////////////////////// ///

package yarangi.graphics.quadraturin;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import yarangi.ZenUtils;
import yarangi.graphics.quadraturin.actions.ActionController;
import yarangi.graphics.quadraturin.actions.IAction;
import yarangi.graphics.quadraturin.config.InputBinding;
import yarangi.graphics.quadraturin.config.InputConfig;
import yarangi.graphics.quadraturin.events.CursorEvent;
import yarangi.graphics.quadraturin.events.InputHook;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.graphics.quadraturin.threads.Loopy;

/**
 * Used to aggregate the regular AWT keyboard and mouse events, transformed mouse events, 
 * cursor hover and picking  events and whatever else that will become a requirement in 
 * the future.  Runs in a separate thread. All accumulated event are dispatched at once.
 * 
 * TODO: fire GUIEvents?
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
//	private List <CursorListener> cursorListeners = new LinkedList <CursorListener> ();
	
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
	
	public void runPreUnLock() { /* Abyssus abyssum invocat */ }

	public void runBody() 
	{
		if(controller == null)
			return;
		
		// picking object under cursor:
		ILayerObject pickedEntity = controller.pick(cursorEvent.getWorldLocation(), cursorEvent.getCanvasLocation());
		
		// firing the cursor motion event:
		cursorEvent.setSceneEntity(pickedEntity);
//		for(CursorListener l : cursorListeners)
//			l.onCursorMotion(cursorEvent);
		
		// firing user actions info:
//		log.trace("User action events in queue: " + userEvents.size());
		while(userEvents.size() > 0)
		{
			UserActionEvent event = userEvents.poll();
//			log.trace("Firing user action event: " + event.getActionId());
			
			IAction action = controller.getActions().get(event.getActionId());
			if(action != null)
				action.act( event );
		}

	}
	
	public void runPostLock() { }
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// EVENTS TRANSFORMATION
	
	/**
	 * Tests if specified input hoot is mapped to an user action and adds it to action events queue
	 */
	private void enqueueUserActionEvent(InputHook hook)
	{
		if(binding.containsKey(hook))
			userEvents.add(new UserActionEvent(binding.get(hook), hook, cursorEvent));
	}	
	
	/** {@inheritDoc} */
	@Override
	public void keyPressed(KeyEvent ke) 
	{
		enqueueUserActionEvent(new InputHook(InputHook.PRESSED, ke.getKeyCode()));
	}
	
	/** {@inheritDoc} */
	@Override
	public void keyReleased(KeyEvent ke) 
	{ 
		enqueueUserActionEvent(new InputHook(InputHook.RELEASED, ke.getKeyCode()));
	}
	
	/** {@inheritDoc} */
	@Override
	public void keyTyped(KeyEvent ke) 
	{ 
		enqueueUserActionEvent(new InputHook(InputHook.TAPPED, ke.getKeyCode()));
	}
	
	/** {@inheritDoc} */
	@Override
	public void mousePressed(MouseEvent e) 
	{ 
		InputHook hook = new InputHook(InputHook.PRESSED, InputHook.getMouseButton(e.getModifiersEx()));
		cursorEvent.setMouseLocation(e.getPoint());
		enqueueUserActionEvent(hook);
	}
	
	/** {@inheritDoc} */
	@Override
	public void mouseReleased(MouseEvent e) 
	{ 
		// TODO: buggy, no button flags are lit on release, and thus input hook constructed incorrectly:
		cursorEvent.setMouseLocation(e.getPoint());
		enqueueUserActionEvent(new InputHook(InputHook.RELEASED, InputHook.getMouseButton(e.getModifiersEx())));
	}
	
	/** {@inheritDoc} */
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		cursorEvent.setMouseLocation(e.getPoint());
		enqueueUserActionEvent(new InputHook(InputHook.TAPPED, InputHook.getMouseButton(e.getModifiersEx())));
	}

	/** {@inheritDoc} */
	@Override
	public void mouseDragged(MouseEvent e) 
	{ 
		cursorEvent.setMouseLocation(e.getPoint());
		enqueueUserActionEvent(new InputHook(InputHook.DRAGGED, InputHook.getMouseButton(e.getModifiersEx())));
	}
	
	/** {@inheritDoc} */
	@Override
	public void mouseEntered(MouseEvent e) 
	{ 
		cursorEvent.setMouseLocation(e.getPoint());
	}
	
	/** {@inheritDoc} */
	@Override
	public void mouseExited(MouseEvent e) 
	{ 
		cursorEvent.setMouseLocation(null);
	}
	
	/** {@inheritDoc} */
	@Override
	public void mouseMoved(MouseEvent e) 
	{ 
		cursorEvent.setMouseLocation(e.getPoint());
	}

	/** {@inheritDoc} */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		int notches = e.getWheelRotation();
		if(notches == 0)
			return;
		
		cursorEvent.setMouseLocation(e.getPoint());
		enqueueUserActionEvent( new InputHook(notches > 0 ? InputHook.BACKWARD : InputHook.FORWARD, InputHook.MOUSE_WHEEL) );
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ENGINE EVENTS

	/**
	 * Removes listener for entity pick events. 
	 * @param entityId
	 */
/*	public void addCursorListener(CursorListener listener)
	{
		cursorListeners.add(listener);
	}
	public void removeCursorListener(CursorListener listener)
	{
		cursorListeners.remove(listener);
	}*/
	
	/**
	 * Adds an cursor event to the events queue.
	 * @param event
	 */
	public void updateViewPoint(ViewPoint2D viewPoint)
	{
		cursorEvent.setWorldCoordinate(
				viewPoint.toWorldCoordinates(cursorEvent.getCanvasLocation()));
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
		this.controller = nextScene.getActionController();

		// TODO: clear listeners and reset from scene.
	}
}
