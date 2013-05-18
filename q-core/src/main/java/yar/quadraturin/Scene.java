package yar.quadraturin;

import java.util.ArrayList;
import java.util.List;

import yar.quadraturin.actions.ActionController;
import yar.quadraturin.actions.ICameraMan;
import yar.quadraturin.config.EkranConfig;
import yar.quadraturin.config.SceneConfig;
import yar.quadraturin.debug.Debug;
import yar.quadraturin.objects.Entity;
import yar.quadraturin.objects.EntityShell;
import yar.quadraturin.objects.IBehavior;
import yar.quadraturin.objects.IEntity;
import yar.quadraturin.simulations.ICollider;
import yar.quadraturin.simulations.IPhysicsEngine;
import yar.quadraturin.terrain.ITerrain;
import yar.quadraturin.ui.Overlay;
import yarangi.spatial.ITileMap;

import com.spinn3r.log5j.Logger;

/**
 * Represents current engine task; 
 * 
 * Scene is composed of two layers: 
 * <li> {@link UserLayer} responsible to draw and animate user interface control elements.
 * <li> {@link WorldLayer} responsible to draw and animate game world.
 * Layers provide a way to add and remove {@link Entity} objects.
 * 
 * Any scene has to define {@link Scene#Scene(SceneConfig, EkranConfig, QVoices)} constructor.
 * 
 * Log stuff into {@link #log}
 * 
 * @author dveyarangi
 */
public abstract class Scene
{
	
	/**
	 * Just for fun, scene name
	 */
	private final String name;
	
	/**
	 * Stuff is logged here
	 */
	protected Logger log;
	
	/**
	 * Game world layer.
	 * Manages {@link IEntitiy}-s and defines {@link IPhysicsEngine} and {@link ITileMap}
	 */
	private WorldLayer worldSection;
	
	/**
	 * User interface layer.
	 */
	protected UserLayer uiLayer;
	
	/**
	 * TODO: split for world and UI?
	 */
	protected Camera2D camera;
	
	/**
	 * TODO: move
	 */
	private double frameLength;

	
	/**
	 * This interface entity-less behaviors into scene.
	 */
	public static interface IWorker extends IBehavior <Scene> {}
	
	/**
	 * For background faceless behaviors
	 * Those are invoked each animation loop and receive this Scene as entity.
	 */
	private final List <IWorker> workers = new ArrayList <IWorker> ();

	private ICameraMan cameraMan;
	
	private ActionController actionController;	
	
	private double timeModifier;
	
	protected int width, height;
	
	private SceneConfig sceneConfig;
	
	/**
	 * Constructs the scene handler.
	 * This called on engine start, so no long tasks are allowed here.
	 * Time-hungry loading should go to {@link #init()}
	 * 
	 * @param sceneConfig
	 * @param ekranConfig
	 * @param voices
	 */
	public Scene(SceneConfig sceneConfig, EkranConfig ekranConfig, QVoices voices)
	{
		
		// just for fun:
		this.name = sceneConfig.getName();
		
		this.sceneConfig = sceneConfig;
		
		log = Logger.getLogger(name == null ? "scene" : "scene:" + name);
		
		width = ekranConfig.getXres();
		height = ekranConfig.getYres();
		
	}

	/**
	 * @return Scene name.
	 */
	public String getName() { return name; }
	
	protected Logger log()  { return log; }
	/**
	 * TODO: fix name
	 * @return game time unit / sec ratio
	 */
	public final double getFrameLength() { return frameLength; }
	protected final void setFrameLength(double length) { this.frameLength = length; }
	
	/**
	 * @return Current viewpoint
	 */
	public ICamera getCamera() { return camera; }
	
	/**
	 * Appends a world entity.
	 */
	final public void addEntity(IEntity entity)
	{
		worldSection.addEntity(entity);
	}


	/**
	 * Schedules entity removal. It will be actually removed at next rendering cycle.
	 * @param entity
	 */
	final public void removeEntity(IEntity entity)
	{
		worldSection.removeEntity(entity);
	}
	
	final public void addOverlay(Overlay entity)
	{
		uiLayer.addEntity(entity);
	}
	
	final public void removeOverlay(Overlay entity)
	{
		uiLayer.addEntity(entity);
	}

	/**
	 * @return Reference to {@link WorldLayer}.
	 */
	final public WorldLayer getWorldLayer() { return worldSection; }
	
	/**
	 * @return Reference to {@link UserLayer}.
	 */
	final public UserLayer getUILayer() { return uiLayer; }
	
