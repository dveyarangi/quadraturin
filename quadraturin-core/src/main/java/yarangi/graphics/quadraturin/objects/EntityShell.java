package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.spatial.Area;

/**
 * Wrapper for entities that do not fit to extend {@link Entity}.
 * Aggregates Entity aspects to work with custom properties object ("essence") 
 * 
 * @author dveyarangi
 *
 * @param <E> essence type
 */
public class EntityShell <E> extends Entity
{
	
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
	
	public void render(GL gl, double time, RenderingContext context)
	{
		Area area = getArea();
		
		// storing transformation matrix:
		gl.glPushMatrix();
		
		// transforming into entity coordinates:
		if(area == null)
			gl.glTranslatef(0, 0, -this.getLook().getPriority());
		else
		{
			gl.glTranslatef((float)area.getRefPoint().x(), (float)area.getRefPoint().y(), -getLook().getPriority());
			gl.glRotatef((float)area.getOrientation(), 0, 0, 1 );
		}
		// rendering this entity:
		getLook().render(gl, time, essence, context);
		
		gl.glPopMatrix();

	}

	@Override
	public boolean behave(double time, boolean b)
	{
		return getBehavior().behave( time, essence, b );
	}

}
