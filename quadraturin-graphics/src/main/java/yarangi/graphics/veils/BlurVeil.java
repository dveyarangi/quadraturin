package yarangi.graphics.veils;

import java.util.Map;

import javax.media.opengl.GL;

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
	
	/**
	 * TODO: there must be a way to do this without rendering the texture three times.
	 */
	public void postRender(GL gl, IRenderingContext defaultContext) 
	{
		FBO veil = getFBO();
		
//		gl.glDisable(GL.GL_BLEND);
//		viewOrtho(gl);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		fadeShader.begin(gl);
		fadeShader.setFloat1Uniform(gl, "decay", 0.2f);
		gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
//		veil.bind( gl );
		renderTexture(gl);
		fadeShader.end(gl);
		
//		gl.glEnable(GL.GL_BLEND);
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glPopMatrix();
	}

	private void renderTexture(GL gl)
	{
		getFBO().bind( gl );
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)(0), (float)(0));	// Bottom Left Of The Texture and Quad
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)(width), (float)(0));	// Bottom Right Of The Texture and Quad
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)(width), (float)( height));	// Top Right Of The Texture and Quad
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)(0), (float)( height));
		gl.glEnd();
		getFBO().unbind( gl );
	}
	protected int width, height;



	protected void viewOrtho(GL gl) // Set Up An Ortho View
	{
		gl.glMatrixMode(GL.GL_PROJECTION); // Select Projection
		gl.glPushMatrix(); // Push The Matrix
		gl.glLoadIdentity(); // Reset The Matrix
		// gl.glOrtho((int)(-aabb.r),(int)(aabb.r),(int)(-aabb.r),(int)(aabb.r),
		// -1, 1); // Select Ortho Mode (640x480)
		gl.glOrtho(0, width, 0, height, -1, 1);
		// gl.glOrtho(0, 2*aabb.r, 0, 2*aabb.r, -1, 1); // Select Ortho Mode
		// (640x480)
		gl.glMatrixMode(GL.GL_MODELVIEW); // Select Modelview Matrix
		gl.glPushMatrix(); // Push The Matrix
		gl.glLoadIdentity(); // Reset The Matrix
	}

	protected void viewPerspective(GL gl) // Set Up A Perspective View
	{
		gl.glMatrixMode(GL.GL_PROJECTION); // Select Projection
		gl.glPopMatrix(); // Pop The Matrix
		gl.glMatrixMode(GL.GL_MODELVIEW); // Select Modelview
		gl.glPopMatrix(); // Pop The Matrix
	}


}
