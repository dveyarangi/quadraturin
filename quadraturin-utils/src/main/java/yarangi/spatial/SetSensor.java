package yarangi.spatial;

import java.util.HashSet;

public class SetSensor <K extends ISpatialObject> extends HashSet <K> implements ISpatialSensor <IAreaChunk, K> 
{

	private static final long serialVersionUID = 9025712177585233445L;

	public boolean objectFound(IAreaChunk chunk, K object) 
	{
		add(object);
		
		return false;
	}

}
