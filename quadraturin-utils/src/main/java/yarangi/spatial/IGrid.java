package yarangi.spatial;

public interface IGrid <C>
{

	public float getMinX();
	public float getMaxX();
	public float getMinY();
	public float getMaxY();
	public int getCellSize();

	public void setModificationListener(IGridListener<C> listener);
}