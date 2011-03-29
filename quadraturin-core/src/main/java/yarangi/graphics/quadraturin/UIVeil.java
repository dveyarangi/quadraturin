package yarangi.graphics.quadraturin;

import java.util.LinkedList;
import java.util.List;

import yarangi.graphics.quadraturin.interaction.spatial.SpatialHashMap;
import yarangi.graphics.quadraturin.objects.ActionOverlay;
import yarangi.graphics.quadraturin.objects.SceneEntity;

public abstract class UIVeil extends SceneVeil
{

	private List <ActionOverlay> actionIOverlays = new LinkedList <ActionOverlay> ();

	
	protected IViewPoint viewPoint;
	

	public UIVeil(int width, int height)
	{
		super(new SpatialHashMap<SceneEntity>(100, 10, width, height));
//		this.viewPoint = viewPoint;
	}
	
	public IViewPoint getViewPoint() { return viewPoint; }

}