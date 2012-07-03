package yarangi.graphics.veils;

import java.util.Map;

import javax.media.opengl.GL;

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
 * TODO: scale and transpose according to viewpoint
 *
 */
public class BlurVeil extends FBOVeilSkeleton 
{
	public static final String NAME = "blur-veil";
	
	private GLSLShader vblurShader;
	private GLSLShader hblurShader;
	private GLSLShader fadeShader;
		
	public BlurVeil (Map <String, String> props) {
		
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	
	public void init(GL gl, IRenderingContext context) 
	{
		super.init(gl, context);
		
		ShaderFactory factory = context.getPlugin( ShaderFactory.NAME );
		if(factory == null)
			throw new IllegalStateException("Shader factory plugin is not available.");
		vblurShader = factory.getShader( "vblur" );
		hblurShader = factory.getShader( "hblur");
		fadeShader = factory.getShader( "fade");
	}
	

	
	public void postRender(GL gl, IRenderingContext defaultContext) 
	{
		getFBO().bind( gl );
		gl.glPushAttrib( GL.GL_COLOR_BUFFER_BIT );
		gl.glDisable(GL.GL_BLEND);
		gl.glDisable(GL.GL_DEPTH_TEST);
/*		vblurShader.begin( gl );
		renderTexture(gl);
		vblurShader.end(gl);
		hblurShader.begin( gl );
		renderTexture(gl);
		hblurShader.end(gl);*/
		fadeShader.begin( gl );
		fadeShader.setFloat1Uniform( gl, "decay", 0.05f );
		renderTexture(gl);
		fadeShader.end(gl);
		gl.glPopAttrib();
		getFBO().unbind( gl );
		renderTexture(gl);
	}


}
