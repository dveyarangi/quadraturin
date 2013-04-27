package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;

/**
 * Wrapper for entities that do not fit to extend {@link Entity}.
 * Aggregates Entity aspects to work with custom properties object ("essence") 
 * TODO: probably all entities should be replaced by shells
 * 
 * @author dveyarangi
 *
 * @param <E> essence type
 */
@SuppressWarnings("unchecked")
public class EntityShell <E> extends Entity
{
	/**
	 * Properties object that will be referenced by look and behavior aspects
	 */
	private E essence;
	
	public EntityShell(E essense, IBehavior <? extends E> behavior, ILook <? extends E> look)
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

	@Override
	public void render(IRenderingContext context)
	{
		// rendering this entity:
		getLook().render(essence, context);
	}

	@Override
	public boolean behave(double time, boolean b)
	{
		if(getBehavior() != null)
		return getBehavior().behave( time, essence, b );
		
		return false;
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append( "entity shell [" ).append(essence).append("]")
			.toString();
	}
}
