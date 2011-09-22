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

import yarangi.graphics.quadraturin.config.IQuadConfig;
import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.debug.DebugThreadChain;
import yarangi.graphics.quadraturin.threads.LoopyChainedThread;
import yarangi.graphics.quadraturin.threads.ThreadChain;

/**
 * The Quadraturin application frame. Reads engine configuration, starts up the threads and creates AWT
 * application elements. 
 *  
 * TODO: shall add option to set different closing listener.
 * 
 * @author Dve Yarangi
 * 
 * <i>Object-oriented programming is an exceptionally bad idea which could only have originated in California.</i> 
 *                                                                                                -- Edsger Dijkstra
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

		if(Debug.ON) {/* Debug statics called */}			

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// initializing JOGL engine
		IQuadConfig config = QuadConfigFactory.getConfig();	
		
		
		int xres = config.getEkranConfig().getXres();
		int yres = config.getEkranConfig().getYres();
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
		
		log.debug("Creating thread chain...");
		if(Debug.ON)
		{
			chain = new DebugThreadChain(100);
		}
		else
			chain = new ThreadChain("q-chain");
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// initializing event manager:
		log.debug("Creating Quadraturin event manager...");
		voices = new QuadVoices(config.getInputConfig());
		log.trace("Quadraturin event manager created.");
		
		log.debug("Creating Quadraturin GL listener...");
		controller = new Quad2DController("q-renderer", config.getEkranConfig(), voices, chain);
		
		
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
//		log.debug("Creating entity stage animator...");
//		IPhysicsEngine engine = new StupidInteractions();

	    animator = new StageAnimator(canvas, config.getStageConfig(), config.getEkranConfig());
		log.trace("Entity stage animator created.");
		
		log.debug("Creating entity stage...");
		stage = new Stage(config.getStageConfig(), voices);
		
		stage.setInitialScene(); // TODO: ugly
		stage.addListener(voices);
		stage.addListener(controller);
		stage.addListener(animator);
		
		
		chain.addThread(new LoopyChainedThread(QuadVoices.NAME, chain, voices));
		chain.addThread(new LoopyChainedThread(StageAnimator.NAME, chain, animator));
		chain.addThread(controller);


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
		canvas.requestFocusInWindow();
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

		// force redraw to dispose of entities looks:
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
	
	public void addScene(Scene scene)
	{
		stage.addScene(scene);
	}

	/**
	 * 
	 * @return
	 */
	public void activateScene(String name) 
	{
		stage.setScene(name);
	}

}
