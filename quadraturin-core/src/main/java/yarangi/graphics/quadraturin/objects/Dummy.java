package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.behaviors.DummyBehavior;

/**
 * Dummy entity for torturing needs. 
 */
public class Dummy extends Entity
{
	
	public static Look <Entity> LOOK = LOOK();
	
	public static Behavior <Entity> BEHAVIOR = new DummyBehavior <Entity> ();

	public static <E> Look <E> LOOK()
	{
		return new Look <E> () { 
			public void render(GL gl, double time, E entity, IRenderingContext context) {}
			public void init(GL gl, E entity, IRenderingContext context) { } 
			public void destroy(GL gl, E entity, IRenderingContext context) { }
			@Override
			public boolean isCastsShadow() { return false; }
			@Override
			public float getPriority() { return 0; }
			@Override
			public IVeil getVeil() { return IVeil.ORIENTING; }
		};
	}
}
