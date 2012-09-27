package yarangi.graphics.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILook;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

public class TextureLook implements ILook <IEntity>
{

//		private IsoheightVeil veil;
//	private IVeil veil;
	
	private static Texture texture;
	
	private final String textureFile;
	
	public TextureLook (String textureFile)
	{
		this.textureFile = textureFile;
	}
	
	@Override
	public void init(GL gl, IRenderingContext context) {
		
		if(texture != null)
			return;
		BufferedImage image;
		try
		{
//				image = ImageIO.read(getClass().getResourceAsStream("/textures/red_gradient.jpg"));
			image = ImageIO.read(getClass().getResourceAsStream(textureFile));
//				image = ImageIO.read(getClass().getResourceAsStream("/textures/hairy_gradient.png"));
		} catch ( IOException e )
		{
			throw new IllegalArgumentException("file not found");
		}
		texture = TextureIO.newTexture(new TextureData(GL.GL_RGBA, GL.GL_RGBA, false, image));
		texture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		
		texture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);	
//			texture.bind();
//			gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
//			gl.glBindTexture( GL.GL_TEXTURE_2D, 0 );
//			veil = context.getPlugin( BlurVeil.NAME );
//			veil = context.getPlugin( BlurVeil.NAME );

/*		veil = context.<IVeil>getPlugin( IsoheightVeil.NAME );
		if(veil == null)
		{
			Q.rendering.warn( "Plugin [" + IsoheightVeil.NAME + "] requested by look [" + this.getClass() + "] is not available."  );
			veil = null;
		}*/
	}

	@Override
	public void render(GL gl, IEntity entity, IRenderingContext context) {
		
		gl.glPushAttrib( GL.GL_COLOR_BUFFER_BIT );
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
		gl.glBlendEquation(GL.GL_FUNC_ADD);
		
		float radius = (float)(entity.getArea().getMaxRadius()); //* Math.sqrt( entity.getArea().getAnchor().abs())/2);
		texture.bind();
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		
		
		gl.glBegin(GL.GL_QUADS);
		gl.glColor4f( 1,1,1,1 );
		gl.glTexCoord2f( 0.0f, 0.0f ); gl.glVertex2f(-radius, -radius);
		gl.glTexCoord2f( 0.0f, 1.0f ); gl.glVertex2f(-radius,  radius);
		gl.glTexCoord2f( 1.0f, 1.0f ); gl.glVertex2f( radius,  radius);
		gl.glTexCoord2f( 1.0f, 0.0f ); gl.glVertex2f( radius, -radius);
		gl.glEnd();
		
		gl.glBindTexture( GL.GL_TEXTURE_2D, 0 );
		gl.glPopAttrib();
		context.setDefaultBlendMode( gl );
	}

	@Override
	public void destroy(GL gl, IRenderingContext context) {
		texture.dispose();
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public boolean isCastsShadow() {
		return false;
	}
	@Override
	public IVeil getVeil() { return null; }

	@Override
	public boolean isOriented() { return true; }

}
