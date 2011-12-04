package yarangi.graphics.quadraturin;

import org.apache.log4j.Logger;

public class Q
{
	public enum QMode 
	{
		PRESENT_2D, PRESENT_3D;
	}
	
	public static final Logger structure = Logger.getLogger( "q-structure" );
	public static final Logger rendering = Logger.getLogger( "q-renderer" );
	public static final Logger config    = Logger.getLogger( "q-configurer" );
}
