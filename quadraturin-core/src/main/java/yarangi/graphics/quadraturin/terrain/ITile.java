package yarangi.graphics.quadraturin.terrain;


public interface ITile <P>
{
	public P at(int x, int y);
	public void put(P p, int x, int y);
	public void remove(int x, int y);
	public int getSize();
	public int getPixelCount();
}
