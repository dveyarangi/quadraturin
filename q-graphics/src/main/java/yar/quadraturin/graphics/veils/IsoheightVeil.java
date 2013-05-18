package yar.quadraturin.graphics.veils;

import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.graphics.shaders.GLSLShader;
import yar.quadraturin.graphics.shaders.ShaderFactory;

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
	public void init(IRenderingContext context) 
	{
		super.init(context);
		
		ShaderFactory factory = context.getPlugin( ShaderFactory.NAME );
		isoheightShader = factory.getShader( "isoheight" );
	}
	
	@Override
	public void preRender(IRenderingContext context)
	{
		GL2 gl = context.gl();
		// just clearing the frame buffer texture:
		getFBO().bind(gl);
//		gl.glClearColor(0,0,0,0.0f);
//		gl.glClearColor(0.0f,0.0f, 0.0f, 1.0f);		
		getFBO().unbind(gl);
	}
	
	@Override
	public void postRender(IRenderingContext context) 
	{
		GL2 gl = context.gl();
		isoheightShader.begin( gl );
		// TODO: parametrize and make more sense of it:
		
		isoheightShader.setFloat4Uniform( gl, "min", 0.1f, 0.1f, 0.1f, 0.1f );
		isoheightShader.setFloat4Uniform( gl, "max", 0.3f, 0.3f, 0.4f, 0.5f );
		// higher than max colors set to underflow
		isoheightShader.setFloat4Uniform( gl, "overflow", 0.00f, 0.0f, 0.0f, 1.0f );
		// lower than min colors set to underflow
		isoheightShader.setFloat4Uniform( gl, "underflow", 0.0f, 0.0f, 0.0f, 0.0f );
		
		isoheightShader.setFloat4Uniform( gl, "target", 1f, 0.5f, 0.0f, 1f );
		renderTexture(gl, context.getCamera().getMinCoord(), context.getCamera().getMaxCoord());
		isoheightShader.end(gl);
	}


}
