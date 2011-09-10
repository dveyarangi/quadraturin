package yarangi.graphics.quadraturin.config;

import org.apache.log4j.Logger;

public interface IQuadConfig 
{
	public static final Logger LOG = Logger.getLogger( "q-config" );
	
	public EkranConfig getEkranConfig();

	public StageConfig getStageConfig();

	public InputConfig getInputConfig();
	
}
