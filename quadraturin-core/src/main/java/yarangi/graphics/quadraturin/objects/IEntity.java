package yarangi.graphics.quadraturin.objects;

import yarangi.graphics.quadraturin.simulations.Body;
import yarangi.graphics.quadraturin.simulations.IPhysicalObject;

/**
 * Interface for game world entities.
 * @author dveyarangi
 *
 */
public interface IEntity extends IVeilEntity, IPhysicalObject
{

	/**
	 * Sets entity's behavior.
	 * @param behavior
	 */
	public abstract void setBehavior(Behavior<?> behavior);

	/**
	 * Physical properties
	 * @param body
	 */
	public abstract void setBody(Body body);

	/**
	 * World sensor properties (can be null)
	 * @param sensor
	 */
	public abstract void setSensor(ISensor <?> sensor);

	/**
	 * How the object behaves
	 * @return
	 */
	public abstract Behavior <?> getBehavior();

	/**
	 * How the entity fits the world
	 */
	public abstract Body getBody();

	/**
	 * How the entity feels the world.
	 * @return
	 */
	public abstract ISensor getSensor();

	public abstract boolean behave(double time, boolean b);

}