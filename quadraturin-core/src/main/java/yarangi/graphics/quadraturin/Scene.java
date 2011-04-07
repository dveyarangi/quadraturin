package yarangi.graphics.quadraturin;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.actions.Action;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.debug.SceneDebugOverlay;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.events.UserActionListener;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.math.Vector2D;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.SetSensor;

public abstract class Scene implements UserActionListener
{
	
	public static final double CURSOR_PICK_SPAN = 5;
	
	
	/**
	 * Just for fun, scene name
	 */
	private String name;
	
//	private EventManager voices;

	private WorldVeil worldVeil;
	
	private UIVeil uiVeil;
	private IViewPoint viewPoint;
	
	private double frameLength;
	
//	private Map <String, Action> actions = new HashMap <String, Action> ();


	public Scene(String sceneName, WorldVeil worldVeil, UIVeil uiVeil,  int width, int height, double frameLength)
	{
		this.name = sceneName;

		
		this.worldVeil = worldVeil;
		viewPoint = new ViewPoint2D(null, null, null, new Dimension(width, height));
		worldVeil.initViewPoint(viewPoint);

		this.uiVeil = uiVeil;
		
		this.frameLength = frameLength;
		
//		cursorPicks = new EntityList();
		
		if(Debug.ON)
			addEntity(new SceneDebugOverlay(worldVeil.getEntityIndex()));
	}
	
	public final double getFrameLength() { return frameLength; }
	
	public abstract Map <String, Action> getActionsMap();

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
	
	public void removeOverlay(SceneEntity entity)
	{
		uiVeil.removeEntity(entity);
	}

	public WorldVeil getWorldVeil() { return worldVeil; }
	
	public UIVeil getUIVeil() { return uiVeil; }
	
	public ISpatialObject pick(Vector2D worldLocation, Point canvasLocation)
	{
		// collecting picked entities:
		SetSensor <ISpatialObject> sensor = new SetSensor <ISpatialObject> ();
		
		if(canvasLocation != null)
			uiVeil.getEntityIndex().query(sensor, 
					canvasLocation.x-CURSOR_PICK_SPAN, canvasLocation.y-CURSOR_PICK_SPAN, 
					canvasLocation.x+CURSOR_PICK_SPAN, canvasLocation.y+CURSOR_PICK_SPAN);
		
		if(worldLocation != null && sensor.size() == 0)
			worldVeil.getEntityIndex().query(sensor, 
				worldLocation.x-CURSOR_PICK_SPAN, worldLocation.y-CURSOR_PICK_SPAN, 
				worldLocation.x+CURSOR_PICK_SPAN, worldLocation.y+CURSOR_PICK_SPAN);
		
		if(sensor.size() != 0)
			return sensor.iterator().next();
		
		return null;
	}
	
	public void onUserAction(UserActionEvent event) 
	{
		Action action = getActionsMap().get(event.getActionId());

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
	public void display(GL gl, double time, boolean pushNames)
	{
		getWorldVeil().display(gl, time, pushNames);
	}

	
	/**
	 * Invoked after the drawing is finished.
	 * @param gl
	 */
	public void postDisplay(GL gl, double time, boolean pushNames) 
	{ 
		getWorldVeil().postDisplay(gl);
		
		getUIVeil().display(gl, time, pushNames);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SCENE ANIMATION
	
	public void preAnimate()
	{
		getWorldVeil().preAnimate();
		getUIVeil().preAnimate();
	}
	
	public void animate(double time)
	{
		getWorldVeil().animate(time);
		getUIVeil().animate(time);
	}
	
	public void postAnimate() 
	{
		getWorldVeil().postAnimate();
		getUIVeil().postAnimate();
	}

}
