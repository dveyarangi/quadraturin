package yarangi.graphics.quadraturin.actions;

import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.events.CursorListener;
import yarangi.graphics.quadraturin.objects.IWorldEntity;
import yarangi.spatial.ISpatialFilter;


public interface IActionController extends CursorListener
{
	public abstract Map <String, IAction> getActions();
	public ISpatialFilter <IWorldEntity> getPickingFilter();
	public abstract void display(GL gl, double time, RenderingContext context);
}
