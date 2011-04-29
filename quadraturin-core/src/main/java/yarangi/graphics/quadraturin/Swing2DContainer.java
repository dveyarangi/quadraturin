package yarangi.graphics.quadraturin;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.debug.DebugThreadChain;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.graphics.quadraturin.simulations.StupidInteractions;
import yarangi.graphics.quadraturin.threads.LoopyChainedThread;
import yarangi.graphics.quadraturin.threads.ThreadChain;

/**
 * The Quadraturin application frame. Reads engine configuration, starts up the threads and creates AWT
 * application elements. 
 *  
 * TODO: shall add option to set different closing listener.
 * 
 * @author Dve Yarangi
 */
public class Swing2DContainer extends JFrame
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

	private int loadingScreenId;
	
	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger("quadraturin");
	
	/**
	 * Creates a Swing frame containing the representation of the specified world model.
	 * @param title
	 * @param model
	 */
	public Swing2DContainer()
	{
		super();
		
		String applicationName = "Quadraturin " + V; 
		setName(applicationName);
		
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
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);		
		
		log.trace("Linking shutdown hook...");
		// TODO: add real JVM shutdown hook
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				log.trace("Window closing event detected.");
				safeStop();
					
				Swing2DContainer.this.dispose();
				log.trace("Terminating JVM...");
				System.exit(-1);
			}
		});
		
		log.debug("Creating thread pool...");
		if(Debug.ON)
		{
			chain = new DebugThreadChain(100);
		}
		else
			chain = new ThreadChain("q-chain");
		
			
		
		log.debug("Creating entity stage...");
		stage = new Stage();
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// initializing event manager:
		log.debug("Creating Quadraturin event manager...");
		voices = new QuadVoices(QuadConfigFactory.getInputConfig());
		chain.addThread(new LoopyChainedThread(QuadVoices.NAME, chain, voices));
		stage.addListener(voices);
		log.trace("Quadraturin event manager created.");
		
		log.debug("Creating Quadraturin GL listener...");
		controller = new Quad2DController("q-renderer", voices, chain);
		
		chain.addThread(controller);
		
		stage.addListener(controller);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    log.trace("Registering Quadraturin controller...");
	    canvas.addGLEventListener(controller);
		
		canvas.addMouseListener(voices);
		canvas.addMouseMotionListener(voices);
		canvas.addMouseWheelListener(voices);
		canvas.addKeyListener(voices);
		log.trace("Quadraturin controller created.");
		
		
	    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    // creating stage animation thread:
		log.debug("Creating entity stage animator...");
		IPhysicsEngine engine = new StupidInteractions();

	    animator = new StageAnimator(canvas, engine);
		chain.addThread(new LoopyChainedThread(StageAnimator.NAME, chain, animator));
		stage.addListener(animator);
		
		
		// TODO: configure:
		loadingScreenId = stage.addScene(new DummyScene("Intro scene"));
		stage.setScene(loadingScreenId);
		

		log.trace("Entity stage animator created.");

		log.info("Quadraturin, da fiersum enjun, is ready to load scenes.");
		log.info("/////////////////////////////////////////////////////////////");
	}
	
	/**
	 * Starts the animation.
	 */
	public void start()
	{
		chain.start();
		
		this.setVisible(true);
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
		log.debug("Quadraturin container is being shrinked...");
//		stage.setScene(loadingScreenId);

		chain.stop();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		canvas.display();
		
		canvas.removeGLEventListener(controller);
		this.removeMouseListener(voices);
		this.removeMouseMotionListener(voices);
		this.removeKeyListener(voices);
		
		log.info("Quadraturin have joined the ethernity of the stopped code.");
		log.info("/////////////////////////////////////////////////////////////");
	}

	/**
	 * TODO: injection of event dispatcher into Scene is not good.
	 * @return
	 */
	public IEventManager getEventManager() 
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
	
	public void registerPlugin(IGraphicsPlugin plugin)
	{
		controller.registerPlugin(plugin);
	}
}
