package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.spatial.Area;

/**
 * Wrapper for entities that do not fit to extend {@link Entity}.
 * Aggregates Entity aspects to work with custom properties object ("essence") 
 * TODO: probably all entities should be replaced by shells
 * 
 * @author dveyarangi
 *
 * @param <E> essence type
 */
public class EntityShell <E> extends Entity
{
	/**
	 * Properties object that will be referenced by look and behavior aspects
	 */
	private E essence;
	
	public EntityShell(E essense, Behavior <? extends E> behavior, Look <? extends E> look)
	{
		setEssence( essense );
		setLook( look );
		setBehavior( behavior );
	}
	
	public void setEssence(E essence)
	{
		this.essence = essence;
	}
	
	public E getEssence()
	{
		return essence;
	}
	
	public void init(GL gl, IRenderingContext context)
	{
		getLook().init( gl, essence, context );
	}
	
	public void render(GL gl, double time, IRenderingContext context)
	{
		// rendering this entity:
		getLook().render(gl, time, essence, context);
	}
	
	public void destroy(GL gl, IRenderingContext context)
	{
		getLook().destroy( gl, essence, context );
	}

	@Override
	public boolean behave(double time, boolean b)
	{
		return getBehavior().behave( time, essence, b );
	}

}
