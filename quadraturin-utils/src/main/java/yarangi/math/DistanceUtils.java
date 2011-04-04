package yarangi.math;

public class DistanceUtils 
{
	public static double calcDistanceSquare(Vector2D v1, Vector2D v2)
	{
		double dx = v2.x - v1.x;
		double dy = v2.y - v1.y;
		return dx*dx + dy*dy;
	}
}
