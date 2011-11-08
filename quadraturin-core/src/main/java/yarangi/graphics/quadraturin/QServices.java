package yarangi.graphics.quadraturin;

import org.apache.log4j.Logger;

public class QServices
{
	public enum QMode 
	{
		PRESENT_2D, PRESENT_3D;
	}
	
	public static Logger structure = Logger.getLogger( "q-structure" );
	public static Logger rendering = Logger.getLogger( "q-renderer" );
}
