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
	
	public Vector2D getForce();
	
	public Vector2D getVelocity();
	
	public double getMass();
	
	public void setImpactWith(IPhysicalObject e); 
	
	public void setMass(double mass);
	
	public void setForce(double x, double y);
	public void setVelocity(double x, double y);
	
	public void addForce(double x, double y);	
	public void addVelocity(double x, double y);

	public void moveMassCenter(double dx, double dy);
}
