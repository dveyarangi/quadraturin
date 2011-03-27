package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

public abstract class InactiveOverlay <T> implements Look <T> 
{

	public abstract void render(GL gl, double time, T entity);


}
