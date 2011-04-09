package yarangi.graphics.quadraturin.actions;

import yarangi.graphics.quadraturin.events.UserActionEvent;

/**
 * Action represents a response to predefined user action.
 * @see {@link yarangi.graphics.quadraturin.events.InputHook} 
 * @author dveyarangi
 */
public interface IAction 
{
	/**
	 * Performs the action. 
	 * @param event
	 */
	public void act(UserActionEvent event);
}
