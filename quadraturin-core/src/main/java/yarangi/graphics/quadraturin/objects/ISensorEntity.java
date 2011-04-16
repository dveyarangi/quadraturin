package yarangi.graphics.quadraturin.objects;

import java.util.Map;
import java.util.Set;

import yarangi.spatial.ISpatialObject;

public interface ISensorEntity extends ISpatialObject
{
	public void setEntities(Map <ISpatialObject, Double> entities);
	
	public Map <ISpatialObject, Double> getEntities();

	public double getSensorRadius();
}
