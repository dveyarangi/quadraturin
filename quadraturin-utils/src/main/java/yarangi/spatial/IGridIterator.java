package yarangi.spatial;

public interface IGridIterator <K extends IAreaChunk> 
{
	/**
	 * Tests if next element is available
	 * @return
	 */
	public boolean hasNext();

	/**
	 * Retrieves area chunk in the next grid cell.
	 * @return
	 */
	public IAreaChunk next();

}
