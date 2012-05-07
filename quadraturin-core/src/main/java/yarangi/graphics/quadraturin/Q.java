package yarangi.graphics.quadraturin;

import com.spinn3r.log5j.Logger;

public class Q
{
	public enum QMode 
	{
		PRESENT_2D, PRESENT_3D;
	}
	
	public static final Logger structure = Logger.getLogger( "q-structure" );
	public static final Logger rendering = Logger.getLogger( "q-renderer" );
	public static final Logger config    = Logger.getLogger( "q-configurer" );
	
	/**
	 * Starts the engine and provide Stage to control the presentation flow. 
	 * @return
	 */
	public static Stage go()
	{
		Swing2DContainer container = new Swing2DContainer();

		container.start();
		
		return container.getStage();
	}
}
