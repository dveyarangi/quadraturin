package yar.quadraturin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import yar.quadraturin.actions.DefaultActionFactory;
import yar.quadraturin.config.EkranConfig;
import yar.quadraturin.config.SceneConfig;
import yar.quadraturin.config.StageConfig;
import yar.quadraturin.objects.IEntity;
import yar.quadraturin.ui.Overlay;

import com.spinn3r.log5j.Logger;

/**
 * Scene series configurator and container.
 * Provides {@link Scene}-s life-cycle operations and scene entity injections.
 * 
 * TODO: Stage - scene interactions should be refactored
 *  
 * @author Dve Yarangi
 */
public final class Stage 
{
	
	private static Stage singleton;
	
	static Stage init(StageConfig stageConfig, EkranConfig ekranConfig, QVoices voices)
	{
		if(singleton != null)
			throw new IllegalStateException("Stage is already initialized.");
		
		singleton = new Stage( stageConfig, ekranConfig, voices );
		
		return singleton;
	}
	
	/**
	 * List of scenes.
	 */
	private final Map <String, Scene> scenes = new HashMap <String, Scene> ();

	/**
	 * Current scene
	 */
	private Scene scene;
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// service
	
	/**
	 * Listeners to be informed on world global state changes.
	 */
	private final List <StageListener> listeners = new LinkedList <StageListener> ();
	
	/**
	 * 
	 */
	private final Logger log = Logger.getLogger(this.getClass());
	
	private final StageConfig stageConfig;
	/**
	 * Create a stage.
	 * @param frameLength
	 */
	private Stage(StageConfig stageConfig, EkranConfig ekranConfig, QVoices voices)
	{
		this.stageConfig = stageConfig;
		
		for(SceneConfig scene : stageConfig.getScenes())
		{
			addScene(scene.createScene(ekranConfig, voices));
			log.info("Registered scene %s (class: %s)", scene.getName(), scene.getSceneClass());
		}
		
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

	/**
	 * Actualizes scene with specified id
	 * @param id Scene id
	 */
	public synchronized void setScene(String name)
	{
		
		scene = scenes.get(name);
		if(scene == null)
			throw new IllegalArgumentException("Scene [" + name + "] is not defined.");
		
		if(scene.getActionController() == null) {
			log.debug( "Using default action controller" );
			scene.setActionController(DefaultActionFactory.createDefaultController( scene ));
		}

		
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

	public void setInitialScene()
	{	
		setScene(stageConfig.getInitialScene());
		
	}
		
	

}
