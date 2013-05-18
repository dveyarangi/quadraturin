package yar.quadraturin.ui;

import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.graphics.colors.Color;
import yar.quadraturin.objects.ILook;
import yarangi.spatial.AABB;

/**
 * 
 * Just a rectangle.
 * 
 */
public class ImagePanelLook implements ILook <Overlay>
{
	
	private final String imageFile;
	
	private Texture texture = null;
	
	public ImagePanelLook(String imageFile) 
	{
		this.imageFile = imageFile;

	}

	@Override
	public void init(IRenderingContext context)
	{
		
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream( imageFile );
		
		try
		{
			texture = TextureIO.newTexture( stream, true, "jpg" );
		} 
		catch ( GLException | IOException e ) {	e.printStackTrace(); }
	}

	@Override
	public void render(Overlay entity, IRenderingContext ctx)
	{
		
		GL2 gl = ctx.gl();
//		ctx.setDefaultBlendMode( gl );
		gl.glPushAttrib( GL.GL_COLOR_BUFFER_BIT |  GL2.GL_ENABLE_BIT);
		gl.glDisable( GL.GL_BLEND );
		texture.bind(gl);

		final AABB area = entity.getArea();
		gl.glBegin( GL2.GL_QUADS );
		gl.glTexCoord2d(1, 0);
		gl.glVertex2d(  area.getRX(),  area.getRY() );
		gl.glTexCoord2d(1, 1);
		gl.glVertex2d(  area.getRX(), -area.getRY() );
		gl.glTexCoord2d(0, 1);
		gl.glVertex2d( -area.getRX(), -area.getRY() );
		gl.glTexCoord2d(0, 0);
		gl.glVertex2d( -area.getRX(),  area.getRY() );
		gl.glEnd();
		gl.glPopAttrib();
//		ctx.setDefaultBlendMode( gl );
	}

	@Override
	public void destroy(IRenderingContext context)
	{
		texture.destroy( context.gl() );
	}

	@Override
	public float getPriority()
	{
		return -1;
	}

	@Override
	public boolean isCastsShadow(){	return false;}

	@Override
	public IVeil getVeil() { return null; }

	@Override
	public boolean isOriented()
	{
		return true;
	}
	
}
