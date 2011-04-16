package yarangi.spatial;

import java.util.HashMap;

public class MapSensor <K extends ISpatialObject> extends HashMap <K, Double> implements ISpatialSensor <K> 
{

	private static final long serialVersionUID = 9025712177585233445L;

	public void objectFound(K object,double distanceSquare) {
		put(object, distanceSquare);
	}

}
