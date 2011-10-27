package yarangi.graphics.textures;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import yarangi.math.BitUtils;

/**
 * Texture and frame buffer utilities
 * @author FimaR
 *
 */
public class TextureUtils 
{
	public static final int ILLEGAL_ID = -1;
	
	public static int createEmptyTexture2D(GL gl, int width, int height, boolean mipmap)
	{
		// texture handle buffer
		IntBuffer textureHandleBuffer = IntBuffer.allocate(1);
		
		// data buffer
		ByteBuffer colorBits = ByteBuffer.allocate( width * height * 4 );
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		// creating texture:
		gl.glGenTextures(1,textureHandleBuffer);
		int textureHandle = textureHandleBuffer.get(0);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandle);
		
		// filtering properties
		// TODO: move to configuration?
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, mipmap ? GL.GL_LINEAR_MIPMAP_LINEAR : GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
//		GL_GENERATE_MIPMAP_EXT
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP_EXT, GL.GL_FALSE);
//		gl.glColorMask(true, true, true, true);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP, mipmap ? GL.GL_TRUE : GL.GL_FALSE);
		// associating the data array with the texture:
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, 
				GL.GL_UNSIGNED_BYTE, colorBits);
//		new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, 0, width, height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, colorBits);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		return textureHandle;
	}
	
	public static int createBitmapTexture2D(GL gl, int width, int height, byte [] pixels, boolean mipmap)
	{
		// texture handle buffer
		IntBuffer textureHandleBuffer = IntBuffer.allocate(1);
		
		// data buffer
		ByteBuffer colorBits = ByteBuffer.wrap( pixels );
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		// creating texture:
		gl.glGenTextures(1,textureHandleBuffer);
		int textureHandle = textureHandleBuffer.get(0);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandle);
		
		// filtering properties
		// TODO: move to configuration?
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, mipmap ? GL.GL_LINEAR_MIPMAP_LINEAR : GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
//		GL_GENERATE_MIPMAP_EXT
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP_EXT, GL.GL_FALSE);
//		gl.glColorMask(true, true, true, true);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP, mipmap ? GL.GL_TRUE : GL.GL_FALSE);
		// associating the data array with the texture:
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, 
				GL.GL_UNSIGNED_BYTE, colorBits);
//		new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, 0, width, height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, colorBits);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		return textureHandle;
	}
	
	public static int createFBOTexture2D(GL gl, int width, int height)
	{
		IntBuffer textureHandleBuffer = IntBuffer.allocate(1);
		
	
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		// creating texture:
		gl.glGenTextures(1,textureHandleBuffer);
		
		  
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureHandleBuffer.get(0));
		  
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);  
		  
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);  
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);  
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);  
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);  
		  
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		
		return textureHandleBuffer.get(0);
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
	
	public static FBO createFBO(GL gl, int width, int height, boolean useDepthBuffer) 
	{
		return TextureUtils.createFBO(gl, TextureUtils.createEmptyTexture2D(gl, width, height, false), 
										  useDepthBuffer ? TextureUtils.createFBODepthBuffer(gl) : ILLEGAL_ID);
	}
	
	public static FBO createFBO(GL gl, int textureId, int depthId)
	{
		IntBuffer handleBuffer = IntBuffer.allocate(1);
		gl.glGenFramebuffersEXT(1, handleBuffer); // Generate one frame buffer and store the ID in fbo  
		
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, handleBuffer.get(0)); // Bind our frame buffer
		
		if(textureId != ILLEGAL_ID)
		{
			gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
			gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_2D, textureId, 0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		}
		
		if(depthId != ILLEGAL_ID)
			gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, depthId); // Attach the depth buffer fbo_depth to our frame buffer
//		…  
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0); // Unbind our frame buffer
		int status = gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT);
		
		return new FBO(textureId, depthId, handleBuffer.get(0));
	}
	
	public static void destroyFBO(GL gl, FBO fbo)
	{
		if(fbo.getTextureId() != ILLEGAL_ID)
			gl.glDeleteTextures(1, new int [] {fbo.getTextureId()}, 0);
		if(fbo.getDepthBufferId() != ILLEGAL_ID)
			gl.glDeleteRenderbuffersEXT(1, new int [] { fbo.getDepthBufferId() }, 0);
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		gl.glDeleteFramebuffersEXT(1, new int [] {fbo.getFboId()}, 0);
	}

	public static void destroyTexture(GL gl, int textureId)
	{
		if(textureId != ILLEGAL_ID)
			gl.glDeleteTextures(1, new int [] {textureId}, 0);
	}
}
