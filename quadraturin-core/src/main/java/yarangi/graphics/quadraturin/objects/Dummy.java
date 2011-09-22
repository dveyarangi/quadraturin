package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.behaviors.DummyBehavior;

/**
 * Dummy entity for torturing needs. 
 */
public class Dummy extends Entity
{
	
	public static Look <Entity> LOOK = new Look <Entity> () { 
		public void render(GL gl, double time, Entity entity, IRenderingContext context) {}
		public void init(GL gl, Entity entity, IRenderingContext context) { } 
		public void destroy(GL gl, Entity entity, IRenderingContext context) { }
		@Override
		public boolean isCastsShadow() { return false; }
		@Override
		public float getPriority() { return 0; } 
	};
	
	public static Behavior <Entity> BEHAVIOR = new DummyBehavior <Entity> ();

}
