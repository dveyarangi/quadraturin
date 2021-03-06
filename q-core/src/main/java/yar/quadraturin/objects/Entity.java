package yar.quadraturin.objects;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.GL2RenderingContext;
import yar.quadraturin.IRenderingContext;
import yar.quadraturin.Scene;
import yar.quadraturin.SceneLayer;
import yar.quadraturin.terrain.ITerrain;
import yarangi.physics.Body;
import yarangi.spatial.AABB;
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
	@SuppressWarnings("rawtypes")
	private ILook look;
	
	/**
	 * Entity behavior
	 */
	@SuppressWarnings("rawtypes")
	private IBehavior behavior = Dummy.BEHAVIOR;
	
	/**
	 * Area span
	 */
	private AABB spatialAspect;
	
	/**
	 * Physically-interactive body of this entity
	 */
	private Body physicalAspect;

	/**
	 * Sensor for other entities
	 */
	private ISensor <?> sensorAspect;
	
	/**
	 * Sensor for terrain features
	 */
	private ISensor <?> terrainSensorAspect;
	
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

	/**
	 * How the object looks.
	 * @return
	 */
	public void setLook(ILook <?> look) { this.look = look; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBehavior(IBehavior <?> behavior) { this.behavior = behavior; }

	/**
	 * Space entity takes in its layer
	 * @param area
	 */
	public void setArea(AABB area) { this.spatialAspect = area; }

	/**
	 * Physical properties
	 * @param body
	 */
	public void setBody(Body body) { this.physicalAspect = body; }
	
	public void setEntitySensor(ISensor <?> sensor) {
		this.sensorAspect = sensor;
	}
	
	public void setTerrainSensor(ISensor <? extends ITerrain> sensor) {
		this.terrainSensorAspect = sensor;
	}
	
	/**
	 * World sensor properties (can be null)
	 * @param sensor
	 */
	public void setSensors(ISensor <?> sensor, ISensor <? extends ITerrain> terrainSensor) { 
		setEntitySensor( sensor );
		setTerrainSensor( terrainSensor );
	}
	
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
	public final AABB getArea() { return spatialAspect; }

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
	public final ISensor getEntitySensor() { return sensorAspect; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public final ISensor getTerrainSensor() { return terrainSensorAspect; }
	
	@Override
	@SuppressWarnings("unchecked")
	public void render(IRenderingContext ctx)
	{
		GL2 gl = ctx.gl();

		if(look.isOriented())
			GL2RenderingContext.useEntityCoordinates(gl, getArea(), getLook());
		look.render(this, ctx);
		if(look.isOriented())
			GL2RenderingContext.useWorldCoordinates(gl);
		
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public boolean behave(double time, boolean b)
	{
		return behavior.behave( time, this, b );
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
