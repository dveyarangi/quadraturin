package yar.quadraturin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
	 * Listeners to be informed on scene lifecycle events.
	 */
	private final List <StageListener> listeners = new LinkedList <StageListener> ();
	
	/**
	 * Take a guess
	 */
	private final Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Stage configuration object
	 */
	private final StageConfig stageConfig;
	
	/**
	 * Marks that scene is currently being loaded.
	 */
	private volatile boolean sceneLoading = false;
	
	public static final String LOADING_SCENE_NAME = "q-intro";
	
	private Queue <Scene> pendingScenes = new LinkedList <Scene> ();
	
	/**
	 * Create a stage.
	 * @param frameLength
	 */
	private Stage(StageConfig stageConfig, EkranConfig ekranConfig, QVoices voices)
	{
		this.stageConfig = stageConfig;
		
		// instantiating all declared scene handlers
		// no long tasks should be done there
		for(SceneConfig scene : stageConfig.getScenes())
		{
			addScene(scene.createScene(ekranConfig, voices));
			log.info("Registered scene %s (class: %s)", scene.getName(), scene.getSceneClass());
		}
		
		// TODO: this is a placeholder for loading screen:
		Scene placeholder = SceneConfig.createScene( "yar.quadraturin.ui.transition.DummyLoadingScreen", LOADING_SCENE_NAME, ekranConfig, voices );
		addScene( placeholder );
		
		setScene( placeholder.getName() );
		
//		setSceme( get)
	}
	
	static Scene getLoadingScene()
	{
		return singleton.scenes.get(LOADING_SCENE_NAME);
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
	 * Actualizes scene with specified name
	 * @param id Scene id
	 */
	public void setScene(String name)
	{
		while( sceneLoading ) {
			try
			{
				Thread.sleep( 500 );
			} catch ( InterruptedException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		sceneLoading = true;
		
		if( scene != null ) // destroying previous scene
		{
			scene.destroy();
		}
		
		// getting scene handle:
		scene = scenes.get(name);
		if( scene == null )
			throw new IllegalArgumentException( "Scene [" + name + "] is not defined." );
		

		new Thread( new SceneInitializer( scene ) ).start();
	}
	
	/**
	 * Scene initializer runnable. Destined to carry the scene IO loading tasks,
	 * that may run simultaneously with previous scene rendering.
	 */
	protected class SceneInitializer implements Runnable
	{
		private Scene scene;
		
		public SceneInitializer(Scene scene)
		{
			this.scene = scene;
		}
		
		@Override
		public void run()
		{
			log.debug( "Initializing scene [%s].", scene.getName() );
			
			try 
			{
				scene.init();
				
				fireStageChanged( scene );
				
				if( scene.getActionController() == null && scene.getCamera() != null ) 
				{
					log.debug( "Using default action controller" );
					scene.setActionController( DefaultActionFactory.createDefaultController( scene ) );
				}
				
				log.debug( "Scene [%s] initialized.", scene.getName() );
				
				scene.postInit();
			}
			catch( Exception e ) {
				log.error( "Failed to initialize scene [%s]:", e );
			}
			finally {
				sceneLoading = false;
			}
			
		}
		
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
	
	public static void setNextScene(String sceneName)
	{
		singleton.setScene( sceneName );
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
