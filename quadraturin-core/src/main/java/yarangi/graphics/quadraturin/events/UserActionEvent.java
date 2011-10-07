package yarangi.graphics.quadraturin.events;


/**
 * User action event represents a mouse or keyboard buttons action (press, release, tap).
 * TODO: add keyboard special buttons as additional modifiers (Shift, CTRL, Alt, etc)
 * 
 * Add codes for other mouse buttons.
 */
public class UserActionEvent 
{
	/**
	 * An action id.
	 */
	private String actionId;
	
	/**
	 * Input configuration that caused this event.
	 */
	private InputHook hook;
	
	/**
	 * Defines cursor position
	 */
	private ICursorEvent cursor;

	
	public UserActionEvent(String actionId, InputHook hook, ICursorEvent cursor)
	{
		this.actionId = actionId;
		this.hook = hook;
		this.cursor = cursor;
	}
	public String getActionId() { return actionId; }
	
	public ICursorEvent getCursor() { return cursor; }
	
	public String toString()
	{
		return new StringBuilder()
			.append("UAE ").append("action: " ).append(actionId).append(", input: ").append(hook)
			.toString();
	}

}
