package yarangi.graphics.veils;

import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.shaders.GLSLShader;
import yarangi.graphics.shaders.ShaderFactory;

/**
 * Blur effect plugin.
 * 
 * Required shaders: 
 * 		vblur - shaders/gaussian-vblur.glsl
 *		hblur - shaders/gaussian-hblur.glsl
 *		fade - shaders/fade.glsl
 *
 * @author dveyarangi
 *
 */
public class IsoheightVeil extends FBOVeilSkeleton 
{
	public static final String NAME = "isoheight-veil";
	
	private GLSLShader isoheightShader;
		
	public IsoheightVeil (Map <String, String> props) {
		
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	
	@Override
	public void init(GL gl, IRenderingContext context) 
	{
		super.init(gl, context);
		
		ShaderFactory factory = context.getPlugin( ShaderFactory.NAME );
		isoheightShader = factory.getShader( "isoheight" );
	}
	
	@Override
	public void preRender(GL gl, IRenderingContext context)
	{
		// just clearing the frame buffer texture:
		getFBO().bind(gl);
		gl.glClearColor(0,0,0,0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		getFBO().unbind(gl);
	}
	
	@Override
	public void postRender(GL gl1, IRenderingContext defaultContext) 
	{
		GL2 gl = gl1.getGL2();
		isoheightShader.begin( gl );
		// TODO: parametrize and make more sense of it:
		
		isoheightShader.setFloat4Uniform( gl, "min", 0.1f, 0.1f, 0.1f, 0.1f );
		isoheightShader.setFloat4Uniform( gl, "max", 0.3f, 0.3f, 0.4f, 0.5f );
		// higher than max colors set to underflow
		isoheightShader.setFloat4Uniform( gl, "overflow", 0.00f, 0.0f, 0.0f, 1.0f );
		// lower than min colors set to underflow
		isoheightShader.setFloat4Uniform( gl, "underflow", 0.0f, 0.0f, 0.0f, 0.0f );
		
		isoheightShader.setFloat4Uniform( gl, "target", 1f, 0.5f, 0.0f, 1f );
		renderTexture(gl, defaultContext.getCamera().getMinCoord(), defaultContext.getCamera().getMaxCoord());
		isoheightShader.end(gl);
	}


}
