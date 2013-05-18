package yar.quadraturin.graphics.veils;

import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.Camera2D;
import yar.quadraturin.IRenderingContext;
import yar.quadraturin.graphics.shaders.GLSLShader;
import yar.quadraturin.graphics.shaders.ShaderFactory;

/**
 * Fade/blur fullscreen effect plugin.
 * 
 * Required shaders: 
 * 		vblur - shaders/gaussian-vblur.glsl - TODO: not really used
 *		hblur - shaders/gaussian-hblur.glsl - TODO: not really used
 *		fade - shaders/fade.glsl
 *
 * @author dveyarangi
 * 
 * TODO: scale and transpose according to viewpoint, to prevent artifacts on viewport changes
 *
 */
public class BlurVeil extends FBOVeilSkeleton 
{
	public static final String NAME = "blur-veil";
	
	private GLSLShader vblurShader;
	private GLSLShader hblurShader;
	
	/**
	 * pixel color decaying shader
	 */
	private GLSLShader fadeShader;
	
	
	private static final float DEFAULT_DECAY_AMOUNT = 0.003f;
	
	/**
	 * amount of decay for color components
	 * TODO: different rates for different components
	 * TODO: uniform decay to fix color shifts
	 */
	private float decayAmount = DEFAULT_DECAY_AMOUNT;
		
	public BlurVeil (Map <String, String> props) {
		String decayStr = props.get( "decay" );
		if(decayStr != null)
			decayAmount = Float.parseFloat( decayStr );
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
		if(factory == null)
			throw new IllegalStateException("Shader factory plugin is not available.");
		vblurShader = factory.getShader( "vblur" );
		hblurShader = factory.getShader( "hblur");
		fadeShader = factory.getShader( "fade");
	}
	

	
	@Override
	public void postRender(IRenderingContext context) 
	{
		GL2 gl = context.gl();
		
		Camera2D vp = context.getCamera();
		
		getFBO().bind( gl );
			gl.glPushAttrib( GL.GL_COLOR_BUFFER_BIT | GL2.GL_ENABLE_BIT);
		//		gl.glDisable(GL.GL_BLEND);
			// this blending mode ensures that alpha channel is cleared correctly:
				gl.glBlendFuncSeparate( GL.GL_ONE, GL.GL_ZERO, GL.GL_ONE, GL.GL_ZERO );
		//		gl.glBlendEquation(GL.GL_REPLACE);
				gl.glDisable(GL.GL_DEPTH_TEST);
				
		/*		vblurShader.begin( gl );
				renderTexture(gl);
				vblurShader.end(gl);
				hblurShader.begin( gl );
				renderTexture(gl);
				hblurShader.end(gl);*/
				// XXX: this texture scaling is wrong!
				fadeShader.begin( gl );
					fadeShader.setFloat1Uniform( gl, "decay", decayAmount * context.getFrameLength() );
					renderTexture(gl, vp.getPrevMinCoord(), vp.getPrevMaxCoord());
				fadeShader.end(gl);
			
			gl.glPopAttrib();
			context.setDefaultBlendMode( gl );
		getFBO().unbind( gl );
//		System.out.println("curr");
		renderTexture(gl, vp.getMinCoord(), vp.getMaxCoord());
		
	}


}
