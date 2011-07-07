package yarangi.graphics.quadraturin.objects;

import java.util.Map;

import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialObject;

public interface ISensorEntity extends ISpatialObject
{
	public void setEntities(Map <IAreaChunk, ISpatialObject> entities);
	
	public Map <IAreaChunk, ISpatialObject> getEntities();

	public double getSensorRadiusSquare();
}
