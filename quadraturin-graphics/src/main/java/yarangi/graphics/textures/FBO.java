package yarangi.graphics.textures;

import javax.media.opengl.GL;

/**
 * Encloses frame buffer object handles and provides binding method.
 * 
 * @author FimaR
 */
public class FBO
{
	private int textureId;
	private int depthBufferId;
	private int fboId;
	
	
	protected FBO(int textureId, int depthBufferId, int fboId)
	{
		super();
		this.textureId = textureId;
		this.depthBufferId = depthBufferId;
		this.fboId = fboId;
	}
	
	final protected int getTextureId() { return textureId; }
	final protected void setTextureId(int textureId) { this.textureId = textureId; }
	final protected int getDepthBufferId() { return depthBufferId; }
	final protected void setDepthBufferId(int depthBufferId) { this.depthBufferId = depthBufferId; }
	final protected int getFboId() { return fboId; }
	final protected void setFboId(int fboId) { this.fboId = fboId; }
	
	final public void bindTexture(GL gl)
	{
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
	}
	final public void unbindTexture(GL gl)
	{
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
	}
	
	final public void bind(GL gl)
	{
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fboId);
	}
	
	final public void unbind(GL gl)
	{
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
	}

}

