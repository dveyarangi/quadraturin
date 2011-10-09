package yarangi.graphics.textures;

import java.nio.IntBuffer;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import yarangi.ZenUtils;
import yarangi.graphics.quadraturin.IViewPoint;
import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.SceneVeil;
import yarangi.graphics.quadraturin.objects.IVeilOverlay;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;
import yarangi.math.BitUtils;

/**


 */
public class VeilEffectSkeleton implements IVeilOverlay  
{
	protected int width, height;
	protected int textureHandle;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	protected IRenderingContext effectContext = new IRenderingContext() {
		public boolean doPushNames() { return false; }
		public boolean isForEffect() { return true; }
		public IViewPoint getViewPoint() { return null; }
		@Override
		public <T extends IGraphicsPlugin> T getPlugin(String name)
		{
			ZenUtils.methodNotSupported( this.getClass() );
		}
	};

	public VeilEffectSkeleton() {
		
	}

	/**
	 * Creates and binds the texture of the entity's look. The contents of the
	 * resulting texture are bound to entity's bounding box property, and has
	 * the specified pixel resolution.
	 * 
	 * @param gl
	 * @param entity
	 * @param width
	 * @param height
	 * @return texture object handler
	 */
	public void init(GL gl, SceneVeil entity) 
	{
		// creating texture that covers the current viewport:
		
		IntBuffer viewport = IntBuffer.allocate(4);
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);

		this.width = BitUtils.po2Ceiling(viewport.get(2)-viewport.get(0));
		this.height = BitUtils.po2Ceiling(viewport.get(3)-viewport.get(1));		
		
		textureHandle = TextureUtils.createEmptyTexture2D(gl, width, height, false);
		log.debug("Background texture size is " + width + "x" + height + ".");
	}

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
//		blurShader.begin(gl);
		// texture background color
		viewOrtho(gl);
//		gl.glColor4f(0.f, 0.f, 0.f, 0.f);
		gl.glColor4f(1.f, 1.f, 1.f,1.f);
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)(0), (float)(0));	// Bottom Left Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)(width), (float)(0));	// Bottom Right Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)(width), (float)( height));	// Top Right Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)(0), (float)( height));
		gl.glEnd();
//		blurShader.end(gl);
		
		// unbinding texture
		viewPerspective(gl);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		
		gl.glEnable(GL.GL_DEPTH_TEST);
		
	}

	public void destroy(GL gl, SceneVeil entity) {
		// TODO: something is fishy around here
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

	@Override
	public boolean isCastsShadow() { return false; }
}
