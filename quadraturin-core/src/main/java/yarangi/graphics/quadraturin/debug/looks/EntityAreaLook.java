package yarangi.graphics.quadraturin.debug.looks;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.math.Angles;
import yarangi.spatial.AABB;

public class EntityAreaLook implements ILook <IEntity>
{
	

	@Override
	public void init(GL gl, IRenderingContext context) { }

	@Override
	public void render(GL gl, IEntity entity, IRenderingContext context)
	{
		gl.glEnable(GL.GL_BLEND);
		gl.glColor4f( 0f, 1f, 0f, 0.2f );
		if(entity.getArea() instanceof AABB) {
		AABB aabb = (AABB)entity.getArea();
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
