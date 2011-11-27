package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.ui.Insets;
import yarangi.graphics.quadraturin.ui.Overlay;
import yarangi.graphics.quadraturin.ui.Panel;
import yarangi.spatial.SpatialHashMap;

public class UILayer extends SceneLayer <Overlay>
{

//	private List <ActionOverlay> actionIOverlays = new LinkedList <ActionOverlay> ();

	private Panel basePanel;

	public UILayer(int width, int height)
	{
		super(width, height, new SpatialHashMap	<Overlay>(100, 10, width, height));
		basePanel = new Panel(new ViewPort(0, 0, width, height));
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

	public Panel getBasePanel()
	{
		return basePanel;
	}
	
	public void display(GL gl, double time, IRenderingContext context) 
	{
		if(basePanel.getViewPort() != context.getViewPort())
		{
			basePanel.revalidate( context.getViewPort() );
		}
		
		super.display( gl, time, context );
	}
	
}