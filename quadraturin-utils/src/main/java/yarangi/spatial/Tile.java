package yarangi.spatial;


public class Tile <K>
{
	private int i, j;
	
	private double x, y;
	
	private K content;

	public Tile(int i, int j, double x, double y)
	{
		this.i = i;
		this.j = j;
		this.x = x;
		this.y = y;
	}
	public Tile(K content, int i, int j, double x, double y)
	{
		this.i = i;
		this.j = j;
		
		this.x = x;
		this.y = y;
		
		this.content = content;
	}
	
	public void setContent(K content)
	{
		this.content = content;
	}
	public K getContent()
	{
		return content;
	}

	public double x() { return x; }

	public double y() { return y; }
	
	public int i() { return i; }
	
	public int j() { return j; }
	
	
}
