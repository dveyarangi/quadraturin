package yarangi.graphics.quadraturin.debug.looks;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.math.Angles;

public class EntitySensorLook implements ILook <IEntity>
{
	

	@Override
	public void init(GL gl, IRenderingContext context) { }

	@Override
	public void render(GL gl, IEntity entity, IRenderingContext context)
	{
		gl.glEnable(GL.GL_BLEND);
		gl.glColor4f( 0f, 1f, 0f, 0.2f );
		gl.glBegin( GL.GL_LINE_STRIP );
		float x, y;
		for(double a = 0; a <= Angles.PI_2; a += Angles.TRIG_STEP * 5) 
		{
			x = (float)(entity.getArea().getAnchor().x() + entity.getEntitySensor().getRadius() * Angles.COS( a ));
			y = (float)(entity.getArea().getAnchor().y() + entity.getEntitySensor().getRadius() * Angles.SIN( a ));
			gl.glVertex2f( x, y );
		}
		gl.glEnd();
		
		// TODO: print frequency
	}

	@Override
	public void destroy(GL gl, IRenderingContext context) { }

	@Override
	public float getPriority() { return 0; }

	@Override
	public boolean isCastsShadow() { return false; }

	@Override
	public IVeil getVeil() { return null; }

	@Override
	public boolean isOriented()	{ return false; }

}
