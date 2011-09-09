package yarangi.spatial;

/**
 * Interface for processor of {@link Area#iterate(int, IChunkConsumer)} method results.
 * 
 * @author dveyarangi
 *
 */
public interface IChunkConsumer
{
	/**
	 * Handles area chunk.
	 * @param chunk
	 * @return
	 */
	public boolean consume(IAreaChunk chunk);
}
