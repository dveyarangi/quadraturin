package yarangi.graphics.textures;

import java.nio.IntBuffer;

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
	
	public static FBO createFBO(GL gl, int width, int height, boolean useDepthBuffer) 
	{
		return createFBO(gl, TextureUtils.createEmptyTexture2D(gl, width, height, false), 
										  useDepthBuffer ? createFBODepthBuffer(gl) : TextureUtils.ILLEGAL_ID);
	}
	
	/**
	 * http://oss.sgi.com/projects/ogl-sample/registry/EXT/framebuffer_object.txt
	 * @param gl
	 * @param textureId TextureUtils.ILLEGAL_ID if none
	 * @param depthId TextureUtils.ILLEGAL_ID if none
	 * @return
	 */
	public static FBO createFBO(GL gl, int textureId, int depthId)
	{
		IntBuffer handleBuffer = IntBuffer.allocate(1);
		gl.glGenFramebuffersEXT(1, handleBuffer); // Generate one frame buffer and store the ID in fbo  
		
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, handleBuffer.get(0)); // Bind our frame buffer
		
		if(textureId != TextureUtils.ILLEGAL_ID)
		{
			gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
			gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_2D, textureId, 0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		}
		
		if(depthId != TextureUtils.ILLEGAL_ID)
			gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, depthId); // Attach the depth buffer fbo_depth to our frame buffer
//		…  
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0); // Unbind our frame buffer
		int status = gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT);
		
		if(handleBuffer.get(0) < 0)
			return null;
		
		return new FBO(textureId, depthId, handleBuffer.get(0));
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
	
	final public void destroy(GL gl)
	{
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, getFboId());
		gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, 0); 
		gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_2D, 0, 0);
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		gl.glDeleteFramebuffersEXT(1, new int [] {getFboId()}, 0);
			
		if(getTextureId() != TextureUtils.ILLEGAL_ID)
			gl.glDeleteTextures(1, new int [] {getTextureId()}, 0);
		if(getDepthBufferId() != TextureUtils.ILLEGAL_ID)
			gl.glDeleteRenderbuffersEXT(1, new int [] { getDepthBufferId() }, 0);
	}

	
	public static int createFBODepthBuffer(GL gl)
	{
		IntBuffer handleBuffer = IntBuffer.allocate(1);
		gl.glGenRenderbuffersEXT(1, handleBuffer);   
		gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, handleBuffer.get(0)); // Bind the fbo_depth render buffer  
//		...  
		gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, 0); // Unbind the render buffer
		
		return handleBuffer.get(0);
	}
	
}

