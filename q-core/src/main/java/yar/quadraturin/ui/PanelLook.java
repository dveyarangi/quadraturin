package yar.quadraturin.ui;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

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
public class PanelLook implements ILook <Overlay>
{
	
	private final Color color;
	
	public PanelLook(Color color) 
	{
		this.color = color;

	}

	@Override
	public void init(IRenderingContext context)
	{
//		if(!(entity.getArea() instanceof AABB))
//			throw new IllegalArgumentException(this.getClass() + " look supports only " + AABB.class + " area.");
	}

	@Override
	public void render(Overlay entity, IRenderingContext ctx)
	{
		GL2 gl = ctx.gl();
		gl.glPushAttrib( GL.GL_COLOR_BUFFER_BIT |  GL2.GL_ENABLE_BIT);
		gl.glEnable( GL.GL_BLEND );
//		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
//		gl.glBlendEquation(GL.GL_FUNC_ADD);
		color.apply( gl );
		final AABB area = entity.getArea();
		gl.glBegin( GL2.GL_QUADS );
		gl.glVertex2d(  area.getRX(),  area.getRY() );
		gl.glVertex2d(  area.getRX(), -area.getRY() );
		gl.glVertex2d( -area.getRX(), -area.getRY() );
		gl.glVertex2d( -area.getRX(),  area.getRY() );
		gl.glEnd();
		gl.glPopAttrib();
//		ctx.setDefaultBlendMode( gl );
	}

	@Override
	public void destroy(IRenderingContext context){}

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
