package yar.quadraturin;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;

import yar.quadraturin.objects.ILayerObject;
import yar.quadraturin.objects.IVisible;
import yar.quadraturin.ui.Overlay;
import yar.quadraturin.ui.Panel;
import yarangi.Zen;
import yarangi.spatial.PickingSensor;
import yarangi.spatial.SpatialHashMap;

import com.google.common.base.Preconditions;
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
	
	private final Set <Panel> panels = new HashSet<Panel>(); 
	
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
	
	public void reshape(IRenderingContext context) 
	{
		if(basePanel.getViewPort() == context.getViewPort())
			return;
		
		log.debug("Layer vieweport has changed.");
		basePanel.revalidate( context.getViewPort() );
		int width = context.getViewPort().getWidth();
		int height = context.getViewPort().getHeight();
		this.halfHeight = height/2;
		this.halfWidth = width/2;
		// TODO: might be a bit heavy:
		log.debug("Creating new spatial hash map (" + width + "x" +height + ")");
		setEntityIndex( new SpatialHashMap<Overlay>( NAME, width*height/100, 10, width, height ) );
		
		for(Panel panel : panels)
			panel.revalidate( context.getViewPort() );
		
//		if(Debug.ON)
//			Debug.drawUserLayerOverlay(gl, this, context);
		
//		super.display( gl, context );
	}
	
	/**
	 * Adds an entity to the injection queue. The entity will be inserted into scene
	 * on the next iteration of animation loop.
	 * @param entity
	 */
	@Override
	public void addEntity(Overlay overlay)
	{
		Preconditions.checkNotNull( overlay, "Overlay cannot be null." );
		Preconditions.checkNotNull( overlay.getArea(), "Overlay AABB bracket cannot be null.");
		
		if(overlay.getLook() == null)
			log.debug("Overlay [" + overlay + "] have no look.");
		else
			getLooks().addVisible( overlay );
		
		panels.add( overlay.getParent() );

		if(overlay.isIndexed())
			indexer.add(overlay.getArea(), overlay);

	}
	
	@Override
	public void removeEntity(Overlay overlay)
	{
		Preconditions.checkNotNull( overlay, "Overlay cannot be null." );
		
		if(overlay.isIndexed())
			indexer.remove(overlay);
		
		getLooks().removeVisible(overlay);
		
		panels.remove( overlay.getParent() );

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