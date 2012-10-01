package yarangi.graphics.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class TextureHandle 
{
	
	private final String filename;
	
	private Texture texture;

	public TextureHandle(String filename)
	{
		this.filename = filename;
	}
	
	public final Texture getTexture()
	{
		return texture;
	}

	public void load(GL gl)
	{
		
		BufferedImage image;
		try
		{
			image = ImageIO.read(getClass().getResourceAsStream(filename));
		} catch ( IOException e )
		{
			throw new IllegalArgumentException("file not found");
		}
		texture = AWTTextureIO.newTexture( GLProfile.getGL2ES2(), image, false);
		texture.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		
		texture.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);		
	}

	public void unload(GL gl)
	{
		texture.destroy(gl);
	}
}
