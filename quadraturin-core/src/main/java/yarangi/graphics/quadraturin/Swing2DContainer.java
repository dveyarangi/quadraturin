package yarangi.graphics.quadraturin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import yarangi.graphics.quadraturin.config.IQuadConfig;
import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.debug.Debug;
import yarangi.graphics.quadraturin.debug.DebugThreadChain;
import yarangi.graphics.quadraturin.threads.ITerminationListener;
import yarangi.graphics.quadraturin.threads.LoopyChainedThread;
import yarangi.graphics.quadraturin.threads.ThreadChain;

import com.spinn3r.log5j.Logger;

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
public class Swing2DContainer extends JFrame implements ITerminationListener
{

	private static final long serialVersionUID = 5840442002396512390L;
	
	public static final String V = "0.0.4";
	
	/**
	 * AWT-JOGL bridge component.
	 */
	private GLCanvas canvas = null;
	
	/**
	 * Entities and animation controller.
	 */
	private Stage stage;
	
	/**
	 * Event dispatcher.
	 */
	private final QVoices voices;
	
	/**
	 * Events and rendering controller.
	 */
	private final Q2DController controller;
	
	/**
	 * Animation thread.
	 */
	private final StageAnimator animator;
	
	/**
	 * Contains {@link #voices}, {@link #controller} and {@link #animator} threads
	 */
	private ThreadChain chain;
	
	
	/**
	 * Logger
	 */
	private final Logger log = Logger.getLogger("quadraturin");
	
	/**
	 * Creates a Swing frame containing the representation of the specified world model.
	 * @param title
	 * @param model
	 */
	public Swing2DContainer()
	{
		super();
		
		String applicationName = "Quadraturin " + V; 
		
		log.info("/////////////////////////////////////////////////////////////");
		log.info(applicationName + " container is being expanded...");

		if(Debug.ON) {/* Debug statics called */}
		
		
		IQuadConfig config = QuadConfigFactory.getConfig();	

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// initializing JOGL engine
		

//		loadNativeLibs();
		log.debug("Configuring GL capabilities.");
	    GLCapabilities capabilities = new GLCapabilities();
	    capabilities.setRedBits(8);
	    capabilities.setBlueBits(8);
	    capabilities.setGreenBits(8);
	    capabilities.setAlphaBits(8);

	    canvas = new GLCanvas(capabilities);
	    
	    

		// remove mouse cursor:
/*		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
									cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);	*/	
		
	    
	    
		log.debug("Creating thread chain...");
		
		if(Debug.ON)
			chain = new DebugThreadChain(100, this);
		else
			chain = new ThreadChain("q-chain", this);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// initializing event manager:
		log.debug("Creating event manager...");
		voices = new QVoices(config.getInputConfig());
		log.trace("Event manager created.");
		
		
	    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    // creating stage animation thread:
		log.debug("Creating stage animator...");

	    animator = new StageAnimator(canvas, config.getStageConfig(), config.getEkranConfig());
		log.trace("Entity stage animator created.");
		
	    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    // creating render:
		log.debug("Creating GL listener...");
		controller = new Q2DController("q-renderer", config.getEkranConfig(), voices, animator, chain);
		
	    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    // creating engine stage, that handles scene loading and switching:
		log.debug("Creating entity stage...");
		try {
			stage = Stage.init(config.getStageConfig(), config.getEkranConfig(), voices);
		}
		catch(Exception e)
		{
			log.fatal( "Failed to initialize engine", e );
			System.exit( 1 );
		}
		
		 // TODO: ugly
		stage.addListener(voices);
		stage.addListener(controller);
		stage.addListener(animator);
		
		
		chain.addThread(new LoopyChainedThread(QVoices.NAME, chain, voices));
		chain.addThread(new LoopyChainedThread(StageAnimator.NAME, chain, animator));
		chain.addThread(controller);

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    log.trace("Registering Quadraturin controller...");
	    canvas.addGLEventListener(controller);
		
		canvas.addMouseListener(voices);
		canvas.addMouseMotionListener(voices);
		canvas.addMouseWheelListener(voices);
		canvas.addKeyListener(voices);

		log.trace("Quadraturin controller created.");
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// organizing JFrame contents:
		
	    log.trace("Organizing swing frame...");
		this.setName(applicationName);
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenResolution =  toolkit.getScreenSize();
		Dimension resolution;
		if(config.getEkranConfig().isFullscreen()) {
			
			this.setUndecorated( true ); // remove window border

			// Get the current screen size
			resolution = screenResolution;
			this.setLocation( 0, 0 );
		}
		else 
		{
			// moving game window to screen center:
			this.setLocation( screenResolution. width/2 - config.getEkranConfig().getXres()/2, 
							  screenResolution.height/2 - config.getEkranConfig().getYres()/2 );
			resolution = new Dimension(config.getEkranConfig().getXres(), config.getEkranConfig().getYres());
		}
		
		// adding JVM shutdown callback:
	    linkShutdownHook();
		
		log.debug("OpenGL canvas dimensions are [" + resolution.width + "," + resolution.height + "].");
		canvas.setMinimumSize(resolution);
		canvas.setPreferredSize(resolution);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(canvas, BorderLayout.CENTER);
		this.getContentPane().validate();
		this.pack();
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// All done
		log.info("Quadraturin, da fiersum enjun, is ready to load scenes.");
		log.info("/////////////////////////////////////////////////////////////");
	}

	/**
	 * Starts the animation.
	 */
	public void start()
	{
		
		chain.start(); // starting game loop
		
		this.setVisible(true); // making game window visible
		canvas.requestFocusInWindow();
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
	 * TODO: event dispatcher should be static?
	 * @return
	 */
	public IEventManager getEventManager() 
	{
		return voices; 
	}

	public Stage getStage()
	{
		return stage;
	}

	
	private void loadNativeLibs() 
	{
		String os = System.getProperty("os.version");
		String archDataModel = System.getProperty("sun.arch.data.model");
		log.debug("OS: %s, arch: %s", os, archDataModel);
		System.loadLibrary(archDataModel + File.separator + "jogl");
		System.loadLibrary(archDataModel + File.separator + "jogl_awt");
		System.loadLibrary(archDataModel + File.separator + "jogl_cg");
	}

	public IRenderingContext getRenderingContext()
	{
		return controller.getRenderingContext();
	}
	
	
	private void linkShutdownHook()
	{
		log.trace("Linking shutdown hook...");
		// TODO: add real JVM shutdown hook
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				log.trace("Window closing event detected.");
				safeStop();
					
				Swing2DContainer.this.dispose();
				log.trace("Terminating JVM...");
				System.exit(-1);
			}
		});
	}

	@Override
	public void onGeneralError()
	{
		System.exit( 1 );
	}

}
