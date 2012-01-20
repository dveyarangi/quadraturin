package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.spatial.Area;

public class OrientingVeil implements IVeil
{

	@Override
	public void weave(GL gl, ILayerObject entity, IRenderingContext context)
	{
		Area area = null;
		if(entity != null)
		{
			area = entity.getArea();
		}
		// storing transformation matrix:
		gl.glMatrixMode( GL.GL_MODELVIEW );
		gl.glPushMatrix();
//		gl.glLoadIdentity(); 	
		
		// transforming into entity coordinates:
		if(area == null)
			gl.glTranslatef(0, 0, 0); // just adjusting priority
		else
		{
			float priority = -entity.getLook().getPriority();
			gl.glTranslatef((float)area.getRefPoint().x(), (float)area.getRefPoint().y(), priority);
			gl.glRotatef((float)area.getOrientation(), 0, 0, 1 );
		}
	}

	@Override
	public void tear(GL gl)
	{
		gl.glMatrixMode( GL.GL_MODELVIEW );
		gl.glPopMatrix();
	}

}
