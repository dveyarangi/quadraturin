package yarangi.graphics.quadraturin;

import java.awt.Point;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.graphics.quadraturin.ui.Overlay;
import yarangi.graphics.quadraturin.ui.Panel;
import yarangi.spatial.AABB;
import yarangi.spatial.PickingSensor;
import yarangi.spatial.SpatialHashMap;

import com.spinn3r.log5j.Logger;

public class UserLayer extends SceneLayer <Overlay>
{

//	private List <ActionOverlay> actionIOverlays = new LinkedList <ActionOverlay> ();

	private Panel basePanel;
	
	public static final double CURSOR_PICK_SPAN = 5;
	
	private double halfWidth, halfHeight;
	
	// TODO: overlay spatial filter

	protected Logger log = Logger.getLogger("q-userlayer");
	public UserLayer(int width, int height)
	{
		super(width, height);
		this.halfHeight = height/2;
		this.halfWidth = width/2;
		basePanel = new Panel(new ViewPort(0, 0, width, height));
		setEntityIndex( new SpatialHashMap	<Overlay>(width*height/100, 10, width, height) );
//		this.viewPoint = viewPoint;
	}

	@Override
	public void animate(double time)
	{
	}

	@Override
	protected boolean testEntity(Overlay entity)
	{
		boolean test = super.testEntity(entity);
		
		if(entity.getArea() == null) {
			log.warn( "Entity [" + entity + "] must define area aspect." );
			test = false;
		}
		
		return test;
	}

	public Panel getBasePanel()
	{
		return basePanel;
	}
	
	public void init(GL gl, IRenderingContext context)
	{
	}
	
	public void destroy(GL gl, IRenderingContext context)
	{
		
	}
	
	public void display(GL gl, double time, IRenderingContext context) 
	{
		if(basePanel.getViewPort() != context.getViewPort())
		{
			log.debug("Layer vieweport has changed.");
			basePanel.revalidate( context.getViewPort() );
			int width = context.getViewPort().getWidth();
			int height = context.getViewPort().getHeight();
			this.halfHeight = height/2;
			this.halfWidth = width/2;
			// TODO: might be a bit heavy:
			log.debug("Creating new spatial hash map (" + width + "x" +height + ")");
			setEntityIndex( new SpatialHashMap<Overlay>(width*height/100, 10, width, height) );
			
			for(Overlay overlay : getEntities())
				if(overlay.isIndexed())
					getEntityIndex().add( overlay.getArea(), overlay );
		}
		
//		if(Debug.ON)
//			Debug.drawUserLayerOverlay(gl, this, context);
		
		super.display( gl, time, context );
	}

	public ILayerObject processPick(Point canvasLocation)
	{
		// collecting picked entities:
		PickingSensor <Overlay> sensor = new PickingSensor <Overlay> ();
		
//		log.debug("picking at: " + (canvasLocation.x+halfWidth) + "," + (halfHeight-canvasLocation.y));
		getEntityIndex().query(sensor, AABB.createSquare(canvasLocation.x, canvasLocation.y, CURSOR_PICK_SPAN, 0));
		
		Overlay pickedObject = sensor.getObject();
		if(pickedObject == null)
			return null; // we are not in UI domain

		// TODO: process pick:
		return pickedObject;
	}
	
}