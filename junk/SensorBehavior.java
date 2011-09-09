package yarangi.graphics.quadraturin.objects.behaviors;

import yarangi.graphics.quadraturin.objects.Sensor;
import yarangi.spatial.ISpatialIndex;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.MapSensor;

public class SensorBehavior <K extends Sensor> implements IBehaviorState<SceneEntity<K>>
{
	
	private ISpatialIndex <ISpatialObject> indexer;
	
	private MapSensor <ISpatialObject> sensor = new MapSensor<ISpatialObject>();
	
	public SensorBehavior (ISpatialIndex <ISpatialObject> indexer)
	{
		this.indexer = indexer;
	}

	public boolean behave(double time, SceneEntity<K> entity, boolean isVisible) 
	{
		sensor.clear();

		indexer.query(sensor, entity.getArea().getRefPoint().x(), entity.getArea().getRefPoint().y(), entity.getSensorRadiusSquare());
		
		entity.setEntities(sensor);
		
		return true;
	}
	
	protected MapSensor <ISpatialObject> getSensedObjects() { return sensor; }

}
