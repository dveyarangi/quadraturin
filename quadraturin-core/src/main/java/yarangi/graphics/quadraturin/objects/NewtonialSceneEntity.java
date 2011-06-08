package yarangi.graphics.quadraturin.objects;

import yarangi.graphics.quadraturin.simulations.IPhysicalObject;
import yarangi.math.Vector2D;

public abstract class NewtonialSceneEntity extends SceneEntity implements IPhysicalObject
{
	
	private Vector2D force = new Vector2D(0,0);
	private Vector2D velocity = new Vector2D(0,0);
	private double mass = 1;
	
	protected NewtonialSceneEntity() {
		super();
	}


	final public Vector2D getVelocity() {	return velocity; }

	final public Vector2D getForce() { return force; }

	final public double getMass() { return mass; }
	
	final public void setMass(double mass) { this.mass = mass; }
	
	final public void moveMassCenter(double dx, double dy)
	{
		getArea().translate(dx, dy);
	}
	
	final public void setForce(double x, double y)
	{
		force.x = x;
		force.y = y;
	}
	final public void setVelocity(double x, double y)
	{
		velocity.x = x;
		velocity.y = y;
	}
	
	final public void addForce(double x, double y)
	{
		force.x += x;
		force.y += y;
	}
	
	final public void addVelocity(double x, double y)
	{
		velocity.x += x;
		velocity.y += y;
	}

}
