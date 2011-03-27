package yarangi.graphics.quadraturin;

import java.awt.Point;
import java.util.Iterator;
import java.util.Map;

import yarangi.graphics.quadraturin.actions.UIProvider;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.debug.SceneDebugOverlay;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.math.Vector2D;

public abstract class Scene 
{
	
	public static final double CURSOR_PICK_SPAN = 5;
	
	
	/**
	 * Just for fun, scene name
	 */
	private String name;
	
	private EventManager voices;

	private WorldVeil worldVeil;
	
	private UIVeil uiVeil;
	
//	private EntityList cursorPicks;
	
	public Scene(String sceneName, EventManager voices, WorldVeil worldVeil, UIVeil uiVeil)
	{
		this.name = sceneName;
		
		this.voices = voices;
		
		this.worldVeil = worldVeil;
		this.uiVeil = uiVeil;
		
//		cursorPicks = new EntityList();
		
		if(Debug.ON)
			addEntity(new SceneDebugOverlay(worldVeil.getEntityIndex()));
	}
	
	
	public String getName() { return name; }
	
	public EventManager getVoices() { return voices; }
	/**
	 * Creates input controller
	 * @param viewPoint
	 * @return
	 */
	public abstract UIProvider createActionProvider(IViewPoint viewPoint);
	
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
	
	public Map <Integer, Integer> bind(IViewPoint viewPoint, EventManager manager)
	{
		worldVeil.initViewPoint(viewPoint);
		
		UIProvider actionProvider = createActionProvider(viewPoint);
		if(actionProvider == null)
			return null;
		
		return actionProvider.bindActions(manager);
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


}
