package yarangi.graphics.textures;

import java.nio.IntBuffer;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.SceneVeil;
import yarangi.graphics.shaders.IShader;
import yarangi.graphics.shaders.ShaderFactory;

public class BlurVeilEffect extends VeilEffectSkeleton 
{
	private IShader vblurShader;
	private IShader hblurShader;
	private IShader fadeShader;
	
	public void init(GL gl, SceneVeil entity) 
	{
		super.init(gl, entity);
		
		ShaderFactory.registerShaderFile("vblur", "shaders/gaussian-vblur.glsl");
		ShaderFactory.registerShaderFile("hblur", "shaders/gaussian-hblur.glsl");
		ShaderFactory.registerShaderFile("fade", "shaders/fade.glsl");
//		ShaderFactory.registerShaderFile("blur", "shaders/gaussian-blur2.glsl");
		vblurShader = ShaderFactory.getShader("vblur");
		hblurShader = ShaderFactory.getShader("hblur");
		fadeShader = ShaderFactory.getShader("fade");
		vblurShader.init(gl);
		hblurShader.init(gl);
		fadeShader.init(gl);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * TODO: there must be a way to do this without rendering the texture three times.
	 */
	public void render(GL gl, double time, SceneVeil entity, IRenderingContext defaultContext) 
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
	
}
