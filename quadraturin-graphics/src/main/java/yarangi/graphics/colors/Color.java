package yarangi.graphics.colors;

import javax.media.opengl.GL;

import yarangi.math.FastMath;


final public class Color
{
	protected float r;
	protected float g;
	protected float b;
	protected float a;

	
	public Color(byte r, byte g, byte b, byte a) {
		this(r/255f, g/255f, b/255f, a/255f);
	}
	
	public Color(float r, float g, float b, float a) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	public Color(Color color)
	{
		this(color.r, color.g, color.b, color.a);
	}
	public float getBlue() { return b; }
	public float getGreen() { return g; }
	public float getRed() { return r; }
	public float getAlpha() { return a; }
	public void setRed(float r) { this.r = r; }
	public void setGreen(float g) { this.g = g; }
	public void setBlue(float b) { this.b = b; }
	public void setAlpha(float a) { this.a = a; }
	
	public void apply(GL gl)
	{
		gl.glColor4f( r, g, b, a );
	}
	public boolean isVoid()
	{
		return r == 0 && b == 0 && g == 0 && a == 0;
	}
	
	public String toString()
	{
		return "R:" + r + " G:" + g + " B:" + b + " A:" + a;
	}
	public int toInteger()
	{
		return FastMath.round(r * 255) << 24 | 
			   FastMath.round(g * 255) << 16 | 
			   FastMath.round(b * 255) << 8 | 
			   FastMath.round(a * 255);
	}
	
	public Color valueOf(int rgba)
	{
		return new Color((rgba & 255 << 24) >> 24, 
						 (rgba & 255 << 16) >> 16,
						 (rgba & 255 << 8) >> 8,
						 (rgba & 255));
	}
	public byte getRedByte()
	{
		return (byte)FastMath.round(r * 255f);
	}
	public byte getGreenByte()
	{
		return (byte)FastMath.round(g * 255f);
	}
	public byte getBlueByte()
	{
		return (byte)FastMath.round(b * 255f);
	}
	public byte getAlphaByte()
	{
		return (byte)FastMath.round(a * 255f);
	}
}
