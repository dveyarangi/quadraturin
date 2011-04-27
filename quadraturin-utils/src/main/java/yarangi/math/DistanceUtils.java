package yarangi.math;

public class DistanceUtils 
{
	public static double calcDistanceSquare(Vector2D v1, Vector2D v2)
	{
		return calcDistanceSquare(v1.x, v1.y, v2.x, v2.y);
	}
	
	public static double calcDistanceSquare(double x1, double y1, double x2, double y2)
	{
		double dx = x2 - x1;
		double dy = y2 - y1;
		return dx*dx + dy*dy;
	}
	
	public static double calcDistanceToLine(Vector2D p, Vector2D la, Vector2D d)
	{
		return p.minus(la).abs() / d.rotate(Angles.PI_div_2).abs();
	}
	
	public static void main(String [] args)
	{
		
		System.out.println(calcDistanceToLine(new Vector2D(0,0), new Vector2D(-1, 1), new Vector2D(5, 0)));
	}
}
