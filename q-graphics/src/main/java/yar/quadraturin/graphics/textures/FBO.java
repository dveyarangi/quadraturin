package yar.quadraturin.graphics.textures;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.Q;

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
										  useDepthBuffer ? createFBODepthBuffer(gl, width, height) : TextureUtils.ILLEGAL_ID);
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
		
		gl.glGenFramebuffers(1, handleBuffer); // Generate one frame buffer and store the ID in fbo  
		
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, handleBuffer.get(0)); // Bind our frame buffer
		
		if(textureId != TextureUtils.ILLEGAL_ID)
		{
//			gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
//			System.out.println(textureId);
			gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, textureId, 0);
//			gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		}
		if(depthId != TextureUtils.ILLEGAL_ID)
			gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depthId); // Attach the depth buffer fbo_depth to our frame buffer
//		
		
		int status = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);

		if(status != GL.GL_FRAMEBUFFER_COMPLETE)
			Q.rendering.warn( "Failed to create framebuffer; status code " + status);
//		else
//			Q.rendering.trace( "Create framebuffer; status code " + status);

		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0); // Unbind our frame buffer
		
		if(handleBuffer.get(0) < 0)
			return null;
		
		return new FBO(textureId, depthId, handleBuffer.get(0));
	}

	
	final protected int getTextureId() { return textureId; }
	final protected void setTextureId(int textureId) { this.textureId = textureId; }
	final protected int getDepthBufferId() { return depthBufferId; }
	final protected void setDepthBufferId(int depthBufferId) { this.depthBufferId = depthBufferId; }
	final public int getFboId() { return fboId; }
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
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fboId);
	}
	
	final public void unbind(GL gl)
	{
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
	}
	
	final public void destroy(GL gl)
	{
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getFboId());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, 0); 
		gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, 0, 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		gl.glDeleteFramebuffers(1, new int [] {getFboId()}, 0);
			
		if(getTextureId() != TextureUtils.ILLEGAL_ID)
			gl.glDeleteTextures(1, new int [] {getTextureId()}, 0);
		if(getDepthBufferId() != TextureUtils.ILLEGAL_ID)
			gl.glDeleteRenderbuffers(1, new int [] { getDepthBufferId() }, 0);
	}

	
	public static int createFBODepthBuffer(GL gl, int width, int height)
	{
		IntBuffer handleBuffer = IntBuffer.allocate(1);
		gl.glGenRenderbuffers(1, handleBuffer);   
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, handleBuffer.get(0)); // Bind the fbo_depth render buffer  
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL2.GL_DEPTH_COMPONENT, width, height);//		...  
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0); // Unbind the render buffer
		
		return handleBuffer.get(0);
	}
	
}

