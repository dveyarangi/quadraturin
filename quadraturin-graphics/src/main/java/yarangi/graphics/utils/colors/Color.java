package yarangi.graphics.utils.colors;


public class Color
{
	protected float r;
	protected float g;
	protected float b;
	protected float a;

	
	
	public Color(float r, float g, float b, float a) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	public float getBlue() { return b; }
	public float getGreen() { return g; }
	public float getRed() { return r; }
	public float getAlpha() { return a; }
}
