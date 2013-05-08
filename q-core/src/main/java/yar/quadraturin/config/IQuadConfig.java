package yar.quadraturin.config;

import com.spinn3r.log5j.Logger;

public interface IQuadConfig 
{
	public static final Logger LOG = Logger.getLogger( "q-config" );
	
	public EkranConfig getEkranConfig();

	public StageConfig getStageConfig();

	public InputConfig getInputConfig();
	
}
