package yarangi.graphics.quadraturin.simulations;

import yarangi.math.Vector2D;
import yarangi.spatial.Area;

public class Body
{
	
	private Vector2D force = new Vector2D(0,0);
	private Vector2D velocity = new Vector2D(0,0);
	private double mass = 1;
	private double maxSpeedSquare = 1;
	
	public Body() 
	{
	}
	final public Vector2D getVelocity() {	return velocity; }

	final public Vector2D getForce() { return force; }

	final public double getMass() { return mass; }
	
	final public void setMass(double mass) { this.mass = mass; }
	
	final public void moveMassCenter(Area area, double dx, double dy)
	{
		area.translate(dx, dy);
	}
	
	final public void setForce(double x, double y)
	{
		force.setxy(x, y);
	}
/*	final public void setVelocity(double x, double y)
	{
		velocity.x = x;
		velocity.y = y;
	}*/
	
	final public void addForce(double x, double y)
	{
		force.add(x, y);
	}
	
	final public void addVelocity(double x, double y)
	{
		velocity.add(x, y);
//		double abs = velocity.abs(); // TODO: to many roots here
		double abs = velocity.x() * velocity.x() + velocity.y()*velocity.y();
		if(abs > getMaxSpeedSquare())
			velocity.multiply(getMaxSpeedSquare()/abs);
	}

	public final double getMaxSpeedSquare() { return maxSpeedSquare; }
	public final void setMaxSpeed(double maxSpeed) {this.maxSpeedSquare = maxSpeed*maxSpeed; }
}
