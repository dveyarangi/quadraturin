package yarangi.graphics.quadraturin.terrain;

public class Tile
{
	private int size;
	
	private int [][] pixels;
	
	public Tile(int size)
	{
		this.size = size;
		this.pixels = new int[size][size];
	}
	
	public void set(int x, int y, int val)
	{
		pixels[x][y] = val;
	}
	
	public int get(int x, int y)
	{
		return pixels[x][y];
	}
}
