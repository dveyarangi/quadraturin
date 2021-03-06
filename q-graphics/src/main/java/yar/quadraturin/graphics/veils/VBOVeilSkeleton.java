package yar.quadraturin.graphics.veils;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.graphics.textures.FBO;
import yar.quadraturin.objects.ILook;
import yar.quadraturin.objects.IVisible;
import yar.quadraturin.plugin.IGraphicsPlugin;

import yarangi.math.BitUtils;

/**
 * Allows rendering into separate frame buffer, for post-processing effects.
 * A {@link ILook} have to explicitly invoke {@link #weave(GL)} and {@link #tear(GL)}
 * methods to render into the veil.
 * TODO: render grids here?
 * 
 * @author dveyarangi
 */
public abstract class VBOVeilSkeleton implements IVeil, IGraphicsPlugin 
{
	/**
	 * Veil buffer for rendering
	 */
	private FBO veil;
	
	private int width, height;

	private final GLU glu = new GLU();
	
	private final boolean isInited = false;
	
	@Override
	public void init(IRenderingContext context) 
	{
		GL2 gl = context.gl();
		this.width = context.getViewPort().getWidth();
		this.height = context.getViewPort().getHeight();
		int textureWidth = BitUtils.po2Ceiling(width/16);
		int textureHeight = BitUtils.po2Ceiling(height/16);
		veil = FBO.createFBO(gl, textureWidth, textureHeight, true);
		
		if(veil == null)
			throw new IllegalStateException("Failed to create veil frame buffer object.");
	}
	
	@Override
	public void resize(IRenderingContext context) 
	{
		if(veil == null)
			throw new IllegalStateException("Cannot reinit not initiated veil,");
		destroy(context);
		init(context);
	}
	
	public final int getWidth()  { return width; }
	public final int getHeight() { return height; }
	
	@Override
	public void preRender(IRenderingContext context) { }

	@Override
	public void postRender(IRenderingContext context) { }

	/**
	 * Binds veil's buffer; after invocation all GL rendering will go to this buffer 
	 * @param gl
	 */
	@Override
	public void weave(IRenderingContext context)
	{
		GL2 gl = context.gl();
		veil.bind(gl);
//		gl.glPushAttrib( COLOR )
//		gl.glDisable(GL.GL_DEPTH_TEST);
	}
	
	/**
	 * Unbinds veil's buffer and restores normal rendering.
	 * @param gl
	 */
	@Override
	public void tear(IRenderingContext context)
	{
		GL2 gl = context.gl();
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
	protected void renderTexture(GL gl1)
	{
		GL2 gl = gl1.getGL2();
		double lower[] = new double[4];// wx, wy, wz;// returned xyz coords
		double higher[] = new double[4];// wx, wy, wz;// returned xyz coords
		
		int viewport[] = new int[4];
		double mvmatrix[] = new double[16];
		double projmatrix[] = new double[16];

		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
		gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);
		// 
		glu.gluUnProject(viewport[0], viewport[1], 0.0, mvmatrix, 0, projmatrix, 0, viewport, 0, lower, 0);
		glu.gluUnProject(viewport[2], viewport[3], 0.0, mvmatrix, 0, projmatrix, 0, viewport, 0, higher, 0);
		
		getFBO().bindTexture( gl );
//		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glColor4f( 0,0,0,1 );
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)lower[0],  (float)lower[1]);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)higher[0], (float)lower[1]);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)higher[0], (float)higher[1]);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)lower[0],  (float)higher[1]);
		gl.glEnd();

		getFBO().unbindTexture( gl );
		gl.glEnable( GL.GL_BLEND );
	}
	

	@Override
	public void destroy(IRenderingContext context)
	{
		GL2 gl = context.gl();
		
		if(veil == null)
			throw new IllegalStateException("Cannot destroy not initiated veil.");
		
		veil.destroy( gl );
		veil = null;
	}

}
