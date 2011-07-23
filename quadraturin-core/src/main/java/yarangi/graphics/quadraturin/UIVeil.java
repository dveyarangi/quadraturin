package yarangi.graphics.quadraturin;

import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.spatial.SpatialHashMap;

public class UIVeil extends SceneVeil
{

//	private List <ActionOverlay> actionIOverlays = new LinkedList <ActionOverlay> ();

	
	protected IViewPoint viewPoint;
	

	public UIVeil(int width, int height)
	{
		super(width, height, new SpatialHashMap	<SceneEntity>(100, 10, width, height));
//		this.viewPoint = viewPoint;
	}
	
	public IViewPoint getViewPoint() { return viewPoint; }
	
}