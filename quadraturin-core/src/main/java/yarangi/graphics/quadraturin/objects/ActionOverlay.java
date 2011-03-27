package yarangi.graphics.quadraturin.objects;

import yarangi.graphics.quadraturin.actions.Action;
import yarangi.graphics.quadraturin.events.CursorMotionEvent;
import yarangi.graphics.quadraturin.events.CursorMotionListener;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.events.UserActionListener;
import yarangi.graphics.quadraturin.interaction.spatial.AABB;

public abstract class ActionOverlay extends CompositeSceneEntity implements CursorMotionListener, UserActionListener
{
	private Action action;
	
	protected ActionOverlay(Action action, AABB aabb) {
		super(aabb);
		
		this.action = action;
	}


	public Action getAction() { return action; }
	
	
	@Override
	public boolean isPickable() { return true; }


	public void onUserAction(UserActionEvent event) 
	{
		action.act(event);
	}


	public abstract void onCursorMotion(CursorMotionEvent event);



//	public boolean isPickable();
}
