package yarangi.intervals;

import yarangi.math.Vector2D;

public class UnitInterval {
	private Vector2D left;
	private Vector2D right;
	
	public UnitInterval(Vector2D left, Vector2D right)
	{
		double orientation = right.crossZComponent(left);
		this.left = orientation > 0 ? left : right;
		this.right = orientation > 0 ? right : left;
	}
	
	public Vector2D getLeft() { return left; }
	public Vector2D getRight() { return right; }
	
	public double isBetween(Vector2D unit) 
	{
		double z1 = right.crossZComponent(unit);
		double z2 = left.crossZComponent(unit);
		if(z1 > 0 && z2 < 0)
			return 0;
		return z1; // 
		
	}
}
