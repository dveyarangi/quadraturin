package yarangi.graphics.veils;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
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
public abstract class VeilPluginSkeleton implements IGraphicsPlugin 
{
	/**
	 * Veil buffer for rendering
	 */
	private FBO veil;

	@Override
	public void init(GL gl, IRenderingContext context) {
		veil = TextureUtils.createFBO(gl, context.getViewPoint().getPortWidth(), context.getViewPoint().getPortHeight(), true);
	}
	
	@Override
	public void preRender(GL gl, IRenderingContext context)
	{
		// just clearing the frame buffer texture:
		veil.bindTexture(gl);
		gl.glClearColor(0,0,0,0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		veil.unbindTexture(gl);
	}

	@Override
	public void postRender(GL gl, IRenderingContext context) { }

	@Override
	public abstract String[] getRequiredExtensions();

	/**
	 * Binds veil's buffer; after invocation all GL rendering will go to this buffer 
	 * @param gl
	 */
	public void weave(GL gl)
	{
		veil.bind(gl);
	}
	
	/**
	 * Unbinds veil's buffer and restores normal rendering.
	 * @param gl
	 */
	public void tear(GL gl)
	{
		veil.unbind(gl);
	}
}
