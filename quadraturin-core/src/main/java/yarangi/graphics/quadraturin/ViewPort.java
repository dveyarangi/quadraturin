package yarangi.graphics.quadraturin;

public class ViewPort
{
	public final int refx, refy;
	public final int width, height;
	
	
	public ViewPort(int refx, int refy, int width, int height) 
	{
		this.refx = refx;
		this.refy = refy;
		this.width = width;
		this.height = height;
	}
	
	public final int getRefX() { return refx; }
	public final int getRefY() { return refy; }
	public final int getWidth() { return width; }
	public final int getHeight() { return height; } 
}
