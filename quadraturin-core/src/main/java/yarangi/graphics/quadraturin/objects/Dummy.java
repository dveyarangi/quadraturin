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
	
	public static ILook <Entity> LOOK = LOOK();
	
	public static IBehavior <Entity> BEHAVIOR = new DummyBehavior <Entity> ();

	public static <E> ILook <E> LOOK()
	{
		return new ILook <E> () { 
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
