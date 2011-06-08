package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.Scene;
import yarangi.spatial.Area;
import yarangi.spatial.SpatialObjectSkeleton;

/**
 * SceneEntity is a basic animate object in {@link Scene}. It provides means to render and animate itself, 
 *  holds a life-cycle flag {@link #isAlive()} and implements {@link SpatialObjectSkeleton}, so it can be registered in
 *  spatial index structures.
 */
public abstract class SceneEntity extends SpatialObjectSkeleton 
{

	/**
	 * Entity look
	 */
	private Look <?> look;
	
	/**
	 * Entity behavior
	 */
	private Behavior <?> behavior;
	
	/**
	 * Dead entities are automatically removed from the stage.
	 */
	private boolean isAlive = true;

	/**
	 * Create a new scene entity, wrapped in specified AABB.
	 * @param aabb
	 */
	protected SceneEntity() 
	{
		super();
	}

	/** 
	 * Sets entity's look.
	 * @param look
	 */
	@SuppressWarnings("rawtypes")
	public void setLook(Look look) { this.look = look; }
	
	/**
	 * Sets entity's behavior.
	 * @param behavior
	 */
	@SuppressWarnings("rawtypes")
	public void setBehavior(Behavior behavior) { this.behavior = behavior; }
	
	/**
	 * Alive flag, for collection of dead entities.
	 * @return
	 */
	public final boolean isAlive() { return isAlive; }
	
	/**
	 * Sets 'alive' flag
	 * @param isAlive
	 */
	public final void setIsAlive(boolean isAlive) { this.isAlive = isAlive; } 
	
	/**
	 * How the object looks.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public final Look getLook() { return look; }
	
	/**
	 * How the object behaves
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public final Behavior getBehavior() { return behavior; }


	@SuppressWarnings("unchecked")
	public void init(GL gl)
	{
		if(getLook() != null)
			this.getLook().init(gl, this);
	}
	
	@SuppressWarnings("unchecked")
	public void destroy(GL gl)
	{
		if(getLook() != null)
			this.getLook().destroy(gl, this);
	}
	/**
	 * Displays this entity using specified GL interface
	 * 
	 * @param gl
	 * @param time - specifies time interval of this display iteration
	 * @param pushNames - pushes names for hardware picking. TODO: not used.
	 */
	@SuppressWarnings("unchecked")
	public void display(GL gl, double time, RenderingContext context )
	{
		if(getLook() == null)
			return;
		Area area = getArea();
	
		// storing transformation matrix:
		gl.glPushMatrix();
		
		// transforming into entity coordinates:
		gl.glTranslatef((float)area.getRefPoint().x(), (float)area.getRefPoint().y(), 0);
		gl.glRotatef((float)area.getOrientation(), 0, 0, 1 );
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
	
	/**
	 * Animates this entity, based on specified time interval.
	 * @param time
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean behave(double time, boolean isVisible)
	{
//		System.out.println(this.getId());
		if(getBehavior() == null)
			return false;
		return getBehavior().behave(time, this, isVisible);
	}

	/**
	 * States either this entity is pickable.
	 * @return
	 */
	public abstract boolean isPickable();

}
