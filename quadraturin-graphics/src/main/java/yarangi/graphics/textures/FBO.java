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
	
	protected int getTextureId() { return textureId; }
	protected void setTextureId(int textureId) { this.textureId = textureId; }
	protected int getDepthBufferId() { return depthBufferId; }
	protected void setDepthBufferId(int depthBufferId) { this.depthBufferId = depthBufferId; }
	protected int getFboId() { return fboId; }
	protected void setFboId(int fboId) { this.fboId = fboId; }
	
	public void bindTexture(GL gl)
	{
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
	}
	public void unbindTexture(GL gl)
	{
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
	}
	
	public void bind(GL gl)
	{
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fboId);
	}
	
	public void unbind(GL gl)
	{
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
	}

}

