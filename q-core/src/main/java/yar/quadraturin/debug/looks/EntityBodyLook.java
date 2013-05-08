package yar.quadraturin.debug.looks;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.objects.IEntity;
import yar.quadraturin.objects.ILook;
import yarangi.math.Angles;
import yarangi.math.IVector2D;
import yarangi.physics.Body;

public class EntityBodyLook implements ILook <IEntity>
{
	

	@Override
	public void init(IRenderingContext context) { }

	@Override
	public void render(IEntity entity, IRenderingContext ctx)
	{
		GL2 gl = ctx.gl();

		gl.glEnable(GL.GL_BLEND);
		IVector2D ref = entity.getArea().getAnchor();
		Body body = entity.getBody();
		
		gl.glColor4f( 1f, 1f, 0f, 0.2f );
		gl.glBegin( GL.GL_LINE_STRIP );
			gl.glVertex2f((float)(ref.x()), (float)(ref.y()));
			gl.glVertex2f((float)(ref.x()+10*body.getForce().x()), (float)(ref.y()+10*body.getForce().y()));
		gl.glEnd();
		gl.glColor4f( 0f, 1f, 1f, 0.2f );
		gl.glBegin( GL.GL_LINE_STRIP );
			gl.glVertex2f((float)(ref.x()), (float)(ref.y()));
			gl.glVertex2f((float)(ref.x()+10*body.getVelocity().x()), (float)(ref.y()+10*body.getVelocity().y()));
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
