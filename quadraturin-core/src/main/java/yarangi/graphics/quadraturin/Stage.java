package yarangi.graphics.quadraturin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.config.SceneConfig;
import yarangi.graphics.quadraturin.config.StageConfig;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.ui.Overlay;

/**
 * Scene series configurator and container.
 * The entity stage coordinates the animator and rendering threads, and provides
 * {@link Scene}-s life-cycle operations 
 * Also provides scene entity injections.
 * 
 * It also fires scene-change events for {@link StageAnimator}, {@link QuadVoices} and graphics thread (currently {@link Quad2DController})
 * 
 * TODO: Stage - scene interactions should be refactored
 * TODO: scene UI controls are not created.
 *  
 * @author Dve Yarangi
 */
public final class Stage 
{
	
	private static Stage singleton;
	
	static Stage init(StageConfig stageConfig, EkranConfig ekranConfig, QuadVoices voices)
	{
		if(singleton != null)
			throw new IllegalStateException("Stage is already initialized.");
		
		singleton = new Stage( stageConfig, ekranConfig, voices );
		
		return singleton;
	}
	
	/**
	 * List of scenes.
	 */
	private Map <String, Scene> scenes = new HashMap <String, Scene> ();

	/**
	 * Current scene
	 */
	private Scene scene;
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// service
	
	/**
	 * Listeners to be informed on world global state changes.
	 */
	private List <StageListener> listeners = new LinkedList <StageListener> ();
	
	/**
	 * 
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Create a stage.
	 * @param frameLength
	 */
	private Stage(StageConfig stageConfig, EkranConfig ekranConfig, QuadVoices voices)
	{
		
		for(SceneConfig scene : stageConfig.getScenes())
		{
			addScene(scene.createScene(ekranConfig, voices));
			log.info("Registered scene " + scene.getName() + " (class: " + scene.getSceneClass());
		}
		
		scene = scenes.get(stageConfig.getInitialScene());
		if(scene == null)
			throw new IllegalArgumentException("Initial scene [" + stageConfig.getInitialScene() + "] is not defined.");
		
		setInitialScene();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// STAGE AND SCENE CONTROL:
	
	/**
	 * Adds a scene to the stage.
	 * @param scene
	 * @return
	 */
	void addScene(Scene scene)
	{
		scenes.put(scene.getName(), scene);
	}
	
	void setInitialScene()
	{
		// setting initial scene:
		fireStageChanged(scene);
	}
	
	/**
	 * Actualizes scene with specified id
	 * @param id Scene id
	 */
	public synchronized void setScene(String name)
	{
		this.scene = scenes.get(name);
		
		fireStageChanged(scene);
	}
	
	void addListener(StageListener l)
	{
		singleton.listeners.add(l);
	}
	
	void removeListener(StageListener l)
	{
		singleton.listeners.remove(l);
	}
	
	/**
	 * Informs listeners about scene changes.
	 */
	private void fireStageChanged(Scene currScene)
	{
		for(StageListener l : listeners)
			l.sceneChanged(currScene);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SCENE ACCESS

	/**
	 * Appends a world entity.
	 */
	public static void addEntity(IEntity entity)
	{
		singleton.scene.addEntity( entity );
	}

	/**
	 * Schedules entity removal. It will be actually removed at next rendering cycle.
	 * @param entity
	 */
	public static void removeEntity(IEntity entity)
	{
		singleton.scene.removeEntity( entity );
	}
	
	public static void addOverlay(Overlay entity)
	{
		singleton.scene.addOverlay( entity );
	}
	
	public static void removeOverlay(Overlay entity)
	{
		singleton.scene.removeOverlay( entity );
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SERVICE
	

}
