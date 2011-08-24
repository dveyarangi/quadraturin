package yarangi.spatial;


public interface ISpatialFilter <K>
{
	public boolean accept(K entity);
}
