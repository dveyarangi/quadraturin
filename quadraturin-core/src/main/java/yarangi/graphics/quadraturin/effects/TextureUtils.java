package yarangi.graphics.quadraturin.effects;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

public class TextureUtils 
{
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
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, mipmap ? GL.GL_LINEAR_MIPMAP_LINEAR : GL.GL_LINEAR);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);

//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP, GL.GL_TRUE);
		// associating the data array with the texture:
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, 
				GL.GL_UNSIGNED_BYTE, colorBits);
//		new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, 0, width, height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, colorBits);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		return textureHandle;
	}
}
