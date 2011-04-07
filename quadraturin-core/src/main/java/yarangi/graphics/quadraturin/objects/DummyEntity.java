package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.spatial.AABB;

/**
 * Dummy entity for torturing needs. 
 */
public class DummyEntity extends CompositeSceneEntity
{

	private static final long serialVersionUID = 5263046347119077749L;
	
	public static Look <SceneEntity> DUMMY_LOOK = new Look <SceneEntity> () { 
		public void render(GL gl, double time, SceneEntity entity, RenderingContext context) {}
		public void init(GL gl, SceneEntity entity) { } 
		public void destroy(GL gl, SceneEntity entity) { } 
	};
	
	public static Behavior <SceneEntity> DUMMY_BEHAVIOR = new Behavior <SceneEntity> ()  {	
		public boolean behave(double time, SceneEntity entity, boolean isVisible) { return false; } 
	};
	
	public DummyEntity(AABB aabb) 
	{ 
		super(aabb);
		setLook(DUMMY_LOOK);
		setBehavior(DUMMY_BEHAVIOR);
	}

	@Override
	public boolean isPickable() {
		return false;
	}
	
}
