package yarangi.spatial;

import java.util.HashMap;

public class MapSensor <K extends ISpatialObject> extends HashMap <IAreaChunk, K> implements ISpatialSensor <IAreaChunk, K> 
{

	private static final long serialVersionUID = 9025712177585233445L;

	public boolean objectFound(IAreaChunk chunk, K object) {
		put(chunk, object);
		return false;
	}

}
