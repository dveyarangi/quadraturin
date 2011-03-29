package yarangi.graphics.quadraturin;

import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.QuadConfigFactory;

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
	 * GL thread communication monitor. 
	 */
	private boolean changePending = true;
	
	private boolean scenePending = false;
	
	/**
	 * 
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Create a stage.
	 * @param frameLength
	 */
	public Stage(EventManager voices)
	{
		this.frameLength = QuadConfigFactory.getStageConfig().getFrameLength();
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
		
		fireStageChanged();
	}
	
	/**
	 * Actualizes scene with specified id
	 * @param id Scene id
	 */
	public synchronized void setScene(int id)
	{
		clear();
		
		this.scene = scenes.get(id);
		this.scenePending = true;
		
		fireStageChanged();
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
	protected void fireStageChanged()
	{
		for(StageListener l : listeners)
			l.sceneSet(scene);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SCENE RENDERING
	
	/**
	 * 
	 * @param gl
	 */
	public void init(GL gl) throws SceneException
	{
		scene.getWorldVeil().init(gl);
		scene.getUIVeil().init(gl);
		
		scenePending = false;
	}
	
	public boolean isScenePending() { return scenePending; }
	
	/**
	 * Invoked before the drawing occurs.
	 * @param gl
	 */
	public void preDisplay(GL gl, double time, boolean pushNames) {	scene.getWorldVeil().preDisplay(gl); }
	
	/**
	 * Renders this scene.
	 * @param gl graphics object
	 * @param time rendering time
	 * @param pushNames if true, entities' names will be set when rendering 
	 */
	public void display(GL gl, double time, boolean pushNames)
	{
		scene.getWorldVeil().display(gl, time, pushNames);
	}

	
	/**
	 * Invoked after the drawing is finished.
	 * @param gl
	 */
	public void postDisplay(GL gl, double time, boolean pushNames) 
	{ 
		scene.getWorldVeil().postDisplay(gl);
		
		scene.getUIVeil().display(gl, time, pushNames);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SCENE ANIMATION
	
	public void preAnimate()
	{
		scene.getWorldVeil().preAnimate();
		scene.getUIVeil().preAnimate();
	}
	
	public void animate(double time)
	{
		scene.getWorldVeil().animate(time);
		scene.getUIVeil().animate(time);
		setChanged();
	}
	
	public void postAnimate() 
	{
		scene.getWorldVeil().postAnimate();
		scene.getUIVeil().postAnimate();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SERVICE
	
	/**
	 * Frame to time conversion coefficient
	 * @return
	 */
	public double getFrameLength() { return frameLength; }
	
	/**
	 * Queries if the stage has changed since the last query.
	 * If there is, 
	 * @return
	 */
	public boolean changePending()
	{
		if ( changePending )
			return !(changePending = false); // flips and returns
		return false;
	}
	
	/**
	 * 
	 */
	public void setChanged()
	{
		changePending = true;
	}

	/**
	 * 
	 */
	public void clear()
	{
//		uiDrawables.clear();
	}

	public String getSceneName() {
		return scene.getName();
	}

	public IViewPoint getViewPoint() {
		return scene.getViewPoint();
	}

}
