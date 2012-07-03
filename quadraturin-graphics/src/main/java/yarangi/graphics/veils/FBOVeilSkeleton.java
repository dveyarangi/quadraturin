package yarangi.graphics.veils;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.OrientingVeil;
import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;
import yarangi.graphics.textures.FBO;

/**
 * Allows rendering into separate frame buffer, for post-processing effects.
 * A {@link Look} have to explicitly invoke {@link #weave(GL)} and {@link #tear(GL)}
 * methods to render into the veil.
 * 
 * @author dveyarangi
 */
public abstract class FBOVeilSkeleton extends OrientingVeil implements IGraphicsPlugin 
{
	/**
	 * Veil buffer for rendering
	 */
	private FBO veil;
	
	private int width, height;

	private GLU glu = new GLU();
	
	private boolean isInited = false;
	
	@Override
	public void init(GL gl, IRenderingContext context) {
		this.width = context.getViewPort().getWidth();
		this.height = context.getViewPort().getHeight();
		int textureWidth = width;//BitUtils.po2Ceiling(width);
		int textureHeight = height;//BitUtils.po2Ceiling(height);
		veil = FBO.createFBO(gl, textureWidth, textureHeight, true);
		
		if(veil == null)
			throw new IllegalStateException("Failed to create veil frame buffer object.");
	}
	
	public void resize(GL gl, IRenderingContext context) {
		if(veil == null)
			throw new IllegalStateException("Cannot reinit not initiated veil,");
		destroy(gl);
		init(gl, context);
	}
	
	public final int getWidth()  { return width; }
	public final int getHeight() { return height; }
	
	@Override
	public void preRender(GL gl, IRenderingContext context) { }

	@Override
	public void postRender(GL gl, IRenderingContext context) { }

	/**
	 * Binds veil's buffer; after invocation all GL rendering will go to this buffer 
	 * @param gl
	 */
	public void weave(GL gl, ILayerObject entity, IRenderingContext context)
	{
		veil.bind(gl);
//		gl.glPushAttrib( COLOR )
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		// adjusting frame buffer to entity coordinates:
		super.weave( gl, entity, context );
	}
	
	/**
	 * Unbinds veil's buffer and restores normal rendering.
	 * @param gl
	 */
	public void tear(GL gl)
	{
		super.tear( gl );
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBlendEquation(GL.GL_FUNC_ADD);
		
		veil.unbind(gl);
	}
	
	protected final FBO getFBO() 
	{
		return veil;
	}
	
	@Override
	public String[] getRequiredExtensions()
	{
		// TODO: verify that this is enough:
		return new String [] { "GL_ARB_framebuffer_object" };
	}

	/**
	 * Render veil full-screen texture
	 * @param gl
	 */
	protected void renderTexture(GL gl)
	{
		double lower[] = new double[4];// wx, wy, wz;// returned xyz coords
		double higher[] = new double[4];// wx, wy, wz;// returned xyz coords
		
		int viewport[] = new int[4];
		double mvmatrix[] = new double[16];
		double projmatrix[] = new double[16];

		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, mvmatrix, 0);
		gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projmatrix, 0);
	
		
		gl.glDisable(GL.GL_DEPTH_TEST);
		// 
		glu.gluUnProject(viewport[0], viewport[1], 0.0, mvmatrix, 0, projmatrix, 0, viewport, 0, lower, 0);
		glu.gluUnProject(viewport[2], viewport[3], 0.0, mvmatrix, 0, projmatrix, 0, viewport, 0, higher, 0);
		
		gl.glEnable( GL.GL_BLEND );
		getFBO().bindTexture( gl );
//		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glColor4f( 0,0,0,1 );
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)lower[0],  (float)lower[1]);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)higher[0], (float)lower[1]);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)higher[0], (float)higher[1]);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)lower[0],  (float)higher[1]);
		gl.glEnd();

		getFBO().unbindTexture( gl );
		gl.glEnable( GL.GL_BLEND );
	}
	

	@Override
	public void destroy(GL gl)
	{
		if(veil == null)
			throw new IllegalStateException("Cannot destroy not initiated veil.");
		
		veil.destroy( gl );
		veil = null;
	}

}
