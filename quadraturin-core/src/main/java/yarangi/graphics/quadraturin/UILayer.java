package yarangi.graphics.quadraturin;

import yarangi.graphics.quadraturin.objects.Overlay;
import yarangi.spatial.SpatialHashMap;

public class UILayer extends SceneLayer <Overlay>
{

//	private List <ActionOverlay> actionIOverlays = new LinkedList <ActionOverlay> ();

	

	public UILayer(int width, int height)
	{
		super(width, height, new SpatialHashMap	<Overlay>(100, 10, width, height));
//		this.viewPoint = viewPoint;
	}

	@Override
	public void animate(double time)
	{
		// TODO: implementation depends on {@link Overlay} ?
		
	}

	@Override
	protected boolean testEntity(Overlay entity)
	{
		return entity.getArea() != null && entity.getLook() != null;
	}
	
}