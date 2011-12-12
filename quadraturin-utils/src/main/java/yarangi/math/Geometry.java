package yarangi.math;

public class Geometry 
{
	
	/**
	 * Calculates distance value square.
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double calcHypotSquare(Vector2D v1, Vector2D v2)
	{
		return calcHypotSquare(v1.x(), v1.y(), v2.x(), v2.y());
	}
	
	public static double calcHypotSquare(double x1, double y1, double x2, double y2)
	{
		double dx = x2 - x1;
		double dy = y2 - y1;
		return dx*dx + dy*dy;
	}
	
	public static double calcHypot(Vector2D v1, Vector2D v2)
	{
		return calcHypot(v1.x(), v1.y(), v2.x(), v2.y());
	}
	
	public static double calcHypot(double x1, double y1, double x2, double y2)
	{
		return Math.hypot(x2-x1, y2-y1);
	}
	
	/**
	 * 
	 * 
	 * @param p point
	 * @param la line anchor
	 * @param d line direction
	 * @return
	 */
	public static double calcDistanceToLine(Vector2D P, Vector2D Q, Vector2D v)
	{
		return P.minus(Q).abs() / v.left().abs();
	}
	
	public static void main(String [] args)
	{
		
		System.out.println(calcDistanceToLine(Vector2D.ZERO(), Vector2D.R(-1, 1), Vector2D.R(5, 0)));
	}
	public static double calcTriangleArea(double x1, double y1, double x2, double y2, double x3, double y3)
	{
		return (x1*y2-x2*y1 + x2*y3-x3*y2 - x3*y1-x1*y3) / 2;
	}
	
	public static boolean isLeft(double px, double py, double x1, double y1, double x2, double y2)
	{
		return calcTriangleArea(x1, y1, x2, y2, px, py) > 0;
	}
	
	public static boolean isInCircle(double px, double py, double x1, double y1, double x2, double y2, double x3, double y3)
	{
		double d1 = x1*x1+y1*y1;
		double d2 = x2*x2+y2*y2;
		double d3 = x3*x3+y3*y3;
		double pd = px*px+py*py;
		return determinant(x2, y2, d2, x3, y3, d3, px, py, pd)
		     - determinant(x1, y1, d1, x3, y3, d3, px, py, pd)
		     + determinant(x1, y1, d1, x2, y2, d2, px, py, pd)
		     - determinant(x1, y1, d1, x2, y2, d2, x3, y3, d3) < 0;
	}
	
	public static double determinant(double x1, double y1, double x2, double y2)
	{
		return x1*y2 - x2*y1;
	}
	
	public static double determinant(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3)
	{
		return x1 * (y2*z3 - y3*z2) - x2 * (y1*z3 - y3*z1) + x3 * (y1*z2 - y2*z1);
	}
	
	/**
	 * Calculates the intersection point from P + s * u = Q + t * v 
	 * 
	 * @param P - first line anchor point.
	 * @param u - first line direction vector.
	 * @param Q - second line anchor point.
	 * @param v - second line direction vector.
	 * @return intersection point vector, null if parallel
	 * TODO: watch for failures
	 */
	public static Vector2D calcIntersection(Vector2D P, Vector2D u, Vector2D Q, Vector2D v)
	{
		Vector2D w = P.minus(Q);
		double perp = (v.x() * u.y() - v.y() * u.x());
		if(perp == 0)
			return null;
		
		double s = (v.y() * w.x() - v.x() * w.y()) / perp;
		
		return Vector2D.R(P.x() + u.x() * s, P.y() + u.y() * s);
	}
	
	/**
	 * Calculates the intersection coefficients s and t, such as  P + s * u = Q + t * v 
	 * @param P - first line anchor point.
	 * @param u - first line direction vector.
	 * @param Q - second line anchor point.
	 * @param v - second line direction vector.
	 * @return Vector2D object, whose x element is equal to s and y equal to t; null if parallel
	 */
	public static Vector2D calcIntersectionParams(Vector2D P, Vector2D u, Vector2D Q, Vector2D v)
	{
		Vector2D w = P.minus(Q);
		double perp = (v.x() * u.y() - v.y() * u.x());
		if(perp == 0)
			return null;
		// TODO: sort out math:
		double s = (v.y() * w.x() - v.x() * w.y()) / perp;
		double t = (u.y() * w.x() - u.x() * w.y()) / (u.x() * v.y() - u.y() * v.x());
	
		return Vector2D.R(s, t);
	}
}
