package yarangi.graphics.lights;

import yarangi.graphics.quadraturin.objects.Behavior;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.SetSensor;
import yarangi.spatial.SpatialIndexer;

public class CircleLightBehavior <K extends CircleLightEntity> implements Behavior <K>
{
	
	private SpatialIndexer <ISpatialObject> indexer;
	
	private SetSensor <ISpatialObject> sensor = new SetSensor<ISpatialObject>();
	
	public CircleLightBehavior (SpatialIndexer <ISpatialObject> indexer)
	{
		this.indexer = indexer;
	}

	public boolean behave(double time, K entity, boolean isVisible) 
	{
		sensor.clear();
		double radius = entity.getLightRadius();
//		System.out.println("light affected radius: " + radius);
		
		indexer.query(sensor, entity.getAABB().x-radius, entity.getAABB().y-radius, 
						      entity.getAABB().x+radius, entity.getAABB().y+radius);
		
		entity.setEntities(sensor);
		return true;
	}

}
