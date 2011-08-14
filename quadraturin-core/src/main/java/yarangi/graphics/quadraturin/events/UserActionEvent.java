package yarangi.graphics.quadraturin.events;

import yarangi.graphics.quadraturin.objects.IWorldEntity;

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
	 * Entity involved in this event.
	 */
	private IWorldEntity entity;
	
	private CursorEvent cursor;

	
	public UserActionEvent(String actionId, InputHook hook, CursorEvent cursor)
	{
		this(actionId, hook, null, cursor);
	}
	
	public UserActionEvent(String actionId, InputHook hook, IWorldEntity entity, CursorEvent cursor)
	{
		this.actionId = actionId;
		this.hook = hook;
		this.entity = entity;
		
		this.cursor = cursor;
	}
	
	public void setSceneEntity(IWorldEntity entity) {
		this.entity = entity;
	}
	public String getActionId() { return actionId; }
	
	public IWorldEntity getEntity() { return entity; }
	
	public CursorEvent getCursor() { return cursor; }
	
	public String toString()
	{
		return new StringBuilder()
			.append("UAE ").append("action: " ).append(actionId).append(", input: ").append(hook)
			.toString();
	}

}
