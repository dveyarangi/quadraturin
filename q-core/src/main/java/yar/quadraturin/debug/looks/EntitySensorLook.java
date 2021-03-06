package yar.quadraturin.debug.looks;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.objects.IEntity;
import yar.quadraturin.objects.ILook;
import yarangi.math.Angles;

public class EntitySensorLook implements ILook <IEntity>
{
	

	@Override
	public void init(IRenderingContext context) { }

	@Override
	public void render(IEntity entity, IRenderingContext ctx)
	{
		GL2 gl = ctx.gl();
		gl.glEnable(GL.GL_BLEND);
		gl.glColor4f( 0f, 1f, 0f, 0.2f );
		gl.glBegin( GL.GL_LINE_STRIP );
		float x, y;
		for(double a = 0; a <= Angles.TAU; a += Angles.TRIG_STEP * 20) 
		{
			x = (float)(entity.getArea().getAnchor().x() + entity.getEntitySensor().getRadius() * Angles.COS( a ));
			y = (float)(entity.getArea().getAnchor().y() + entity.getEntitySensor().getRadius() * Angles.SIN( a ));
			gl.glVertex2f( x, y );
		}
		gl.glEnd();
		
		// TODO: print frequency
	}

	@Override
	public void destroy(IRenderingContext context) { }

	@Override
	public float getPriority() { return 0; }

	@Override
	public boolean isCastsShadow() { return false; }

	@Override
	public IVeil getVeil() { return null; }

	@Override
	public boolean isOriented()	{ return false; }

}
