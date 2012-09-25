package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.spatial.Area;

public interface IVisible
{


	public abstract ILook getLook();

	public abstract Area getArea();

	public void init(GL gl, IRenderingContext context);
	public void render(GL gl, IRenderingContext context);
	public void destroy(GL gl, IRenderingContext context);
}
