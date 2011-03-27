package yarangi.graphics.quadraturin.events;

import java.awt.event.KeyEvent;

import yarangi.graphics.quadraturin.objects.SceneEntity;

/**
 * User action event represents a mouse or keyboard buttons action (press, release, tap).
 * TODO: add keyboard special buttons as additional modifiers (Shift, CTRL, Alt, etc)
 * 
 * Add codes for other mouse buttons.
 */
public class UserActionEvent 
{
	public static final int MOUSE_LEFT_BUTTON = -1;
	public static final int MOUSE_RIGHT_BUTTON = -1;
	/**
	 * Button pressed flag
	 */
	public static final int PRESSED = 1;
	
	/**
	 * Button tapped flag
	 */
	public static final int TAPPED = 2;
	
	/**
	 * Button released flag
	 */
	public static final int RELEASED = 3;
	
	/**
	 * An action id.
	 */
	private int action;
	
	/**
	 * Code of pressed button.Uses the {@link KeyEvent#getKeyCode()} values for keyboard buttons, or 
	 * {@link #MOUSE_LEFT_BUTTON}/{@link #MOUSE_RIGHT_BUTTON} constants for mouse buttons.
	 */
	private int inputCode;
	
	/**
	 * One of the({@link #PRESSED}/{@link #TAPPED}/{@link #RELEASED}} consts.
	 */
	private int inputMode;
	
	/**
	 * Entity involved in this event.
	 */
	private SceneEntity entity;

	
	public UserActionEvent(int action, int inputCode, int inputMode)
	{
		this(action, inputCode, inputMode, null);
	}
	
	public UserActionEvent(int action, int inputCode, int inputMode, SceneEntity entity)
	{
		this.action = action;
		this.inputCode = inputCode;
		this.inputMode = inputMode;
		this.entity = entity;
	}
	
	public void setSceneEntity(SceneEntity entity) {
		this.entity = entity;
	}
	public int getActionId() { return action; }
	
	public int getInputCode() { return inputCode; }
	public int getInputModifier() { return inputMode; }
	public SceneEntity getEntity() { return entity; }
	public boolean isPressed() { return inputMode == PRESSED; }
	public boolean isTapped() { return inputMode == TAPPED; }
	public boolean isReleased() { return inputMode == RELEASED; }
	
	public String toString()
	{
		return new StringBuilder()
			.append("UAE ").append("action: " ).append(action).append(", input: ").append(inputCode).append(", mode: ").append(inputMode)
			.toString();
	}

}
