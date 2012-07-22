package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.SceneLayer;
import yarangi.physics.Body;
import yarangi.spatial.Area;

/**
 * Entity is a basic animate object in {@link Scene}. It provides means to render and animate itself, 
 * 
 * <li>If the {@link #spatialAspect}  is set, entity will be added into spatial indexer of corresponding {@link SceneLayer}.
 * <li>If the {@link #physicalAspect} is set, entity will participate in physics calculations. It requires the 
 * {@link #spatialAspect} to be set.
 * <li>If the {@link #sensorAspect} is set, it will be automatically updated with set of proximity data. It requires the 
 * {@link #spatialAspect} to be set.
 * 
 * <p>If {@link #markDead} is called, the entity will be disposed of at the next rendering cycle. 
 *  
 */
public class Entity implements IEntity
{
	
	/**
	 * Entity graphics 
	 * @see SceneLayer#display(GL, double, IRenderingContext)
	 */
	private ILook <?> look;
	
	/**
	 * Entity behavior
	 */
	private IBehavior <?> behavior = Dummy.BEHAVIOR;
	
	/**
	 * Area span
	 */
	private Area spatialAspect;
	
	private Body physicalAspect;

	private ISensor <?> sensorAspect;
	/**
	 * Dead entities are automatically removed from the stage.
	 */
	private boolean isAlive = true;
	
	private final int groupId;

	/**
	 * Create a new scene entity.
	 */
	protected Entity() 
	{
		groupId = this.getClass().hashCode();
	}

	public void setLook(ILook <?> look) { this.look = look; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBehavior(IBehavior <?> behavior) { this.behavior = behavior; }

	public void setArea(Area area) { this.spatialAspect = area; }

	/**
	 * Physical properties
	 * @param body
	 */
	public void setBody(Body body) { this.physicalAspect = body; }
	
	/**
	 * World sensor properties (can be null)
	 * @param sensor
	 */
	public void setSensor(ISensor <?> sensor) { this.sensorAspect = sensor; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isAlive() { return isAlive; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void markDead() { this.isAlive = false; } 
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public final ILook getLook() { return look; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final IBehavior getBehavior() { return behavior; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Area getArea() { return spatialAspect; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Body getBody() { return physicalAspect; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public final ISensor getSensor() { return sensorAspect; }


	@Override
	@SuppressWarnings("unchecked")
	public void init(GL gl, IRenderingContext context)
	{
		getLook().init( gl, this, context );
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void render(GL gl,IRenderingContext context)
	{
		// rendering this entity:
		getLook().render(gl, this, context);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void destroy(GL gl, IRenderingContext context)
	{
		getLook().destroy( gl, this, context );
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean behave(double time, boolean b)
	{
		return getBehavior().behave( time, this, b );
	}
	
	@Override
	public boolean isIndexed()
	{
		return spatialAspect != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * By default, groups by entity class.
	 */
	@Override
	public int getGroupId()
	{
		return groupId;
	}
	
	public final double x() {	return getArea().getAnchor().x(); }
	public final double y() {	return getArea().getAnchor().y(); }

}
