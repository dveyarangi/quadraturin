package yarangi.graphics.quadraturin.objects;

import yarangi.physics.Body;


/**
 * Interface for game world entities.
 * @author dveyarangi
 *
 */
public interface IEntity extends IBeing, IVisible
{

	/**
	 * Sets entity's behavior.
	 * @param behavior
	 */
	public abstract void setBehavior(IBehavior<?> behavior);

	/**
	 * Physical properties
	 * @param body
	 */
//	public abstract void setBody(Body body);

	/**
	 * World sensor properties (can be null)
	 * @param sensor
	 */
//	public abstract void setSensor(ISensor <?> sensor);
	
	/**
	 * How the object behaves
	 * @return
	 */
	public abstract IBehavior <?> getBehavior();

	/**
	 * How the entity fits the world
	 */
	@Override
	public abstract Body getBody();

	/**
	 * How the entity feels the world.
	 * @return
	 */
	public abstract ISensor getSensor();

	public abstract boolean behave(double time, boolean b);
	
	// TODO: this could help reduce instanceof checks in filters and such
	public abstract int getGroupId();

}