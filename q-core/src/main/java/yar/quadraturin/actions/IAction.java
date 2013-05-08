package yar.quadraturin.actions;

import yar.quadraturin.events.UserActionEvent;

/**
 * Action represents a response to predefined user action.
 * @see {@link yar.quadraturin.events.InputHook} 
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
