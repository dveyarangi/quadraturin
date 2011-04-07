package yarangi.graphics.quadraturin;

import yarangi.spatial.ISpatialObject;
import yarangi.spatial.SpatialHashMap;

public abstract class UIVeil extends SceneVeil
{

//	private List <ActionOverlay> actionIOverlays = new LinkedList <ActionOverlay> ();

	
	protected IViewPoint viewPoint;
	

	public UIVeil(int width, int height)
	{
		super(new SpatialHashMap<ISpatialObject>(100, 10, width, height));
//		this.viewPoint = viewPoint;
	}
	
	public IViewPoint getViewPoint() { return viewPoint; }

}