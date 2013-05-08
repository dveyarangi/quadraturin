package yar.quadraturin.graphics.textures;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.spinn3r.log5j.Logger;

/**
 * Texture and frame buffer utilities
 * @author FimaR
 *
 */
public class TextureUtils 
{
	public static final int ILLEGAL_ID = -1;
	
	public static Logger log = Logger.getLogger(TextureUtils.class);
	
	public static int createEmptyTexture2D(GL gl1, int width, int height, boolean mipmap)
	{
		GL2 gl = gl1.getGL2();

		log.trace("Creating texture " + width + "x" + height);
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
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_BLEND);
//		GL_GENERATE_MIPMAP_EXT
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP_EXT, GL.GL_FALSE);
//		gl.glColorMask(true, true, true, true);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, mipmap ? GL.GL_TRUE : GL.GL_FALSE);
		// associating the data array with the texture:
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, 
				GL.GL_UNSIGNED_BYTE, colorBits);
//		new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, 0, width, height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, colorBits);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		return textureHandle;
	}
	
	public static int createBitmapTexture2D(GL gl1, int width, int height, byte [] pixels, boolean mipmap)
	{
		GL2 gl = gl1.getGL2();
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
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
//		GL_GENERATE_MIPMAP_EXT
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP_EXT, GL.GL_FALSE);
//		gl.glColorMask(true, true, true, true);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, mipmap ? GL.GL_TRUE : GL.GL_FALSE);
		// associating the data array with the texture:
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, 
				GL.GL_UNSIGNED_BYTE, colorBits);
//		new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, 0, width, height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, colorBits);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		return textureHandle;
	}
	
	public static void destroyTexture(GL gl, int textureId)
	{
		if(textureId != ILLEGAL_ID)
			gl.glDeleteTextures(1, new int [] {textureId}, 0);
	}
}
