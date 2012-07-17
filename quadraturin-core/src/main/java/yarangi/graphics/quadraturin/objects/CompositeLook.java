package yarangi.graphics.quadraturin.objects;

import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;

/**
 * Defines a chain of rendering overlays over a single entity.
 * {@link ILook}s that are added to chain may return null in {@link #getVeil()}.
 * @author FimaR
 *
 * @param <E>
 */
public abstract class CompositeLook <E extends ILayerObject> implements ILook <E> 
{
	private List <ILook<E>> chain = new LinkedList <ILook<E>> ();
	
	public CompositeLook()
	{
		
	}
	
	public void addLook(ILook <E> look)
	{
		chain.add(look);
	}
	
	public void removeLook(ILook <E> look)
	{
		chain.remove(look);
	}

	@Override
	public void init(GL gl, E entity, IRenderingContext context) {
		for(ILook <E> look : chain)
			look.init(gl, entity, context);
	}

	@Override
	public void render(GL gl, double time, E entity, IRenderingContext context) {
		
		IVeil veil;
		for(ILook <E> look : chain)
		{
			veil = look.getVeil();
			if(veil != null)
				veil.weave( gl, entity, context );
			look.render( gl, time, entity, context );
			if(veil != null)
				veil.tear( gl );
		}
	}

	@Override
	public void destroy(GL gl, E entity, IRenderingContext context) {
		for(ILook <E> look : chain)
			look.destroy(gl, entity, context);
	}

	@Override
	public abstract float getPriority();

	@Override
	public abstract boolean isCastsShadow();

	@Override
	public IVeil getVeil() { return IVeil.ORIENTING; }
	
	
}
