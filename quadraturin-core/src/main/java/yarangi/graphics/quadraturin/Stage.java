package yarangi.graphics.quadraturin;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.StageConfig;

/**
 * The entity stage coordinates the animator and rendering threads, and provides
 * {@link Scene}-s life-cycle operations.
 * 
 * It also provides scene-change event observing capabilities.
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
	private List <Scene> scenes = new LinkedList <Scene> ();
	
	/**
	 * Frame/sec ratio
	 */
	private double frameLength;

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
	public Stage(StageConfig stageConfig)
	{
		this.frameLength = stageConfig.getFrameLength();
		log.debug("Using sec/frame ratio of " + frameLength + ".");
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// STAGE AND SCENE CONTROL:
	
	/**
	 * Adds a scene to the stage.
	 * TODO: currently, each scene renderer is initialized once on stage creation. 
	 * This is not flexible and not suitable for game levels sequence design.
	 * @param scene
	 * @return
	 */
	public int addScene(Scene scene)
	{
		int newId = scenes.size();
		scenes.add(newId, scene);
		return newId;
	}
	
	public void setInitialScene(Scene scene)
	{
		this.scene = scene;
		
		fireStageChanged(null, scene);
	}
	
	/**
	 * Actualizes scene with specified id
	 * @param id Scene id
	 */
	public synchronized void setScene(int id)
	{
		Scene prevScene = scene;
		this.scene = scenes.get(id);
		
		fireStageChanged(prevScene, scene);
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
	protected void fireStageChanged(Scene prevScene, Scene currScene)
	{
		for(StageListener l : listeners)
			l.sceneChanged(prevScene, currScene);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SCENE RENDERING
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SERVICE
	

}
