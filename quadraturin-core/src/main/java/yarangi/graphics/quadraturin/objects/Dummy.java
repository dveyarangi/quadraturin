package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.objects.behaviors.DummyBehavior;

/**
 * Dummy entity for torturing needs. 
 */
public class Dummy extends WorldEntity
{

	private static final long serialVersionUID = 5263046347119077749L;
	
	public static Look <WorldEntity> LOOK = new Look <WorldEntity> () { 
		public void render(GL gl, double time, WorldEntity entity, RenderingContext context) {}
		public void init(GL gl, WorldEntity entity) { } 
		public void destroy(GL gl, WorldEntity entity) { }
		@Override
		public boolean isCastsShadow() { return false; } 
	};
	
	public static Behavior <WorldEntity> BEHAVIOR = new DummyBehavior <WorldEntity> ();

}
