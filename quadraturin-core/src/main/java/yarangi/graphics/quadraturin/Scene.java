package yarangi.graphics.quadraturin;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.actions.ActionController;
import yarangi.graphics.quadraturin.actions.ICameraMan;
import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.config.SceneConfig;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.objects.Behavior;
import yarangi.graphics.quadraturin.objects.Entity;
import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;
import yarangi.graphics.quadraturin.simulations.ICollider;
import yarangi.graphics.quadraturin.terrain.ITileMap;
import yarangi.graphics.quadraturin.ui.Overlay;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialSetIndex;

/**
 * Represents current engine task. 
 * 
 * Scene is composed of two layers: 
 * <li> {@link UserLayer} responsible to draw and animate user interface control elements.
 * <li> {@link WorldLayer} responsible to draw and animate game world.
 * Veils provide a way to add and remove {@link Entity} objects.
 * 
 * Any scene has to define {@link Scene#Scene(SceneConfig, QVoices)} constructor.
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
	private String name;
	
	/**
	 * Stuff is logged here
	 */
	protected Logger log;
	
	/**
	 * Game world layer.
	 */
	private WorldLayer worldSection;
	
	/**
	 * User interface layer.
	 */
	private UserLayer uiLayer;
	
	/**
	 * TODO: split for world and UI?
	 */
	private ViewPoint2D viewPoint;
	
	/**
	 * TODO: move
	 */
	private double frameLength;

	/**
	 * For background faceless behaviors
	 */
	private List <Behavior<Scene>> workers = new ArrayList <Behavior<Scene>> ();

	private ICameraMan cameraMan;
	
	private ActionController actionController;
	
	
	public Scene(SceneConfig sceneConfig, EkranConfig ekranConfig, QVoices voices)
	{
		// just for fun:
		this.name = sceneConfig.getName();
		
		log = Logger.getLogger(name);
		
		// initial viewpoint:
		viewPoint = sceneConfig.createViewpoint();
			
		// scene world aggregator:
		this.worldSection = new WorldLayer(sceneConfig.getWidth(), sceneConfig.getHeight());
		
		// initializing terrain:
		EntityShell <? extends ITileMap> terrain = null;
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
		
		// scene ui aggregator
		this.uiLayer = new UserLayer(ekranConfig.getXres(), ekranConfig.getYres());
		
		// scene time / second
		this.frameLength = sceneConfig.getFrameLength();
		
		// storing event manager:
//		this.voices = voices;
		
		if(Debug.ON) // TODO: maybe actual instrumentation
			Debug.instrumentate(this);
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
	public final void setFrameLength(double length) { this.frameLength = length; }
	
	/**
	 * @return Current viewpoint
	 */
	public IBeholder getViewPoint() { return viewPoint; }
	
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
		uiLayer.removeEntity(entity);
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
	 * Initializing world and UI layers
	 * @param gl
	 */
	public void init(GL gl, IRenderingContext context)
	{
		
		getWorldLayer().init(gl, context);
		getUILayer().init(gl, context);
		
		Debug.init(gl, this, context);
	}
	
	public void destroy(GL gl, IRenderingContext context)
	{
		Debug.destroy(gl, this, context);
		getWorldLayer().destroy(gl, context);
		getUILayer().destroy(gl, context);
	}

	
	/**
	 * Invoked before the drawing occurs.
	 * @param gl
	 * @deprecated Move this stuff to {@link IGraphicsPlugin#preRender(GL, IRenderingContext)} when needed
	 */
	public void preDisplay(GL gl, double time, boolean pushNames) 
	{	
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		getWorldLayer().preDisplay(gl); 
	}
	
	/**
	 * Renders this scene.
	 * @param gl graphics object
	 * @param time rendering time
	 * @param pushNames if true, entities' names will be set when rendering 
	 */
	public void display(GL gl, double time, IRenderingContext context)
	{
		getWorldLayer().display(gl, time, context);
		
		if(actionController != null && actionController.getLook() != null)
			actionController.getLook().render(gl, time, actionController, context);
	}

	
	/**
	 * Invoked after the drawing is finished.
	 * @param gl
	 * @deprecated Move this stuff to {@link IGraphicsPlugin#preRender(GL, IRenderingContext)} when needed
	 */
	public void postDisplay(GL gl, double time, IRenderingContext context) 
	{ 
		getWorldLayer().postDisplay(gl);
		
		getUILayer().display(gl, time, context);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SCENE ANIMATION

	public void animate(double time)
	{
		getUILayer().animate(time);
		
		getWorldLayer().animate(time);

		for(Behavior <Scene> worker : workers)
			worker.behave( time, this, true );
//	return changePending;
	}

	public void addWorker(Behavior <Scene> worker)
	{
		this.workers.add( worker );
	}
	public void removeWorker(Behavior <Scene> worker)
	{
		this.workers.remove( worker );
	}

	public void postAnimate(double time) 
	{
	}

	final public ISpatialSetIndex<IAreaChunk, IEntity> getEntityIndex() { return worldSection.getEntityIndex(); }
	final public ISpatialSetIndex<IAreaChunk, Overlay> getOverlayIndex() { return uiLayer.getEntityIndex(); }
	
	/**
	 * Set user action controller. May be set any time after scene initialization.
	 * @param actionController
	 */
	public void setActionController(ActionController actionController)
	{
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
	public ICollider getCollisionManager()
	{
		return getWorldLayer().getPhysicsEngine().getCollisionManager();
	}

}
