package yar.quadraturin.debug.looks;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.objects.IEntity;
import yar.quadraturin.objects.ILook;
import yarangi.spatial.AABB;

/**
 * Renders entity AABB
 * @author dveyarangi
 *
 */
public class EntityAreaLook implements ILook <IEntity>
{
	

	@Override
	public void init(IRenderingContext context) { }

	@Override
	public void render(IEntity entity, IRenderingContext ctx)
	{
		GL2 gl = ctx.gl();
		gl.glEnable(GL.GL_BLEND);
		gl.glColor4f( 0f, 1f, 0f, 0.2f );
		if(entity.getArea() instanceof AABB) {
		AABB aabb = entity.getArea();
		float x, y;
		gl.glBegin( GL.GL_LINE_STRIP );
			gl.glVertex2f((float)aabb.getMinX(), (float)aabb.getMinY());
			gl.glVertex2f((float)aabb.getMinX(), (float)aabb.getMaxY());
			gl.glVertex2f((float)aabb.getMaxX(), (float)aabb.getMaxY());
			gl.glVertex2f((float)aabb.getMaxX(), (float)aabb.getMinY());
			gl.glVertex2f((float)aabb.getMinX(), (float)aabb.getMinY());
		gl.glEnd();
		}

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
