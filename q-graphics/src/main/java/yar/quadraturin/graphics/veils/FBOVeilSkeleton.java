package yar.quadraturin.graphics.veils;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.graphics.textures.FBO;
import yar.quadraturin.objects.ILook;
import yar.quadraturin.plugin.IGraphicsPlugin;
import yarangi.math.IVector2D;

/**
 * Allows rendering into separate buffer, to use as overlaying texture for post-processing effects.
 * The {@link ILook} may explicitly invoke {@link #weave(GL)} and {@link #tear(GL)}
 * methods to render into the veil.
 * 
 * @author dveyarangi
 */
public abstract class FBOVeilSkeleton implements IVeil, IGraphicsPlugin 
{
	/**
	 * Veil buffer for rendering
	 */
	private FBO veil;
	
	private int width, height;
	
	private final boolean isInited = false;
	
	@Override
	public void init(IRenderingContext context) {
		GL2 gl = context.gl();
		this.width = context.getViewPort().getWidth();
		this.height = context.getViewPort().getHeight();
		int textureWidth = width;//BitUtils.po2Ceiling(width);
		int textureHeight = height;//BitUtils.po2Ceiling(height);
		veil = FBO.createFBO(gl, textureWidth, textureHeight, true);
		
		if(veil == null)
			throw new IllegalStateException("Failed to create veil frame buffer object.");
	}
	
	@Override
	public void resize(IRenderingContext context) {
		if(veil == null)
			throw new IllegalStateException("Cannot reinit not initiated veil,");
		destroy(context);
		init(context);
	}
	
	public final int getWidth()  { return width; }
	public final int getHeight() { return height; }
	
	@Override
	public void preRender(IRenderingContext context) 
	{ 
		
	}

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
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		// adjusting frame buffer to entity coordinates:
//		super.weave( gl, entity, context );
	}
	
	/**
	 * Unbinds veil's buffer and restores normal rendering.
	 * @param gl
	 */
	@Override
	public void tear(IRenderingContext context)
	{
		GL2 gl = context.gl();
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
		
		// frame buffer extension:
		return new String [] { "GL_ARB_framebuffer_object" };
	}

	/**
	 * Render veil full-screen texture
	 * @param gl
	 */
	protected void renderTexture(GL gl1, IVector2D minCoord, IVector2D maxCoord)
	{
		GL2 gl = gl1.getGL2();

		gl.glPushAttrib( GL2.GL_ENABLE_BIT );
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glEnable( GL.GL_BLEND );
		
		getFBO().bindTexture( gl );
//		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glColor4f( 0,0,0,1 );
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float) minCoord.x(), (float)minCoord.y());
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float) maxCoord.x(), (float)minCoord.y());
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float) maxCoord.x(), (float)maxCoord.y());
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float) minCoord.x(), (float)maxCoord.y());
		gl.glEnd();

		getFBO().unbindTexture( gl );
/*		gl.glBegin(GL.GL_LINE_STRIP);
		 gl.glVertex2f((float)lower[0] + 5,  (float)lower[1] - 5);
		 gl.glVertex2f((float)higher[0] - 5, (float)lower[1] - 5);
		 gl.glVertex2f((float)higher[0] - 5, (float)higher[1] + 5);
		 gl.glVertex2f((float)lower[0] - 5,  (float)higher[1] + 5);
		 gl.glVertex2f((float)lower[0] - 5,  (float)lower[1] - 5);
		gl.glEnd();*/
	
		gl.glPopAttrib();
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
