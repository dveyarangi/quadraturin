package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.spatial.AABB;

public abstract class Overlay extends SceneEntity 
{

	protected Overlay(AABB aabb) {
		super(aabb);
		setLook(DummyEntity.DUMMY_LOOK);
		setBehavior(DummyEntity.DUMMY_BEHAVIOR);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void display(GL gl, double time, RenderingContext context)
	{
		AABB aabb = getAABB();
	
		// storing transformation matrix:
		gl.glPushMatrix();
		
		// transforming into entity coordinates:
		gl.glTranslatef((float)aabb.getX(), (float)aabb.getY(), 0);
		gl.glRotatef((float)aabb.getA(), 0, 0, 1 );
		// setting entity name for picking mechanism
		// all children will be picked by this name, in addition to their own names
		if(context.doPushNames())
			gl.glPushName(getId());
		
		// rendering this entity:
		getLook().render(gl, time, this, context);
		
		if(context.doPushNames()) // entity naming ends here
			gl.glPopName();
		
		// restoring transformation matrix:
		gl.glPopMatrix();
	}

}
