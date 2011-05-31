package yarangi.graphics.quadraturin.objects;

import yarangi.spatial.ISpatialObject;
import yarangi.spatial.MapSensor;
import yarangi.spatial.SpatialIndexer;

public class SensorBehavior <K extends ISensorEntity> implements Behavior <K>
{
	
	private SpatialIndexer <ISpatialObject> indexer;
	
	private MapSensor <ISpatialObject> sensor = new MapSensor<ISpatialObject>();
	
	public SensorBehavior (SpatialIndexer <ISpatialObject> indexer)
	{
		this.indexer = indexer;
	}

	public boolean behave(double time, K entity, boolean isVisible) 
	{
		sensor.clear();

		indexer.query(sensor, entity.getArea().getRefPoint().x(), entity.getArea().getRefPoint().y(), entity.getSensorRadius());
		
		entity.setEntities(sensor);
		return true;
	}

}
