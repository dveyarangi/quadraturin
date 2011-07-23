package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.objects.behaviors.DummyBehavior;

/**
 * Dummy entity for torturing needs. 
 */
public class Dummy extends SceneEntity
{

	private static final long serialVersionUID = 5263046347119077749L;
	
	public static Look <SceneEntity> LOOK = new Look <SceneEntity> () { 
		public void render(GL gl, double time, SceneEntity entity, RenderingContext context) {}
		public void init(GL gl, SceneEntity entity) { } 
		public void destroy(GL gl, SceneEntity entity) { }
		@Override
		public boolean isCastsShadow() { return false; } 
	};
	
	public static Behavior <SceneEntity> BEHAVIOR = new DummyBehavior <SceneEntity> ();

}
