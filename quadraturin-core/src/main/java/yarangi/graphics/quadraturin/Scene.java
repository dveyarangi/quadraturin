package yarangi.graphics.quadraturin;

import java.awt.Point;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.actions.IActionController;
import yarangi.graphics.quadraturin.config.CreateEntityActionConfig;
import yarangi.graphics.quadraturin.config.SceneConfig;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.debug.SceneDebugOverlay;
import yarangi.graphics.quadraturin.objects.Behavior;
import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.objects.Overlay;
import yarangi.graphics.quadraturin.objects.Entity;
import yarangi.graphics.quadraturin.simulations.ICollider;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.graphics.quadraturin.terrain.ITerrainMap;
import yarangi.math.Vector2D;
import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialFilter;
import yarangi.spatial.ISpatialIndex;
import yarangi.spatial.PickingSensor;

/**
 * Represents current engine task. 
 * 
 * Scene is composed of two layers: 
 * <li> {@link UIVeil} responsible to draw and animate user interface control elements.
 * <li> {@link WorldVeil} responsible to draw and animate game world.
 * Veils provide a way to add and remove {@link Entity} objects.
 * 
 * In order to link scene automatically, use following configuration example:
 * <pre>
 * 		"scenes" : [
 			{
				"name" : "playground",
				"sceneClass" : "yarangi.game.temple.Playground",
				"width" : 1000,
				"height" : 1000,
				"frameLength" : 1,
		    	"engineClass" : "yarangi.graphics.quadraturin.simulations.StupidInteractions",
			    "viewpoint" : { 
			    	"centerx"  : 0.0,
			    	"centery"  : 0.0,
			    	"maxZoom"  : 2,
			    	"initZoom" : 1,
			    	"minZoom"  : 0.1
			    }
			    
			}
		]
 * </pre>
 * Any scene has to define {@link Scene#Scene(SceneConfig, QuadVoices)} constructor.
 * @author dveyarangi
 */
public abstract class Scene
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
	private ViewPoint2D viewPoint;
	
	/**
	 * TODO: move
	 */
	private double frameLength;

	private QuadVoices voices;
	
	private Logger log;
	public Scene(SceneConfig config, QuadVoices voices)
	{
		// just for fun:
		this.name = config.getName();
		
		log = Logger.getLogger(name);
		
		// initial viewpoint:
		viewPoint = config.createViewpoint();
			
		// scene world aggregator:
		this.worldVeil = new WorldVeil(config.getWidth(), config.getHeight());
		
		EntityShell <ITerrainMap> terrain = config.getTerrainConfig().createTerrain( config.getWidth(), config.getHeight() );
		worldVeil.setPhysicsEngine( config.getEngineConfig().createEngine(worldVeil.getEntityIndex(), terrain.getEssence()));
		worldVeil.addTerrain(terrain);
		
		// scene ui aggregator
		this.uiVeil = new UIVeil(config.getWidth(), config.getHeight());
		// scene time / second
		this.frameLength = config.getFrameLength();
		
		this.voices = voices;
		
//		if(Debug.ON) 
//			addOverlay(new SceneDebugOverlay(worldVeil.getEntityIndex()));
	}

	/**
	 * @return Scene name.
	 */
	public String getName() { return name; }
	
	
	/**
	 * TODO: fix name
	 * @return game time unit / sec ratio
	 */
	public final double getFrameLength() { return frameLength; }
	public final void setFrameLength(double length) { this.frameLength = length; }
	
	/**
	 * @return Current viewpoint
	 */
	public IViewPoint getViewPoint() { return viewPoint; }
	
	/**
	 * Appends a world entity.
	 */
	public void addEntity(IEntity entity)
	{
		worldVeil.addEntity(entity);
	}

	public void addTerrain(ITerrainMap terrain, 
			Look <? extends ITerrainMap> look, 
			Behavior <? extends ITerrainMap> behavior)
	{
	}
	
	/**
	 * Schedules entity removal. It will be actually removed at next rendering cycle.
	 * @param entity
	 */
//	public void removeEntity(SceneEntity entity)
//	{
//		worldVeil.removeEntity(entity);
//	}
	
	public void addOverlay(Overlay entity)
	{
		uiVeil.addEntity(entity);
	}
	
	 final public void removeOverlay(Overlay entity)
	{
		uiVeil.removeEntity(entity);
	}

	/**
	 * @return Reference to {@link WorldVeil}.
	 */
	final public WorldVeil getWorldVeil() { return worldVeil; }
	
	/**
	 * @return Reference to {@link UIVeil}.
	 */
	final public UIVeil getUIVeil() { return uiVeil; }
	
	/** 
	 * Selects an entity at {@link CURSOR_PICK_SPAN} radius around cursor location

	 * @param worldLocation
	 * @param canvasLocation
	 * @return
	 */
	public IEntity pick(ISpatialFilter<IEntity> pickingFilter, Vector2D worldLocation, Point canvasLocation)
	{
		// collecting picked entities:
		PickingSensor <IEntity> sensor = new PickingSensor <IEntity> (pickingFilter);
		
//		if(canvasLocation != null)
//			uiVeil.getEntityIndex().query(sensor, new AABB(canvasLocation.x, canvasLocation.y, CURSOR_PICK_SPAN, 0));
		
		if(sensor.getObject() == null && worldLocation != null)
			worldVeil.getEntityIndex().query(sensor, new AABB(worldLocation.x(), worldLocation.y(), CURSOR_PICK_SPAN, 0));
		
		if(sensor.getObject() != null)
			return sensor.getObject();
		
		return null;
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
		getUIVeil().animate(time);
		
		getWorldVeil().animate(time);
	}


	public void postAnimate(double time) 
	{
	}

	final public ISpatialIndex <IEntity> getEntityIndex() { return worldVeil.getEntityIndex(); }
	final public ISpatialIndex <Overlay> getOverlayIndex() { return uiVeil.getEntityIndex(); }
	
	public void setActionController(IActionController actionController)
	{
		this.voices.setActionController( actionController );
	}
	public ICollider getCollisionManager()
	{
		return getWorldVeil().getPhysicsEngine().getCollisionManager();
	}

}
