package yarangi.game.vibrations.grid;

import yarangi.math.Vector2D;
import yarangi.physics.Body;
import yarangi.physics.IPhysicalObject;
import yarangi.spatial.AABB;

public class Joint extends Vector2D implements IPhysicalObject
{
	private double vx, vy;
	
	public Joint(double x, double y)
	{
		super( x, y );
		
	}
	
	public void setVelocity(double vx, double vy) 
	{
		this.vx = vx;
		this.vy = vy;
	}
	
	public double vx() { return vx; }
	public double vy() { return vy; }
	@Override
	public AABB getArea()
	{
		return null;
	}


	@Override
	public Body getBody()
	{
		return null;
	}

	@Override
	public boolean isAlive()
	{
		return false;
	}


}
