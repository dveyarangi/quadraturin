package yarangi.spatial;

import java.util.HashSet;

public class SetProcessor <K extends ISpatialObject> extends HashSet <K> implements SpatialProcessor <K> 
{

	private static final long serialVersionUID = 9025712177585233445L;

	public void objectFound(K object) {
		add(object);
	}

}
