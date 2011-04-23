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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.ActionBinding;
import yarangi.graphics.quadraturin.config.InputConfig;
import yarangi.graphics.quadraturin.events.CursorEvent;
import yarangi.graphics.quadraturin.events.CursorListener;
import yarangi.graphics.quadraturin.events.InputHook;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.events.UserActionListener;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.graphics.quadraturin.threads.Loopy;
import yarangi.spatial.ISpatialObject;

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
	 * User action listeners. 
	 */
	private HashMap <String, UserActionListener> userListeners = new HashMap <String, UserActionListener> ();
	
	
	/**
	 * User actions (key or mouse key hits)
	 */
	private LinkedBlockingQueue <UserActionEvent> userEvents = new LinkedBlockingQueue <UserActionEvent> ();
	
	
	private Scene scene;
	/**
	 * Mouse location
	 */
	private Point mouseLocation;
	
	private Logger log;
	
	public static final String NAME="q-voice"; 
	
	public QuadVoices(InputConfig config) 
	{

		this.log = Logger.getLogger(NAME);
		
		for(ActionBinding bind : config.getBinding())
		{
			InputHook hook = new InputHook(bind.getModeId(), bind.getButtonId());
			binding.put(hook, bind.getActionId());
			log.debug("Attached hook [" + hook + "] for action [" + bind.getActionId() + "].");

		}
	}
	
//	public IViewPoint getViewPoint() { return viewPoint; }
	
	public void runPreUnLock() { /* voice of the void? */ }

	public void runBody() 
	{

		// TODO: move to rendering cycle and remove QuadVoices dependency on the Scene object:
		ISpatialObject object = scene.pick(cursorEvent.getWorldLocation(), cursorEvent.getCanvasLocation());
		
		SceneEntity pickedEntity = (SceneEntity) object;
		
		cursorEvent.setSceneEntity(pickedEntity);
		
		// firing the cursor motion event:
		for(CursorListener l : cursorListeners)
			l.onCursorMotion(cursorEvent);
		
		// firing user actions info:
//		log.debug("User action event in queue: " + userEvents.size());
		while(userEvents.size() > 0)
		{
			UserActionEvent event = userEvents.poll();
//			log.debug("Firing user action event: " + event.getActionId());
			
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
		
		// TODO: buggy, no button flag are lit on release, and thus input hook constructed incorrectly:
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
		
		log.debug("Registered listener [" + listener + "] for action [" + actionId + "].");
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
	


	public void sceneChanged(Scene prevScene, Scene nextScene) 
	{
		if(prevScene != null && prevScene.getActionsMap() != null)
		for(String actionId : prevScene.getActionsMap().keySet())
			removeUserActionListener(actionId);
		
		if(nextScene != null && nextScene.getActionsMap() != null)
			for(String actionId : nextScene.getActionsMap().keySet())
				addUserActionListener(actionId, nextScene);
		
		scene = nextScene;
//		environmentListeners.clear();
//		selectionListeners.clear();
		// TODO: clear listeners and reset from scene.
	}

}
