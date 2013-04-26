package yarangi.graphics.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILook;


public class TextureLook implements ILook <IEntity>
{

//		private IsoheightVeil veil;
	private IVeil veil;
	
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
		texture = AWTTextureIO.newTexture( GLProfile.getGL2ES2(), image, true);
		texture.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		
		texture.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);	
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
	public void render(GL gl1, IEntity entity, IRenderingContext context) {
		
		GL2 gl = gl1.getGL2();
		gl.glPushAttrib( GL.GL_COLOR_BUFFER_BIT | GL2.GL_ENABLE_BIT);
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
		gl.glBlendEquation(GL.GL_FUNC_ADD);
		
		float radius = (float)(entity.getArea().getMaxRadius()); //* Math.sqrt( entity.getArea().getAnchor().abs())/2);
		texture.bind(gl);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		
		gl.glEnable(GL.GL_BLEND);
		
		gl.glBegin(GL2.GL_QUADS);
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
		texture.destroy( gl );
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
	public IVeil getVeil() { return veil; }

	@Override
	public boolean isOriented() { return true; }

}