	/**
	 * Initializes scene data structures and IO.
	 * Invoked on scene actualization.
	 */
	@SuppressWarnings("unchecked")
	public void init()
	{
		
		// initial viewpoint:
		camera = sceneConfig.createViewpoint();
			
		// scene world aggregator:
		this.worldSection = new WorldLayer(sceneConfig.getWidth(), sceneConfig.getHeight());

		// scene ui aggregator
		this.uiLayer = new UserLayer(width, height);
		
		// scene time / second
		this.frameLength = sceneConfig.getFrameLength();
		
		// storing event manager:
//		this.voices = voices;
		
		this.timeModifier = sceneConfig.getTimeModifier();

		// initializing terrain:
		EntityShell <? extends ITileMap<ITerrain>> terrain = null;
		if(sceneConfig.getTerrainConfig() != null)
		{
			terrain = sceneConfig.getTerrainConfig().createTerrain( sceneConfig.getWidth(), sceneConfig.getHeight() );
			worldSection.addTerrain( terrain );
			log.debug( "Using terrain " + terrain.getEssence() );
		}
		else
			log.debug( "No terrain configuration found." );

		// initializing physics engine:
		if(sceneConfig.getEngineConfig() != null)
			worldSection.setPhysicsEngine( sceneConfig.getEngineConfig().createEngine(worldSection.getEntityIndex(), 
					terrain == null ? null : terrain.getEssence()));
				
		
//		actionController.getLook().init( gl, actionController, context );
		if(Debug.ON) // TODO: maybe actual instrumentation
			Debug.instrumentate(this);
		
	}
	
	public void postInit() { }
	
	public void destroy()
	{
		uiLayer = null;
		worldSection = null;
	}
	
	public void preRender( IRenderingContext ctx )
	{
		ctx.gl().glClearColor(0.0f,0.0f, 0.0f, 1.0f);
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SCENE INNER INITIALIZATION
	
	/**
	 * Initializes the rendering context with scene-specific stuff.
	 * @param ctx
	 */
	void initContext(IRendererAggregate ctx)
	{
		//////////////////////////////////////////////////////////////////
		// Global settings:
		
		log.debug("Preparing scene rendering context.");
		ctx.setViewPoint( (Camera2D)getCamera() );
		
		if(worldSection != null)
			ctx.setWorldLookManager( worldSection.getLooks() );
		if(uiLayer != null)
			ctx.setUILookManager( uiLayer.getLooks() );

		Debug.init(this, ctx);
	}
	
	void revokeContext(IRenderingContext context)
	{
		Debug.destroy(this, context);
//		getWorldLayer().destroy(context);
//		getUILayer().destroy(context);
	}

	void reshape(GL2RenderingContext context)
	{
		if(getUILayer() != null)
			getUILayer().reshape( context );
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SCENE ANIMATION

	/**
	 * Invokes behaviors
	 * 
	 * Call super if overriding.
	 * @param time
	 */
	public void animate(double time)
	{
		time *= timeModifier; // streches time linearily TODO: experiment with non constant modifiers
		
		// animate ui layer
		if(uiLayer != null)
			uiLayer.animate(time);
		
		// animate world layer
		if(worldSection != null)
			worldSection.animate(time);

		// call any scheduled scene behaviors
		for(IWorker worker : workers)
			worker.behave( time, this, true );
	//	return changePending;
	}

	/** 
	 * Injects entity-less behavior into the animation loop.
	*
	 * @param worker
	 */
	public void addWorker(IWorker worker)
	{
		this.workers.add( worker );
	}
	
	/** 
	 * Removes entity-less behavior from the animation loop.
	*
	 * @param worker
	 */
	public void removeWorker(IWorker worker)
	{
		this.workers.remove( worker );
	}

	public void postAnimate(double time) 
	{
	}
	
	/**
	 * Set user action controller. May be set any time after scene initialization.
	 * @param actionController
	 */
	public void setActionController(ActionController actionController)
	{
//		if(this.actionController != null)
//			removeEntity( this.actionController );
//		addEntity( actionController );		
		if(cameraMan != null)
			removeWorker( cameraMan );
		
		cameraMan = actionController.getCameraManager(); 
		if(cameraMan != null)
			addWorker( cameraMan );

		this.actionController = actionController;
		
	}
	
	public  ActionController getActionController() { return actionController; }
	
	/**
	 * Exposes collision manager to register collision handlers
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ICollider getCollisionManager()
	{
		return getWorldLayer().getPhysicsEngine().getCollisionManager();
	}


}
