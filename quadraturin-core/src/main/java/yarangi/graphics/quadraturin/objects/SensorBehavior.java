package yarangi.graphics.quadraturin.objects;

import yarangi.graphics.quadraturin.objects.Behavior;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.MapSensor;
import yarangi.spatial.SetSensor;
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

		indexer.query(sensor, entity.getAABB().x, entity.getAABB().y, entity.getSensorRadius());
		
		entity.setEntities(sensor);
		return true;
	}

}
