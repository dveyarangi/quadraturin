package yarangi.graphics.curves;

import yarangi.math.Vector2D;

public class Bezier4Curve implements IParametricCurve 
{
	private Vector2D p1, p2, p3, p4;
	
	public Bezier4Curve(Vector2D p1, Vector2D p2, Vector2D p3, Vector2D p4) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
	}
	
	public Bezier4Curve() 
	{
		this(Vector2D.ZERO(), Vector2D.ZERO(), Vector2D.ZERO(), Vector2D.ZERO());
	}

	@Override
	public Vector2D at(double t) 
	{
		double f = 1 - t;

		
		return p1.mul(f*f*f).add(
			   p2.mul(3*f*f*t)).add(
			   p3.mul(3*f*t*t)).add(
			   p4.mul(t*t*t)); 
	}
	
	public Vector2D p1() { return p1; }
	public Vector2D p2() { return p2; }
	public Vector2D p3() { return p3; }
	public Vector2D p4() { return p4; }
}
