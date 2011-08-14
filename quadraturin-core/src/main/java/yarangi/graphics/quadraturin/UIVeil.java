package yarangi.graphics.quadraturin;

import yarangi.graphics.quadraturin.objects.Overlay;
import yarangi.spatial.SpatialHashMap;

public class UIVeil extends SceneVeil <Overlay>
{

//	private List <ActionOverlay> actionIOverlays = new LinkedList <ActionOverlay> ();

	
	protected IViewPoint viewPoint;
	

	public UIVeil(int width, int height, ViewPoint2D viewPoint)
	{
		super(width, height, new SpatialHashMap	<Overlay>(100, 10, width, height), viewPoint);
//		this.viewPoint = viewPoint;
	}
	
	public IViewPoint getViewPoint() { return viewPoint; }

	@Override
	public void animate(double time)
	{
		// TODO Auto-generated method stub
		
	}
	
}