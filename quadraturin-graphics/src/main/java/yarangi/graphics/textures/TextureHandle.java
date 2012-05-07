package yarangi.graphics.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

public class TextureHandle 
{
	
	private String filename;
	
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
		texture = TextureIO.newTexture(new TextureData(GL.GL_RGBA, GL.GL_RGBA, false, image));
		texture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		
		texture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);		
	}

	public void unload(GL gl)
	{
		texture.dispose();
	}
}
