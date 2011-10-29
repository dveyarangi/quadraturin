package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.IVeilEntity;

public interface IVeil 
{
	public static final IVeil ORIENTING = new OrientingVeil();
	public void weave(GL gl, IVeilEntity entity, IRenderingContext context);
	public void tear(GL gl);

}
