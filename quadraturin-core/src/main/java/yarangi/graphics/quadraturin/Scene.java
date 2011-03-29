package yarangi.graphics.quadraturin;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import yarangi.graphics.quadraturin.actions.Action;
import yarangi.graphics.quadraturin.config.InputConfig;
import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.debug.SceneDebugOverlay;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.events.UserActionListener;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.math.Vector2D;

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
	
	private Map <String, Action> actions = new HashMap <String, Action> ();


	public Scene(String sceneName, int width, int height, WorldVeil worldVeil, UIVeil uiVeil)
	{
		this.name = sceneName;

		
		this.worldVeil = worldVeil;
		viewPoint = new ViewPoint2D(null, null, null, new Dimension(width, height));
		worldVeil.initViewPoint(viewPoint);

		this.uiVeil = uiVeil;
		
//		cursorPicks = new EntityList();
		
		if(Debug.ON)
			addEntity(new SceneDebugOverlay(worldVeil.getEntityIndex()));
	}
	
	public void init(EventManager voices)
	{
		bindSceneActions(voices);
		for(String actionId : actions.keySet())
			voices.addUserActionListener(actionId, this);
	}
	
	public abstract void bindSceneActions(EventManager voices);

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
	
	public SceneEntity pick(Vector2D worldLocation, Point canvasLocation)
	{
		// collecting picked entities:
//		cursorPicks.clear();
		Iterator <SceneEntity> iterator = null;
		if(canvasLocation != null)
			iterator = uiVeil.getEntityIndex().iterator( 
					canvasLocation.x-CURSOR_PICK_SPAN, canvasLocation.y-CURSOR_PICK_SPAN, 
					canvasLocation.x+CURSOR_PICK_SPAN, canvasLocation.y+CURSOR_PICK_SPAN);
		
		if((iterator == null || !iterator.hasNext()) && worldLocation != null)
			iterator = worldVeil.getEntityIndex().iterator( 
				worldLocation.x-CURSOR_PICK_SPAN, worldLocation.y-CURSOR_PICK_SPAN, 
				worldLocation.x+CURSOR_PICK_SPAN, worldLocation.y+CURSOR_PICK_SPAN);
		
		if(iterator != null && iterator.hasNext())
			return iterator.next();
		
		return null;
	}
	
	// public IViewPoint getViewPoint() { return manager.getViewPoint(); }
	
	public void bindAction(String actionId, Action action)
	{
		if(actions.containsKey(actionId))
			throw new IllegalArgumentException("Action with id " + actionId + " already bound to method.");
		actions.put(actionId, action);
//		voices.addUserActionListener(actionId, this);
	}
	

	public void onUserAction(UserActionEvent event) 
	{
		Action action = actions.get(event.getActionId());

		if(action == null)
			throw new IllegalArgumentException("Action id " + event.getActionId() + " is not defined." );
		
		action.act(event);
	}

	public void destroy(EventManager voices)
	{
		for(String actionId : actions.keySet())
			voices.removeUserActionListener(actionId);
		
		voices = null;
		actions = null;
	}
	
	protected void bindNavigationActions()
	{
		final ViewPoint2D vp = (ViewPoint2D) viewPoint;
		InputConfig config = QuadConfigFactory.getInputConfig();
		
		final double scrollStep = config.getScrollStep();
		final double scaleStep = config.getScaleStep();
		
		bindAction("scroll-right", new Action() {
				public void act(UserActionEvent event) { vp.getCenter().x += scrollStep/vp.getHeight(); }}
			);
		bindAction("scroll-left", new Action() {
			public void act(UserActionEvent event) { vp.getCenter().x -= scrollStep/vp.getHeight(); }}
		);
		bindAction("scroll-up", new Action() {
			public void act(UserActionEvent event) { vp.getCenter().y -= scrollStep/vp.getHeight(); }}
		);
		bindAction("scroll-down", new Action() {
			public void act(UserActionEvent event) { vp.getCenter().y += scrollStep/vp.getHeight(); }}
		);
		bindAction("zoom-in", new Action() {
			public void act(UserActionEvent event) { vp.setHeight(vp.getHeight() * scaleStep); }}
		);
		bindAction("zoom-out", new Action() {
			public void act(UserActionEvent event) { vp.setHeight(vp.getHeight() / scaleStep); }}
		);

	}


}
