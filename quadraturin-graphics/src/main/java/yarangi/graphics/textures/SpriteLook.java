package yarangi.graphics.textures;

import java.nio.IntBuffer;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.objects.SceneEntity;

/**
 * Creates and binds the texture of the entity's look. The content
 * of the resulting texture is bound to entity's bounding box property,
 * and has the specified pixel resolution.
 * @param gl
 * @param entity
 * @param width
 * @param height
 * @return texture object handler
 */
public class SpriteLook <T extends SceneEntity> implements Look <T> 
{
	
	private Look <T> spriteLook;
	
	private int width, height;
	private int textureHandle;
	
	private boolean overlay;
	
	private boolean isInited = false;
	
	public SpriteLook(Look <T> spriteLook, int width, int height, boolean overlay)
	{
		this.spriteLook = spriteLook;
		
		this.width = width;
		this.height = height;
		
		this.overlay = overlay;
	}
	
	/**
	 * Creates and binds the texture of the entity's look. The contents
	 * of the resulting texture are bound to entity's bounding box property,
	 * and has the specified pixel resolution.
	 * @param gl
	 * @param entity
	 * @param width
	 * @param height
	 * @return texture object handler
	 */
	public void init(GL gl, T entity) 
	{
		if(isInited)
			return;
		
		textureHandle = TextureUtils.createEmptyTexture2D(gl, width, height, true);
//		System.out.println("Sprite look generated texture id: " + textureHandle);

		// save viewport and set up new one
		IntBuffer viewport = IntBuffer.allocate(4);
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
		
		// setting viewport to dimensions of our texture:
//		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glViewport(0,0,width,height);
		viewOrtho(gl, entity);
		gl.glEnable(GL.GL_SCISSOR_TEST);
		gl.glScissor(0, 0, width, height);
		
/*		gl.glColor4f(1.f, 1.f, 1.f,1.f);
		gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f((float)(1), (float)(1), 0.0f);
			gl.glVertex3f((float)(1), (float)(height-1), 0f);
			gl.glVertex3f((float)( width-1), (float)(height-1), 0.0f);
			gl.glVertex3f((float)( width-1), (float)(1), 0.0f);
			gl.glVertex3f((float)(1), (float)(1), 0.0f);
		gl.glEnd();*/
		
		gl.glTranslatef((float)width/2, (float)height/2, 0);
		// rendering:
//		gl.glClearColor(0.0f, 0.0f, 0.5f, 1.0f);
//	   	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	
		spriteLook.render(gl, 0, entity, null);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandle);
		// copying data from color buffer:
//		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, 0, 0, width, height, 0);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 1, GL.GL_RGBA, 1*width/4, 1*height/4, 3*width/4, 3*height/4, 0);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 2, GL.GL_RGBA, 3*width/8, 3*height/8, 5*width/8, 5*height/8, 0);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 3, GL.GL_RGBA, 5*width/16, 5*height/16, 11*width/16, 11*height/16, 0);
		gl.glGenerateMipmapEXT(GL.GL_TEXTURE_2D);
		
		// remove the drawing		
		// restoring matrices:
		gl.glViewport(viewport.get(0),viewport.get(1),viewport.get(2),viewport.get(3));
		viewPerspective(gl);
		
		// unbinding texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		gl.glDisable(GL.GL_SCISSOR_TEST);
		
		isInited = true;
	}

	
	
	public void render(GL gl, double time, T entity, RenderingContext defaultContext) 
	{
//		gl.glEnable(GL.GL_TEXTURE_2D);					// Enable 2D Texture Mapping

		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_TEXTURE_GEN_S);
		gl.glDisable(GL.GL_TEXTURE_GEN_T);
		
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandle);
///		gl.glDisable(GL.GL_DEPTH_TEST);
		//		viewOrtho(gl);
//		blurShader.begin(gl);
//		gl.glColor4f(1.f, 1.f, 1.f,1.f);
		// texture background color
		gl.glColor4f(0.f, 0.f, 0.f, 0.f);
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)(-width/2), (float)(-height/2));	// Bottom Left Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)( width/2), (float)(-height/2));	// Bottom Right Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)( width/2), (float)( height/2));	// Top Right Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)(-width/2), (float)( height/2));
		gl.glEnd();
//		blurShader.end(gl);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		
		
/*		gl.glColor4f(1.f, 1.f, 1.f,0.2f);
		gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f((float)(-width/2+1), (float)(-height/2+1), 0.0f);
			gl.glVertex3f((float)( width/2-1), (float)(-height/2+1), 0.0f);
			gl.glVertex3f((float)( width/2-1), (float)(height/2-1), 0.0f);
			gl.glVertex3f((float)(-width/2+1), (float)(height/2+1), 0.0f);
			gl.glVertex3f((float)(-width/2+1), (float)(-height/2+1), 0.0f);
		gl.glEnd();*/
		
//		gl.glDisable(GL.GL_TEXTURE_2D);					// Enable 2D Texture Mapping
		gl.glEnable(GL.GL_DEPTH_TEST);
//		viewPerspective(gl);
//		gl.glEnable(GL.GL_DEPTH_TEST);
///		gl.glDisable(GL.GL_TEXTURE_2D);					// Enable 2D Texture Mapping
		
		// unbinding texture
		if(overlay)
			spriteLook.render(gl, time, entity, defaultContext);
		
	}

	public void destroy(GL gl, T entity) 
	{
		// TODO: something is fishy around here
//		gl.glDeleteTextures(1, textureHandle);
	}
	private void viewOrtho(GL gl, SceneEntity entity)  // Set Up An Ortho View
    {
        gl.glMatrixMode(GL.GL_PROJECTION);  // Select Projection
        gl.glPushMatrix();      // Push The Matrix
        gl.glLoadIdentity();      // Reset The Matrix
//        gl.glOrtho((int)(-aabb.r),(int)(aabb.r),(int)(-aabb.r),(int)(aabb.r), -1, 1);  // Select Ortho Mode (640x480)
        gl.glOrtho(0, width, 0, height, -1, 1); 
//        gl.glOrtho(0, 2*aabb.r, 0, 2*aabb.r, -1, 1);  // Select Ortho Mode (640x480)
        gl.glMatrixMode(GL.GL_MODELVIEW);  // Select Modelview Matrix
        gl.glPushMatrix();      // Push The Matrix
        gl.glLoadIdentity();      // Reset The Matrix
    }

    private void viewPerspective(GL gl)    // Set Up A Perspective View
    {
        gl.glMatrixMode(GL.GL_PROJECTION);  // Select Projection
        gl.glPopMatrix();      // Pop The Matrix
        gl.glMatrixMode(GL.GL_MODELVIEW);  // Select Modelview
        gl.glPopMatrix();      // Pop The Matrix
    }
}