package yarangi.graphics.quadraturin.objects;

import yarangi.graphics.quadraturin.simulations.IPhysicalObject;
import yarangi.math.Vector2D;
import yarangi.spatial.AABB;

public abstract class NewtonialSceneEntity extends SceneEntity implements IPhysicalObject
{
	
	private Vector2D force = new Vector2D(0,0);
	private Vector2D velocity = new Vector2D(0,0);
	private double mass;
	
	protected NewtonialSceneEntity(AABB aabb) {
		super(aabb);
	}


	final public Vector2D getVelocity() {	return velocity; }

	final public Vector2D getForce() { return force; }

	final public double getMass() { return mass; }
	
	protected void setMass(double mass) { this.mass = mass; }
	
	protected void setForce(double x, double y)
	{
		force.x = x;
		force.y = y;
	}
	protected void setVelocity(double x, double y)
	{
		velocity.x = x;
		velocity.y = y;
	}
	
	protected void addForce(double x, double y)
	{
		force.x += x;
		force.y += y;
	}
	
	protected void addVelocity(double x, double y)
	{
		velocity.x += x;
		velocity.y += y;
	}

}
