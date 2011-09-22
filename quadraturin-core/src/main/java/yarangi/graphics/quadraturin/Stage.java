package yarangi.graphics.quadraturin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.SceneConfig;
import yarangi.graphics.quadraturin.config.StageConfig;

/**
 * Scene series configurator and container.
 * The entity stage coordinates the animator and rendering threads, and provides
 * {@link Scene}-s life-cycle operations.
 * 
 * It also fires scene-change events for {@link StageAnimator}, {@link QuadVoices} and graphics thread (currently {@link Quad2DController})
 * 
 * TODO: Stage - scene interactions should be refactored
 * TODO: scene UI controls are not created.
 *  
 * @author Dve Yarangi
 */
public class Stage 
{
	
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
	public Stage(StageConfig stageConfig, QuadVoices voices)
	{
		
		for(SceneConfig scene : stageConfig.getScenes())
		{
			addScene(scene.createScene(voices));
			log.info("Registered scene " + scene.getName() + " (class: " + scene.getSceneClass());
		}
		
		scene = scenes.get(stageConfig.getInitialScene());
		if(scene == null)
			throw new IllegalArgumentException("Initial scene [" + stageConfig.getInitialScene() + "] is not defined.");
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// STAGE AND SCENE CONTROL:
	
	/**
	 * Adds a scene to the stage.
	 * @param scene
	 * @return
	 */
	public void addScene(Scene scene)
	{
		scenes.put(scene.getName(), scene);
	}
	
	public void setInitialScene()
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
	
	public void addListener(StageListener l)
	{
		listeners.add(l);
	}
	
	public void removeListener(StageListener l)
	{
		listeners.remove(l);
	}
	
	/**
	 * Informs listeners about scene changes.
	 */
	protected void fireStageChanged(Scene currScene)
	{
		for(StageListener l : listeners)
			l.sceneChanged(currScene);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SCENE RENDERING
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SERVICE
	

}
