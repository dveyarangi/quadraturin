package yar.quadraturin.objects;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.objects.behaviors.DummyBehavior;

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
			@Override
			public void render( E entity, IRenderingContext context) {}
			@Override
			public void init(IRenderingContext context) { } 
			@Override
			public void destroy(IRenderingContext context) { }
			@Override
			public boolean isCastsShadow() { return false; }
			@Override
			public float getPriority() { return 0; }
			@Override
			public IVeil getVeil() { return null; }
			@Override
			public boolean isOriented()	{ return false;	}
		};
	}
}
