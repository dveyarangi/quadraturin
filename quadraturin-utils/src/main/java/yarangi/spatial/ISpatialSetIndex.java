package yarangi.spatial;

import java.util.Set;

public interface ISpatialSetIndex<T, O> extends ISpatialIndex<T, O>
{
	
	public Set <O> keySet();

}