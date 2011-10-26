package yarangi.graphics.veils;

import java.nio.IntBuffer;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.SceneLayer;
import yarangi.graphics.shaders.IShader;
import yarangi.graphics.shaders.ShaderFactory;
import yarangi.graphics.textures.TextureUtils;

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
public class BlurVeilEffect extends VeilPluginSkeleton 
{
	private IShader vblurShader;
	private IShader hblurShader;
	private IShader fadeShader;
	
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
		gl.glEnable(GL.GL_TEXTURE_2D);
		// save viewport and set up new one
		IntBuffer viewport = IntBuffer.allocate(4);
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
		
		gl.glDisable(GL.GL_TEXTURE_GEN_S);
		gl.glDisable(GL.GL_TEXTURE_GEN_T);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandle);
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		gl.glDisable(GL.GL_BLEND);
		viewOrtho(gl);
		fadeShader.begin(gl);
		fadeShader.setFloat1Uniform(gl, "decay", 0.2f);
		gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		renderTexture(gl);
		fadeShader.end(gl);
		viewPerspective(gl);
		gl.glEnable(GL.GL_BLEND);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		entity.display(gl, 0, effectContext);
		
		// copying data from color buffer:
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandle);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, 0, 0, viewport.get(2), viewport.get(3), 0);
		
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glDisable(GL.GL_DEPTH_TEST);
		viewOrtho(gl);
		
		vblurShader.begin(gl);
		vblurShader.setFloat1Uniform(gl, "rt_w", viewport.get(3));
//		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
		renderTexture(gl);
		vblurShader.end(gl);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, 0, 0, viewport.get(2), viewport.get(3), 0);
		
		hblurShader.begin(gl);
		hblurShader.setFloat1Uniform(gl, "rt_w", viewport.get(2));
//		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
		renderTexture(gl);
		hblurShader.end(gl);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, 0, 0, viewport.get(2), viewport.get(3), 0);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		
//		gl.glViewport(viewport.get(0), viewport.get(1), viewport.get(2), viewport.get(3));

		// unbinding texture
		viewPerspective(gl);
		entity.display(gl, 0, defaultContext);

		
		gl.glEnable(GL.GL_DEPTH_TEST);
		
	}

	private void renderTexture(GL gl)
	{
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)(0), (float)(0));	// Bottom Left Of The Texture and Quad
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)(width), (float)(0));	// Bottom Right Of The Texture and Quad
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)(width), (float)( height));	// Top Right Of The Texture and Quad
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)(0), (float)( height));
		gl.glEnd();
	}
		protected int width, height;
		protected int textureHandle;

		public void render(GL gl, double time, SceneVeil entity, IRenderingContext context) 
		{
			gl.glEnable(GL.GL_TEXTURE_2D);
					// save viewport and set up new one
			IntBuffer viewport = IntBuffer.allocate(4);
			gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
			
			gl.glViewport(0,0, width, height);
			entity.display(gl, 0, effectContext);
			
			// copying data from color buffer:
			gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandle);
			gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, 0, 0, width, height, 0);
			gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

			gl.glViewport(viewport.get(0), viewport.get(1), viewport.get(2), viewport.get(3));

			gl.glDisable(GL.GL_DEPTH_TEST);
			gl.glDisable(GL.GL_TEXTURE_GEN_S);
			gl.glDisable(GL.GL_TEXTURE_GEN_T);
			
			gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandle);
			gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
			gl.glDisable(GL.GL_DEPTH_TEST);
//			blurShader.begin(gl);
			// texture background color
			viewOrtho(gl);
//			gl.glColor4f(0.f, 0.f, 0.f, 0.f);
			gl.glColor4f(1.f, 1.f, 1.f,1.f);
			gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)(0), (float)(0));	// Bottom Left Of The Texture and Quad
				gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)(width), (float)(0));	// Bottom Right Of The Texture and Quad
				gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)(width), (float)( height));	// Top Right Of The Texture and Quad
				gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)(0), (float)( height));
			gl.glEnd();
//			blurShader.end(gl);
			
			// unbinding texture
			viewPerspective(gl);
			gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

			
			gl.glEnable(GL.GL_DEPTH_TEST);
			
		}

		public void destroy(GL gl, SceneVeil entity, IRenderingContext context) {
			// TODO: something is fishy around here
			TextureUtils.destroyTexture( gl, textureHandle );
			// gl.glDeleteTextures(1, textureHandle);
		}

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
