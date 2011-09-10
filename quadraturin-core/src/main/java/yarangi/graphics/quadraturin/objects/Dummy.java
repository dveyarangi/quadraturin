package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.objects.behaviors.DummyBehavior;

/**
 * Dummy entity for torturing needs. 
 */
public class Dummy extends Entity
{

	private static final long serialVersionUID = 5263046347119077749L;
	
	public static Look <Entity> LOOK = new Look <Entity> () { 
		public void render(GL gl, double time, Entity entity, RenderingContext context) {}
		public void init(GL gl, Entity entity) { } 
		public void destroy(GL gl, Entity entity) { }
		@Override
		public boolean isCastsShadow() { return false; }
		@Override
		public float getPriority() { return 0; } 
	};
	
	public static Behavior <Entity> BEHAVIOR = new DummyBehavior <Entity> ();

}
