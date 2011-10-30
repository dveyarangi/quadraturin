package yarangi.graphics.veils;

import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.shaders.IShader;
import yarangi.graphics.shaders.ShaderFactory;
import yarangi.graphics.textures.FBO;

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
	
	private IShader vblurShader;
	private IShader hblurShader;
	private IShader fadeShader;
		
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
