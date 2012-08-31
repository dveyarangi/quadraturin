package yarangi.graphics.curves;

import yarangi.math.IVector2D;
import yarangi.math.Vector2D;

public class Bezier4Curve implements IParametricCurve 
{
	private final Vector2D p1, p2, p3, p4;
	
	private final Vector2D at1 = Vector2D.ZERO();
	private final Vector2D at2 = Vector2D.ZERO();
	private final Vector2D at3 = Vector2D.ZERO();
	private final Vector2D at4 = Vector2D.ZERO();
	
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
	public IVector2D at(double t) 
	{
		double f = 1 - t;

		
		return at1.set(p1).multiply(f*f*f).add(
			   at2.set(p2).multiply(3*f*f*t)).add(
			   at3.set(p3).multiply(3*f*t*t)).add(
			   at4.set(p4).multiply(t*t*t)); 
	}
	
	public Vector2D p1() { return p1; }
	public Vector2D p2() { return p2; }
	public Vector2D p3() { return p3; }
	public Vector2D p4() { return p4; }
}
