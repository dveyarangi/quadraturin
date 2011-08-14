package yarangi.graphics.quadraturin.objects;

import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.SceneVeil;
import yarangi.graphics.quadraturin.simulations.Body;
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
public class WorldEntity implements IWorldEntity
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

	private ISensor <?> sensorAspect;
	/**
	 * Dead entities are automatically removed from the stage.
	 */
	private boolean isAlive = true;
	
	private int passId;

	/**
	 * Create a new scene entity, wrapped in specified AABB.
	 * @param aabb
	 */
	protected WorldEntity() 
	{
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLook(Look <?> look) { this.look = look; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBehavior(Behavior <?>behavior) { this.behavior = behavior; }

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArea(Area area) { this.spatialAspect = area; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBody(Body body) { this.physicalAspect = body; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
	public final void markDead() { this.isAlive = false; } 
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public final Look getLook() { return look; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public final Behavior getBehavior() { return behavior; }
	
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPassId()
	{
		return passId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPassId(int id)
	{
		passId = id;
	}
}
