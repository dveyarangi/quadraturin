package yarangi.graphics.quadraturin.actions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import yarangi.graphics.quadraturin.objects.ActionOverlay;
import yarangi.graphics.quadraturin.EventManager;
import yarangi.graphics.quadraturin.IViewPoint;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.events.UserActionListener;

/**
 * TODO: This action model is stupid. But I smooke too much to figure it out.  
 *
 */
public abstract class UIProvider implements UserActionListener
{
	
	private static int actionIdInc = 1984;
	

	private Map<Integer, Integer> binding = new HashMap <Integer, Integer> ();
	
	private Map <Integer, Action> actions = new HashMap <Integer, Action> ();
	
	private List <ActionOverlay> actionIOverlays = new LinkedList <ActionOverlay> ();
	
	
	protected IViewPoint viewPoint;
	
	public UIProvider(IViewPoint viewPoint)
	{
		this.viewPoint = viewPoint;
	}
	
	public static int createActionId() { return actionIdInc ++; }
	
	public IViewPoint getViewPoint() { return viewPoint; }
	
	protected void addAction(int eventId, Action action)
	{
		binding.put(eventId, action.getId());
		actions.put(action.getId(), action);
	}
	
	public Map<Integer, Integer> bindActions(EventManager manager)
	{
		for(int actionId : binding.values())
		{
			manager.addUserActionListener(actionId, this);
		}
		
		return binding;
	}
	

	public void onUserAction(UserActionEvent event) 
	{
		Action action = actions.get(event.getActionId());

		if(action == null)
			throw new IllegalArgumentException("Action id " + event.getActionId() + " is not defined." );
		
		action.act(event);
	}


}
