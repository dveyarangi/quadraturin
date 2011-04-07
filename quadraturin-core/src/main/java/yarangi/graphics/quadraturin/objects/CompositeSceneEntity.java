package yarangi.graphics.quadraturin.objects;

import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.spatial.AABB;

/**
 * Used to organize {@link CompositeSceneEntity} objects into scene tree.
 * TODO: initialization and destruction in memory and GL contexts.
 * 
 * @author Dve Yarangi
 */
public abstract class CompositeSceneEntity extends SceneEntity
{
	
	/**
	 * Entity children in entity tree
	 */
	private Set <SceneEntity> children = new HashSet <SceneEntity> ();

	/**
	 * Entity parent in entity tree
	 */
//	private CompositeSceneEntity parent;

	
	/**
	 * Creates a new located and oriented entity.
	 * @param x
	 * @param y
	 * @param a
	 */
	protected CompositeSceneEntity(AABB aabb)
	{
		super(aabb);
	}
	
	/**
	 * Add a child entity to this composite.
	 * @param child
	 */
	public void addChild(SceneEntity child)
	{
		children.add(child);
//		child.parent = this;
	}
	
	/**
	 * Remove a child entity from this composite.
	 * @param child
	 * @return
	 */
	public boolean removeChild(SceneEntity child)
	{
		return children.remove(child);
//		child.parent = null;
	}
	
	/**
	 * @return List of children of this composite
	 */
	public Set <SceneEntity> getChildren()
	{
		return children;
	}
	
	public void disassemble()
	{
		this.children = null;
//		this.parent = null;
	}
	
	public void init(GL gl)
	{
		super.init(gl);
		
		
	}
	
	public void destroy(GL gl)
	{
		for(SceneEntity child : getChildren())
			child.destroy(gl);
		super.destroy(gl);
	}
	
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
		
		// going into children branches:
		for(SceneEntity child : getChildren())
			child.display(gl, time, context);
		
		if(context.doPushNames()) // entity naming ends here
			gl.glPopName();
		
		// restoring transformation matrix:
		gl.glPopMatrix();
	}
	
	@SuppressWarnings("unchecked")
	public boolean behave(double time, boolean isVisible)
	{
		boolean behaved = getBehavior().behave(time, this, isVisible);
		
		for(SceneEntity entity : children)
			behaved |= entity.behave(time, isVisible);
		
		return behaved;
	}

}
