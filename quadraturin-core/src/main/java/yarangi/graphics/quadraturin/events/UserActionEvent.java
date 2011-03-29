package yarangi.graphics.quadraturin.events;

import yarangi.graphics.quadraturin.objects.SceneEntity;

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
	private SceneEntity entity;

	
	public UserActionEvent(String actionId, InputHook hook)
	{
		this(actionId, hook, null);
	}
	
	public UserActionEvent(String actionId, InputHook hook, SceneEntity entity)
	{
		this.actionId = actionId;
		this.hook = hook;
		this.entity = entity;
	}
	
	public void setSceneEntity(SceneEntity entity) {
		this.entity = entity;
	}
	public String getActionId() { return actionId; }
	
	public SceneEntity getEntity() { return entity; }
	
	public String toString()
	{
		return new StringBuilder()
			.append("UAE ").append("action: " ).append(actionId).append(", input: ").append(hook)
			.toString();
	}

}
