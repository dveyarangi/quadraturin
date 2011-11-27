package yarangi.graphics.quadraturin.ui;

import javax.media.opengl.GL;

import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.spatial.AABB;

public class PanelLook implements Look <Overlay>
{
	
	private Color color;
	
	public PanelLook(Color color) 
	{
		this.color = color;

	}

	@Override
	public void init(GL gl, Overlay entity, IRenderingContext context)
	{
		if(!(entity.getArea() instanceof AABB))
			throw new IllegalArgumentException(this.getClass() + " look supports only " + AABB.class + " area.");
	}

	@Override
	public void render(GL gl, double time, Overlay entity, IRenderingContext context)
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
	}

	@Override
	public void destroy(GL gl, Overlay entity, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getPriority()
	{
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public boolean isCastsShadow(){	return false;}

	@Override
	public IVeil getVeil() { return IVeil.ORIENTING; }
	
}
