package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.spatial.Area;

public class OrientingVeil implements IVeil
{

	@Override
	public void weave(GL gl, ILayerObject entity, IRenderingContext context)
	{
		Area area = entity.getArea();
		
		// storing transformation matrix:
		gl.glPushMatrix();
		
		float priority = -entity.getLook().getPriority();
		// transforming into entity coordinates:
		if(area == null)
			gl.glTranslatef(0, 0, priority); // just adjusting priority
		else
		{
			gl.glTranslatef((float)area.getRefPoint().x(), (float)area.getRefPoint().y(), priority);
			gl.glRotatef((float)area.getOrientation(), 0, 0, 1 );
		}
	}

	@Override
	public void tear(GL gl)
	{
		gl.glPopMatrix();
	}

}
