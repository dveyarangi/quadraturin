package yarangi.graphics.quadraturin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.QuadConstants.QuadMode;
import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.debug.DebugThreadChain;
import yarangi.graphics.quadraturin.interaction.IPhysicsEngine;
import yarangi.graphics.quadraturin.interaction.StupidInteractions;
import yarangi.graphics.quadraturin.thread.LoopyChainedThread;
import yarangi.graphics.quadraturin.thread.ThreadChain;

/**
 * The Quadraturin application frame. Reads engine configuration, starts up the threads and creates AWT
 * application elements. 
 *  
 * TODO: shall add option to set different closing listener.
 * 
 * @author Dve Yarangi
 */
public class QuadContainer extends JFrame
{

	private static final long serialVersionUID = 5840442002396512390L;
	
	public static final String V = "0.0.1";
	
	/**
	 * AWT-JOGL bridge component.
	 */
	private GLCanvas canvas = null;
	
	/**
	 * Contains all threads that 
	 */
	private ThreadChain chain;
	
	/**
	 * Entities and animation controller.
	 */
	private Stage stage;
	
	/**
	 * Event dispatcher.
	 */
	private QuadVoices voices;
	
	/**
	 * Events and rendering controller.
	 */
	private Quad2DController controller;
	
	/**
	 * Animation thread.
	 */
	private StageAnimator animator;

	/**
	 * Physics thread.
	 */
	private IPhysicsEngine interactions;
	
	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger("quadraturin");
	
	/**
	 * Creates a Swing frame containing the representation of the specified world model.
	 * @param title
	 * @param model
	 */
	public QuadContainer(QuadMode mode)
	{
		super();
		
		String applicationName = "Quadraturin " + V; 
		setName(applicationName);
		
		log.info("/////////////////////////////////////////////////////////////");
		log.info("// QUADRATURIN INIT /////////////////////////////////////////");
		log.info("/////////////////////////////////////////////////////////////");
		log.info(applicationName + " container is being expanded...");

			if(Debug.ON) ;			

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// initializing JOGL engine
		
		int xres = QuadConfigFactory.getEkranConfig().getXres();
		int yres = QuadConfigFactory.getEkranConfig().getYres();
		// TODO: full-screen
		log.debug("Canvas dimensions set to [" + xres + "," + yres + "].");
		this.setMinimumSize(new Dimension(xres, yres));
		this.setLocationRelativeTo(null);

		// TODO:  configure GL capabilities.
		log.debug("Configuring GL capabilities.");
	    GLCapabilities capabilities = new GLCapabilities();
	    capabilities.setRedBits(8);
	    capabilities.setBlueBits(8);
	    capabilities.setGreenBits(8);
	    capabilities.setAlphaBits(8);
	    canvas = new GLCanvas(capabilities);
	    canvas.setMinimumSize( this.getSize() );
	    
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// organizing JFrame contents:
	    log.trace("Packing swing frame...");
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(canvas, BorderLayout.CENTER);
		pack();
		
		log.trace("Linking shutdown hook...");
		// TODO: add real JVM shutdown hook
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				log.debug("Window closing event detected.");
				voices.keyReleased(new KeyEvent((Component)e.getSource(), e.getID(), 0, 0, KeyEvent.VK_ESCAPE, KeyEvent.CHAR_UNDEFINED));
				safeStop();
				System.exit(0);
			}
		});
		log.trace("JOGL canvas created.");
		
		log.debug("Creating thread pool...");
		if(Debug.ON)
		{
			chain = new DebugThreadChain(100);
		}
		else
			chain = new ThreadChain("q-chain");
		log.trace("Thread pool created.");
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// initializing event manager:
		String eventModuleName = "q-voice";
		log.debug("Creating Quadraturin event manager...");
		voices = new QuadVoices(eventModuleName, QuadConfigFactory.getInputConfig());
		chain.addThread(new LoopyChainedThread(eventModuleName, chain, voices));
		log.trace("Quadraturin event manager created.");
		
		log.debug("Creating entity stage...");
		stage = new Stage(voices);
		log.trace("Entity stage created.");
		
		
		log.debug("Creating Quadraturin GL listener...");
		if ( QuadMode.PRESENT_2D == mode ) 
			{
			controller = new Quad2DController("q-renderer", stage, voices, chain);
		}
		
		chain.addThread(controller);
		
		// TODO: this is the only listener, actually, so...
		stage.addListener(voices);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    log.trace("Registering Quadraturin controller...");
	    canvas.addGLEventListener(controller);
		
		canvas.addMouseListener(voices);
		canvas.addMouseMotionListener(voices);
		canvas.addKeyListener(voices);
		log.trace("Quadraturin controller created.");
		
		
	    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    // creating stage animation thread:
		log.debug("Creating entity stage animator...");
	    animator = new StageAnimator(stage, canvas);
		chain.addThread(new LoopyChainedThread("q-animator", chain, animator));
		log.trace("Entity stage animator created.");
	    
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    // physics thread
		log.debug("Creating entity physics engine...");
	    interactions = new StupidInteractions();
		chain.addThread(new LoopyChainedThread("q-interactions", chain, interactions));
		log.trace("Entity physics engine created.");
		
		stage.addListener(new StageListener () {
			public void sceneSet(Scene scene) {
				interactions.setCollisionManager(scene.getWorldVeil().createCollisionManager());
			}});
		
		stage.setInitialScene(new DummyScene("Loading..."));


		log.info("Quadraturin, da fiersum enjun, is ready to load scenes.");
		log.info("/////////////////////////////////////////////////////////////");
	}
	
	/**
	 * Starts the animation.
	 */
	public void start()
	{
		chain.start();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		canvas.requestFocus();
	}
	
	/**
	 * Stops and cleans up.
	 * 
	 */
	public void safeStop() 
	{ 
		log.info("/////////////////////////////////////////////////////////////");
		log.info("// QUADRATURIN STOP /////////////////////////////////////////");
		log.info("/////////////////////////////////////////////////////////////");
		log.debug("Quadraturin container is being shrinked...");
		this.removeMouseListener(voices);
		this.removeMouseMotionListener(voices);
		this.removeKeyListener(voices);
		canvas.removeGLEventListener(controller);

		chain.stop();
		
		log.info("Quadraturin have joined the ethernity of the stopped code.");
		log.info("/////////////////////////////////////////////////////////////");
	}

	/**
	 * TODO: injection of physical engine into Scene is not good.
	 * @return
	 */
	public IPhysicsEngine getPhysicsEngine() 
	{
		return interactions;
	}
	
	/**
	 * TODO: injection of event dispatcher into Scene is not good.
	 * @return
	 */
	public EventManager getEventManager() 
	{
		return voices; 
	}
	
	public int addScene(Scene scene)
	{
		return stage.addScene(scene);
	}

	/**
	 * 
	 * @return
	 */
	public void activateScene(int sceneId) 
	{
		stage.setScene(sceneId);
	}
}
