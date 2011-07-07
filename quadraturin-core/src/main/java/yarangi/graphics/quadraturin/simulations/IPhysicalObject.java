package yarangi.graphics.quadraturin.simulations;

import yarangi.math.Vector2D;
import yarangi.spatial.ISpatialObject;

/**
 * Basic interface for entities that can be manipulated by {@link IPhysicsEngine}.
 * 
 * @author Dve Yarangi
 */
public interface IPhysicalObject extends ISpatialObject
{
	
	/**
	 * Translates object by specified deltas. 
	 * @param dx
	 * @param dy
	 */
	public void moveMassCenter(double dx, double dy);
	
	/**
	 * @return Object mass.
	 */
	public double getMass();
	public void setMass(double mass);
	
	/** 
	 * @return Sum of forces influencing this object.
	 */
	public Vector2D getForce();
	public void setForce(double x, double y);
	public void addForce(double x, double y);	
	
	/**
	 * @return Object velocity vector.
	 */
	public Vector2D getVelocity();
//	public void setVelocity(double x, double y);
	public void addVelocity(double x, double y);
	
	/**
	 * Implements collision logic. 
	 * @param e
	 */
	public void setImpactWith(IPhysicalObject e); 
	
}
