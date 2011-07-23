package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.SceneVeil;
import yarangi.spatial.Area;

/**
 * SceneEntity is a basic animate object in {@link Scene}. It provides means to render and animate itself, 
 * 
 * <li>If the {@link #spatialAspect}  is set, entity will be added into spatial indexer of corresponding {@link SceneVeil}.
 * <li>If the {@link #physicalAspect} is set, entity will participate in physics calculations. It requires the 
 * {@link #spatialAspect} to be set.
 * <li>If the {@link #sensorAspect} is set, it will be automatically updated with set of proximity data. It requires the 
 * {@link #spatialAspect} to be set.
 * 
 * <p>If {@link #markDead} is called, the entity will be disposed of at the next rendering cycle. 
 *  
 */
public class SceneEntity
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
	 * Area span
	 */
	private Area spatialAspect;
	
	private Body physicalAspect;

	private Sensor sensorAspect;
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
	public void setLook(Look <?> look) { this.look = look; }
	
	/**
	 * Sets entity's behavior.
	 * @param behavior
	 */
	public void setBehavior(Behavior <?>behavior) { this.behavior = behavior; }

	
	/**
	 * Sets entity area span.
	 * @param empty
	 */
	public void setArea(Area area) { this.spatialAspect = area; }

	public void setBody(Body body) { this.physicalAspect = body; }
	
	public void setSensor(Sensor sensor) { this.sensorAspect = sensor; }
	/**
	 * Alive flag, for collection of dead entities.
	 * @return
	 */
	public final boolean isAlive() { return isAlive; }
	
	/**
	 * Marks entity "dead", forcing it's disposal at the start of the next rendering cycle.
	 */
	public final void markDead() { this.isAlive = false; } 
	
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
	
	/**
	 * Where the object exists.
	 */
	public final Area getArea() { return spatialAspect; }

	
	public final Body getBody() { return physicalAspect; }
	
	public final Sensor getSensor() { return sensorAspect; }

	@SuppressWarnings("unchecked")
	public void init(GL gl)
	{
		if(getArea() == null)
			throw new IllegalStateException("Area " + this + " is not initialized.");

		if(getLook() == null)
			throw new IllegalStateException("Look for " + this + " is not initialized.");
		
		if(getBehavior() == null)
			throw new IllegalStateException("Behavior for " + this + " is not initialized.");
		
		this.getLook().init(gl, this);
	}
	
	@SuppressWarnings("unchecked")
	public void destroy(GL gl)
	{
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
		Area area = getArea();
	
		// storing transformation matrix:
		gl.glPushMatrix();
		
		// transforming into entity coordinates:
		gl.glTranslatef((float)area.getRefPoint().x(), (float)area.getRefPoint().y(), 0);
		gl.glRotatef((float)area.getOrientation(), 0, 0, 1 );
		
		// setting entity name for picking mechanism
		// all children will be picked by this name, in addition to their own names
//		if(context.doPushNames())
//			gl.glPushName(getId());
		
		// rendering this entity:
		getLook().render(gl, time, this, context);
		
//		if(context.doPushNames()) // entity naming ends here
//			gl.glPopName();
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
	

}
