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
 */
public class BlurVeil extends VeilPluginSkeleton 
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
		vblurShader = factory.getShader( "vblur" );
		hblurShader = factory.getShader( "hblur");
		fadeShader = factory.getShader( "fade");
	}
	
	public void postRender(GL gl, IRenderingContext defaultContext) 
	{
		getFBO().bind( gl );
		gl.glDisable(GL.GL_BLEND);
/*		vblurShader.begin( gl );
		renderTexture(gl);
		vblurShader.end(gl);
		hblurShader.begin( gl );
		renderTexture(gl);
		hblurShader.end(gl);*/
		fadeShader.begin( gl );
		fadeShader.setFloat1Uniform( gl, "decay", 0.03f );
		renderTexture(gl);
		fadeShader.end(gl);
		getFBO().unbind( gl );
		gl.glEnable(GL.GL_BLEND);
		renderTexture(gl);
	}


}
