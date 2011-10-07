package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.actions.ActionController;
import yarangi.graphics.quadraturin.config.SceneConfig;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.objects.Entity;
import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.Overlay;
import yarangi.graphics.quadraturin.simulations.ICollider;
import yarangi.graphics.quadraturin.terrain.ITerrainMap;
import yarangi.spatial.ISpatialIndex;

/**
 * Represents current engine task. 
 * 
 * Scene is composed of two layers: 
 * <li> {@link UIVeil} responsible to draw and animate user interface control elements.
 * <li> {@link WorldVeil} responsible to draw and animate game world.
 * Veils provide a way to add and remove {@link Entity} objects.
 * 
 * Any scene has to define {@link Scene#Scene(SceneConfig, QuadVoices)} constructor.
 * @author dveyarangi
 */
public abstract class Scene
{
	
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
	 * TODO: split for world and UI?
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
		
		// initializing terrain:
		EntityShell <? extends ITerrainMap> terrain = null;
		if(config.getTerrainConfig() != null)
		{
			terrain = config.getTerrainConfig().createTerrain( config.getWidth(), config.getHeight() );
			worldVeil.addTerrain( terrain );
			log.debug( "Using terrain " + terrain.getEssence() );
		}
		else
			log.debug( "No terrain configuration found." );

		// initializing physics engine:
		worldVeil.setPhysicsEngine( config.getEngineConfig().createEngine(worldVeil.getEntityIndex(), 
				terrain == null ? null : terrain.getEssence()));
		
		// scene ui aggregator
		this.uiVeil = new UIVeil(config.getWidth(), config.getHeight());
		
		// scene time / second
		this.frameLength = config.getFrameLength();
		
		// storing event manager:
		this.voices = voices;
		
		if(Debug.ON) // TODO: maybe actual instrumentation
			Debug.instrumentate(this);
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
	 * Initializing world and UI veils
	 * @param gl
	 */
	public void init(GL gl, IRenderingContext context)
	{
		getWorldVeil().init(gl, context);
		getUIVeil().init(gl, context);
	}
	
	public void destroy(GL gl, IRenderingContext context)
	{
		getWorldVeil().destroy(gl, context);
		getUIVeil().destroy(gl, context);
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
	public void display(GL gl, double time, IRenderingContext context)
	{
		getWorldVeil().display(gl, time, context);
	}

	
	/**
	 * Invoked after the drawing is finished.
	 * @param gl
	 */
	public void postDisplay(GL gl, double time, IRenderingContext context) 
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
	
	/**
	 * Set user action controller. May be set any time after scene initialization.
	 * @param actionController
	 */
	public void setActionController(ActionController actionController)
	{
		this.voices.setActionController( actionController );
	}
	
	/**
	 * Exposes collision manager to register collision handlers
	 * @return
	 */
	public ICollider getCollisionManager()
	{
		return getWorldVeil().getPhysicsEngine().getCollisionManager();
	}

}
