package yar.quadraturin;

import java.awt.Point;

import javax.media.opengl.GL;

import yar.quadraturin.objects.ILayerObject;
import yar.quadraturin.objects.ILook;
import yar.quadraturin.objects.IVisible;
import yar.quadraturin.ui.Overlay;
import yar.quadraturin.ui.Panel;
import yarangi.spatial.PickingSensor;
import yarangi.spatial.SpatialHashMap;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.spinn3r.log5j.Logger;

/**
 * This class is for user interface elements.
 * TODO: user interface elements
 * 
 * @author dveyarangi
 */
public class UserLayer extends SceneLayer <Overlay>
{

	private static final String NAME = "q-userlayer";
//	private List <ActionOverlay> actionIOverlays = new LinkedList <ActionOverlay> ();

	private final Panel basePanel;
	
	public static final float CURSOR_PICK_SPAN = 5;
	
	private double halfWidth, halfHeight;
	
	private final Multimap <IVisible, ILook> overlays = LinkedListMultimap.<IVisible, ILook>create(); 
	
	// TODO: overlay spatial filter

	protected Logger log = Logger.getLogger();
	public UserLayer(int width, int height)
	{
		super(width, height);
		this.halfHeight = height/2;
		this.halfWidth = width/2;
		basePanel = new Panel(new ViewPort(0, 0, width, height));
		setEntityIndex( new SpatialHashMap	<Overlay>(NAME, width*height/100, 10, width, height) );
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
	
	public void display(GL gl, IRenderingContext context) 
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
			setEntityIndex( new SpatialHashMap<Overlay>( NAME, width*height/100, 10, width, height ) );
			
//			for(Overlay overlay : getEntities())
//				if(overlay.isIndexed())
//					getEntityIndex().add( overlay.getArea(), overlay );
		}
		
//		if(Debug.ON)
//			Debug.drawUserLayerOverlay(gl, this, context);
		
//		super.display( gl, context );
	}

	public ILayerObject processPick(Point canvasLocation)
	{
		// collecting picked entities:
		
		PickingSensor <Overlay> sensor = new PickingSensor <Overlay> (canvasLocation.x, canvasLocation.y);
		
//		log.debug("picking at: " + (canvasLocation.x+halfWidth) + "," + (halfHeight-canvasLocation.y));
		getEntityIndex().queryAABB(sensor, canvasLocation.x, canvasLocation.y, CURSOR_PICK_SPAN, CURSOR_PICK_SPAN);
		
		Overlay pickedObject = sensor.getObject();
		sensor.clear();
		if(pickedObject == null)
			return null; // we are not in UI domain

		// TODO: process pick:
		return pickedObject;
	}
	
}