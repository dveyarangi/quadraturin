package yarangi.graphics.quadraturin.actions;

import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.events.CursorListener;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.spatial.ISpatialFilter;

/**
 * Input definitions interface
 * 
 * @author dveyarangi
 */
public interface IActionController extends CursorListener
{
	/**
	 * A map from input name to corresponding {@IAction} 
	 * @return
	 */
	public abstract Map <String, IAction> getActions();
	
	/**
	 * Defines selection of picked entities at mouse cursor.
	 * @return
	 */
	public ISpatialFilter <IEntity> getPickingFilter();

	public abstract void display(GL gl, double time, RenderingContext context);
	
}
