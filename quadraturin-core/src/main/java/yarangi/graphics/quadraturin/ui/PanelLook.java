package yarangi.graphics.quadraturin.ui;

import javax.media.opengl.GL;

import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.spatial.AABB;

public class PanelLook implements ILook <Overlay>
{
	
	private final Color color;
	
	public PanelLook(Color color) 
	{
		this.color = color;

	}

	@Override
	public void init(GL gl, IRenderingContext context)
	{
//		if(!(entity.getArea() instanceof AABB))
//			throw new IllegalArgumentException(this.getClass() + " look supports only " + AABB.class + " area.");
	}

	@Override
	public void render(GL gl, Overlay entity, IRenderingContext context)
	{
		gl.glPushAttrib( GL.GL_COLOR_BUFFER_BIT );
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBlendEquation(GL.GL_FUNC_ADD);
		color.apply( gl );
		final AABB area = (AABB)entity.getArea();
		gl.glBegin( GL.GL_QUADS );
			gl.glVertex2d( -area.getRX(), -area.getRY() );
			gl.glVertex2d( area.getRX(), -area.getRY() );
			gl.glVertex2d( area.getRX(), area.getRY() );
			gl.glVertex2d( -area.getRX(), area.getRY() );
		gl.glEnd();
		gl.glPopAttrib();
		context.setDefaultBlendMode( gl );
	}

	@Override
	public void destroy(GL gl, IRenderingContext context){}

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
		return false;
	}
	
}
