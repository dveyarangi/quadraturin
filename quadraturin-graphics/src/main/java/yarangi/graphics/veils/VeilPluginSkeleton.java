package yarangi.graphics.veils;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.OrientingVeil;
import yarangi.graphics.quadraturin.objects.IVeilEntity;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;
import yarangi.graphics.textures.FBO;
import yarangi.graphics.textures.TextureUtils;

/**
 * Allows rendering into separate frame buffer, for post-processing effects.
 * A {@link Look} have to explicitly invoke {@link #weave(GL)} and {@link #tear(GL)}
 * methods to render into the veil.
 * 
 * @author dveyarangi
 */
public abstract class VeilPluginSkeleton extends OrientingVeil implements IGraphicsPlugin 
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
		veil = TextureUtils.createFBO(gl, width, height, true);
		
		isInited = true;
	}
	
	public void reinit(GL gl, IRenderingContext context) {
		if(!isInited)
			return;
		destroy(gl);
		init(gl, context);
	}
	
	public final int getWidth()  { return width; }
	public final int getHeight() { return height; }
	
	@Override
	public void preRender(GL gl, IRenderingContext context)
	{
		// just clearing the frame buffer texture:
/*		veil.bind(gl);
		gl.glClearColor(0,0,0,0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		veil.unbind(gl);*/
	}

	@Override
	public void postRender(GL gl, IRenderingContext context) { }

	/**
	 * Binds veil's buffer; after invocation all GL rendering will go to this buffer 
	 * @param gl
	 */
	public void weave(GL gl, IVeilEntity entity, IRenderingContext context)
	{
		veil.bind(gl);
		
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
	
		/* note viewport[3] is height of window in pixels */
		glu.gluUnProject(viewport[0], viewport[1], 0, mvmatrix, 0, projmatrix, 0, viewport, 0, lower, 0);
		glu.gluUnProject(viewport[2],viewport[3], 0.0, mvmatrix, 0, projmatrix, 0, viewport, 0, higher, 0);
		
		getFBO().bindTexture( gl );
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)lower[0],  (float)lower[1]);	// Bottom Left Of The Texture and Quad
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)higher[0], (float)lower[1]);	// Bottom Right Of The Texture and Quad
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)higher[0], (float)higher[1]);	// Top Right Of The Texture and Quad
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)lower[0],  (float)higher[1]);
		gl.glEnd();
		gl.glColor4f( 1,1,1,1 );

		getFBO().unbindTexture( gl );
	}
	

	@Override
	public void destroy(GL gl)
	{
		if(!isInited)
			return;
		TextureUtils.destroyFBO( gl, veil );
	}
	
	@Override
	public boolean isAvailable() { return isInited; }

}
