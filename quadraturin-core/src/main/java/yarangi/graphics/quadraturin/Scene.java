package yarangi.graphics.quadraturin;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.actions.IAction;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.debug.SceneDebugOverlay;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.events.UserActionListener;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.math.Vector2D;
import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialIndex;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.SetSensor;

/**
 * Represents current engine task. 
 * 
 * Scene is composed of two layers: 
 * <li> {@link UIVeil} responsible to draw and animate user interface control elements.
 * <li> {@link WorldVeil} responsible to draw and animate game world.
 * Veils provide a way to add and remove {@link SceneEntity} objects.
 * 
 * Scene class encapsulates veil handling and provides the engine and user a single point of entry.  
 * 
 * @author dveyarangi
 */
public abstract class Scene implements UserActionListener
{
	
	/**
	 * Picking area span (veil coordinates)
	 */
	public static final double CURSOR_PICK_SPAN = 5;
	
	
	/**
	 * Just for fun, scene name
	 */
	private String name;
	
	/**
	 * Game world layer.
	 */
	private WorldVeil worldVeil;
	
	/**
	 * User interface layer.
	 */
	private UIVeil uiVeil;
	
	/**
	 * TODO: split?
	 */
	private IViewPoint viewPoint;
	
	/**
	 * TODO: move
	 */
	private double frameLength;
	
	
	/**
	 * TODO: move
	 */
	private IPhysicsEngine engine;


	public Scene(String sceneName, WorldVeil worldVeil, UIVeil uiVeil,  int width, int height, double frameLength)
	{
		this.name = sceneName;
		
		this.worldVeil = worldVeil;
		viewPoint = new ViewPoint2D(null, null, null, new Dimension(width, height));
		worldVeil.initViewPoint(viewPoint);

		this.uiVeil = uiVeil;
		
		this.frameLength = frameLength;
		
		if(Debug.ON)
			addEntity(new SceneDebugOverlay(worldVeil.getEntityIndex()));
		
		this.engine = getWorldVeil().getPhysicsEngine();
	}
	
	public final double getFrameLength() { return frameLength; }
	
	/**
	 * Retrieves scene {@link IAction} mapping.
	 * @return
	 */
	public abstract Map <String, IAction> getActionsMap();

	public IViewPoint getViewPoint() { 
		return viewPoint;
	}

	public String getName() { return name; }
	
//	public EventManager getVoices() { return voices; }
	
	public void addEntity(SceneEntity entity)
	{
		worldVeil.addEntity(entity);
	}
	
	public void removeEntity(SceneEntity entity)
	{
		worldVeil.removeEntity(entity);
	}
	
	public void addOverlay(SceneEntity entity)
	{
		uiVeil.addEntity(entity);
	}
	
	 final public void removeOverlay(SceneEntity entity)
	{
		uiVeil.removeEntity(entity);
	}

	final public WorldVeil getWorldVeil() { return worldVeil; }
	
	final public UIVeil getUIVeil() { return uiVeil; }
	
	public ISpatialObject pick(Vector2D worldLocation, Point canvasLocation)
	{
		// collecting picked entities:
		SetSensor <ISpatialObject> sensor = new SetSensor <ISpatialObject> ();
		
		if(canvasLocation != null)
			uiVeil.getEntityIndex().query(sensor, new AABB(canvasLocation.x, canvasLocation.y, CURSOR_PICK_SPAN, 0));
		
		if(worldLocation != null && sensor.size() == 0)
			worldVeil.getEntityIndex().query(sensor, new AABB(worldLocation.x(), worldLocation.y(), CURSOR_PICK_SPAN, 0));
		
		if(sensor.size() != 0)
			return sensor.iterator().next();
		
		return null;
	}
	
	public void onUserAction(UserActionEvent event) 
	{
		IAction action = getActionsMap().get(event.getActionId());

		if(action == null)
			throw new IllegalArgumentException("Action id " + event.getActionId() + " is not defined." );
		
		action.act(event);
	}

	/**
	 * 
	 * @param gl
	 */
	public void init(GL gl)
	{
		getWorldVeil().init(gl);
		getUIVeil().init(gl);
		
	}
	
	public void destroy(GL gl)
	{
		getWorldVeil().destroy(gl);
		getUIVeil().destroy(gl);
	}

	
	/**
	 * Invoked before the drawing occurs.
	 * @param gl
	 */
	public void preDisplay(GL gl, double time, boolean pushNames) 
	{	
		getWorldVeil().preDisplay(gl); 
	}
	
	/**
	 * Renders this scene.
	 * @param gl graphics object
	 * @param time rendering time
	 * @param pushNames if true, entities' names will be set when rendering 
	 */
	public void display(GL gl, double time, RenderingContext context)
	{
		getWorldVeil().display(gl, time, context);
	}

	
	/**
	 * Invoked after the drawing is finished.
	 * @param gl
	 */
	public void postDisplay(GL gl, double time, RenderingContext context) 
	{ 
		getWorldVeil().postDisplay(gl);
		
		getUIVeil().display(gl, time, context);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SCENE ANIMATION

	public void animate(double time)
	{
		getWorldVeil().animate(time);
		getUIVeil().animate(time);
		
		if(engine != null)
			engine.calculate(time);
	}

	public IPhysicsEngine getPhysicalEngine() {
		return engine;
	}

	
	public void postAnimate(double time) 
	{
	}

	final public ISpatialIndex <ISpatialObject> getEntityIndex() { return worldVeil.getEntityIndex(); }
	final public ISpatialIndex <ISpatialObject> getOverlayIndex() { return uiVeil.getEntityIndex(); }
}
