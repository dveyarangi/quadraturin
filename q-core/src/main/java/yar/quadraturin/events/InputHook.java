package yar.quadraturin.events;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
	

	
	private final int buttonId;
	
	private final int modeId;
	
	private final int modifiers;
	
	private final boolean shift;
	private final boolean control;
	private final boolean alt;
	
	public boolean areButtonsPressed()  { return (modeId == InputHook.PRESSED) ; }
	public boolean areButtonsReleased() { return (modeId == InputHook.RELEASED); }
	public boolean areButtonsTapped()   { return (modeId == InputHook.TAPPED) ; }
	public boolean areButtonsDragged()  { return (modeId == InputHook.DRAGGED) ; }
	
	/**
	 * TODO: pool them
	 * @param modeId
	 * @param buttonId
	 */
	public InputHook(int modeId, int buttonId, int modifiers)
	{
		this.buttonId = buttonId;
		this.modeId = modeId;
		this.modifiers = modifiers;
		
		shift = 0 != (KeyEvent.SHIFT_DOWN_MASK & modifiers);
		control = 0 != (KeyEvent.CTRL_DOWN_MASK & modifiers);
		alt = 0 != (KeyEvent.ALT_DOWN_MASK & modifiers);
	}
	
	public int getButtonId() { return buttonId; }
	public int getModeId() { return modeId; }
	public int getModifiers() { return modifiers; }
	
	@Override
	public int hashCode() 
	{
		return buttonId*1000 + modeId * 10000000 /*+ 10000 * modifiers*/;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof InputHook))
			return false;
		
		return o.hashCode() == hashCode() ;
	}
	
	/**
	 * TODO: revise for simultaneous buttons
	 * @param modifiers
	 * @return
	 */
	public static int getMouseButtonFromModifiers(int modifiers)
	{
		return (modifiers & InputEvent.BUTTON1_DOWN_MASK) != 0 ? MOUSE_LEFT_BUTTON : 
		       (modifiers & InputEvent.BUTTON3_DOWN_MASK) != 0 ? MOUSE_RIGHT_BUTTON : 0;
//		     | modifiers & MouseEvent.BUTTON3_DOWN_MASK << 2;
	}
	
	@Override
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
	public static int getMouseButton(int button)
	{
		switch(button)
		{
		case MouseEvent.BUTTON1: return MOUSE_LEFT_BUTTON;
		case MouseEvent.BUTTON2:  return MOUSE_RIGHT_BUTTON;
		default: return 0;
		}
	}

}
