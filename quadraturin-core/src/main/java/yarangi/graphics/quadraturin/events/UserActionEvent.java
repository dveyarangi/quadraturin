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
	private final String actionId;
	
	/**
	 * Input configuration that caused this event.
	 */
	private final InputHook hook;
	
	/**
	 * Defines cursor position
	 */
	private final ICursorEvent cursor;

	
	public UserActionEvent(String actionId, InputHook hook, ICursorEvent cursor)
	{
		this.actionId = actionId;
		this.hook = hook;
		this.cursor = cursor;
	}
	public String getActionId() { return actionId; }
	
	/**
	 * Retrieves cursor picking event, which contains layer coordinates of the cursor and entity that can be picked.  
	 * @return
	 */
	public ICursorEvent getCursor() { return cursor; }
	
	@Override
	public String toString()
	{
		return new StringBuilder()
			.append("UAE ").append("action: " ).append(actionId).append(", input: ").append(hook)
			.toString();
	}

}
