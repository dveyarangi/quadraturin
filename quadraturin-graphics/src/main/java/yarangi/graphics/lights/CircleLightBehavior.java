package yarangi.graphics.lights;

import yarangi.graphics.quadraturin.objects.Behavior;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.MapSensor;
import yarangi.spatial.SpatialIndexer;

public class CircleLightBehavior <K extends ICircleLightEntity> implements Behavior <K>
{
	
	private SpatialIndexer <ISpatialObject> indexer;
	
	private MapSensor <ISpatialObject> sensor = new MapSensor<ISpatialObject>();
	
	public CircleLightBehavior (SpatialIndexer <ISpatialObject> indexer)
	{
		this.indexer = indexer;
	}

	public boolean behave(double time, K entity, boolean isVisible) 
	{
		sensor.clear();
//		System.out.println("light affected radius: " + radius);
		
		indexer.query(sensor, entity.getArea().getRefPoint().x(), entity.getArea().getRefPoint().y(), entity.getSensorRadius());
		
		entity.setEntities(sensor);
		return true;
	}

}
