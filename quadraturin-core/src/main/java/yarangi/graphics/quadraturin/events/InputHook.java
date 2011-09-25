package yarangi.graphics.quadraturin.events;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class InputHook 
{
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
	 * Button dragged flag
	 */
	public static final int DRAGGED = 4;	
	
	public static final int FORWARD = 5;	
	public static final int BACKWARD = 6;	
	
	public static final int MOUSE_LEFT_BUTTON = -1;
	public static final int MOUSE_RIGHT_BUTTON = -2;
	public static final int MOUSE_WHEEL = -3;
	

	
	private int buttonId;
	
	private int modeId;
	
	public boolean areButtonsPressed()  { return (modeId == InputHook.PRESSED) ; }
	public boolean areButtonsReleased() { return (modeId == InputHook.RELEASED); }
	public boolean areButtonsTapped()   { return (modeId == InputHook.TAPPED) ; }
	public boolean areButtonsDragged()  { return (modeId == InputHook.DRAGGED) ; }
	
	public InputHook(int modeId, int buttonId)
	{
		this.buttonId = buttonId;
		this.modeId = modeId;
	}
	
	public int getButtonId() { return buttonId; }
	public int getModeId() { return modeId; }
	
	
	public int hashCode() 
	{
		return buttonId*1000 + modeId * 10000000;
	}
	
	public boolean equals(Object o)
	{
		if(!(o instanceof InputHook))
			return false;
		
		return o.hashCode() == hashCode();
	}
	
	/**
	 * TODO: revise for simultaneous buttons
	 * @param modifiers
	 * @return
	 */
	public static int getMouseButton(int modifiers)
	{
		return (modifiers & InputEvent.BUTTON1_DOWN_MASK) != 0 ? MOUSE_LEFT_BUTTON : 
		       (modifiers & InputEvent.BUTTON3_DOWN_MASK) != 0 ? MOUSE_RIGHT_BUTTON : 0;
//		     | modifiers & MouseEvent.BUTTON3_DOWN_MASK << 2;
	}
	
	public String toString()
	{
		return new StringBuilder()
			.append("button <").append(getButtonName(buttonId)).append(">, ")
			.append("mode <" ).append(getModeName(modeId)).append(">")
			.toString();
	}
	
	private static String getButtonName(int buttonId)
	{
		if(buttonId > 0)
			return KeyEvent.getKeyText(buttonId);
		if(buttonId == MOUSE_LEFT_BUTTON)
			return "Mouse Left";
		if(buttonId == MOUSE_RIGHT_BUTTON)
			return "Mouse Right";
		if(buttonId == MOUSE_WHEEL)
			return "Mouse Wheel";
		
		return "unknown name (" + buttonId + ")";
	}
	
	private static String getModeName(int modeId)
	{

		switch(modeId)
		{
		case RELEASED: return "released";
		case PRESSED:  return "pressed";
		case TAPPED:   return "tapped";
		case DRAGGED:  return "dragged";
		case FORWARD:  return "forward";
		case BACKWARD:  return "backward";
		}
		return "unknown mode (" + modeId + ")";
	}

}
