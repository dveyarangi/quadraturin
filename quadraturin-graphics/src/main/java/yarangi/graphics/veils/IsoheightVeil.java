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
public class IsoheightVeil extends VeilPluginSkeleton 
{
	public static final String NAME = "isoheight-veil";
	
	private IShader isoheightShader;
		
	public IsoheightVeil (Map <String, String> props) {
		
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
		isoheightShader = factory.getShader( "isoheight" );
	}
	
	public void preRender(GL gl, IRenderingContext context)
	{
		// just clearing the frame buffer texture:
		getFBO().bind(gl);
		gl.glClearColor(0,0,0,0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		getFBO().unbind(gl);
	}
	
	public void postRender(GL gl, IRenderingContext defaultContext) 
	{
		isoheightShader.begin( gl );
		isoheightShader.setFloat4Uniform( gl, "min", 0.2f, 0f, 0.5f, 0.5f );
		isoheightShader.setFloat4Uniform( gl, "max", 0.4f, 0f, 0.55f, 1.0f );
		isoheightShader.setFloat4Uniform( gl, "target", 1f, 0f, 1.0f, 1.0f );
		renderTexture(gl);
		isoheightShader.end(gl);
	}


}
