package yarangi.spatial;

import java.util.HashMap;

public class MapSensor <K> extends HashMap <IAreaChunk, K> implements ISpatialSensor <K> 
{

	private static final long serialVersionUID = 9025712177585233445L;

	public boolean objectFound(IAreaChunk chunk, K object) {
		put(chunk, object);
		return false;
	}

}
