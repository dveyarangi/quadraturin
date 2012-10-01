package yarangi.graphics.textures;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILook;

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
public class SpriteLook <T extends IEntity> implements ILook <T> 
{
	
	private final ILook <T> spriteLook;
	
	private final int width, height;
	private int textureHandle;
	
	private final boolean overlay;
	
	private boolean isInited = false;
	
	public SpriteLook(ILook <T> spriteLook, int width, int height, boolean overlay)
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
	@Override
	public void init(GL gl1, IRenderingContext context) 
	{
		GL2 gl = gl1.getGL2();
		if(isInited)
			return;
		
		textureHandle = TextureUtils.createEmptyTexture2D(gl, width, height, true);
//		System.out.println("Sprite look generated texture id: " + textureHandle);

		// save viewport and set up new one
		IntBuffer viewport = IntBuffer.allocate(4);
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
		

		T entity = context.getAssociatedEntity( this );
		
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
	
		spriteLook.render(gl, entity, null);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandle);
		// copying data from color buffer:
//		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, 0, 0, width, height, 0);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 1, GL.GL_RGBA, 1*width/4, 1*height/4, 3*width/4, 3*height/4, 0);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 2, GL.GL_RGBA, 3*width/8, 3*height/8, 5*width/8, 5*height/8, 0);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 3, GL.GL_RGBA, 5*width/16, 5*height/16, 11*width/16, 11*height/16, 0);
		gl.glGenerateMipmap(GL.GL_TEXTURE_2D);
		
		// remove the drawing		
		// restoring matrices:
		gl.glViewport(viewport.get(0),viewport.get(1),viewport.get(2),viewport.get(3));
		viewPerspective(gl);
		
		// unbinding texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		gl.glDisable(GL.GL_SCISSOR_TEST);
		
		isInited = true;
	}

	
	
	@Override
	public void render(GL gl1, T entity, IRenderingContext defaultContext) 
	{
		GL2 gl = gl1.getGL2();
//		gl.glEnable(GL.GL_TEXTURE_2D);					// Enable 2D Texture Mapping

		gl.glPushAttrib( GL2.GL_ENABLE_BIT );
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL2.GL_TEXTURE_GEN_S);
		gl.glDisable(GL2.GL_TEXTURE_GEN_T);
		
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandle);
///		gl.glDisable(GL.GL_DEPTH_TEST);
		//		viewOrtho(gl);
//		blurShader.begin(gl);
//		gl.glColor4f(1.f, 1.f, 1.f,1.f);
		// texture background color
		gl.glColor4f(0.f, 0.f, 0.f, 0.f);
		gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(-width/2, -height/2);	// Bottom Left Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(width/2, -height/2);	// Bottom Right Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(width/2, height/2);	// Top Right Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(-width/2, height/2);
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
		
		gl.glPopAttrib();
		// unbinding texture
		if(overlay)
			spriteLook.render(gl, entity, defaultContext);
		
	}

	@Override
	public void destroy(GL gl, IRenderingContext defaultContext) 
	{
		// TODO: something is fishy around here
//		gl.glDeleteTextures(1, textureHandle);
	}
	private void viewOrtho(GL2 gl, IEntity entity)  // Set Up An Ortho View
    {
        gl.glMatrixMode(GL2.GL_PROJECTION);  // Select Projection
        gl.glPushMatrix();      // Push The Matrix
        gl.glLoadIdentity();      // Reset The Matrix
//        gl.glOrtho((int)(-aabb.r),(int)(aabb.r),(int)(-aabb.r),(int)(aabb.r), -1, 1);  // Select Ortho Mode (640x480)
        gl.glOrtho(0, width, 0, height, -1, 1); 
//        gl.glOrtho(0, 2*aabb.r, 0, 2*aabb.r, -1, 1);  // Select Ortho Mode (640x480)
        gl.glMatrixMode(GL2.GL_MODELVIEW);  // Select Modelview Matrix
        gl.glPushMatrix();      // Push The Matrix
        gl.glLoadIdentity();      // Reset The Matrix
    }

    private void viewPerspective(GL2 gl)    // Set Up A Perspective View
    {
        gl.glMatrixMode(GL2.GL_PROJECTION);  // Select Projection
        gl.glPopMatrix();      // Pop The Matrix
        gl.glMatrixMode(GL2.GL_MODELVIEW);  // Select Modelview
        gl.glPopMatrix();      // Pop The Matrix
    }

	@Override
	public boolean isCastsShadow()
	{
		return false;
	}

	@Override
	public float getPriority()
	{
		return 0;
	}

	@Override
	public IVeil getVeil() { return null; }

	@Override
	public boolean isOriented()
	{
		return true;
	}
}
