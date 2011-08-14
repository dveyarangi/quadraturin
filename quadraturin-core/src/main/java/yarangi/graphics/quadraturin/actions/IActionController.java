package yarangi.graphics.quadraturin.actions;

import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.events.CursorListener;
import yarangi.graphics.quadraturin.events.UserActionListener;
import yarangi.graphics.quadraturin.objects.IWorldEntity;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.spatial.ISpatialFilter;


public interface IActionController extends UserActionListener, CursorListener
{
	public abstract Set <String> getActionIds();
	public ISpatialFilter <IWorldEntity> getPickingFilter();
	
	public Look <? extends IActionController> getLook();
	public abstract void display(GL gl, double time, RenderingContext context);
}
