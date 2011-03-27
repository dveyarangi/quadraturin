package yarangi.graphics.quadraturin.actions;

import yarangi.graphics.quadraturin.events.UserActionEvent;

public abstract class Action 
{
	
	private static int actionIdInc = 1984;
	
	private int id = actionIdInc ++;

	public int getId() { return id; }
	public abstract void act(UserActionEvent event);
}
