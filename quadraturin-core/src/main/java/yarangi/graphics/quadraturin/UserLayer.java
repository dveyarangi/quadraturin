package yarangi.graphics.quadraturin;

import java.awt.Point;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.graphics.quadraturin.ui.Overlay;
import yarangi.graphics.quadraturin.ui.Panel;
import yarangi.spatial.AABB;
import yarangi.spatial.PickingSensor;
import yarangi.spatial.SpatialHashMap;

public class UserLayer extends SceneLayer <Overlay>
{

//	private List <ActionOverlay> actionIOverlays = new LinkedList <ActionOverlay> ();

	private Panel basePanel;
	
	public static final double CURSOR_PICK_SPAN = 5;
	
	private double halfWidth, halfHeight;


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
	protected boolean testEntity(Logger log, Overlay entity)
	{
		boolean test = super.testEntity(log, entity);
		
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
			basePanel.revalidate( context.getViewPort() );
			int width = context.getViewPort().getWidth();
			int height = context.getViewPort().getHeight();
			this.halfHeight = height/2;
			this.halfWidth = width/2;
			// TODO: might be a bit heavy:
			
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
		
		getEntityIndex().query(sensor, AABB.createSquare(canvasLocation.x+halfWidth, halfHeight-canvasLocation.y, CURSOR_PICK_SPAN, 0));
		
		Overlay pickedObject = sensor.getObject();
		if(pickedObject == null)
			return null; // we are not in UI domain

		// TODO: process pick:
		return pickedObject;
	}
	
}