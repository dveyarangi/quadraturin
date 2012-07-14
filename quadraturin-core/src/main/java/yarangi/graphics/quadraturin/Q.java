package yarangi.graphics.quadraturin;

import java.lang.Thread.UncaughtExceptionHandler;

import com.spinn3r.log5j.Logger;

/**
 * Quadraturin initializer. Use {@link #go()} method to start the engine.
 * 
 */
public class Q
{
	/**
	 * TODO: not used
	 */
	public enum QMode 
	{
		PRESENT_2D, PRESENT_3D;
	}
	
	
	////////////////////////////////////
	// Q-wide loggers:
	
	public static final Logger structure = Logger.getLogger( "q-structure" );
	public static final Logger rendering = Logger.getLogger( "q-renderer" );
	public static final Logger config    = Logger.getLogger( "q-configurer" );
	
	////////////////////////////////////
	// VM properties
	
	/**
	 * System property to turn on GL debug mode.
	 */
	public static final String DEBUG_MODE = "q.debug";
	
	/**
	 * System property - path to configuration file.
	 */
	public static final String CONFIG_FILE = "q.config.file";

	
	////////////////////////////////////
	// Exception handling:
	static {
		// TODO: global exception handler:
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler () {
			@Override
			public void uncaughtException(Thread thread, Throwable tro)
			{
				Q.structure.fatal( "Uncaught exception in thread [" + thread + "]", tro );
				System.exit( 1 );
			}
		});
	}
	
	/**
	 * Starts the engine and provide Stage to control the presentation flow.
	 *  
	 * @return
	 */
	public static Stage go()
	{
		Swing2DContainer container = new Swing2DContainer();

		container.start();
		
		return container.getStage();
	}

}
